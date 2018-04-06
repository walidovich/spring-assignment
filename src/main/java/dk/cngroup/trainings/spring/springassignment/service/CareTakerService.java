package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;

import java.util.List;
import java.util.Optional;

public interface CareTakerService {
	List<CareTaker> getCareTakers();

	Optional<CareTaker> getCareTakerById(long id) throws CareTakerNotFoundException;

	Optional<CareTaker> addCareTaker(CareTaker careTaker) throws InvalidCareTakerException;

	Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker) throws InvalidCareTakerException, CareTakerNotFoundException;

	void deleteCareTakerById(long id) throws CareTakerNotFoundException;

	void checkCareTakerExistsById(long id) throws CareTakerNotFoundException;

	Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal) throws InvalidAnimalException, CareTakerNotFoundException;

	void addExistingAnimalToExistingCareTaker(long careTakerId, long animalId) throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked;
}
