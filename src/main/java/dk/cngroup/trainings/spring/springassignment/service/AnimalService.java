package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;

import java.util.List;
import java.util.Optional;

public interface AnimalService {
	List<Animal> getAnimals();

	Optional<Animal> getAnimalById(long id);

	List<Animal> getAnimalsByName(String name);

	Optional<Animal> addAnimal(Animal animal) throws InvalidAnimalException;

	boolean deleteAnimalById(long id);

	Optional<Animal> updateAnimalById(long id, Animal animal) throws InvalidAnimalException;

	Optional<CareTaker> addNewCareTakerToExistingAnimal(long id, CareTaker careTaker) throws InvalidCareTakerException;

	String addExistingCareTakerToExistingAnimal(long animalId, long careTakerId);
}
