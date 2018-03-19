package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

public interface AnimalService {
    ResponseEntity<List<Animal>> getAnimals();
    ResponseEntity<Animal> getAnimalById(long id);
    ResponseEntity<List<Animal>> getAnimalsByName(String name);
    ResponseEntity<Animal> addAnimal(@Valid Animal animal);
    ResponseEntity<Animal> deleteAnimalById(long id);
    ResponseEntity<Animal> updateAnimalById(long id, @Valid Animal animal);
}
