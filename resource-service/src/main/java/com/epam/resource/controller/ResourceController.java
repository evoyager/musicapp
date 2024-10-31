package com.epam.resource.controller;

import com.epam.resource.domain.Resource;
import com.epam.resource.exceptions.ResourceNotFoundException;
import com.epam.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadResource(@RequestParam("file") MultipartFile file) {
        try {
            Resource resource = resourceService.saveResource(file);
            return ResponseEntity.ok(Map.of("id", resource.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        try {
            byte[] data = resourceService.getResource(id);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResource(@RequestParam List<Long> ids) {
        try {
            resourceService.deleteResources(ids);
            return ResponseEntity.ok(Map.of("ids", ids));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}