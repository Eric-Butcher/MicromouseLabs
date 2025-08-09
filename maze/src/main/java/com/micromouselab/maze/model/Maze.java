package com.micromouselab.maze.model;

public record Maze(
    Long Id,
    String name,
    String representation,
    MazeType mazeType,
    Boolean is_public
    // User[] owners
){}
