package com.hotel_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file);

    boolean deleteFile(String imgUrl);
}
