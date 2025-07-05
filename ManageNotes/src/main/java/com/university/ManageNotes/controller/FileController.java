package com.university.ManageNotes.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<FileSystemResource> download(@PathVariable String fileName) {
        Path path = Path.of("generated-reports").resolve(fileName);
        FileSystemResource resource = new FileSystemResource(path);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
