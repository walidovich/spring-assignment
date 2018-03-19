package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<Animal> addAnimal(@RequestBody @Valid Animal animal){
        return animalService.addAnimal(animal);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Animal>> getAnimals(){
        return animalService.getAnimals();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Animal> deleteAnimalById(@PathVariable("id") long id){
        return animalService.deleteAnimalById(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") long id,
                                                   @RequestBody @Valid Animal animal){
        return animalService.updateAnimalById(id, animal);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Animal> getAnimalById(@PathVariable("id") long id){
        return animalService.getAnimalById(id);
    }

    @RequestMapping(path = "/search/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Animal>> getAnimalByName(@PathVariable("name") String name){
        return animalService.getAnimalsByName(name);
    }
}
