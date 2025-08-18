package com.micromouselab.mazes;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MazeService {

    private final MazeRepository mazeRepository;

    public MazeService(MazeRepository mazeRepository){
        this.mazeRepository = mazeRepository;
    }

    MazeDTO save(MazeDTO mazeDTO){
        return null;
    }

    Optional<MazeDTO> findById(Long id){
        return null;
    }


    boolean existsById(Long id){
        return id == 0L;
    }


    Iterable<MazeDTO> findAll(){
        return null;
    }



    void delete(Long id){
        return;
    }
}
