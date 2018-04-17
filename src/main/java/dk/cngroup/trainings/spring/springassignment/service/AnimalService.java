package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

import java.util.List;

public interface AnimalService {
	List<Animal> getAnimals();

	Animal getAnimalById(long id) throws AnimalNotFoundException;

	List<Animal> getAnimalsByName(String name);

	Animal addAnimal(Animal animal) throws InvalidAnimalException;

	void deleteAnimalById(long id) throws AnimalNotFoundException;

	void checkAnimalExistsById(long id) throws AnimalNotFoundException;

	Animal updateAnimalById(long id, Animal animal)
			throws InvalidAnimalException, AnimalNotFoundException;

	CareTaker addNewCareTakerToExistingAnimal(long id, CareTaker careTaker)
			throws InvalidCareTakerException, AnimalNotFoundException;

	CareTaker addExistingCareTakerToExistingAnimal(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked;

	void removeCareTakerFromCareTakersList(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException, CareTakersListEmptyException, CareTakerNotInTheAnimalCareTakersListException;
}
