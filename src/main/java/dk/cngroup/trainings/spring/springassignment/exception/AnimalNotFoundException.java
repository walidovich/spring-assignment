package dk.cngroup.trainings.spring.springassignment.exception;

public class AnimalNotFoundException extends Exception {

	public AnimalNotFoundException(long id) {
		super("Animal with id " + id + " not found");
	}
}
