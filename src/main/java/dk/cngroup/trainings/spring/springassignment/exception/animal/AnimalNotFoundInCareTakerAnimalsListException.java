package dk.cngroup.trainings.spring.springassignment.exception.animal;

public class AnimalNotFoundInCareTakerAnimalsListException extends Exception {
	public AnimalNotFoundInCareTakerAnimalsListException(long careTakerId, long animalId) {
		super("Animal with id " + animalId + " doesn't exist in the animals list of Care Taker with id " + careTakerId);
	}
}
