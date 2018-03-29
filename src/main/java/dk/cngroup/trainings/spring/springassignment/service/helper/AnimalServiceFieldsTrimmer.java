package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.Animal;

public class AnimalServiceFieldsTrimmer {

	public static void trimFields(Animal animal) {
		animal.setName(animal.getName().trim());
		animal.setDescription(animal.getDescription().trim());
	}
}
