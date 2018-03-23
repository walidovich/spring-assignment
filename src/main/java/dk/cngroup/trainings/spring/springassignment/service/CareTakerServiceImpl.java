package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
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
		return null;
	}

	@Override
	public Optional<CareTaker> getCareTakerById(long id) {
		return Optional.empty();
	}

	@Override
	public Optional<CareTaker> addCareTaker(CareTaker careTaker) {
		return Optional.empty();
	}

	@Override
	public Optional<CareTaker> updateCareTakerById(CareTaker careTaker) {
		return Optional.empty();
	}

	@Override
	public boolean deleteCareTakerById(long id) {
		return false;
	}

	@Override
	public List<Animal> getAnimalsInCareByCareTakerId(long id) {
		return null;
	}

	@Override
	public Optional<Animal> addAnimalToCare(long id, Animal animal) {
		return null;
	}
}
