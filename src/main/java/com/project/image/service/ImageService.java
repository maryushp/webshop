package com.project.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile image, String imageName);

    void deleteImage(String imageURI);
}