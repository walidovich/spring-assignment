package dk.cngroup.trainings.spring.springassignment.controller.dto;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import org.modelmapper.ModelMapper;

public class CareTakerEntityDtoConverter {

	public static CareTaker toCareTakerEntity(CareTakerCreateDTO careTakerCreateDTO) {
		ModelMapper modelMapper = new ModelMapper();
		CareTaker careTaker = modelMapper.map(careTakerCreateDTO, CareTaker.class);
		return careTaker;
	}

	public static CareTaker toCareTakerEntity(CareTakerLinkIdDTO careTakerLinkIdDTO) {
		ModelMapper modelMapper = new ModelMapper();
		CareTaker careTaker = modelMapper.map(careTakerLinkIdDTO, CareTaker.class);
		return careTaker;
	}

	public static CareTakerLinkIdDTO toCareTakerLinkIdDTO(CareTaker careTaker) {
		ModelMapper modelMapper = new ModelMapper();
		CareTakerLinkIdDTO careTakerLinkIdDTO = modelMapper.map(careTaker, CareTakerLinkIdDTO.class);
		return careTakerLinkIdDTO;
	}

	public static CareTakerCreateDTO toCareTakerCreateDTO(CareTaker careTaker) {
		ModelMapper modelMapper = new ModelMapper();
		CareTakerCreateDTO careTakerCreateDTO = modelMapper.map(careTaker, CareTakerCreateDTO.class);
		return careTakerCreateDTO;
	}
}
