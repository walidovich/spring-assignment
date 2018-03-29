package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;

import java.util.List;
import java.util.Optional;

public interface CareTakerService {
	List<CareTaker> getCareTakers();

	Optional<CareTaker> getCareTakerById(long id);

	Optional<CareTaker> addCareTaker(CareTaker careTaker) throws InvalidCareTakerException;

	Optional<CareTaker> updateCareTakerById(long id, CareTaker careTaker) throws InvalidCareTakerException;

	boolean deleteCareTakerById(long id);

	Optional<Animal> addNewAnimalToExistingCareTaker(long id, Animal animal) throws InvalidAnimalException;

	String addExistingAnimalToExistingCareTaker(long careTakerId, long animalId);
}
