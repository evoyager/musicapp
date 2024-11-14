package com.epam.song.controller;

import com.epam.song.domain.Song;
import com.epam.song.domain.SongMetadataDto;
import com.epam.song.exceptions.ResourceNotFoundException;
import com.epam.song.service.SongService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.song.converter.SongConverter.toDto;
import static com.epam.song.converter.SongConverter.toEntity;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    Logger logger = LoggerFactory.getLogger(SongController.class);

    @PostMapping
    public ResponseEntity<Map<String, Long>> createSong(@RequestBody @Valid SongMetadataDto songMetadata) throws Exception {
        try {
            Song song = toEntity(songMetadata);
            Song savedSong = songService.saveSong(song);
            return ResponseEntity.ok(Map.of("id", savedSong.getId()));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw new Exception("Error saving song: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongMetadataDto> getSongById(@PathVariable Long id) throws Exception {
        try {
            Song song = songService.getSongById(id);
            return ResponseEntity.ok(toDto(song));
        } catch (ResourceNotFoundException e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            throw new Exception("Error retrieving song: " + e.getMessage(), e);
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteSongs(@RequestParam String ids) {
        List<Long> deletedIds;
        try {
            deletedIds = songService.deleteSongs(ids);
            return ResponseEntity.ok(Map.of("deletedIds", deletedIds));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        try {
            List<Song> songs = songService.getAllSongs();
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().body(List.of());
        }
    }
}
