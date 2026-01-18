package com.micromouselab.mazes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micromouselab.mazes.domain.MazeCreateDTO;
import com.micromouselab.mazes.domain.MazeDTO;
import com.micromouselab.mazes.domain.MazeFormat;
import com.micromouselab.mazes.domain.MazeEntity;
import com.micromouselab.mazes.repository.MazeRepository;
import com.micromouselab.mazes.service.MazeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    public void testFindAllMazesReturnNothingWhenNoMazesExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mazes")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        MazeDTO[] mazes = objectMapper.readValue(responseBody, MazeDTO[].class);

        assertEquals(0, mazes.length);
    }

    @Test
    public void testFindByIdReturnsMazeWhenMazeExists() throws Exception {
        this.initializeTestMazes();
        MazeEntity mazeEntity = this.mazeRepository.findAll().get(0);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mazes/" + mazeEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testFindByIdReturnsHTTTP404WhenMazeDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/mazes/999")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }
    @Test
    public void testCreateMazeSuccessfullyReturnsHTTP201() throws Exception {
        MazeCreateDTO mazeDTO = new MazeCreateDTO("Maze Description", MazeTestConstants.maze1Base64Digest, MazeFormat.B64_DIGEST);
        String mazeJson = objectMapper.writeValueAsString(mazeDTO);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/mazes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mazeJson)
        );

        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void testCreateMazeSuccessfullyAddsMazeToDatabase(){
        MazeCreateDTO mazeDTO = new MazeCreateDTO("Maze Description", MazeTestConstants.maze1Base64Digest, MazeFormat.B64_DIGEST);
        this.mazeService.save(mazeDTO);

        Iterable<MazeEntity> mazes = this.mazeRepository.findAll();
        List<MazeEntity> mazeList = StreamSupport.stream(mazes.spliterator(), false).collect(Collectors.toList());

        assertEquals(1, mazeList.size());
        MazeEntity savedMaze = mazeList.get(0);
        assertEquals("Maze Description", savedMaze.getDescription());
        assertEquals(MazeTestConstants.maze1Base64Digest, savedMaze.getRawRepresentation());
    }

    @Test
    public void testCreateMazeUnSuccessfullyReturnsHTTP422() throws Exception, JsonProcessingException{
        MazeCreateDTO mazeDTO = new MazeCreateDTO("Maze Description", "InvalidRepresentation", MazeFormat.B64_DIGEST);
        String mazeJson = objectMapper.writeValueAsString(mazeDTO);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/mazes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mazeJson)
        );

        resultActions.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDeleteMazeReturns204ForNonExistingMaze() throws Exception {
        
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/mazes/999")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMazeReturns204ForExistingMaze(){
        this.initializeTestMazes();
        MazeEntity mazeEntity = this.mazeRepository.findAll().get(0);

        ResultActions resultActions;
        try {
            resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.delete("/mazes/" + mazeEntity.getId())
                            .contentType(MediaType.APPLICATION_JSON)
            );

            resultActions.andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean mazeExists = this.mazeRepository.existsById(mazeEntity.getId());
        assertFalse(mazeExists);
    }





}
