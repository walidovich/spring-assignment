package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

import java.util.List;
import java.util.Optional;

public interface CareTakerService {
	List<CareTaker> getCareTakers();

	Optional<CareTaker> getCareTakerById(long id);

	Optional<CareTaker> addCareTaker(CareTaker careTaker);

	Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker);

	boolean deleteCareTakerById(long id);

	Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal);

	Optional<Animal> getAnimalById(long animalId);

	String addExistingAnimalToExistingCareTaker(long careTakerId, long animalId);
}
