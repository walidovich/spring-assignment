package dk.cngroup.trainings.spring.springassignment.exception.caretaker;

public class CareTakerNotFoundException extends Exception {

	public CareTakerNotFoundException(long id) {
		super("Care Taker with id " + id + " not found");
	}
}
