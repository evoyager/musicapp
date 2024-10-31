package com.epam.song.service;

import com.epam.song.domain.Song;
import com.epam.song.exceptions.ResourceNotFoundException;
import com.epam.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    // Saves the song metadata to the database
    public Song saveSong(Song song) {
        // Here you might add additional business logic before saving the song
        return songRepository.save(song);
    }

    // Retrieves a song by its ID
    public Song getSongById(Long id) throws ResourceNotFoundException {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
    }

    // Deletes a song or multiple songs by IDs (processable in batch)
    public void deleteSongs(List<Long> ids) {
        songRepository.deleteAllById(ids);
        // Optionally, you can handle cases where some IDs do not exist,
        // but this straightforward method handles such cleanly by ignoring non-existent IDs.
    }

    // Get all songs (optional method)
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
}