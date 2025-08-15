package com.micromouselab.maze.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.micromouselab.maze.repository.MazeCollectionRepository;
import com.micromouselab.maze.model.Maze;

@RestController
@RequestMapping(path = "/mazes")
public class MazeController {

    private final MazeCollectionRepository repository;

    @Autowired
    public MazeController(MazeCollectionRepository repository){
        this.repository = repository;
    }

    @GetMapping("")
    public Collection<Maze> findAll(){
        return repository.findAll();
    }
    
    @GetMapping("/{mazeId}")
    public Maze findById(@PathVariable Long mazeId){
        return repository.findById(mazeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void save(@RequestBody Maze maze){
        this.repository.save(maze);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{mazeId}")
    public void update(@RequestBody Maze maze, @PathVariable Long mazeId){

        if (!repository.existsById(mazeId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!");
        }

        repository.save(maze);
    }

    @DeleteMapping("/{mazeId}")
    public void delete(@PathVariable Long mazeId){

        if (!repository.existsById(mazeId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!");
        }

        repository.delete(mazeId);
    }
}
