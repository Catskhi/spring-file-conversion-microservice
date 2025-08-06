package com.catskhi.processing.producer;

import com.catskhi.processing.configuration.RabbitMqConfiguration;
import com.catskhi.processing.dto.VideoDto;
import com.catskhi.processing.dto.VideoEmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoEmailProducer {
    @Autowired
    private RabbitMqConfiguration rabbitMqConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendVideoProcessingDoneEmail(VideoDto videoDto, String videoUrl) {
        VideoEmailDto dto = new VideoEmailDto();
        dto.setUserId(videoDto.userId());
        dto.setEmailTo(videoDto.userEmail());
        dto.setEmailSubject("Video Processing Completed");
        dto.setBody("Your video has been processed successfully. You can view it here: " + videoUrl);

        rabbitTemplate.setMessageConverter(rabbitMqConfig.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(rabbitMqConfig.emailExchange().getName(), "video-processing-done", dto);
        System.out.println(" [x] Sent video processing done event for video url: '" + videoUrl + "'");

    }
}
