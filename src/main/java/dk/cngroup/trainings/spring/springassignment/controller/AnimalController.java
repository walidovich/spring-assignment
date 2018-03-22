package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Animal> addAnimal(@RequestBody @Valid Animal animal){
        Optional<Animal> addedAnimal= animalService.addAnimal(animal);
        if(addedAnimal.isPresent()){
            return new ResponseEntity<>(addedAnimal.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Animal>> getAnimals(){
        return new ResponseEntity<>(animalService.getAnimals(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAnimalById(@PathVariable("id") long id){
        if(animalService.deleteAnimalById(id))
            return new ResponseEntity<>("Success",HttpStatus.OK);
        else
            return new ResponseEntity<>("Fail: Id not found.",HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") long id,
                                                   @RequestBody @Valid Animal animal){
        /*
        Optional<Animal> updatedAnimal= animalService.updateAnimalById(id, animal);
        if(updatedAnimal.isPresent()) {
            return new ResponseEntity<>(updatedAnimal.get(), HttpStatus.OK);
        }else if(animalService.getAnimalById(id).isPresent()){
            // updatedAnimal is empty but the id exists, means bad request
            return ResponseEntity.badRequest().build();
        }else{
            // updatedAnimal is empty, and id doesn't exist, means not found
            return ResponseEntity.notFound().build();
        }
        */
        if(animalService.getAnimalById(id).isPresent()){
            animal.setId(id);
            return this.addAnimal(animal);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Animal> getAnimalById(@PathVariable("id") long id){
        Optional<Animal> searchedAnimal= animalService.getAnimalById(id);
        if(searchedAnimal.isPresent())
            return new ResponseEntity<>(searchedAnimal.get(),HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/search/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Animal>> getAnimalByName(@PathVariable("name") String name){
        List<Animal> sameNameAnimals= animalService.getAnimalsByName(name);
        if(!sameNameAnimals.isEmpty())
            return new ResponseEntity<>(sameNameAnimals, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }
}
