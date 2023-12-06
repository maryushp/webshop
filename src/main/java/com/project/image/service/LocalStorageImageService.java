package com.project.image.service;

import com.project.utils.exceptionhandler.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
public class LocalStorageImageService implements ImageService {
    @Value("${image.storage.path}")
    private String imageStoragePath;

    @Override
    public String uploadImage(MultipartFile image, String imageName) {
        Path imagePath = Path.of(imageStoragePath, imageName + JPG);
        File imageFile = imagePath.toFile();

        if (imageFile.exists()) {
            deleteImage(imagePath.toString());
        }

        try {
            image.transferTo(imagePath);
        } catch (IOException e) {
            throw new InvalidImageException(INVALID_IMAGE);
        }

        return imagePath.toString();
    }

    @Override
    public void deleteImage(String imageURI) {
        Path imagePath = Path.of(imageURI);

        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new InvalidImageException(IMAGE_NOT_EXIST);
        }
    }
}