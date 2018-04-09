package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;

public class AnimalValidationService {

	public static void validate(Animal animal) throws InvalidAnimalException {
		checkAnimalNotNull(animal);
		checkAnimalNameLengthBiggerThanMinimumSize(animal);
		checkAnimalDescriptionLengthSmallerThanMaximumSize(animal);
		checkAnimalDescriptionNotContainingPenguin(animal);
	}

	private static void checkAnimalDescriptionNotContainingPenguin(Animal animal) throws InvalidAnimalException {
		if (animal.getDescription().toLowerCase().contains("penguin")) {
			throw new InvalidAnimalException("Fail: Animal description contains penguin");
		}
	}

	private static void checkAnimalDescriptionLengthSmallerThanMaximumSize(Animal animal) throws InvalidAnimalException {
		if (animal.getDescription().length() >= Animal.DESCRIPTION_MAXIMUM_SIZE) {
			throw new InvalidAnimalException("Fail: Animal description length is longer than 10000 characters");
		}
	}

	private static void checkAnimalNameLengthBiggerThanMinimumSize(Animal animal) throws InvalidAnimalException {
		if (animal.getName().length() < Animal.NAME_MINIMUM_SIZE) {
			throw new InvalidAnimalException("Fail: Animal name length is smaller than 2 characters");
		}
	}

	private static void checkAnimalNotNull(Animal animal) throws InvalidAnimalException {
		if (animal == null) {
			throw new InvalidAnimalException("Fail: Animal is null");
		}
	}
}
