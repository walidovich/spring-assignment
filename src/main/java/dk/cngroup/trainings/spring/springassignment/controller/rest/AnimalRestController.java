package dk.cngroup.trainings.spring.springassignment.controller.rest;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.animal.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalRestController {

	private final AnimalService animalService;

	public AnimalRestController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Animal> addAnimal(@RequestBody @Valid Animal animal) throws InvalidAnimalException {
		return new ResponseEntity<>(animalService.addAnimal(animal), HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") long id, @RequestBody @Valid Animal animal)
			throws AnimalNotFoundException, InvalidAnimalException {
		return new ResponseEntity<>(animalService.updateAnimalById(id, animal),
				HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimals() {
		return new ResponseEntity<>(animalService.getAnimals(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAnimalById(@PathVariable("id") long id) throws AnimalNotFoundException {
		animalService.deleteAnimalById(id);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Animal> getAnimalById(@PathVariable("id") long id) throws AnimalNotFoundException {
		Animal searchedAnimal = animalService.getAnimalById(id);
		return new ResponseEntity<>(searchedAnimal, HttpStatus.OK);
	}

	@RequestMapping(path = "/name", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimalsByName(@RequestParam("name") String name) {
		List<Animal> sameNameAnimals = animalService.getAnimalsByName(name);
		if (!sameNameAnimals.isEmpty()) {
			return new ResponseEntity<>(sameNameAnimals, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "/{id}/careTakers", method = RequestMethod.POST)
	public ResponseEntity<CareTaker> addNewCareTakerToExistingAnimal(@PathVariable("id") long id,
																	 @RequestBody @Valid CareTaker careTaker)
			throws AnimalNotFoundException, InvalidCareTakerException {
		CareTaker addedCareTaker = animalService.addNewCareTakerToExistingAnimal(id, careTaker);
		return new ResponseEntity<>(addedCareTaker, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/careTakers", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakersByAnimalId(@PathVariable("id") long id)
			throws AnimalNotFoundException {
		Animal animalById = animalService.getAnimalById(id);
		return new ResponseEntity<>(animalById.getCareTakers(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{animalId}/careTakers/{careTakerId}", method = RequestMethod.PUT)
	public ResponseEntity<CareTaker> addExistingCareTakerToExistingAnimal(
			@PathVariable("animalId") long animalId, @PathVariable("careTakerId") long careTakerId)
			throws AnimalAndCareTakerAlreadyLinked, AnimalNotFoundException, CareTakerNotFoundException {
		CareTaker careTaker = animalService.addExistingCareTakerToExistingAnimal(animalId, careTakerId);
		return new ResponseEntity<>(careTaker, HttpStatus.OK);
	}

	@RequestMapping(path = "/{animalId}/careTakers/{careTakerId}", method = RequestMethod.DELETE)
	public ResponseEntity<List<CareTaker>> removeCareTakerFromCareTakersList(
			@PathVariable("animalId") long animalId, @PathVariable("careTakerId") long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException,
			CareTakersListEmptyException, CareTakerNotInTheAnimalCareTakersListException {
		List<CareTaker> careTakers = animalService.removeCareTakerFromCareTakersList(animalId, careTakerId);
		return new ResponseEntity<>(careTakers, HttpStatus.OK);
	}
}
