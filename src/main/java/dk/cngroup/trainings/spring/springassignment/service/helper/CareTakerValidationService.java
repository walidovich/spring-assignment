package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;

public class CareTakerValidationService {
	public static void validate(CareTaker careTaker) throws InvalidCareTakerException {
		checkCareTakerNotNull(careTaker);
		checkCareTakerNameLengthBiggerThanMinimumSize(careTaker);
	}

	private static void checkCareTakerNameLengthBiggerThanMinimumSize(CareTaker careTaker) throws InvalidCareTakerException {
		if (careTaker.getName().length() < 1) {
			throw new InvalidCareTakerException("Fail: Care Taker name should not be empty");
		}
	}

	private static void checkCareTakerNotNull(CareTaker careTaker) throws InvalidCareTakerException {
		if (careTaker == null) {
			throw new InvalidCareTakerException("Fail: Care Taker is null");
		}
	}
}
