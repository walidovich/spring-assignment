package dk.cngroup.trainings.spring.springassignment.controller.advice;

import dk.cngroup.trainings.spring.springassignment.exception.animal.*;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.CareTakerNotFoundException;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.CareTakerNotInTheAnimalCareTakersListException;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.CareTakersListEmptyException;
import dk.cngroup.trainings.spring.springassignment.exception.caretaker.InvalidCareTakerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class RestControllerExceptionsAdvice {

	@ExceptionHandler(value = AnimalNotFoundException.class)
	public ResponseEntity<String> animalNotFound(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidAnimalException.class)
	public ResponseEntity<String> invalidAnimal(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CareTakerNotFoundException.class)
	public ResponseEntity<String> careTakerNotFound(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidCareTakerException.class)
	public ResponseEntity<String> invalidCareTakerException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AnimalAndCareTakerAlreadyLinked.class)
	public ResponseEntity<String> animalAndCareTakerAlreadyLinked(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AnimalsListEmptyException.class)
	public ResponseEntity<String> animalsListIsEmpty(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CareTakersListEmptyException.class)
	public ResponseEntity<String> careTakersListIsEmpty(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AnimalNotFoundInCareTakerAnimalsListException.class)
	public ResponseEntity<String> animalInCareNotFound(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CareTakerNotInTheAnimalCareTakersListException.class)
	public ResponseEntity<String> careTakerNotTaking(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
