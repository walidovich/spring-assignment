package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

public class CareTakerValidationService {
	public static boolean isValid(CareTaker careTaker) {
		return isCareTakerNotNull(careTaker)
				&& isCareTakerNameLengthBiggerThanMinimumSize(careTaker);
	}

	public static boolean isCareTakerNameLengthBiggerThanMinimumSize(CareTaker careTaker) {
		return careTaker.getName().length() >= CareTaker.NAME_MINIMUM_SIZE;
	}

	public static boolean isCareTakerNotNull(CareTaker careTaker) {
		return careTaker != null;
	}
}
