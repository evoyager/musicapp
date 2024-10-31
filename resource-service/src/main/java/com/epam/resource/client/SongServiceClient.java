package com.epam.resource.client;

import com.epam.song.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SongServiceClient {

    private final RestTemplate restTemplate;

    // Assume URL is configured in application properties
    private final String songServiceUrl = "http://localhost:8081/songs";

    @Autowired
    public SongServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void createSongMetadata(Map<String, String> metadata, Long resourceId) {
        Song song = Song.builder()
                .name(metadata.get("Name"))
                .artist(metadata.get("Artist"))
                .album(metadata.get("Album"))
                .length("Length")
                .resourceId(resourceId)
                .build();
        restTemplate.postForObject(songServiceUrl, song, Song.class);
    }
}
