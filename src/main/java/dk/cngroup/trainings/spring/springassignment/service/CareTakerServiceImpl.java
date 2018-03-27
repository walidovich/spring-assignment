package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalValidationService;
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
	public Optional<CareTaker> addCareTaker(CareTaker careTaker) {
		if (CareTakerValidationService.isValid(careTaker)) {
			careTaker.setId(IdGenerator.getId());
			return Optional.ofNullable(careTakerRepository.save(careTaker));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker) {
		if (careTakerRepository.existsById(id)) {
			careTaker.setId(id);
			return Optional.ofNullable(careTakerRepository.save(careTaker));
		} else {
			return Optional.empty();
		}
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
	public Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal) {
		Optional<CareTaker> careTaker = careTakerRepository.findById(id);
		if (AnimalValidationService.isValid(animal)) {
			Optional<Animal> addedAnimal = animalService.addAnimal(animal);
			careTaker.get().addAnimalToCareTaker(addedAnimal.get());
			careTakerRepository.save(careTaker.get());
			return Optional.ofNullable(addedAnimal.get());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Animal> getAnimalById(long animalId) {
		return animalService.getAnimalById(animalId);
	}

	@Override
	public String addExistingAnimalToExistingCareTaker(long careTakerId, long animalId) {
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		Optional<Animal> animal = animalService.getAnimalById(animalId);
		if (!careTaker.isPresent()) {
			return "Failed: CareTaker id doesn't exist";
		} else if (!animal.isPresent()) {
			return "Failed: Animal id doesn't exist";
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
