package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;

import java.util.List;
import java.util.Optional;

public interface AnimalService {
	List<Animal> getAnimals();

	Optional<Animal> getAnimalById(long id) throws AnimalNotFoundException;

	List<Animal> getAnimalsByName(String name);

	Optional<Animal> addAnimal(Animal animal) throws InvalidAnimalException;

	void deleteAnimalById(long id) throws AnimalNotFoundException;

	void checkAnimalExistsById(long id) throws AnimalNotFoundException;

	Optional<Animal> updateAnimalById(long id, Animal animal)
			throws InvalidAnimalException, AnimalNotFoundException;

	Optional<CareTaker> addNewCareTakerToExistingAnimal(long id, CareTaker careTaker)
			throws InvalidCareTakerException, AnimalNotFoundException;

	Optional<CareTaker> addExistingCareTakerToExistingAnimal(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked;
}
