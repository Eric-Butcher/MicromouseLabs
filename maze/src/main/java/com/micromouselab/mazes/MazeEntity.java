package com.micromouselab.mazes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class MazeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String rawRepresentation;

    private String description;

    private MazeFormat mazeFormat;

    protected MazeEntity() {}

    public MazeEntity(String rawRepresentation){
        this.rawRepresentation = rawRepresentation;
    }

    public  MazeEntity(String rawRepresentation, String description){
        this.rawRepresentation = rawRepresentation;
        this.description = description;
        this.mazeFormat = MazeFormat.B64_DIGEST;
    }

    public  MazeEntity(String rawRepresentation, String description, MazeFormat mazeFormat){
        this.rawRepresentation = rawRepresentation;
        this.description = description;
        this.mazeFormat = mazeFormat;
    }

    public Long getId(){
        return this.id;
    }

    public String getDescription() {return this.description;}

    public String getRawRepresentation(){
        return this.rawRepresentation;
    }

    public MazeFormat getMazeFormat(){
        return this.mazeFormat;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setRawRepresentation(String rawRepresentation){
        this.rawRepresentation = rawRepresentation;
    }

    public void setMazeFormat(MazeFormat mazeFormat){
        this.mazeFormat = mazeFormat;
    }



}
