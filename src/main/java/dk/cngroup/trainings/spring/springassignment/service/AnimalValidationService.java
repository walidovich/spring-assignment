package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;

public class AnimalValidationService {

    public static boolean isValid(Animal animal) {
        return (animal!=null)
                && animal.getName().length()>=Animal.NAME_MINIMUM_SIZE
                && animal.getDescription().length()<Animal.DESCRIPTION_MAXIMUM_SIZE
                && !animal.getDescription().toLowerCase().contains("penguin");
    }
}
