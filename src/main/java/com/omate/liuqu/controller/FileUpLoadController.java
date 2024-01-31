package com.omate.liuqu.controller;

//import jakarta.annotation.Resource;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/api") // This means URL's start with /demo (after Application path)
public class FileUpLoadController {

    @Value("${app.base-url}")
    private String baseUrl;
    private final FileService svc;

    @Autowired
    FileUpLoadController(FileService svc) {
        this.svc = svc;
    }

    @PostMapping("/upload/test")
    public ResponseEntity<Result> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        Result result = new Result();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("imageURL",this.svc.upload(file));
        result.setResultSuccess(0,resultMap);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
