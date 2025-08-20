package com.micromouselab.mazes;

public record MazeCell(
        boolean northWall,
        boolean eastWall,
        boolean southWall,
        boolean westWall
) {

    private static final byte NORTH_MASK = (byte) 0b10000000;
    private static final byte EAST_MASK = 0b01000000;
    private static final byte SOUTH_MASK = 0b00100000;
    private static final byte WEST_MASK = 0b00010000;

    public static MazeCell fromByte(byte cellChar){

        boolean northWallVal = (cellChar & NORTH_MASK) == NORTH_MASK;
        boolean eastWallVal = (cellChar & EAST_MASK) == EAST_MASK;
        boolean southWallVal = (cellChar & SOUTH_MASK) == SOUTH_MASK;
        boolean westWallVal = (cellChar & WEST_MASK) == WEST_MASK;
        return new MazeCell(northWallVal, eastWallVal, southWallVal, westWallVal);
    }

    public byte toByte(){
        byte byteVal = 0;
        if (northWall){
            byteVal |= NORTH_MASK;
        }
        if (eastWall){
            byteVal |= EAST_MASK;
        }
        if (southWall){
            byteVal |= SOUTH_MASK;
        }
        if (westWall){
            byteVal |= WEST_MASK;
        }
        return byteVal;
    }
}
