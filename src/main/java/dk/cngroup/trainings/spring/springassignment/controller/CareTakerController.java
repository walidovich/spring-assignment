package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
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

	public CareTakerController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakers() {
		return new ResponseEntity<>(careTakerService.getCareTakers(), HttpStatus.OK);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<CareTaker> addCareTaker(@RequestBody @Valid CareTaker careTaker) {
		Optional<CareTaker> addedCareTaker = careTakerService.addCareTaker(careTaker);
		if (addedCareTaker.isPresent()) {
			return new ResponseEntity<>(addedCareTaker.get(), HttpStatus.OK);
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

	@RequestMapping(path = "/{id}/animals", method = RequestMethod.POST)
	public ResponseEntity<Animal> addNewAnimalToExistingCareTaker(@PathVariable("id") long id,
																  @RequestBody @Valid Animal animal) {
		if (careTakerService.getCareTakerById(id).isPresent()) {
			Optional<Animal> addedAnimal = careTakerService.addNewAnimalToExistingCareTaker(id, animal);
			if (addedAnimal.isPresent()) {
				return new ResponseEntity<>(addedAnimal.get(), HttpStatus.OK);
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
			return new ResponseEntity<>(careTakerById.get().getAnimals()
					, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(path = "/{careTakerId}/animals/{animalId}", method = RequestMethod.PUT)
	public ResponseEntity<String> addExistingAnimalToExistingCareTaker(@PathVariable("careTakerId") long careTakerId,
																	   @PathVariable("animalId") long animalId) {
		System.out.println(">>>>>>> From careTakerController");
		String result = careTakerService.addExistingAnimalToExistingCareTaker(careTakerId, animalId);
		if (result.startsWith("Success")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}
}
