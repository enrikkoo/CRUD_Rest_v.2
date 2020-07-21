package com.example.Rest;

import com.example.Rest.controller.UserController;
import com.example.Rest.model.User;
import com.example.Rest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    public void readUsersTest() throws Exception {

        HashMap<Integer, User> map = new HashMap<>();
        map.put(1, new User("Erik", "Osipov", "18.03.1998"));

        when(userService.read()).thenReturn(map);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*.*", hasSize(3)))
                .andExpect(jsonPath("$.*.name").value("Erik"))
                .andExpect(jsonPath("$.1.surname").value("Osipov"))
                .andExpect(jsonPath("$.1.birthday").value("18.03.1998"));

        verify(userService).read();
    }


    @Test
    public void createUsersTest() throws Exception {
        User user = new User("Erik", "Osipov", "18.03.1998");

        when(userService.create(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/users")
                .content(om.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Erik"))
                .andExpect(jsonPath("$.surname").value("Osipov"))
                .andExpect(jsonPath("$.birthday").value("18.03.1998"))
                .andExpect(status().isCreated());

        verify(userService).create(any(User.class));
    }

    @Test
    public void createUsersTest400() throws Exception {
        User user = new User("Erik123", "Osipov123", "18.03.1998");

        when(userService.create(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/users")
                .content(om.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService).create(any(User.class));
    }


    @Test
    public void readUserByIdTest() throws Exception {
        User user = new User("Erik", "Osipov", "18.03.1998");
        when(userService.readUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Erik"))
                .andExpect(jsonPath("$.surname").value("Osipov"))
                .andExpect(jsonPath("$.birthday").value("18.03.1998"))
                .andExpect(status().isOk());

        verify(userService).readUserById(1);
    }


    @Test
    public void updateTest() throws Exception {
        User user = new User("Erik", "Osipov", "18.03.1998");

        when(userService.update(anyInt(), any(User.class))).thenReturn(true);
        mockMvc.perform(put("/users/{id}", 3)
                .content(om.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Erik"))
                .andExpect(jsonPath("$.surname").value("Osipov"))
                .andExpect(jsonPath("$.birthday").value("18.03.1998"))
                .andExpect(status().isOk());

        verify(userService).update(anyInt(), any(User.class));
    }

    @Test
    public void deleteTest() throws Exception {
        User user = new User("Erik", "Osipov", "18.03.1998");

        when(userService.delete(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/users/{id}", 1)
                .content(om.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUsersTest404() throws Exception {
        mockMvc.perform(post("/user"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readUserByIdTest404() throws Exception {
        when(userService.readUserById(1)).thenReturn(null);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readUsersTest404() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }
}
