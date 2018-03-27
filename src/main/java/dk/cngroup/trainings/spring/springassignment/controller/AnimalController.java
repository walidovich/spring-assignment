package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
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
	public ResponseEntity<Animal> addAnimal(@RequestBody @Valid Animal animal) {
		Optional<Animal> addedAnimal = animalService.addAnimal(animal);
		if (addedAnimal.isPresent()) {
			return new ResponseEntity<>(addedAnimal.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimals() {
		return new ResponseEntity<>(animalService.getAnimals(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAnimalById(@PathVariable("id") long id) {
		if (animalService.deleteAnimalById(id)) {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Fail: Id not found.", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") long id,
												   @RequestBody @Valid Animal animal) {
		if (animalService.getAnimalById(id).isPresent()) {
			animal.setId(id);
			return this.addAnimal(animal);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Animal> getAnimalById(@PathVariable("id") long id) {
		Optional<Animal> searchedAnimal = animalService.getAnimalById(id);
		if (searchedAnimal.isPresent()) {
			return new ResponseEntity<>(searchedAnimal.get(), HttpStatus.OK);
		} else {
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
		if (animalService.getAnimalById(id).isPresent()) {
			Optional<CareTaker> addedCareTaker = animalService.addNewCareTakerToExistingAnimal(id, careTaker);
			if (addedCareTaker.isPresent()) {
				return new ResponseEntity<>(addedCareTaker.get(), HttpStatus.OK);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/careTakers", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakersByAnimalId(@PathVariable("id") long id) {
		Optional<Animal> animalById = animalService.getAnimalById(id);
		if (animalById.isPresent()) {
			return new ResponseEntity<>(animalById.get().getCareTakers()
					, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	Is it Ok if I injected CareTakerController and I used its method
	addExistingAnimalToExistingCareTaker(@PathVariable("careTakerId") long careTakerId,
	@PathVariable("animalId") long animalId) to not replicate the same logic with just
	the order of the ids reversed?
	In your comment you said controllers should not depend on each other.
	 */
	@RequestMapping(path = "/{animalId}/careTakers/{careTakerId}", method = RequestMethod.PUT)
	public ResponseEntity<String> addExistingCareTakerToExistingAnimal(@PathVariable("animalId") long animalId,
																	   @PathVariable("careTakerId") long careTakerId) {
		System.out.println(">>>>>>> From animalController");
		String result = animalService.addExistingCareTakerToExistingAnimal(animalId, careTakerId);
		if (result.startsWith("Success")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}
}
