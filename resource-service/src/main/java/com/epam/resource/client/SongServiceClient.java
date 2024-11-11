package com.epam.resource.client;

import com.epam.resource.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SongServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public SongServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void createSongMetadata(Map<String, String> metadata, Long resourceId) {
        Song song = Song.builder()
                .name(metadata.get("title"))
                .artist(metadata.get("Author"))
                .album(metadata.get("xmpDM:album"))
                .length(metadata.get("xmpDM:duration"))
                .year(metadata.get("xmpDM:releaseDate"))
                .resourceId(resourceId)
                .build();
        // Assume URL is configured in application properties
        String songServiceUrl = "http://song-service:8081/songs";
        restTemplate.postForObject(songServiceUrl, song, Song.class);
    }
}
