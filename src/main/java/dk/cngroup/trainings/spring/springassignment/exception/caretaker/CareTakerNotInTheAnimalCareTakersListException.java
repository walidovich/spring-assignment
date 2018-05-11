package dk.cngroup.trainings.spring.springassignment.exception.caretaker;

public class CareTakerNotInTheAnimalCareTakersListException extends Exception {
	public CareTakerNotInTheAnimalCareTakersListException(long animalId, long careTakerId) {
		super("Care Taker with id " + careTakerId + " doesn't exist in the care takers list of Animal with id " + animalId);
	}
}
