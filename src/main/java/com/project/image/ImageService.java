package com.project.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile image, String imageName);

    void deleteImage(String imageURI);
}