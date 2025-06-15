package com.cropdeal.user;

import com.cropdeal.user.controller.UserController;
import com.cropdeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	void home_shouldReturnWelcomeMessage() throws Exception {
		when(userService.home()).thenReturn(ResponseEntity.ok("Welcome to CropDeal User Service"));

		mockMvc.perform(get("/api/user"))
				.andExpect(status().isOk())
				.andExpect(content().string("Welcome to CropDeal User Service"));
	}

	@Test
	void register_shouldReturnSuccess() throws Exception {
		String json = """
                {
                  "email": "test@example.com",
                  "password": "Password123",
                  "fullName": "Test User",
                  "phone": "+919876543210",
                  "address": "Chennai",
                  "role": "FARMER"
                }
                """;

		when(userService.register(any()))
				.thenReturn(ResponseEntity.ok("User registered"));

		mockMvc.perform(post("/api/user/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().string("User registered"));
	}

	@Test
	void removeUser_shouldReturnSuccess() throws Exception {
		String email = "test@example.com";
		when(userService.removeUser(email))
				.thenReturn(ResponseEntity.ok("User removed"));

		mockMvc.perform(post("/api/user/remove/" + email))
				.andExpect(status().isOk())
				.andExpect(content().string("User removed"));
	}
}