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

    public void createSongMetadata(Map<String, String> metadata, Long id) {
        String rawDuration = metadata.get("xmpDM:duration");
        String formattedDuration = formatDuration(rawDuration);
        Song song = Song.builder()
                .id(id)
                .name(metadata.get("title"))
                .artist(metadata.get("Author"))
                .album(metadata.get("xmpDM:album"))
                .duration(formattedDuration)
                .year(metadata.get("xmpDM:releaseDate"))
                .build();
        String songServiceUrl = "http://song-service:8081/songs";
        restTemplate.postForObject(songServiceUrl, song, Song.class);
    }

    private String formatDuration(String rawDuration) {
        if (rawDuration == null) return "00:00";

        try {
            // assuming the raw duration is in seconds with possible decimal points, e.g., 179.38
            long seconds = Long.parseLong(rawDuration.split("\\.")[0]);
            long minutes = seconds / 60;
            seconds = seconds % 60;

            return String.format("%02d:%02d", minutes, seconds);
        } catch (NumberFormatException e) {
            return "00:00";
        }
    }
}
