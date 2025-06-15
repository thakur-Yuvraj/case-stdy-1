package com.cropdeal.dealer;

import com.cropdeal.dealer.controller.DealerController;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.service.DealerService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DealerApplicationTests {

	private MockMvc mockMvc;

	@Mock
	private DealerService dealerService;

	@InjectMocks
	private DealerController dealerController;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(dealerController).build();
	}

	@Test
	void testHomeEndpoint() throws Exception {
		when(dealerService.test()).thenReturn(ResponseEntity.ok("Dealer Service is up"));

		mockMvc.perform(get("/api/dealer"))
				.andExpect(status().isOk())
				.andExpect(content().string("Dealer Service is up"));
	}

	@Test
	void testCreateDealer() throws Exception {
		String json = """
            {
              "email": "dealer@example.com",
              "password": "Password123",
              "fullName": "Dealer Dan",
              "phone": "+911234567890",
              "address": "Market Street",
              "role": "DEALER"
            }
            """;

		when(dealerService.createDealer(any(UserDto.class)))
				.thenReturn(ResponseEntity.ok("Dealer created"));

		mockMvc.perform(post("/api/dealer/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().string("Dealer created"));
	}

	@Test
	void testGetAllCrop() throws Exception {
		when(dealerService.getAllCrop()).thenReturn(ResponseEntity.ok(List.of()));

		mockMvc.perform(get("/api/dealer/find/all/crop"))
				.andExpect(status().isOk());
	}

	@Test
	void testPurchaseCrop() throws Exception {
		when(dealerService.makePurchase(10, 1))
				.thenReturn(ResponseEntity.ok("Purchase successful"));

		mockMvc.perform(post("/api/dealer/purchase/10")
						.header("X-User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(content().string("Purchase successful"));
	}

	@Test
	void testRemoveDealer() throws Exception {
		String email = "dealer@example.com";
		when(dealerService.removeDealer(email))
				.thenReturn(ResponseEntity.ok("Dealer removed"));

		mockMvc.perform(delete("/api/dealer/remove/" + email))
				.andExpect(status().isOk())
				.andExpect(content().string("Dealer removed"));
	}
}