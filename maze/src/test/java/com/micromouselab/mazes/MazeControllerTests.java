package com.micromouselab.mazes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MazeControllerTests {

    private final MazeService mazeService;

    private final MazeRepository mazeRepository;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public MazeControllerTests(MockMvc mockMvc, MazeService mazeService, MazeRepository mazeRepository){
        this.mockMvc = mockMvc;
        this.mazeService = mazeService;
        this.mazeRepository = mazeRepository;
        this.objectMapper = new ObjectMapper();
    }

    private void initializeTestMazes(){
        MazeEntity mazeEntity1 = new MazeEntity(null, MazeTestConstants.maze1Base64Digest, "Maze 1");
        MazeEntity mazeEntity2 = new MazeEntity(null, MazeTestConstants.maze2Base64Digest, "Maze 2");
        MazeEntity mazeEntity3 = new MazeEntity(null, MazeTestConstants.maze3Base64Digest, "Maze 3");
        MazeEntity mazeEntity4 = new MazeEntity(null, MazeTestConstants.maze4Base64Digest, "Maze 4");
        MazeEntity mazeEntity5 = new MazeEntity(null, MazeTestConstants.maze5Base64Digest, "Maze 5");
        MazeEntity mazeEntity6 = new MazeEntity(null, MazeTestConstants.maze6Base64Digest, "Maze 6");
        MazeEntity mazeEntity7 = new MazeEntity(null, MazeTestConstants.maze7Base64Digest, "Maze 7");
        this.mazeRepository.save(mazeEntity1);
        this.mazeRepository.save(mazeEntity2);
        this.mazeRepository.save(mazeEntity3);
        this.mazeRepository.save(mazeEntity4);
        this.mazeRepository.save(mazeEntity5);
        this.mazeRepository.save(mazeEntity6);
        this.mazeRepository.save(mazeEntity7);
    }



    @Test
    public void testFindAllMazesReturnsAllMazes() throws Exception {
        this.initializeTestMazes();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mazes")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        MazeDTO[] mazes = objectMapper.readValue(responseBody, MazeDTO[].class);

        assertEquals(7, mazes.length);

        List<String> expectedNames = Arrays.asList("Maze 1", "Maze 2", "Maze 3", "Maze 4", "Maze 5", "Maze 6", "Maze 7");
        List<String> actualNames = Arrays.stream(mazes).map(MazeDTO::description).collect(Collectors.toList());

        assertTrue(actualNames.containsAll(expectedNames));





    }

    @Test
    public void testFindAllMazesReturnNothingWhenNoMazesExist(){

    }

    @Test
    public void testFindByIdReturnsMazeWhenMazeExists(){

    }

    @Test
    public void testFindByIdReturnsHTTTP404WhenMazeDoesNotExist(){

    }

    @Test
    public void testCreateMazeSuccessfullyReturnsHTTP201(){

    }

    @Test
    public void testCreateMazeSuccessfullyAddsMazeToDatabase(){

    }

    @Test
    public void testCreateMazeUnSuccessfullyReturnsHTTP422(){

    }

    @Test
    public void testDeleteMazeReturns204ForNonExistingMaze(){

    }

    @Test
    public void testDeleteMazeReturns204ForExistingMaze(){

    }





}
