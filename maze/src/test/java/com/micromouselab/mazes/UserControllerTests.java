package com.micromouselab.mazes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micromouselab.mazes.domain.*;
import com.micromouselab.mazes.service.AuthService;
import com.micromouselab.mazes.service.UserService;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerTests {

    private final UserService userService;

    private final AuthService authService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, AuthService authService, UserService userService){
        this.mockMvc = mockMvc;
        this.authService = authService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    private final String insecure_password = "INSECURE_PASSWORD";

    private final RegisterDTO userRegistrationA = new RegisterDTO("A", insecure_password);
    private final RegisterDTO userRegistrationB = new RegisterDTO("B", insecure_password);
    private final RegisterDTO userRegistrationC = new RegisterDTO("C", insecure_password);
    private final RegisterDTO userRegistrationD = new RegisterDTO("D", insecure_password);
    private final RegisterDTO userRegistrationE = new RegisterDTO("E", insecure_password);
    private final RegisterDTO userRegistrationF = new RegisterDTO("F", insecure_password);
    private final RegisterDTO userRegistrationG = new RegisterDTO("G", insecure_password);


    private void initializeTestUsers(){
        this.authService.registerUser(userRegistrationA);
        this.authService.registerUser(userRegistrationB);
        this.authService.registerUser(userRegistrationC);
        this.authService.registerUser(userRegistrationD);
        this.authService.registerUser(userRegistrationE);
        this.authService.registerUser(userRegistrationF);
        this.authService.registerUser(userRegistrationG);
    }

    @Test
    public void testFindAllUsersReturnsAllUsers() throws Exception {
        this.initializeTestUsers();

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO[] users = objectMapper.readValue(responseBody, UserDTO[].class);

        assertEquals(7, users.length);

        List<String> expectedNames = Arrays.asList(
                userRegistrationA.username(),
                userRegistrationB.username(),
                userRegistrationC.username(),
                userRegistrationD.username(),
                userRegistrationE.username(),
                userRegistrationF.username(),
                userRegistrationG.username()
        );
        List<String> actualNames = Arrays.stream(users).map(UserDTO::username).toList();

        assertTrue(actualNames.containsAll(expectedNames));

    }

    @Test
    public void testFindByIdReturnsUserWhenUserExists() throws Exception {
        this.initializeTestUsers();

        Long validId = 5L;
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testFindByIdReturnsHTTTP404WhenUserDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testCanRetrieveUserByUsernameAndPromoteToApproved() throws Exception {

        this.initializeTestUsers();

        String responseToRetrieveByUsername = mockMvc.perform(get("/api/v1/users/by-username/" + this.userRegistrationA.username()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO userDTO = objectMapper.readValue(responseToRetrieveByUsername, UserDTO.class);

        assertEquals(1, userDTO.id());
        assertEquals(Role.USER, userDTO.role());
        assertEquals(this.userRegistrationA.username(), userDTO.username());

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO(Role.APPROVED);

        String responseToRoleChange = mockMvc.perform(
                put("/api/v1/users/" + userDTO.id() + "/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRoleUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO updatedUserDTO = objectMapper.readValue(responseToRoleChange, UserDTO.class);

        assertEquals(1, updatedUserDTO.id());
        assertEquals(Role.APPROVED, updatedUserDTO.role());
        assertEquals(this.userRegistrationA.username(), updatedUserDTO.username());

    }

    @Test
    public void testCanRetrieveUserByUserIdAndPromoteToApproved() throws Exception {

        this.initializeTestUsers();

        Long userCId = 3L;
        String responseToRetrieveByUsername = mockMvc.perform(get("/api/v1/users/" + userCId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO userDTO = objectMapper.readValue(responseToRetrieveByUsername, UserDTO.class);

        assertEquals(userCId, userDTO.id());
        assertEquals(Role.USER, userDTO.role());
        assertEquals(this.userRegistrationC.username(), userDTO.username());

        UserRoleUpdateDTO userRoleUpdateDTO = new UserRoleUpdateDTO(Role.APPROVED);

        String responseToRoleChange = mockMvc.perform(
                        put("/api/v1/users/" + userDTO.id() + "/role")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRoleUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO updatedUserDTO = objectMapper.readValue(responseToRoleChange, UserDTO.class);

        assertEquals(userCId, updatedUserDTO.id());
        assertEquals(Role.APPROVED, updatedUserDTO.role());
        assertEquals(this.userRegistrationC.username(), updatedUserDTO.username());

    }


}
