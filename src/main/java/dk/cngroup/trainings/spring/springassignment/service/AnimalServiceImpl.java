package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public class AnimalServiceImpl implements AnimalService {

    private AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository=animalRepository;
    }

    @Override
    public List<Animal> getAnimals() {
        return animalRepository.findAll();
    }

    @Override
    public ResponseEntity<Animal> getAnimalById(long id) {
        if(animalRepository.existsById(id)) {
            return new ResponseEntity<>(animalRepository.findById(id).get(),
                    HttpStatus.OK);
        } else
            return ResponseEntity.notFound().build();
    }

    @Override
    public List<Animal> getAnimalsByName(String name) {
       return  animalRepository.findAllByName(name);
    }

    @Override
    public Animal addAnimal(Animal animal) {
        if(!animalRepository.existsById(animal.getId()))
            return animalRepository.save(animal); //TODO generate IDs on your own
        else {
            // Call addAnimal with new id untill it's saved
            animal.setId(animal.getId()+1);
            return this.addAnimal(animal);
        }
    }

    @Override
    public ResponseEntity<Animal> deleteAnimalById(long id) {
        if(animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }else
            return ResponseEntity.notFound().build();
    }

    @Override
    public Animal updateAnimalById(long id, @Valid Animal animal) {
        if(animalRepository.existsById(id)){
            animal.setId(id);
        }
        return animalRepository.save(animal);
    }
}
