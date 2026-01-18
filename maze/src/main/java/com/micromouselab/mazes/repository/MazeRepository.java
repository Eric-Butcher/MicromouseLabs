package com.micromouselab.mazes.repository;

import com.micromouselab.mazes.domain.MazeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MazeRepository extends JpaRepository<MazeEntity, Long> {
    
}
