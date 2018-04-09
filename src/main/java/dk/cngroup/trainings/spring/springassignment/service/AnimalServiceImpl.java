package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalServiceFieldsTrimmer;
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
	public Optional<Animal> getAnimalById(long id) throws AnimalNotFoundException {
		if (animalRepository.existsById(id)) {
			return animalRepository.findById(id);
		} else {
			throw new AnimalNotFoundException(id);
		}
	}

	@Override
	public List<Animal> getAnimalsByName(String name) {
		return animalRepository.findAllByName(name);
	}

	@Override
	public Optional<Animal> addAnimal(Animal animal) throws InvalidAnimalException {
		AnimalServiceFieldsTrimmer.trimFields(animal);
		AnimalValidationService.validate(animal);
		animal.setId(IdGenerator.getId());
		return Optional.ofNullable(animalRepository.save(animal));
	}

	@Override
	public void deleteAnimalById(long id) throws AnimalNotFoundException {
		this.checkAnimalExistsById(id);
		for (CareTaker careTaker : this.getAnimalById(id).get().getCareTakers()) {
			careTaker.getAnimals().remove(this.getAnimalById(id).get());
			careTakerRepository.save(careTaker);
		}
		animalRepository.deleteById(id);
	}

	@Override
	public boolean checkAnimalExistsById(long id) throws AnimalNotFoundException {
		if (animalRepository.existsById(id)) {
			return true;
		} else {
			throw new AnimalNotFoundException(id);
		}
	}

	@Override
	public Optional<Animal> updateAnimalById(long id, Animal animal)
			throws InvalidAnimalException, AnimalNotFoundException {
		AnimalServiceFieldsTrimmer.trimFields(animal);
		AnimalValidationService.validate(animal);
		this.checkAnimalExistsById(id);
		animal.setId(id);
		return Optional.ofNullable(animalRepository.save(animal));
	}

	@Override
	public Optional<CareTaker> addNewCareTakerToExistingAnimal(long id, CareTaker careTaker)
			throws InvalidCareTakerException, AnimalNotFoundException {
		CareTakerValidationService.validate(careTaker);
		Optional<Animal> animal = this.getAnimalById(id);
		careTaker.setId(IdGenerator.getId());
		List<Animal> animals = Arrays.asList(animal.get());
		careTaker.setAnimals(animals);
		CareTaker addedCareTaker = careTakerRepository.save(careTaker);
		return Optional.ofNullable(addedCareTaker);
	}

	@Override
	public Optional<CareTaker> addExistingCareTakerToExistingAnimal(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked {
		Optional<Animal> animal = this.getAnimalById(animalId);
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		// Animal service can't use CareTakerService because we will have recursive dependencies
		if (!careTaker.isPresent()) {
			throw new CareTakerNotFoundException(careTakerId);
		} else {
			if (animal.get().getCareTakers().stream().noneMatch(c -> c.getId() == careTakerId)) {
				List<Animal> animals = careTaker.get().getAnimals();
				animals.add(animal.get());
				careTaker.get().setAnimals(animals);
				careTakerRepository.save(careTaker.get());
				return careTaker;
			} else {
				throw new AnimalAndCareTakerAlreadyLinked(animalId, careTakerId);
			}
		}
	}
}
