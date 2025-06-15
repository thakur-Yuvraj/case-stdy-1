package com.cropdeal.payment;

import com.cropdeal.payment.dto.PaymentRequestDTO;
import com.cropdeal.payment.model.PaymentTransaction;
import com.cropdeal.payment.repository.PaymentTransactionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PaymentTransactionRepository paymentTransactionRepository;

	private String url(String path) {
		return "http://localhost:" + port + path;
	}

	@BeforeEach
	void cleanUp() {
		paymentTransactionRepository.deleteAll();
	}

	@Test
	void shouldCreateOrderAndPersistTransaction() throws JSONException {
		PaymentRequestDTO requestDto = new PaymentRequestDTO("dealer123", "farmer456", 100, "INR");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<PaymentRequestDTO> request = new HttpEntity<>(requestDto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				url("/api/payment/create-order"), request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		JSONObject json = new JSONObject(response.getBody());
		String orderId = json.getString("id");

		Optional<PaymentTransaction> saved = Optional.ofNullable(paymentTransactionRepository.findByRazorpayOrderId(orderId));
		assertThat(saved).isPresent();
		assertThat(saved.get().getDealerId()).isEqualTo("dealer123");
		assertThat(saved.get().getStatus()).isEqualTo("created");
	}

	@Test
	void shouldUpdateTransactionOnSuccess() {
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setDealerId("D001");
		transaction.setFarmerId("F001");
		transaction.setAmount(200);
		transaction.setCurrency("INR");
		transaction.setRazorpayOrderId("ORD001");
		transaction.setStatus("created");
		paymentTransactionRepository.save(transaction);

		ResponseEntity<String> response = restTemplate.postForEntity(
				url("/api/payment/payment-success?razorpay_order_id=ORD001&razorpay_payment_id=PAY001"),
				null, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		PaymentTransaction updated = paymentTransactionRepository.findByRazorpayOrderId("ORD001");
		assertThat(updated.getStatus()).isEqualTo("success");
		assertThat(updated.getRazorpayPaymentId()).isEqualTo("PAY001");
	}

	@Test
	void shouldUpdateTransactionOnFailure() {
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setDealerId("D001");
		transaction.setFarmerId("F001");
		transaction.setAmount(200);
		transaction.setCurrency("INR");
		transaction.setRazorpayOrderId("ORD002");
		transaction.setStatus("created");
		paymentTransactionRepository.save(transaction);

		ResponseEntity<String> response = restTemplate.postForEntity(
				url("/api/payment/payment-failure?razorpay_order_id=ORD002&error_code=E01&error_description=DECLINED"),
				null, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		PaymentTransaction updated = paymentTransactionRepository.findByRazorpayOrderId("ORD002");
		assertThat(updated.getStatus()).isEqualTo("failed");
	}
}