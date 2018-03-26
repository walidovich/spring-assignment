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

	public CareTakerServiceImpl(CareTakerRepository careTakerRepository) {
		this.careTakerRepository = careTakerRepository;
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
	public List<Animal> getAnimalsInCareByCareTakerId(long id) {
		return null;
	}

	@Override
	public Optional<Animal> addAnimalToCare(long id, Animal animal) {
		Optional<CareTaker> careTaker = careTakerRepository.findById(id);
		if (careTaker.isPresent()) {
			if (AnimalValidationService.isValid(animal)) {
				careTaker.get().addAnimalToCare(animal);
				careTakerRepository.save(careTaker.get());
				return Optional.ofNullable(animal);
			} else {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
}
