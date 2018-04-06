package dk.cngroup.trainings.spring.springassignment.controller.rest;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;
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
	public ResponseEntity<Animal> addAnimal(@RequestBody @Valid Animal animal) {
		try {
			return new ResponseEntity<>(animalService.addAnimal(animal).get(), HttpStatus.OK);
		} catch (InvalidAnimalException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") long id,
												   @RequestBody @Valid Animal animal) {
		try {
			return new ResponseEntity<>(animalService.updateAnimalById(id, animal).get(),
					HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (InvalidAnimalException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimals() {
		return new ResponseEntity<>(animalService.getAnimals(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAnimalById(@PathVariable("id") long id) {
		try {
			animalService.deleteAnimalById(id);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return new ResponseEntity<>("Fail: Animal with id " + id + " doesn't exist"
					, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Animal> getAnimalById(@PathVariable("id") long id) {
		Optional<Animal> searchedAnimal;
		try {
			searchedAnimal = animalService.getAnimalById(id);
			return new ResponseEntity<>(searchedAnimal.get(), HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
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
																	 @RequestBody @Valid CareTaker careTaker) {
		Optional<CareTaker> addedCareTaker;
		try {
			addedCareTaker = animalService.addNewCareTakerToExistingAnimal(id, careTaker);
			return new ResponseEntity<>(addedCareTaker.get(), HttpStatus.OK);
		} catch (InvalidCareTakerException e) {
			return ResponseEntity.badRequest().build();
		} catch (AnimalNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/careTakers", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakersByAnimalId(@PathVariable("id") long id) {
		Optional<Animal> animalById;
		try {
			animalById = animalService.getAnimalById(id);
			return new ResponseEntity<>(animalById.get().getCareTakers()
					, HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "/{animalId}/careTakers/{careTakerId}", method = RequestMethod.PUT)
	public ResponseEntity<String> addExistingCareTakerToExistingAnimal(@PathVariable("animalId") long animalId,
																	   @PathVariable("careTakerId") long careTakerId) {
		try {
			animalService.addExistingCareTakerToExistingAnimal(animalId, careTakerId);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return new ResponseEntity<>("Fail: Animal with id " + animalId + " doesn't exist"
					, HttpStatus.NOT_FOUND);
		} catch (CareTakerNotFoundException e) {
			return new ResponseEntity<>("Fail: Care Taker with id " + careTakerId + " doesn't exist"
					, HttpStatus.NOT_FOUND);
		} catch (AnimalAndCareTakerAlreadyLinked e) {
			return new ResponseEntity<>("Warning: Care Taker with id " + careTakerId + " and Animal" +
					" with id " + animalId + " already linked"
					, HttpStatus.BAD_REQUEST);
		}
	}
}
