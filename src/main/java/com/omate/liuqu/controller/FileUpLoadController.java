package com.omate.liuqu.controller;

import com.omate.liuqu.model.Result;
//import jakarta.annotation.Resource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

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

    private String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // no extension found
        }
        return filename.substring(lastIndexOfDot + 1).toLowerCase();
    }

    private String determineContentType(String filename) {
        String extension = getFileExtension(filename);
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }
    
    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        Path filePath = Paths.get("image/" + filename);
        Resource resource = new FileSystemResource(filePath);
        if (resource.exists()) {
            String contentType = determineContentType(filename);
            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Result> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir: " + currentDir);
        Result result = new Result();
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") &&
                !contentType.equals("image/gif") && !contentType.equals("image/bmp") &&
                !contentType.equals("image/tiff") && !contentType.equals("image/svg+xml") &&
                !contentType.equals("image/webp")) {
            result.setResultFailed(8);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        String originalFilename = file.getOriginalFilename();
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        int dotIndex = originalFilename.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? originalFilename : originalFilename.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : originalFilename.substring(dotIndex + 1);
        String newFilename = Base64.getEncoder().encodeToString(baseName.getBytes(StandardCharsets.UTF_8))+ "_" + timestamp + "." + extension;
        Path path = Paths.get("image/" + newFilename);
        Files.createDirectories(path.getParent());
        file.transferTo(path);
        String fileUrl =  baseUrl + "/api/view/" + newFilename;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("imageURL",fileUrl);
        result.setResultSuccess(0,resultMap);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
