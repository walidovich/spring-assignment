package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/careTakers")
public class CareTakerController {

	private CareTakerService careTakerService;
	private AnimalService animalService;

	public CareTakerController(CareTakerService careTakerService, AnimalService animalService) {
		this.careTakerService = careTakerService;
		this.animalService = animalService;
	}

	@RequestMapping(path = "/{id}/animals", method = RequestMethod.POST)
	public ResponseEntity<Animal> addAnimalToCare(
			@PathVariable("id") long id,
			@RequestBody @Valid Animal animal) {
		if (careTakerService.getCareTakerById(id).isPresent()) {
			Optional<Animal> addedAnimal = animalService.addAnimal(animal);
			if (addedAnimal.isPresent()) {
				return new ResponseEntity<>(
						careTakerService.addAnimalToCare(id, animal).get(),
						HttpStatus.OK);
			} else {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/animals", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimalsInCareByCareTakerId(@PathVariable("id") long id) {
		Optional<CareTaker> careTakerById = careTakerService.getCareTakerById(id);
		if (careTakerById.isPresent()) {
			return new ResponseEntity<>(careTakerById.get().getAnimals(),
					HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakers() {
		return new ResponseEntity<>(careTakerService.getCareTakers(), HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CareTaker> addCareTaker(@RequestBody @Valid CareTaker careTaker) {
		if (careTakerService.addCareTaker(careTaker).isPresent()) {
			return new ResponseEntity<>(careTakerService.addCareTaker(careTaker).get(), HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CareTaker> getCareTakerById(@PathVariable("id") long id) {
		if (careTakerService.getCareTakerById(id).isPresent()) {
			return new ResponseEntity<>(careTakerService.getCareTakerById(id).get()
					, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CareTaker> updateCareTaker(@PathVariable("id") long id, @RequestBody @Valid CareTaker careTaker) {
		if (careTakerService.getCareTakerById(id).isPresent()) {
			careTaker.setId(id);
			return this.addCareTaker(careTaker);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCareTaker(@PathVariable("id") long id) {
		if (careTakerService.deleteCareTakerById(id)) {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Fail: Id doesn't exist", HttpStatus.NOT_FOUND);
		}
	}
}
