package dk.cngroup.trainings.spring.springassignment.controller.rest;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import dk.cngroup.trainings.spring.springassignment.service.exception.*;
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

	public CareTakerController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakers() {
		return new ResponseEntity<>(careTakerService.getCareTakers(), HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CareTaker> addCareTaker(@RequestBody @Valid CareTaker careTaker) {
		try {
			return new ResponseEntity<>(careTakerService.addCareTaker(careTaker).get(), HttpStatus.OK);
		} catch (InvalidCareTakerException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CareTaker> updateCareTakerById(@PathVariable("id") long id, @RequestBody @Valid CareTaker careTaker) {
		try {
			return new ResponseEntity<>(careTakerService.updateCareTakerById(id, careTaker).get(),
					HttpStatus.OK);
		} catch (CareTakerNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (InvalidCareTakerException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CareTaker> getCareTakerById(@PathVariable("id") long id) {
		try {
			return new ResponseEntity<>(careTakerService.getCareTakerById(id).get()
					, HttpStatus.OK);
		} catch (CareTakerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCareTakerById(@PathVariable("id") long id) {
		try {
			careTakerService.deleteCareTakerById(id);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CareTakerNotFoundException e) {
			return new ResponseEntity<>("Fail: Care Taker with id " + id + " doesn't exist"
					, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{id}/animals", method = RequestMethod.POST)
	public ResponseEntity<Animal> addNewAnimalToExistingCareTaker(@PathVariable("id") long id,
																  @RequestBody @Valid Animal animal) {
		Optional<Animal> addedAnimal;
		try {
			addedAnimal = careTakerService.addNewAnimalToExistingCareTaker(id, animal);
			return new ResponseEntity<>(addedAnimal.get(), HttpStatus.OK);
		} catch (InvalidAnimalException e) {
			return ResponseEntity.badRequest().build();
		} catch (CareTakerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}


	@RequestMapping(value = "/{id}/animals", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimalsInCareByCareTakerId(@PathVariable("id") long id) {
		Optional<CareTaker> careTakerById;
		try {
			careTakerById = careTakerService.getCareTakerById(id);
			return new ResponseEntity<>(careTakerById.get().getAnimals()
					, HttpStatus.OK);
		} catch (CareTakerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "/{careTakerId}/animals/{animalId}", method = RequestMethod.PUT)
	public ResponseEntity<Animal> addExistingAnimalToExistingCareTaker(@PathVariable("careTakerId") long careTakerId,
																	   @PathVariable("animalId") long animalId) {
		try {
			Optional<Animal> animal = careTakerService.addExistingAnimalToExistingCareTaker(careTakerId, animalId);
			return new ResponseEntity<>(animal.get(), HttpStatus.OK);
		} catch (AnimalNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (CareTakerNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (AnimalAndCareTakerAlreadyLinked e) {
			return ResponseEntity.badRequest().build();
		}
	}
}