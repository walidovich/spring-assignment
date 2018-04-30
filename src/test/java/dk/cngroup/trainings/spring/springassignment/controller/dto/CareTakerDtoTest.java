package dk.cngroup.trainings.spring.springassignment.controller.dto;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import org.junit.Assert;
import org.junit.Test;
import org.modelmapper.ModelMapper;

public class CareTakerDtoTest {
	private static final ModelMapper modelMapper = new ModelMapper();

	@Test
	public void careTakerLinkIdDtoTest() {
		CareTakerLinkIdDTO careTakerLinkIdDTO = new CareTakerLinkIdDTO(3L);
		CareTaker careTaker = modelMapper.map(careTakerLinkIdDTO, CareTaker.class);

		CareTaker careTaker2 = new CareTaker(4L, "Walid");
		CareTakerLinkIdDTO careTakerLinkIdDTO2 = modelMapper.map(careTaker2, CareTakerLinkIdDTO.class);

		Assert.assertEquals(careTaker.getId(), careTakerLinkIdDTO.getId());
		Assert.assertEquals(careTaker2.getId(), careTakerLinkIdDTO2.getId());
	}

	@Test
	public void careTakerCreateDtoTest() {
		CareTakerCreateDTO careTakerCreateDTO = new CareTakerCreateDTO("Walid");
		CareTaker careTaker = CareTakerEntityDtoConverter.toCareTakerEntity(careTakerCreateDTO);

		CareTaker careTaker2 = new CareTaker(4L, "Walid");
		CareTakerCreateDTO careTakerCreateDTO2 = CareTakerEntityDtoConverter.toCareTakerCreateDTO(careTaker2);

		Assert.assertEquals(careTaker.getName(), careTakerCreateDTO.getName());
		Assert.assertEquals(careTaker2.getName(), careTakerCreateDTO2.getName());
	}
}
