package com.micromouselab.mazes;


public class MazeDTO {

    private final Long id;
    private final String condensedFormatString;

    public MazeDTO(Long id, String condensedFormatString) {
        this.id = id;
        this.condensedFormatString = condensedFormatString;
    }

    public String convertToASCIIGridFormat() {
        return "This should have been converted to the ASCII grid format: " + this.condensedFormatString;
    }
}