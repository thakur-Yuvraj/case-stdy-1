package com.cropdeal.crop;

import com.cropdeal.crop.dto.CropDto;
import com.cropdeal.crop.model.Crop;
import com.cropdeal.crop.repository.CropRepository;
import com.cropdeal.crop.service.CropService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CropServiceTest {

	@Mock
	private CropRepository cropRepository;

	@InjectMocks
	private CropService cropService;

	private Crop sampleCrop;

	@BeforeEach
	void setUp() {
		sampleCrop = Crop.builder()
				.Id(1)
				.farmerId(10)
				.cropType("vegetable")
				.cropName("Carrot")
				.quantity(100)
				.pricePerUnit(20.0)
				.address("Green Farm")
				.isAvailable("Yes")
				.build();
	}

	@Test
	void addCrop_ShouldReturnSuccessMessage_WhenCropDoesNotExist() {
		CropDto dto = new CropDto(10, "vegetable", "Carrot", 100, 20.0, "Green Farm", "Yes");

		when(cropRepository.existsById(anyInt())).thenReturn(false);
		when(cropRepository.save(any(Crop.class))).thenReturn(sampleCrop);

		ResponseEntity<String> response = cropService.addCrop(dto);

		assertThat(response.getBody()).contains("Crop added successfully");
		verify(cropRepository).save(any(Crop.class));
	}

	@Test
	void findByFarmerId_ShouldReturnListOfCropDto() {
		when(cropRepository.findByFarmerId(10)).thenReturn(List.of(sampleCrop));

		ResponseEntity<List<CropDto>> response = cropService.findByFarmerId(10);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).hasSize(1);
		assertThat(response.getBody().get(0).getCropName()).isEqualTo("Carrot");
	}

	@Test
	void removeCropByIdAndFarmerId_ShouldReturnOk_WhenCropExists() {
		when(cropRepository.existsByIdAndFarmerId(1, 10)).thenReturn(true);

		ResponseEntity<String> result = cropService.removeCropByIdAndFarmerId(1, 10);

		assertThat(result.getBody()).contains("deleted successfully");
		verify(cropRepository).deleteById(1);
	}

	@Test
	void setAvailabilityToNo_ShouldUpdateCrop_WhenCropExists() {
		when(cropRepository.existsById(1)).thenReturn(true);
		when(cropRepository.findById(1)).thenReturn(Optional.of(sampleCrop));
		when(cropRepository.save(any(Crop.class))).thenReturn(sampleCrop);

		ResponseEntity<String> response = cropService.setAvailabilityToNo(1);

		assertThat(response.getBody()).contains("updated successfully");
		verify(cropRepository).save(any(Crop.class));
	}

	@Test
	void validateCrop_ShouldReturnSuccess_WhenCropExists() {
		when(cropRepository.existsById(1)).thenReturn(true);

		Crop input = Crop.builder()
				.Id(1)
				.farmerId(10)
				.cropType("vegetable")
				.cropName("Carrot")
				.quantity(100)
				.pricePerUnit(20.0)
				.address("Green Farm")
				.isAvailable("Yes")
				.build();

		ResponseEntity<String> result = cropService.validateCrop(input);

		assertThat(result.getBody()).isEqualTo("Validation Successful");
	}

	@Test
	void findByCropId_ShouldReturnCropDto_WhenFound() {
		when(cropRepository.findById(1)).thenReturn(Optional.of(sampleCrop));

		ResponseEntity<CropDto> result = cropService.findByCropId(1);

		assertThat(result.getBody().getCropName()).isEqualTo("Carrot");
	}

	@Test
	void removeCropById_ShouldReturnNotFound_WhenCropDoesNotExist() {
		when(cropRepository.existsById(100)).thenReturn(false);

		ResponseEntity<String> result = cropService.removeCropById(100);

		assertThat(result.getStatusCodeValue()).isEqualTo(404);
		assertThat(result.getBody()).isEqualTo("Crop not found");
	}
}