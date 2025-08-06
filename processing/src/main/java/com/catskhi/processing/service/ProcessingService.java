package com.catskhi.processing.service;

import com.catskhi.processing.dto.VideoDto;
import com.catskhi.processing.producer.VideoEmailProducer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ProcessingService {

    @Autowired
    private final S3Client s3Client;

    @Autowired
    private final VideoEmailProducer videoEmailProducer;

    public ProcessingService(S3Client s3Client, VideoEmailProducer videoEmailProducer) {
        this.s3Client = s3Client;
        this.videoEmailProducer = videoEmailProducer;
    }

    @Transactional
    public void processVideo(VideoDto videoDto) {
        System.out.println("Processing video with ID: " + videoDto.videoId());
        File tempMp4 = null;
        try {
            tempMp4 = Files.createTempFile("video-" + videoDto.videoId(), ".mp4").toFile();
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                    GetObjectRequest.builder().bucket("video-bucket").key(videoDto.videoId()).build());
            FileOutputStream fileOutputStream = new FileOutputStream(tempMp4);
            s3Object.transferTo(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            System.err.println("Error downloading video: " + e.getMessage());
            throw new RuntimeException("Failed to process video with ID: " + videoDto.videoId(), e);
        }

        File tempMp3 = null;
        try {
            tempMp3 = Files.createTempFile("audio-" + UUID.randomUUID(), ".mp3").toFile();
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y", "-i", tempMp4.getAbsolutePath(), "-q:a", "0", "-map", "a", tempMp3.getAbsolutePath());
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg process failed with exit code: " + exitCode);
            }
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket("audio-bucket")
                    .key(videoDto.videoId().replace(".mp4", "") + ".mp3")
                    .contentType("audio/mpeg")
                    .contentLength(tempMp3.length())
                    .build();
            s3Client.putObject(request, tempMp3.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Video processing interrupted for ID: " + videoDto.videoId(), e);
        } finally {
            if (tempMp4 != null && tempMp4.exists()) {
                if (!tempMp4.delete()) {
                    System.err.println("Failed to delete temporary file: " + tempMp4.getAbsolutePath());
                }
            }
            if (tempMp3 != null && tempMp3.exists()) {
                if (!tempMp3.delete()) {
                    System.err.println("Failed to delete temporary file: " + tempMp3.getAbsolutePath());
                }
            }
        }
        System.out.println("Video processing completed for ID: " + videoDto.videoId());
        videoEmailProducer.sendVideoProcessingDoneEmail(videoDto,
                "http://localhost:4566/audio-bucket/" + videoDto.videoId().replace(".mp4", "") + ".mp3");
    }
}
