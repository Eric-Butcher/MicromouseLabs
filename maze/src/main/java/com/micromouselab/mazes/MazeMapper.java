package com.micromouselab.mazes;

import org.springframework.stereotype.Component;

@Component
public class MazeMapper {

    public static MazeDTO mapToDTO(MazeEntity mazeEntity){
        MazeDTO mazeDTO = new MazeDTO(mazeEntity.getId(), mazeEntity.getDescription(), mazeEntity.getRawRepresentation(), MazeFormat.B64_DIGEST);
        return mazeDTO;
    }

    public static MazeDTO mapToDTO(MazeEntity mazeEntity, MazeFormat mazeFormat){
        MazeGrid mazeGrid = new MazeGrid(mazeEntity.getRawRepresentation(), MazeFormat.B64_DIGEST);
        String desiredRepresentation = mazeGrid.getRepresentation(mazeFormat);
        MazeDTO mazeDTO = new MazeDTO(mazeEntity.getId(), mazeEntity.getDescription(), desiredRepresentation, mazeFormat);
        return mazeDTO;
    }


    public static MazeEntity mapToEntity(MazeDTO mazeDTO){
        MazeEntity mazeEntity;
        if (mazeDTO.mazeFormat() == MazeFormat.B64_DIGEST){
            mazeEntity = new MazeEntity(mazeDTO.id(), mazeDTO.representation(), mazeDTO.description());
            return mazeEntity;
        }

        MazeGrid mazeGrid = new MazeGrid(mazeDTO.representation(), mazeDTO.mazeFormat());
        mazeEntity = new MazeEntity(mazeDTO.id(), mazeGrid.getRepresentation(MazeFormat.B64_DIGEST), mazeDTO.description());
        return mazeEntity;
    }

    public static MazeEntity mapToEntity(MazeCreateDTO mazeCreateDTO){
        MazeEntity mazeEntity;
        if (mazeCreateDTO.mazeFormat() == MazeFormat.B64_DIGEST){
            mazeEntity = new MazeEntity(null, mazeCreateDTO.representation(), mazeCreateDTO.description());
            return mazeEntity;
        }

        MazeGrid mazeGrid = new MazeGrid(mazeCreateDTO.representation(), mazeCreateDTO.mazeFormat());
        mazeEntity = new MazeEntity(null, mazeGrid.getRepresentation(MazeFormat.B64_DIGEST), mazeCreateDTO.description());
        return mazeEntity;
    }
}
