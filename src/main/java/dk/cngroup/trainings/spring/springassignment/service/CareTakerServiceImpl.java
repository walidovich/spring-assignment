package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.CareTakerServiceFieldsTrimmer;
import dk.cngroup.trainings.spring.springassignment.service.helper.CareTakerValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareTakerServiceImpl implements CareTakerService {

	private CareTakerRepository careTakerRepository;
	private AnimalService animalService;

	public CareTakerServiceImpl(CareTakerRepository careTakerRepository, AnimalService animalService) {
		this.careTakerRepository = careTakerRepository;
		this.animalService = animalService;
	}

	@Override
	public List<CareTaker> getCareTakers() {
		return careTakerRepository.findAll();
	}

	@Override
	public Optional<CareTaker> getCareTakerById(long id) throws CareTakerNotFoundException {
		if (careTakerRepository.existsById(id)) {
			return careTakerRepository.findById(id);
		} else {
			throw new CareTakerNotFoundException(id);
		}
	}

	@Override
	public Optional<CareTaker> addCareTaker(CareTaker careTaker) throws InvalidCareTakerException {
		CareTakerServiceFieldsTrimmer.trimFields(careTaker);
		CareTakerValidationService.validate(careTaker);
		careTaker.setId(IdGenerator.getId());
		return Optional.ofNullable(careTakerRepository.save(careTaker));
	}

	@Override
	public Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker)
			throws InvalidCareTakerException, CareTakerNotFoundException {
		CareTakerServiceFieldsTrimmer.trimFields(careTaker);
		CareTakerValidationService.validate(careTaker);
		this.checkCareTakerExistsById(id);
		careTaker.setId(id);
		return Optional.ofNullable(careTakerRepository.save(careTaker));
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
	public Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal)
			throws CareTakerNotFoundException, InvalidAnimalException {
		Optional<CareTaker> careTaker = this.getCareTakerById(id);
		AnimalValidationService.validate(animal);
		Optional<Animal> addedAnimal = animalService.addAnimal(animal);
		careTaker.get().addAnimalToCareTaker(addedAnimal.get());
		careTakerRepository.save(careTaker.get());
		return Optional.ofNullable(addedAnimal.get());
	}

	@Override
	public Optional<Animal> addExistingAnimalToExistingCareTaker(long careTakerId, long animalId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked {
		Optional<CareTaker> careTaker = this.getCareTakerById(careTakerId);
		Optional<Animal> animal = animalService.getAnimalById(animalId);
		if (careTaker.get().getAnimals().stream().noneMatch(a -> a.getId().equals(animalId))) {
			careTaker.get().addAnimalToCareTaker(animal.get());
			careTakerRepository.save(careTaker.get());
			return animal;
		} else {
			throw new AnimalAndCareTakerAlreadyLinked(animalId, careTakerId);
		}
	}
}
