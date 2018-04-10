package dk.cngroup.trainings.spring.springassignment.controller.rest;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
	public ResponseEntity<CareTaker> addCareTaker(@RequestBody @Valid CareTaker careTaker)
			throws InvalidCareTakerException {
		return new ResponseEntity<>(careTakerService.addCareTaker(careTaker), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CareTaker> updateCareTakerById(
			@PathVariable("id") long id, @RequestBody @Valid CareTaker careTaker)
			throws CareTakerNotFoundException, InvalidCareTakerException {
		return new ResponseEntity<>(careTakerService.updateCareTakerById(id, careTaker), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CareTaker> getCareTakerById(@PathVariable("id") long id) throws CareTakerNotFoundException {
		return new ResponseEntity<>(careTakerService.getCareTakerById(id), HttpStatus.OK);
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCareTakerById(@PathVariable("id") long id) throws CareTakerNotFoundException {
		careTakerService.deleteCareTakerById(id);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}/animals", method = RequestMethod.POST)
	public ResponseEntity<Animal> addNewAnimalToExistingCareTaker(
			@PathVariable("id") long id, @RequestBody @Valid Animal animal)
			throws InvalidAnimalException, CareTakerNotFoundException {
		Animal addedAnimal = careTakerService.addNewAnimalToExistingCareTaker(id, animal);
		return new ResponseEntity<>(addedAnimal, HttpStatus.OK);
	}


	@RequestMapping(value = "/{id}/animals", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimalsInCareByCareTakerId(@PathVariable("id") long id)
			throws CareTakerNotFoundException {
		CareTaker careTakerById = careTakerService.getCareTakerById(id);
		return new ResponseEntity<>(careTakerById.getAnimals(), HttpStatus.OK);
	}

	@RequestMapping(path = "/{careTakerId}/animals/{animalId}", method = RequestMethod.PUT)
	public ResponseEntity<Animal> addExistingAnimalToExistingCareTaker(
			@PathVariable("careTakerId") long careTakerId, @PathVariable("animalId") long animalId)
			throws AnimalAndCareTakerAlreadyLinked, AnimalNotFoundException, CareTakerNotFoundException {
		Animal animal = careTakerService.addExistingAnimalToExistingCareTaker(careTakerId, animalId);
		return new ResponseEntity<>(animal, HttpStatus.OK);
	}
}