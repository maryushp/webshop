package com.project.image;

import com.project.utils.exceptionhandler.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
public class LocalStorageImageService implements ImageService {
    @Value("${image.storage.path}")
    private String imageStoragePath;
    @Value("${image.server}")
    private String imageServerPath;

    @Override
    public String uploadImage(MultipartFile image, String imageName) {
        Path imagePath = Path.of(imageStoragePath, imageName + JPG);
        File imageFile = imagePath.toFile();

        if (imageFile.exists()) {
            deleteImage(Path.of(this.imageServerPath, imageName + JPG).toString());
        }

        try {
            image.transferTo(imagePath);
        } catch (IOException e) {
            throw new InvalidImageException(INVALID_IMAGE);
        }

        return Path.of(this.imageServerPath, imageName + JPG).toString();
    }

    @Override
    public void deleteImage(String imagePath) {
        String[] directories = imagePath.split(FileSystems.getDefault().getSeparator());
        String imageFile = directories[directories.length - 1];

        try {
            Files.delete(Path.of(imageStoragePath, imageFile));
        } catch (IOException e) {
            throw new InvalidImageException(IMAGE_NOT_EXIST);
        }
    }
}