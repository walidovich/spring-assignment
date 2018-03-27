package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.Animal;

public class AnimalValidationService {

	public static boolean isValid(Animal animal) {
		return isAnimalNotNull(animal)
				&& isAnimalNameLengthBiggerThanMinimumSize(animal)
				&& isAnimalDescriptionLengthSmallerThanMaximumSize(animal)
				&& isAnimalDescriptionNotContainingPenguin(animal);
	}

	public static boolean isAnimalDescriptionNotContainingPenguin(Animal animal) {
		return !animal.getDescription().toLowerCase().contains("penguin");
	}

	public static boolean isAnimalDescriptionLengthSmallerThanMaximumSize(Animal animal) {
		return animal.getDescription().length() < Animal.DESCRIPTION_MAXIMUM_SIZE;
	}

	public static boolean isAnimalNameLengthBiggerThanMinimumSize(Animal animal) {
		return animal.getName().length() >= Animal.NAME_MINIMUM_SIZE;
	}

	private static boolean isAnimalNotNull(Animal animal) {
		return animal != null;
	}
}
