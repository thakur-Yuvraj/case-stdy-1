package com.cropdeal.farmer;

import com.cropdeal.farmer.controller.FarmerController;
import com.cropdeal.farmer.dto.CropDto;
import com.cropdeal.farmer.service.FarmerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FarmerApplicationTest {

	private MockMvc mockMvc;

	@Mock
	private FarmerService farmerService;

	@InjectMocks
	private FarmerController farmerController;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(farmerController).build();
	}

	@Test
	void home_shouldReturnWelcomeMessage() throws Exception {
		when(farmerService.home()).thenReturn(ResponseEntity.ok("Welcome to Farmer Service"));

		mockMvc.perform(get("/api/farmer"))
				.andExpect(status().isOk())
				.andExpect(content().string("Welcome to Farmer Service"));
	}

	@Test
	void createFarmer_shouldReturnSuccess() throws Exception {
		String json = """
            {
              "email": "farmer@example.com",
              "password": "Password123",
              "fullName": "Farmer Joe",
              "phone": "+911234567890",
              "address": "Farmville",
              "role": "FARMER"
            }
            """;

		when(farmerService.createFarmer(any())).thenReturn(ResponseEntity.ok("Farmer created"));

		mockMvc.perform(post("/api/farmer/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().string("Farmer created"));
	}

	@Test
	void addCrop_shouldReturnSuccess() throws Exception {
		String json = """
            {
              "farmerId": 1,
              "cropType": "vegetable",
              "cropName": "Tomato",
              "quantity": 100,
              "pricePerUnit": 10.5,
              "address": "Green Valley",
              "isAvailable": "Yes"
            }
            """;

		when(farmerService.addCrop(eq(1), any(CropDto.class)))
				.thenReturn(ResponseEntity.ok("Crop added"));

		mockMvc.perform(post("/api/farmer/crop")
						.header("X-User-ID", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().string("Crop added"));
	}

	@Test
	void removeCrop_shouldReturnSuccess() throws Exception {
		when(farmerService.removeCrop(1, 42))
				.thenReturn(ResponseEntity.ok("Crop removed"));

		mockMvc.perform(delete("/api/farmer/crop/42")
						.header("X-User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(content().string("Crop removed"));
	}

	@Test
	void getProfile_shouldReturnProfile() throws Exception {
		when(farmerService.getProfile(1))
				.thenReturn(ResponseEntity.ok("Farmer Profile Info"));

		mockMvc.perform(get("/api/farmer/profile")
						.header("X-User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(content().string("Farmer Profile Info"));
	}

	@Test
	void removeFarmer_shouldReturnSuccess() throws Exception {
		String email = "farmer@example.com";
		when(farmerService.removeFarmer(email))
				.thenReturn(ResponseEntity.ok("Farmer removed"));

		mockMvc.perform(delete("/api/farmer/remove/" + email))
				.andExpect(status().isOk())
				.andExpect(content().string("Farmer removed"));
	}
}