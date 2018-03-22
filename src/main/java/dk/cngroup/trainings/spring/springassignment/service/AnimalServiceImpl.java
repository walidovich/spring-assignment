package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
                animal.setId(animal.getId() + 1); //TODO check with generated value
                return this.addAnimal(animal);
            }
        }
        else {
            return null; //TODO dont return null
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
    public Optional<Animal> updateAnimalById(long id, @Valid Animal animal) { //TODO delete @Valid
        if(isValid(animal) && animalRepository.existsById(id)) {
            // Change the updated animal id and override it in the database.
            animal.setId(id);
            return Optional.ofNullable(animalRepository.save(animal));
        } else {
            return null; //TODO Optional.empty();
        }
    }

    @Override
    public boolean isValid(@Valid Animal animal) { //it would be possible to move this e.g. to AnimalValidationService
        //TODO check also nullability
        return animal.getName().length()>=Animal.NAME_MINIMUM_SIZE
                && animal.getDescription().length()<Animal.DESCRIPTION_MAXIMUM_SIZE
                && !animal.getDescription().toLowerCase().contains("penguin");
    }
}
