package com.catskhi.user.service;

import com.catskhi.user.dto.VideoDto;
import com.catskhi.user.producer.VideoProducer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private final S3Client s3Client;

    @Autowired
    private final VideoProducer videoProducer;

    public VideoService(S3Client s3Client, VideoProducer videoProducer) {
        this.s3Client = s3Client;
        this.videoProducer = videoProducer;
    }

    @Transactional
    public String saveVideo(UUID userId, String userEmail, MultipartFile file) {
        String fileName = UUID.randomUUID() + ".mp4";
        String bucketName = "video-bucket";
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video to S3: " + e.getMessage(), e);
        }
        videoProducer.sendVideoProcessingEvent(VideoDto.builder()
                        .userId(userId
                        )
                        .UserEmail(userEmail)
                        .videoId(fileName)
                .build());
        return "Video saved successfully: " + file.getOriginalFilename();

    }
}
