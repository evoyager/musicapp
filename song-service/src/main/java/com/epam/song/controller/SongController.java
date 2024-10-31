package com.epam.song.controller;

import com.epam.song.domain.Song;
import com.epam.song.exceptions.ResourceNotFoundException;
import com.epam.song.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody Song song) {
        try {
            Song savedSong = songService.saveSong(song);
            return ResponseEntity.ok(Map.of("id", savedSong.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving song: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable Long id) {
        try {
            Song song = songService.getSongById(id);
            return ResponseEntity.ok(song);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
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
            return ResponseEntity.badRequest().body("Invalid IDs format");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting songs: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        try {
            List<Song> songs = songService.getAllSongs();
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of());
        }
    }
}
