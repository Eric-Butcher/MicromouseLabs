package com.micromouselab.mazes.controller;

import com.micromouselab.mazes.domain.MazeCreateDTO;
import com.micromouselab.mazes.domain.MazeDTO;
import com.micromouselab.mazes.domain.MazeFormat;
import com.micromouselab.mazes.service.MazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;



@RestController
@RequestMapping(path = "/api/v1/mazes")
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
    public MazeDTO findById(@PathVariable Long mazeId, @RequestParam(defaultValue = "B64_DIGEST") MazeFormat format){
        return mazeService.findById(mazeId, format)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public MazeDTO save(@Valid @RequestBody MazeCreateDTO mazeCreateDTO){
        return this.mazeService.save(mazeCreateDTO);
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
