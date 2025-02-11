package com.epam.resource.controller;

import com.epam.resource.domain.Resource;
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

    @PostMapping(consumes = "audio/mpeg", produces = "application/json")
    public ResponseEntity<Map<String, Long>> uploadResource(@RequestBody byte[] audioData) {
        try {
            Resource resource = resourceService.saveResource(audioData);
            return ResponseEntity.ok(Map.of("id", resource.getId()));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw e;
        }
    }

    @GetMapping(value ="/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        try {
            byte[] data = resourceService.getResource(id);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("audio/mpeg")).body(data);
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw e;
        }
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<Map<String, List<Long>>> deleteResource(@RequestParam(value ="id") String ids) {
        List<Long> deletedIds;
        try {
            deletedIds = resourceService.deleteResources(ids);
            return ResponseEntity.ok(Map.of("deletedIds", deletedIds));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw e;
        }
    }
}
