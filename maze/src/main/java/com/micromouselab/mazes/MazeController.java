package com.micromouselab.mazes;

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

import jakarta.validation.Valid;



@RestController
@RequestMapping(path = "/mazes")
public class MazeController {

    private final MazeService mazeService;

    @Autowired
    public MazeController(MazeService mazeService){
        this.mazeService = mazeService;
    }

    @GetMapping("")
    public Iterable<MazeDTO> findAllMazes(){
        return mazeService.findAll();
    }
    
    @GetMapping("/{mazeId}")
    public MazeDTO findById(@PathVariable Long mazeId){
        return mazeService.findById(mazeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void save(@Valid @RequestBody MazeCreateDTO mazeCreateDTO){
        this.mazeService.save(mazeCreateDTO);
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PutMapping("/{mazeId}")
//    public void update(@RequestBody MazeDTO maze){
//
//        if (!mazeService.existsById(maze.id())){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!");
//        }
//
//        mazeService.update(maze);
//    }

    @DeleteMapping("/{mazeId}")
    public void delete(@PathVariable Long mazeId){

        if (!mazeService.existsById(mazeId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!");
        }

        mazeService.delete(mazeId);
    }
}
