package com.micromouselab.maze.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.micromouselab.maze.model.Maze;
import com.micromouselab.maze.model.MazeType;

import jakarta.annotation.PostConstruct;


@Repository
public class MazeCollectionRepository {
    private final Map<Long, Maze> mazes = new HashMap<>();

    public MazeCollectionRepository(){}

    public Collection<Maze> findAll(){
        return mazes.values();
    }

    public Optional<Maze> findById(Long Id){
        return Optional.ofNullable(this.mazes.get(Id));
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
        mazes.put(m.Id(), m);

    }

    public void save(Maze maze){
        this.mazes.put(maze.Id(), maze);
    }

    public boolean existsById(Long Id){
        return this.mazes.containsKey(Id);
    }

    public void delete(Long Id){
        this.mazes.remove(Id);
    }

}
