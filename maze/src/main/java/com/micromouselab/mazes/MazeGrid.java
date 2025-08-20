package com.micromouselab.mazes;

public class MazeGrid {

    private MazeCell[][] grid;

    private final MazeFormat mazeFormat;

    public MazeGrid(String representation, MazeFormat mazeFormat){
        this.grid = switch (mazeFormat) {
            case B64_DIGEST -> createGridFromB64DigestFormat();
            case HEX_DIGEST -> createGridFromHexDigestFormat();
            case ASCII_GRID -> createGridFromASCIIGridFormat();
        };
        this.mazeFormat = mazeFormat;
    }

    private MazeCell[][] createGridFromB64DigestFormat(){
        return null;
    }

    private MazeCell[][] createGridFromHexDigestFormat(){
        return null;
    }

    private MazeCell[][] createGridFromASCIIGridFormat(){
        return null;
    }

    public String getRepresentation(MazeFormat mazeFormat){
        return switch (mazeFormat) {
            case B64_DIGEST -> getRepresentationAsBase64Format();
            case HEX_DIGEST -> getRepresentationAsHexFormat();
            case ASCII_GRID -> getRepresentationAsASCIIGridFormat();
        };
    }

    private String getRepresentationAsBase64Format(){
        return "";
    }

    private String getRepresentationAsHexFormat(){
        return "";
    }

    private String getRepresentationAsASCIIGridFormat(){
        return "";
    }


}
