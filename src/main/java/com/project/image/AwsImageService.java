package com.project.image;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.project.utils.exceptionhandler.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@Profile("aws")
public class AwsImageService implements ImageService {
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.s3.bucket}")
    private String bucketName;


    @Override
    public String uploadImage(MultipartFile image, String imageName) {
        File imageFile = new File(imageName);

        try (FileOutputStream os = new FileOutputStream(imageFile)){
            os.write(image.getBytes());
        } catch (IOException e) {
            throw new InvalidImageException(INVALID_IMAGE);
        }

        getS3Client().putObject(bucketName, imageName, imageFile);

        imageFile.delete();

        return "https://" + bucketName + ".s3.eu-north-1.amazonaws.com/" + imageName;
    }

    @Override
    public void deleteImage(String imagePath) {
        String[] directories = imagePath.split("/");
        String imageName = directories[directories.length - 1];

        getS3Client().deleteObject(bucketName, imageName);
    }

    private AmazonS3 getS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }
}