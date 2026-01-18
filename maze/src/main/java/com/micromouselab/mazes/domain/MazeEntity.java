package com.micromouselab.mazes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class MazeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @Column(length = 512) // (maze_rows * maze_height) * base64increaseFactor = (16 * 16) * 1.4 = 358.4 => 512
    private String base64Representation;

    private String description;

    protected MazeEntity() {}

    public MazeEntity(String base64Representation){
        this.base64Representation = base64Representation;
    }

    public MazeEntity(String base64Representation, String description){
        this.base64Representation = base64Representation;
        this.description = description;
    }

    public MazeEntity(Long id, String base64Representation, String description){
        this.id = id;
        this.base64Representation = base64Representation;
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

    public void setRawRepresentation(String base64Representation){
        this.base64Representation = base64Representation;
    }




}
