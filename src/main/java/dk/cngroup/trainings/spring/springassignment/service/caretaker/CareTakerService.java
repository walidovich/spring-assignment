package dk.cngroup.trainings.spring.springassignment.service.caretaker;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

import java.util.List;

public interface CareTakerService {
	List<CareTaker> getCareTakers();

	CareTaker getCareTakerById(long id) throws CareTakerNotFoundException;

	CareTaker addCareTaker(CareTaker careTaker) throws InvalidCareTakerException;

	CareTaker updateCareTakerById(long id, CareTaker careTaker) throws InvalidCareTakerException, CareTakerNotFoundException;

	void deleteCareTakerById(long id) throws CareTakerNotFoundException;

	void checkCareTakerExistsById(long id) throws CareTakerNotFoundException;

	Animal addNewAnimalToExistingCareTaker(long id, Animal animal) throws InvalidAnimalException, CareTakerNotFoundException;

	Animal addExistingAnimalToExistingCareTaker(long careTakerId, long animalId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked;

	List<Animal> removeAnimalFromAnimalsList(long careTakerId, long animalId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalsListEmptyException, AnimalNotFoundInCareTakerAnimalsListException;
}
