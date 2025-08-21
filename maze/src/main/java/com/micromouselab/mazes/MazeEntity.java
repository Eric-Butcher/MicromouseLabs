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
    private String base64Representation;

    private String description;

    protected MazeEntity() {}

    public MazeEntity(String rawRepresentation){
        this.base64Representation = rawRepresentation;
    }

    public MazeEntity(String rawRepresentation, String description){
        this.base64Representation = rawRepresentation;
        this.description = description;
    }

    public MazeEntity(Long id, String rawRepresentation, String description){
        this.id = id;
        this.base64Representation = rawRepresentation;
        this.description = description;
    }


    public Long getId(){
        return this.id;
    }

    public String getDescription() {return this.description;}

    public String getRawRepresentation(){
        return this.base64Representation;
    }


    public void setId(Long id){
        this.id = id;
    }

    public void setRawRepresentation(String rawRepresentation){
        this.base64Representation = rawRepresentation;
    }




}
