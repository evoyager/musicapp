package com.epam.resource.service;

import com.epam.resource.client.SongServiceClient;
import com.epam.resource.domain.Resource;
import com.epam.resource.exceptions.ResourceNotFoundException;
import com.epam.resource.repository.ResourceRepository;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SongServiceClient songServiceClient;

    Logger logger = LoggerFactory.getLogger(ResourceService.class);

    public Resource saveResource(byte[] audioData) throws Exception {
        Resource resource = new Resource();
        resource.setData(audioData);
        resource = resourceRepository.save(resource);

        // Assuming Apache Tika is used here to extract metadata
        Map<String, String> metadata = extractMetadata(audioData);

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

    public Map<String, String> extractMetadata(byte[] data) {
        Map<String, String> metadataMap = new HashMap<>();
        Mp3Parser parser = new Mp3Parser();

        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            parser.parse(inputStream, handler, metadata, new ParseContext());

            // Retrieve metadata keys and store in map
            String[] metadataNames = metadata.names();
            for (String name : metadataNames) {
                metadataMap.put(name, metadata.get(name));
            }
        } catch (IOException | SAXException | org.apache.tika.exception.TikaException e) {
            logger.error("An error occurred during MP3 metadata extraction: {}", e.getMessage(), e);
        }

        return metadataMap;
    }
}