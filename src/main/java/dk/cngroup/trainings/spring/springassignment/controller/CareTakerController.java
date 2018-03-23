package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
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

	@RequestMapping(path = "/{careTaker_id}/animals/{animal_id}", method = RequestMethod.POST)
	public ResponseEntity<List<Animal>> addAnimalToCare(@PathVariable("careTaker_id") long careTakerId, @PathVariable("animal_id") long animalId) {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{id}/animals", method = RequestMethod.GET)
	public ResponseEntity<List<Animal>> getAnimalsInCare(@PathVariable("id") long id) {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<List<CareTaker>> getCareTakers() {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Optional<CareTaker>> addCareTaker(@RequestBody @Valid CareTaker careTaker) {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Optional<CareTaker>> getCareTakerById(@PathVariable("id") long id) {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CareTaker> updateCareTaker(long id, @RequestBody @Valid CareTaker careTaker) {
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCareTaker(long id) {
		return ResponseEntity.notFound().build();
	}
}
