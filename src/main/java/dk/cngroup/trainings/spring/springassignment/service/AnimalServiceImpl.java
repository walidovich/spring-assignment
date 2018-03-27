package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.CareTakerValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AnimalServiceImpl implements AnimalService {

	private AnimalRepository animalRepository;
	private CareTakerRepository careTakerRepository;

	public AnimalServiceImpl(AnimalRepository animalRepository, CareTakerRepository careTakerRepository) {
		this.animalRepository = animalRepository;
		this.careTakerRepository = careTakerRepository;
	}

	@Override
	public List<Animal> getAnimals() {
		return animalRepository.findAll();
	}

	@Override
	public Optional<Animal> getAnimalById(long id) {
		return animalRepository.findById(id);
	}

	@Override
	public List<Animal> getAnimalsByName(String name) {
		return animalRepository.findAllByName(name);
	}

	@Override
	public Optional<Animal> addAnimal(Animal animal) {
		if (AnimalValidationService.isValid(animal)) {
			animal.setId(IdGenerator.getId());
			return Optional.ofNullable(animalRepository.save(animal));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteAnimalById(long id) {
		if (animalRepository.existsById(id)) {
			animalRepository.deleteById(id);
			return true;
		} else

		{
			return false;
		}
	}

	@Override
	public Optional<Animal> updateAnimalById(long id, Animal animal) {
		if (AnimalValidationService.isValid(animal) && animalRepository.existsById(id)) {
			// Change the updated animal id and override it in the database.
			animal.setId(id);
			return Optional.ofNullable(animalRepository.save(animal));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<CareTaker> addNewCareTakerToExistingAnimal(long id, CareTaker careTaker) {
		Optional<Animal> animal = animalRepository.findById(id);
		if (CareTakerValidationService.isValid(careTaker)) {
			careTaker.setId(IdGenerator.getId());
			List<Animal> animals = Arrays.asList(animal.get());
			careTaker.setAnimals(animals);
			CareTaker addedCareTaker = careTakerRepository.save(careTaker);
			return Optional.ofNullable(addedCareTaker);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public String addExistingCareTakerToExistingAnimal(long careTakerId, long animalId) {
		return null;
	}
}
