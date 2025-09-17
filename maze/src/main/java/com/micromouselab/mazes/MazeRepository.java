package com.micromouselab.mazes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MazeRepository extends JpaRepository<MazeEntity, Long> {
    
}
