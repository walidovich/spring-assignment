package dk.cngroup.trainings.spring.springassignment.service.exception;

public class AnimalAndCareTakerAlreadyLinked extends Exception {
	public AnimalAndCareTakerAlreadyLinked(long animalId, long careTakerId) {
		super("Animal with id " + animalId + " and Care Taker with id "
				+ careTakerId + " already linked");
	}
}
