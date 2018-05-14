package dk.cngroup.trainings.spring.springassignment.service.caretaker;

import dk.cngroup.trainings.spring.springassignment.exception.animal.*;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.CareTakerNotFoundException;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.InvalidCareTakerException;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.animal.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.CareTakerValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareTakerServiceImpl implements CareTakerService {

	private final CareTakerRepository careTakerRepository;
	private final AnimalService animalService;

	public CareTakerServiceImpl(CareTakerRepository careTakerRepository, AnimalService animalService) {
		this.careTakerRepository = careTakerRepository;
		this.animalService = animalService;
	}

	@Override
	public List<CareTaker> getCareTakers() {
		return careTakerRepository.findAll();
	}

	@Override
	public CareTaker getCareTakerById(long id) throws CareTakerNotFoundException {
		Optional<CareTaker> careTaker = careTakerRepository.findById(id);
		if (careTaker.isPresent()) {
			return careTaker.get();
		} else {
			throw new CareTakerNotFoundException(id);
		}
	}

	@Override
	public CareTaker addCareTaker(CareTaker careTaker) throws InvalidCareTakerException {
		CareTakerValidationService.validate(careTaker);
		careTaker.setId(null);
		return careTakerRepository.save(careTaker);
	}

	@Override
	public CareTaker updateCareTakerById(long id, CareTaker careTaker)
			throws InvalidCareTakerException, CareTakerNotFoundException {
		CareTakerValidationService.validate(careTaker);
		this.checkCareTakerExistsById(id);
		careTaker.setId(id);
		return careTakerRepository.save(careTaker);
	}

	@Override
	public void deleteCareTakerById(long id) throws CareTakerNotFoundException {
		this.checkCareTakerExistsById(id);
		careTakerRepository.deleteById(id);
	}

	@Override
	public void checkCareTakerExistsById(long id) throws CareTakerNotFoundException {
		if (!careTakerRepository.existsById(id)) {
			throw new CareTakerNotFoundException(id);
		}
	}

	@Override
	public Animal addNewAnimalToExistingCareTaker(long id, Animal animal)
			throws CareTakerNotFoundException, InvalidAnimalException {
		CareTaker careTaker = this.getCareTakerById(id);
		AnimalValidationService.validate(animal);
		Animal addedAnimal = animalService.addAnimal(animal);
		careTaker.addAnimalToCareTaker(addedAnimal);
		careTakerRepository.save(careTaker);
		return addedAnimal;
	}

	@Override
	public Animal addExistingAnimalToExistingCareTaker(long careTakerId, long animalId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked {
		CareTaker careTaker = this.getCareTakerById(careTakerId);
		Animal animal = animalService.getAnimalById(animalId);
		if (careTaker.getAnimals().stream().noneMatch(a -> a.getId().equals(animalId))) {
			careTaker.addAnimalToCareTaker(animal);
			careTakerRepository.save(careTaker);
			return animal;
		} else {
			throw new AnimalAndCareTakerAlreadyLinked(animalId, careTakerId);
		}
	}

	@Override
	public List<Animal> removeAnimalFromAnimalsList(long careTakerId, long animalId)
			throws CareTakerNotFoundException, AnimalsListEmptyException,
			AnimalNotFoundInCareTakerAnimalsListException, AnimalNotFoundException {
		CareTaker careTaker = this.getCareTakerById(careTakerId);
		animalService.checkAnimalExistsById(animalId);
		List<Animal> animals = careTaker.getAnimals();
		if (animals.size() == 0) {
			throw new AnimalsListEmptyException(careTakerId);
		} else if (animals.stream().noneMatch(animal -> animal.getId().equals(animalId))) {
			throw new AnimalNotFoundInCareTakerAnimalsListException(careTakerId, animalId);
		} else {
			animals.removeIf(animal -> animal.getId().equals(animalId));
			careTakerRepository.save(careTaker);
			return careTaker.getAnimals();
		}
	}
}
