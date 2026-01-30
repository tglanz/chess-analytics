package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryDTO {
    @JsonProperty("@id")
    public String id;

    public String code;
    public String name;
}
