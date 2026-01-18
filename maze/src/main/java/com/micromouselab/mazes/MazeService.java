package com.micromouselab.mazes;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MazeService {

    private final MazeRepository mazeRepository;

    public MazeService(MazeRepository mazeRepository){
        this.mazeRepository = mazeRepository;
    }

    MazeDTO save(MazeCreateDTO mazeCreateDTO) throws InvalidMicroMouseMazeException {
        Maze maze = MazeMapper.mapToMaze(mazeCreateDTO);
        if (maze.isValidMicromouseMaze()){
            MazeEntity mazeEntity = MazeMapper.mapToMazeEntity(maze);
            MazeEntity savedMazeEntity = this.mazeRepository.save(mazeEntity);
            MazeDTO mazeDTO = MazeMapper.mapToDTO(savedMazeEntity);
            return mazeDTO;
        }
        throw new InvalidMicroMouseMazeException("Not a MicroMouse maze");

    }

    Optional<MazeDTO> findById(Long id){
        Optional<MazeEntity> optionalMazeEntity =  this.mazeRepository.findById(id);
        if (optionalMazeEntity.isPresent()){
            MazeEntity mazeEntity = optionalMazeEntity.get();
            MazeDTO mazeDTO = MazeMapper.mapToDTO(mazeEntity);
            return Optional.of(mazeDTO);
        }
        return Optional.empty();
    }


    boolean existsById(Long id){
        return this.mazeRepository.existsById(id);
    }


    Iterable<MazeDTO> findAll(){
        return this.mazeRepository.findAll().stream().map(MazeMapper::mapToDTO).toList();
    }



    void delete(Long id){
        this.mazeRepository.deleteById(id);
    }
}
