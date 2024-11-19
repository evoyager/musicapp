package com.epam.song.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongMetadataDto {
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String artist;

    private String album;

    private String duration;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be in YYYY format and between 1900 and 2099.")
    private String year;

    private Long resourceId;
}
