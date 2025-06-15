package com.cropdeal.auth;

import com.cropdeal.auth.dto.AuthRequest;
import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.modal.Role;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String url(String path) {
		return "http://localhost:" + port + path;
	}

	@BeforeAll
    static void contextLoads() {
		// Ensures that Spring context loads without errors
	}

	@Test
	@Before("shouldGenerateJwtTokenForValidUser")
	void shouldRegisterUserSuccessfully() {
		UserDto userDto = UserDto.builder()
				.email("testuser@auth.com")
				.password("StrongPass1")
				.fullName("Test User")
				.phone("+911234567890")
				.address("123 Test Street")
				.role(Role.FARMER)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				url("/api/auth/register"), request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("User added successfully");
	}

	@Test
	void shouldGenerateJwtTokenForValidUser() {
		AuthRequest authRequest = AuthRequest.builder()
				.name("Test User")
				.password("StrongPass1")
				.role(Role.FARMER)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				url("/api/auth/token"), request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("eyJ"); // JWTs typically start with 'eyJ...'
	}

}