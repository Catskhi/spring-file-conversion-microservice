package com.catskhi.processing.consumer;

import com.catskhi.processing.dto.VideoDto;
import com.catskhi.processing.service.ProcessingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessingConsumer {

    @Autowired
    final ProcessingService processingService;

    public ProcessingConsumer(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @RabbitListener(queues = "video-processing-queue")
    public void consumeVideoProcessingQueue(VideoDto videoDto) {
        System.out.println("Received message from video-processing-queue: " + videoDto.videoId());
        processingService.processVideo(videoDto);
        System.out.println("Processed video with ID: " + videoDto.videoId());
    }
}
