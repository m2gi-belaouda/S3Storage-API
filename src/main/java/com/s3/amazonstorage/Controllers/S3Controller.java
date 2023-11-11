package com.s3.amazonstorage.Controllers;

import com.s3.amazonstorage.Controllers.Validator.FileValidator;
import com.s3.amazonstorage.Exceptions.FileIsEmptyException;
import com.s3.amazonstorage.Exceptions.FileNotImageException;
import com.s3.amazonstorage.Services.IS3Service;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
public class S3Controller {

    @Autowired
    private IS3Service s3Service;

    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) throws FileNotImageException, FileIsEmptyException {
        new FileValidator(file).isNotEmpty()
                               .isImage();

        return s3Service.saveFile(file);
    }

    @GetMapping("download")
    public ResponseEntity<byte[]> download(@RequestParam("filename") String filename){
        HttpHeaders headers=new HttpHeaders();

        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        byte[] bytes = s3Service.downloadFile(filename);

        return  ResponseEntity.status(HTTP_OK)
                              .headers(headers)
                              .body(bytes);
    }

    @DeleteMapping("delete")
    public  String deleteFile(@RequestParam("filename") String filename){
        return s3Service.deleteFile(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles(){
        return s3Service.listAllFiles();
    }

}