package com.epam.resource.service;

import com.epam.resource.client.SongServiceClient;
import com.epam.resource.domain.Resource;
import com.epam.resource.exceptions.ResourceNotFoundException;
import com.epam.resource.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SongServiceClient songServiceClient;

    public Resource saveResource(MultipartFile file) throws Exception {
        Resource resource = new Resource();
        resource.setData(file.getBytes());
        resource = resourceRepository.save(resource);

        // Assuming Apache Tika is used here to extract metadata
        Map<String, String> metadata = extractMetadata(file.getBytes());

        // Send metadata to Song Service
        songServiceClient.createSongMetadata(metadata, resource.getId());

        return resource;
    }

    public byte[] getResource(Long id) throws ResourceNotFoundException {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"))
                .getData();
    }

    public void deleteResources(List<Long> ids) {
        resourceRepository.deleteAllById(ids);
    }

    private Map<String, String> extractMetadata(byte[] data) {
        // Use Apache Tika to extract metadata
        return Map.of("Artist", "Queen", "Album", "Example");
    }
}