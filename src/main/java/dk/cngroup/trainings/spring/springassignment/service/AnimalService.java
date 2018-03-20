package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface AnimalService {
    List<Animal> getAnimals();
    Optional<Animal> getAnimalById(long id);
    List<Animal> getAnimalsByName(String name);
    Optional<Animal> addAnimal(@Valid Animal animal);
    boolean deleteAnimalById(long id);
    Optional<Animal> updateAnimalById(long id, @Valid Animal animal);
    boolean isValid(@Valid Animal animal);
}
