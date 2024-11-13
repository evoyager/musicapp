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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    Logger logger = LoggerFactory.getLogger(SongController.class);

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody @Valid SongMetadataDto songMetadata) {
        try {
            Song song = new Song();
            song.setId(songMetadata.getId());
            song.setName(songMetadata.getName());
            song.setArtist(songMetadata.getArtist());
            song.setAlbum(songMetadata.getAlbum());
            song.setLength(songMetadata.getLength());
            song.setYear(songMetadata.getYear());
            song.setResourceId(songMetadata.getResourceId());
            Song savedSong = songService.saveSong(song);
            return ResponseEntity.ok(Map.of("id", savedSong.getId()));
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().body("Error saving song: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable Long id) {
        try {
            Song song = songService.getSongById(id);
            return ResponseEntity.ok(song);
        } catch (ResourceNotFoundException e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving song: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSongs(@RequestParam String ids) {
        try {
            List<Long> idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList();
            songService.deleteSongs(idList);
            return ResponseEntity.ok(Map.of("deletedIds", idList));
        } catch (NumberFormatException e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.badRequest().body("Invalid IDs format");
        } catch (Exception e) {
            logger.atError().log(e.getMessage());
            return ResponseEntity.internalServerError().body("Error deleting songs: " + e.getMessage());
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
