package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

import java.util.List;
import java.util.Optional;

public interface CareTakerService {
	List<CareTaker> getCareTakers();

	Optional<CareTaker> getCareTakerById(long id);

	Optional<CareTaker> addCareTaker(CareTaker careTaker);

	Optional<CareTaker> updateCareTaker(CareTaker careTaker);

	List<Animal> getAnimalsInCare();

	List<Animal> addAnimalToCare(long careTakerId, long animalId);
}
