package dk.cngroup.trainings.spring.springassignment.exception;

public class AnimalsListEmptyException extends Exception {
	public AnimalsListEmptyException(long id) {
		super("Care Taker with id " + id + " has empty Animals list");
	}
}
