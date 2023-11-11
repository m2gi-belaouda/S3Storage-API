package com.s3.amazonstorage.Services.Impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.s3.amazonstorage.Services.IS3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service implements IS3Service {

    private final String bucketName;
    private final String accessKey;
    private final String secretKey;
    private final String awsRegion;

    private final AmazonS3 s3;

    public S3Service(
            @Value("${bucketName}") String bucketName,
            @Value("${accessKey}") String accessKey,
            @Value("${secretKey}") String secretKey,
            @Value("${region}") String awsRegion
    ) {
        this.bucketName = bucketName;
        this.accessKey  = accessKey;
        this.secretKey  = secretKey;
        this.awsRegion  = awsRegion;

        this.s3 = AmazonS3ClientBuilder.standard()
                                       .withCredentials(
                                               new AWSStaticCredentialsProvider(
                                                       new BasicAWSCredentials(this.accessKey,this.secretKey)))
                                       .withRegion(this.awsRegion)
                                       .build();
    }

    @Override
    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {
                File file1 = convertMultiPartToFile(file);
                PutObjectResult putObjectResult = s3.putObject(bucketName, originalFilename, file1);

                return putObjectResult.getContentMd5();
            } catch (IOException e) {
                if (++count == maxTries) throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte[] downloadFile(String filename) {
        S3Object object = s3.getObject(bucketName, filename);
        S3ObjectInputStream objectContent = object.getObjectContent();

        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String filename) {
        s3.deleteObject(bucketName,filename);

        return "File deleted";
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);

        return  listObjectsV2Result.getObjectSummaries().stream()
                                                        .map(S3ObjectSummary::getKey)
                                                        .collect(Collectors.toList());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException
    {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);

        fileOutputStream.write( file.getBytes() );
        fileOutputStream.close();

        return convertedFile;
    }

}
