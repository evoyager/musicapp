package com.epam.song.repository;

import java.util.List;

public interface CustomSongRepository {
    List<Long> deleteAllByIdInReturnIds(List<Long> ids);
}
