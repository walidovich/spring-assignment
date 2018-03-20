package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public Optional<Animal> getAnimalById(long id) {
            return animalRepository.findById(id);
    }

    @Override
    public List<Animal> getAnimalsByName(String name) {
            return animalRepository.findAllByName(name);
    }

    @Override
    public Optional<Animal> addAnimal(Animal animal){
        if(isValid(animal)){
            if (!animalRepository.existsById(animal.getId())) {
                return Optional.ofNullable(animalRepository.save(animal));
            }else {
                // Call addAnimal recursively with new id untill it's saved
                animal.setId(animal.getId() + 1);
                return this.addAnimal(animal);
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean deleteAnimalById(long id) {
        if(animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return true;
        }else
            return false;
    }

    @Override
    public Optional<Animal> updateAnimalById(long id, @Valid Animal animal) {
        if(isValid(animal) && animalRepository.existsById(id)) {
            // Change the updated animal id and override it in the database.
            animal.setId(id);
            return Optional.ofNullable(animalRepository.save(animal));
        } else {
            return null;
        }
    }

    @Override
    public boolean isValid(@Valid Animal animal) {
        return animal.getName().length()>=Animal.NAME_MINIMUM_SIZE
                && animal.getDescription().length()<Animal.DESCRIPTION_MAXIMUM_SIZE
                && !animal.getDescription().toLowerCase().contains("penguin");
    }
}
