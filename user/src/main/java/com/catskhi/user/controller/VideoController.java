package com.catskhi.user.controller;

import com.catskhi.user.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class VideoController {

    @Autowired
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/videos/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
           if (file.isEmpty()) {
               return ResponseEntity.badRequest().body("File is empty");
           }
           if (!file.getContentType().startsWith("video/")) {
               return ResponseEntity.badRequest().body("File is not a video");
           }
           if (file.getSize() > 52428800) {
                return ResponseEntity.badRequest().body("File size exceeds 50MB limit");
           }
           videoService.saveVideo(file);
           return ResponseEntity.ok("Video uploaded successfully: " + file.getOriginalFilename());
    }
}
