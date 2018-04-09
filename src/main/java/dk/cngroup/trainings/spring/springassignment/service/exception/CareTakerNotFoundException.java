package dk.cngroup.trainings.spring.springassignment.service.exception;

public class CareTakerNotFoundException extends Exception {

	public CareTakerNotFoundException(long id) {
		super("Care Taker with id " + id + " not found");
	}
}
