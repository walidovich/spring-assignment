package dk.cngroup.trainings.spring.springassignment.exception;

public class CareTakersListEmptyException extends Exception {
	public CareTakersListEmptyException(long id) {
		super("Animal with id " + id + " has empty Care Takers list");
	}
}
