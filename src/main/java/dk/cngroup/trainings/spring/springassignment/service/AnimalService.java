package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface AnimalService {
    List<Animal> getAnimals();
    ResponseEntity<Animal> getAnimalById(long id);
    List<Animal> getAnimalsByName(String name);
    Animal addAnimal(@Valid Animal animal);
    ResponseEntity<Animal> deleteAnimalById(long id);
    Animal updateAnimalById(long id, @Valid Animal animal);
}
