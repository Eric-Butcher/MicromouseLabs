package com.micromouselab.mazes;

import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

public class MazeGrid {

    private final int MICROMOUSE_MAZE_LENGTH = 16;
    private final int MICROMOUSE_MAZE_SIZE = MICROMOUSE_MAZE_LENGTH * MICROMOUSE_MAZE_LENGTH;

    private final double BASE_64_EXPANSION_SIZE_FACTOR = 1.4;
    private final int MAX_BASE_64_ENCODING_SIZE = (int) (MICROMOUSE_MAZE_SIZE * BASE_64_EXPANSION_SIZE_FACTOR);

    private final int HEX_EXPANSION_SIZE_FACTOR = 2;
    private final int MAX_HEX_ENCODING_SIZE = MICROMOUSE_MAZE_SIZE * HEX_EXPANSION_SIZE_FACTOR;

    private final String FILLED_HORIZONTAL_WALL = "+---+";
    private final String EMPTY_HORIZONTAL_WALL = "+   +";
    private final String WEST_FILLED_VERTICAL_WALL = "|    ";
    private final String FILLED_VERTICAL_WALL = "|   |";
    private final String EAST_FILLED_VERTICAL_WALL = "    |";
    private final String EMPTY_VERTICAL_WALL = "     ";


    private MazeCell[][] grid;

    private final MazeFormat mazeFormat;

    public MazeGrid(String representation, MazeFormat mazeFormat){
        this.grid = switch (mazeFormat) {
            case B64_DIGEST -> createGridFromB64DigestFormat(representation);
            case HEX_DIGEST -> createGridFromHexDigestFormat(representation);
            case ASCII_GRID -> createGridFromASCIIGridFormat(representation);
        };
        this.mazeFormat = mazeFormat;
    }

    private MazeCell[][] createGridFromB64DigestFormat(String representation) throws IllegalArgumentException {
        byte[] decoded = Base64.getDecoder().decode(representation);

        return this.createGridFromByteArray(decoded);
    }

    private MazeCell[][] createGridFromHexDigestFormat(String representation) throws IllegalArgumentException {
        byte[] decoded = HexFormat.of().parseHex(representation);
        return this.createGridFromByteArray(decoded);
    }

    private MazeCell[][] createGridFromByteArray(byte[] byteArray){
        if (byteArray.length != MICROMOUSE_MAZE_SIZE){
            throw new IllegalArgumentException("Grid is of incorrect size.");
        }

        MazeCell[][] newGrid = new MazeCell[MICROMOUSE_MAZE_LENGTH][MICROMOUSE_MAZE_LENGTH];

        for (int i = 0; i < byteArray.length; i++){
            int rowIndex = i / MICROMOUSE_MAZE_LENGTH;
            int columnIndex = i % MICROMOUSE_MAZE_LENGTH;

            newGrid[rowIndex][columnIndex] = MazeCell.fromByte(byteArray[i]);
        }

        return newGrid;

    }

    /*
        123456789
        +---+---+
        |       |
        +   +---+
        |   |   |
        +---+---+
    */
    private MazeCell[][] createGridFromASCIIGridFormat(String representation){

        String[] lines = getLines(representation);

        MazeCell[][] newGrid = new MazeCell[MICROMOUSE_MAZE_LENGTH][MICROMOUSE_MAZE_LENGTH];

        for (int mazeRowIndex = 0; mazeRowIndex < MICROMOUSE_MAZE_LENGTH; mazeRowIndex++) {
            for (int mazeColumnIndex = 0; mazeColumnIndex < MICROMOUSE_MAZE_LENGTH; mazeColumnIndex++) {
                newGrid[mazeRowIndex][mazeColumnIndex] = this.createMazeCellFromASCIIGridPosition(mazeRowIndex, mazeColumnIndex, lines);
            }
        }

        return newGrid;
    }

    private String[] getLines(String representation) {
        int expectedRowLength = (MICROMOUSE_MAZE_LENGTH * 4) + 1;
        int expectedColumnLength = (MICROMOUSE_MAZE_LENGTH * 2) + 1;

        String[] lines = representation.split("\\R");

        if (lines.length != expectedColumnLength){
            throw new IllegalArgumentException("Grid is of incorrect size.");
        }

        for (String line : lines){
            if (line.length() != expectedRowLength){
                throw new IllegalArgumentException("Grid is of incorrect size.");
            }
        }
        return lines;
    }

    private MazeCell createMazeCellFromASCIIGridPosition(int rowIndex, int columnIndex, String[] lines){
        int northRowRelativeStartIndexOffset = 2 * rowIndex;
        int eastWestRowRelativeStartIndexOffset = northRowRelativeStartIndexOffset + 1;
        int southRowRelativeIndexOffset = northRowRelativeStartIndexOffset + 2;

        int horizontalLeftIndex = 4 * columnIndex;
        int horizontalRightIndexExclusive = horizontalLeftIndex + 5;

        String northSubstring = lines[northRowRelativeStartIndexOffset].substring(horizontalLeftIndex, horizontalRightIndexExclusive);
        String eastWestSubstring = lines[eastWestRowRelativeStartIndexOffset].substring(horizontalLeftIndex, horizontalRightIndexExclusive);
        String southSubstring = lines[southRowRelativeIndexOffset].substring(horizontalLeftIndex, horizontalRightIndexExclusive);

        boolean northWall;
        boolean eastWall;
        boolean southWall;
        boolean westWall;

        if (northSubstring.equals(FILLED_HORIZONTAL_WALL)){
            northWall = true;
        } else if (northSubstring.equals(EMPTY_HORIZONTAL_WALL)){
            northWall = false;
        } else {
            throw new IllegalArgumentException("Improperly defined grid.");
        }

        if (southSubstring.equals(FILLED_HORIZONTAL_WALL)){
            southWall = true;
        } else if (southSubstring.equals(EMPTY_HORIZONTAL_WALL)){
            southWall = false;
        } else {
            throw new IllegalArgumentException("Improperly defined grid.");
        }

        if (eastWestSubstring.equals(FILLED_VERTICAL_WALL)){
            eastWall = true;
            westWall = true;
        } else if (eastWestSubstring.equals(WEST_FILLED_VERTICAL_WALL)){
            eastWall = false;
            westWall = true;
        } else if (eastWestSubstring.equals(EAST_FILLED_VERTICAL_WALL)){
            eastWall = true;
            westWall = false;
        } else if (eastWestSubstring.equals(EMPTY_VERTICAL_WALL)){
            eastWall = false;
            westWall = false;
        } else {
            throw new IllegalArgumentException("Improperly defined grid.");
        }

        return new MazeCell(northWall, eastWall, southWall, westWall);

    }

    public String getRepresentation(MazeFormat mazeFormat){
        return switch (mazeFormat) {
            case B64_DIGEST -> getRepresentationAsBase64Format();
            case HEX_DIGEST -> getRepresentationAsHexFormat();
            case ASCII_GRID -> getRepresentationAsASCIIGridFormat();
        };
    }

    private String getRepresentationAsBase64Format(){
        byte[] byteArray = this.cellGridToByteArray(this.grid);
        String base64EncodedString = Base64.getEncoder().encodeToString(byteArray);
        return base64EncodedString;
    }

    private String getRepresentationAsHexFormat(){
        byte[] byteArray = this.cellGridToByteArray(this.grid);
        String hexEncodedString = HexFormat.of().formatHex(byteArray);
        return hexEncodedString;
    }

    private byte[] cellGridToByteArray(MazeCell[][] mazeCells){
        byte[] byteArray = new byte[MICROMOUSE_MAZE_SIZE];
        int i = 0;
        for (MazeCell[] cellRow : this.grid){
            for (MazeCell cell : cellRow){
                byteArray[i] = cell.toByte();
                i++;
            }
        }
        return byteArray;
    }

    private String getRepresentationAsASCIIGridFormat(){
        int expectedRowLength = (MICROMOUSE_MAZE_LENGTH * 4) + 1;
        int expectedColumnLength = (MICROMOUSE_MAZE_LENGTH * 2) + 1;
        int expectedASCIIGridSize = (expectedRowLength * expectedColumnLength) + expectedColumnLength;
        StringBuilder sb = new StringBuilder(expectedASCIIGridSize);

        for (MazeCell[] row : this.grid) {
            for (MazeCell mazeCell : row) {
                sb.append(mazeCell.northWall() ? "+---" : "+   ");
            }
            sb.append("+\n");

            for (MazeCell mazeCell : row) {
                sb.append(mazeCell.westWall() ? "|   " : "    ");
            }
            MazeCell lastMazeCellInRow = row[row.length-1];
            sb.append(lastMazeCellInRow.eastWall() ? "|\n" : " \n");
        }

        MazeCell[] lastRow = this.grid[this.grid.length-1];
        for (MazeCell mazeCell : lastRow) {
            sb.append(mazeCell.southWall() ? "+---" : "+   ");
        }
        sb.append("+\n");

        return sb.toString();
    }


}
