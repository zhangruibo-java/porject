package com.mr.controller;

import com.mr.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("img")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String  img=  uploadService.uploadImg(file);
        return ResponseEntity.ok(img);
    }
}
