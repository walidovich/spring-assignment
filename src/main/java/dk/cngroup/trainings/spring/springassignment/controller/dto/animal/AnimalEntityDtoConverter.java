package dk.cngroup.trainings.spring.springassignment.controller.dto.animal;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import org.modelmapper.ModelMapper;

public class AnimalEntityDtoConverter {

	public static Animal toAnimalEntity(AnimalLinkIdDTO animalLinkIdDTO) {
		ModelMapper modelMapper = new ModelMapper();
		Animal animal = modelMapper.map(animalLinkIdDTO, Animal.class);
		return animal;
	}

	public static AnimalLinkIdDTO toAnimalLinkIdDTO(Animal animal) {
		ModelMapper modelMapper = new ModelMapper();
		AnimalLinkIdDTO animalLinkIdDTO = modelMapper.map(animal, AnimalLinkIdDTO.class);
		return animalLinkIdDTO;
	}

	public static Animal toAnimalEntity(AnimalCreateDTO animalCreateDTO) {
		ModelMapper modelMapper = new ModelMapper();
		Animal animal = modelMapper.map(animalCreateDTO, Animal.class);
		return animal;
	}

	public static AnimalCreateDTO toAnimalCreateDTO(Animal animal) {
		ModelMapper modelMapper = new ModelMapper();
		AnimalCreateDTO animalCreateDTO = modelMapper.map(animal, AnimalCreateDTO.class);
		return animalCreateDTO;
	}
}
