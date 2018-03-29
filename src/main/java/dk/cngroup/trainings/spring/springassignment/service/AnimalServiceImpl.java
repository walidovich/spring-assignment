package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;
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
	public Optional<Animal> addAnimal(Animal animal) throws InvalidAnimalException {
		AnimalValidationService.validate(animal);
		animal.setId(IdGenerator.getId());
		return Optional.ofNullable(animalRepository.save(animal));
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
	public Optional<Animal> updateAnimalById(long id, Animal animal) throws InvalidAnimalException {
		AnimalValidationService.validate(animal);
		if (animalRepository.existsById(id)) {
			// Change the updated animal id and override it in the database.
			animal.setId(id);
			return Optional.ofNullable(animalRepository.save(animal));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<CareTaker> addNewCareTakerToExistingAnimal(long id, CareTaker careTaker) throws InvalidCareTakerException {
		CareTakerValidationService.isValid(careTaker);
		Optional<Animal> animal = animalRepository.findById(id);
		careTaker.setId(IdGenerator.getId());
		List<Animal> animals = Arrays.asList(animal.get());
		careTaker.setAnimals(animals);
		CareTaker addedCareTaker = careTakerRepository.save(careTaker);
		return Optional.ofNullable(addedCareTaker);
	}

	@Override
	public String addExistingCareTakerToExistingAnimal(long animalId, long careTakerId) {
		Optional<Animal> animal = animalRepository.findById(animalId);
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		if (!animal.isPresent()) {
			return "Failed: Animal id doesn't exist";
		} else if (!careTaker.isPresent()) {
			return "Failed: CareTaker id doesn't exist";
		} else {
			if (animal.get().getCareTakers().stream().anyMatch(c -> c.getId() == careTakerId)) {
				return "Warning: careTaker id:" + careTakerId + " already added to animal id:" + animalId;
			} else {
				List<Animal> animals = careTaker.get().getAnimals();
				animals.add(animal.get());
				careTaker.get().setAnimals(animals);
				careTakerRepository.save(careTaker.get());
				return "Success";
			}
		}
	}
}
