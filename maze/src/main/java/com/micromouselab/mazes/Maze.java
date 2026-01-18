package com.micromouselab.mazes;

import java.util.Base64;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.Queue;

public class Maze {

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


    private MazeCell[][] mazeGrid;

    private final MazeFormat mazeFormat;

    private final String description;

    public Maze(String representation, MazeFormat mazeFormat, String description) throws InvalidMicroMouseMazeException {
        try {
            this.mazeGrid = switch (mazeFormat) {
                case B64_DIGEST -> createGridFromB64DigestFormat(representation);
                case HEX_DIGEST -> createGridFromHexDigestFormat(representation);
                case ASCII_GRID -> createGridFromASCIIGridFormat(representation);
            };
        } catch (IllegalArgumentException e) {
            throw new InvalidMicroMouseMazeException("Maze format was not valid");
        }
        this.mazeFormat = mazeFormat;
        this.description = description;
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

        // check consistency of walls between adjacent cells
        for (int rowIndex = 0; rowIndex < MICROMOUSE_MAZE_LENGTH; rowIndex++){
            for (int columnIndex = 0; columnIndex < MICROMOUSE_MAZE_LENGTH; columnIndex++){
                MazeCell currentCell = newGrid[rowIndex][columnIndex];

                if (columnIndex < MICROMOUSE_MAZE_LENGTH - 1){
                    MazeCell eastCell = newGrid[rowIndex][columnIndex + 1];
                    if (currentCell.eastWall() != eastCell.westWall()){
                        throw new IllegalArgumentException("Inconsistent wall definitions between adjacent cells.");
                    }
                }

                if (rowIndex < MICROMOUSE_MAZE_LENGTH - 1){
                    MazeCell southCell = newGrid[rowIndex + 1][columnIndex];
                    if (currentCell.southWall() != southCell.northWall()){
                        throw new IllegalArgumentException("Inconsistent wall definitions between adjacent cells.");
                    }
                }
            }
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

    public String getDescription(){
        return this.description;
    }

    public boolean isValidMicromouseMaze(){
        if (this.mazeGrid.length != MICROMOUSE_MAZE_LENGTH){
            return false;
        }
        for (MazeCell[] row : this.mazeGrid){
            if (row.length != MICROMOUSE_MAZE_LENGTH){
                return false;
            }
        }

        if (!allOuterWallsArePresent()){
            return false;
        }

        if (solvableWithLeftWallFollower()){
            return false;
        }

        if (solvableWithRightWallFollower()){
            return false;
        }


        if (!allCellsAreReachable()){
            return false;
        }

        return true;
    }

    private boolean allOuterWallsArePresent(){
        for (int i = 0; i < MICROMOUSE_MAZE_LENGTH; i++){
            if (!this.mazeGrid[0][i].northWall()){
                return false;
            }
            if (!this.mazeGrid[MICROMOUSE_MAZE_LENGTH - 1][i].southWall()){
                return false;
            }
            if (!this.mazeGrid[i][0].westWall()){
                return false;
            }
            if (!this.mazeGrid[i][MICROMOUSE_MAZE_LENGTH - 1].eastWall()){
                return false;
            }
        }
        return true;
    }

    private boolean solvableWithLeftWallFollower(){  
        // TODO
        return false;
    }

    private boolean solvableWithRightWallFollower(){
        // TODO
        return false;
    }

    private boolean allCellsAreReachable(){
        final class CellPosition {
            int row;
            int column;

            CellPosition(int row, int column){
                this.row = row;
                this.column = column;
            }
        }

        // Check that every cell is reachable from every other cell (BFS)
        boolean[][] visited_matrix = new boolean[MICROMOUSE_MAZE_LENGTH][MICROMOUSE_MAZE_LENGTH];
        int visited_number = 0;
        Queue<CellPosition> queue = new LinkedList<>();
        queue.add(new CellPosition(0, 0));
        visited_matrix[0][0] = true;
        visited_number++;
        while (!queue.isEmpty()){
            CellPosition current_cell_position = queue.poll();
            MazeCell current_cell = this.mazeGrid[current_cell_position.row][current_cell_position.column];
            if (current_cell.northWall() == false && !visited_matrix[current_cell_position.row - 1][current_cell_position.column]){
                queue.add(new CellPosition(current_cell_position.row - 1, current_cell_position.column));
                visited_matrix[current_cell_position.row - 1][current_cell_position.column] = true;
                visited_number++;
            }
            if (current_cell.eastWall() == false && !visited_matrix[current_cell_position.row][current_cell_position.column + 1]){
                queue.add(new CellPosition(current_cell_position.row, current_cell_position.column + 1));
                visited_matrix[current_cell_position.row][current_cell_position.column + 1] = true;
                visited_number++;
            }
            if (current_cell.southWall() == false && !visited_matrix[current_cell_position.row + 1][current_cell_position.column]){
                queue.add(new CellPosition(current_cell_position.row + 1, current_cell_position.column));
                visited_matrix[current_cell_position.row + 1][current_cell_position.column] = true;
                visited_number++;
            }
            if (current_cell.westWall() == false && !visited_matrix[current_cell_position.row][current_cell_position.column - 1]){
                queue.add(new CellPosition(current_cell_position.row, current_cell_position.column - 1));
                visited_matrix[current_cell_position.row][current_cell_position.column - 1] = true;
                visited_number++;
            }
        }
        return visited_number == MICROMOUSE_MAZE_SIZE;
            
    }

    private String getRepresentationAsBase64Format(){
        byte[] byteArray = this.cellGridToByteArray(this.mazeGrid);
        String base64EncodedString = Base64.getEncoder().encodeToString(byteArray);
        return base64EncodedString;
    }

    private String getRepresentationAsHexFormat(){
        byte[] byteArray = this.cellGridToByteArray(this.mazeGrid);
        String hexEncodedString = HexFormat.of().formatHex(byteArray);
        return hexEncodedString;
    }

    private byte[] cellGridToByteArray(MazeCell[][] mazeCells){
        byte[] byteArray = new byte[MICROMOUSE_MAZE_SIZE];
        int i = 0;
        for (MazeCell[] cellRow : this.mazeGrid){
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

        for (MazeCell[] row : this.mazeGrid) {
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

        MazeCell[] lastRow = this.mazeGrid[this.mazeGrid.length-1];
        for (MazeCell mazeCell : lastRow) {
            sb.append(mazeCell.southWall() ? "+---" : "+   ");
        }
        sb.append("+\n");

        return sb.toString();
    }



}
