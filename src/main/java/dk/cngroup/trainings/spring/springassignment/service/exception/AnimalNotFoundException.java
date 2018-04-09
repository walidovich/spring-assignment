package dk.cngroup.trainings.spring.springassignment.service.exception;

public class AnimalNotFoundException extends Exception {

	public AnimalNotFoundException(long id) {
		super("Animal with id " + id + " not found");
	}
}
