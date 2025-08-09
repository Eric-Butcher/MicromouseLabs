package com.micromouselab.maze.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.micromouselab.maze.model.Maze;
import com.micromouselab.maze.model.MazeType;

import jakarta.annotation.PostConstruct;


@Repository
public class MazeCollectionRepository {
    private final List<Maze> mazes = new ArrayList<>();

    public MazeCollectionRepository(){}

    public List<Maze> findAll(){
        return mazes;
    }

    public Optional<Maze> findById(Long Id){
        return mazes.stream().filter(c -> c.Id().equals(Id)).findFirst();
    }

    @PostConstruct
    private void init(){
        Maze m = new Maze(
            1L,
            "my name",
            "the representation",
            MazeType.SQUARE,
            true
        );
        mazes.add(m);

    }

    public void save(Maze maze){
        this.mazes.add(maze);
    }

}
