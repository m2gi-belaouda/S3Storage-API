package com.s3.amazonstorage.Services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IS3Service {

    String saveFile(MultipartFile file);
    byte[] downloadFile(String filename);
    String deleteFile(String filename);
    List<String> listAllFiles();

}
