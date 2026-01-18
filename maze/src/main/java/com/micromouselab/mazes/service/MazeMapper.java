package com.micromouselab.mazes.service;

import com.micromouselab.mazes.domain.*;
import org.springframework.stereotype.Component;

@Component
public class MazeMapper {

    public static MazeDTO mapToDTO(MazeEntity mazeEntity){
        MazeDTO mazeDTO = new MazeDTO(mazeEntity.getId(), mazeEntity.getDescription(), mazeEntity.getRawRepresentation(), MazeFormat.B64_DIGEST);
        return mazeDTO;
    }

    public static MazeDTO mapToDTO(MazeEntity mazeEntity, MazeFormat mazeFormat){
        Maze mazeGrid = new Maze(mazeEntity.getRawRepresentation(), MazeFormat.B64_DIGEST, mazeEntity.getDescription());
        String desiredRepresentation = mazeGrid.getRepresentation(mazeFormat);
        MazeDTO mazeDTO = new MazeDTO(mazeEntity.getId(), mazeEntity.getDescription(), desiredRepresentation, mazeFormat);
        return mazeDTO;
    }


    public static Maze mapToMaze(MazeCreateDTO mazeCreateDTO){
        return new Maze(mazeCreateDTO.representation(), mazeCreateDTO.mazeFormat(), mazeCreateDTO.description());
    }

    public static MazeEntity mapToMazeEntity(Maze maze){
        return new MazeEntity(maze.getRepresentation(MazeFormat.B64_DIGEST), maze.getDescription());
    }
}
