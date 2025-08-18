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

    protected MazeEntity() {}

    public MazeEntity(String rawRepresentation){
        this.rawRepresentation = rawRepresentation;
    }

    public Long getId(){
        return this.id;
    }

    public String getRawRepresentation(){
        return this.rawRepresentation;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setRawRepresentation(String rawRepresentation){
        this.rawRepresentation = rawRepresentation;
    }



}
