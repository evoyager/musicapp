package com.epam.resource.controller;

import com.epam.resource.domain.Resource;
import com.epam.resource.exceptions.ResourceNotFoundException;
import com.epam.resource.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<?> uploadResource(@RequestBody byte[] audioData) throws Exception {
        try {
            Resource resource = resourceService.saveResource(audioData);
            return ResponseEntity.ok(Map.of("id", resource.getId()));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        try {
            byte[] data = resourceService.getResource(id);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
        } catch (ResourceNotFoundException ex) {
            logger.atError().log(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResource(@RequestParam List<Long> ids) {
        try {
            resourceService.deleteResources(ids);
            return ResponseEntity.ok(Map.of("ids", ids));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
