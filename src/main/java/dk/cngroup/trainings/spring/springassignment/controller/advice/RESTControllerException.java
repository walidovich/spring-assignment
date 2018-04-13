package dk.cngroup.trainings.spring.springassignment.controller.advice;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class RESTControllerException {

	@ExceptionHandler(value = AnimalNotFoundException.class)
	public ResponseEntity<String> animalNotFoundException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidAnimalException.class)
	public ResponseEntity<String> invalidAnimalException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CareTakerNotFoundException.class)
	public ResponseEntity<String> careTakerNotFoundException(Exception e) {
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
}
