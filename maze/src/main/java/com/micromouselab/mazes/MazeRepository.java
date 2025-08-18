package com.micromouselab.mazes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MazeRepository extends CrudRepository<MazeEntity, Long> {
    
}
