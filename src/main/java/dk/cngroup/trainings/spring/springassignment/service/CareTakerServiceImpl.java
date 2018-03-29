package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;
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
	public Optional<CareTaker> getCareTakerById(long id) {
		return careTakerRepository.findById(id);
	}

	@Override
	public Optional<CareTaker> addCareTaker(CareTaker careTaker) throws InvalidCareTakerException {
		CareTakerServiceFieldsTrimmer.trimFields(careTaker);
		CareTakerValidationService.isValid(careTaker);
		careTaker.setId(IdGenerator.getId());
		return Optional.ofNullable(careTakerRepository.save(careTaker));
	}

	@Override
	public Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker) throws InvalidCareTakerException {
		CareTakerValidationService.isValid(careTaker);
		careTakerRepository.existsById(id);
		careTaker.setId(id);
		return Optional.ofNullable(careTakerRepository.save(careTaker));
	}

	@Override
	public boolean deleteCareTakerById(long id) {
		if (careTakerRepository.existsById(id)) {
			careTakerRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal) throws InvalidAnimalException {
		Optional<CareTaker> careTaker = careTakerRepository.findById(id);
		AnimalValidationService.validate(animal);
		Optional<Animal> addedAnimal = animalService.addAnimal(animal);
		careTaker.get().addAnimalToCareTaker(addedAnimal.get());
		careTakerRepository.save(careTaker.get());
		return Optional.ofNullable(addedAnimal.get());
	}

	@Override
	public String addExistingAnimalToExistingCareTaker(long careTakerId, long animalId) {
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		Optional<Animal> animal = animalService.getAnimalById(animalId);
		if (!careTaker.isPresent()) {
			return "Failed: CareTaker id:" + careTakerId + " doesn't exist";
		} else if (!animal.isPresent()) {
			return "Failed: Animal id:" + animalId + " doesn't exist";
		} else {
			if (careTaker.get().getAnimals().stream().anyMatch(a -> a.getId().equals(animalId))) {
				return "Warning: Animal id:" + animalId + " already added to careTaker id:" + careTakerId;
			} else {
				careTaker.get().addAnimalToCareTaker(animal.get());
				careTakerRepository.save(careTaker.get());
				return "Success";
			}
		}
	}
}
