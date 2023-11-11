package com.s3.amazonstorage.Controllers.Validator;

import com.s3.amazonstorage.Exceptions.FileIsEmptyException;
import com.s3.amazonstorage.Exceptions.FileNotImageException;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator {

    public static void isImage(MultipartFile file) throws FileNotImageException {
        if (!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType()))
            throw new FileNotImageException("Sorry upload cannot be done, the uploaded file wasn't an image.") ;
    }

    public static void isNotEmpty(MultipartFile file) throws FileIsEmptyException {
        if (file.isEmpty())
            throw new FileIsEmptyException("Sorry upload cannot be done, the uploaded file is empty.") ;
    }

}
