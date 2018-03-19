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
    public ResponseEntity<List<Animal>> getAnimals() {
        return new ResponseEntity<>(animalRepository.findAll(),
                HttpStatus.OK);
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
    public ResponseEntity<List<Animal>> getAnimalsByName(String name) {
        if(animalRepository.findAllByName(name)!=null)
            return  new ResponseEntity<>(animalRepository.findAllByName(name),
                    HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Animal> addAnimal(Animal animal) {
        if(animal.getName().length()>=Animal.NAME_MINIMUM_SIZE
                && animal.getDescription().length()<Animal.DESCRIPTION_MAXIMUM_SIZE
                && !doesContainPenguin(animal.getDescription())){
            if (!animalRepository.existsById(animal.getId()))
                return new ResponseEntity<>(animalRepository.save(animal),
                        HttpStatus.OK); //TODO generate IDs on your own
            else {
                // Call addAnimal with new id untill it's saved
                animal.setId(animal.getId() + 1);
                return this.addAnimal(animal);
            }
        }
        else {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<Animal> updateAnimalById(long id, @Valid Animal animal) {
        if(animal.getName().length()>=Animal.NAME_MINIMUM_SIZE
                && animal.getDescription().length()<Animal.DESCRIPTION_MAXIMUM_SIZE
                && !doesContainPenguin(animal.getDescription())) {
            if (animalRepository.existsById(id)) {
                animal.setId(id);
                return new ResponseEntity<>(animalRepository.save(animal), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean doesContainPenguin(String description) {
        String descriptionToLowerCase=description.toLowerCase();
        return descriptionToLowerCase.contains("penguin");
    }
}
