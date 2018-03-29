package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidCareTakerException;

public class CareTakerValidationService {
	public static void isValid(CareTaker careTaker) throws InvalidCareTakerException {
		isCareTakerNotNull(careTaker);
		isCareTakerNameLengthBiggerThanMinimumSize(careTaker);
	}

	private static void isCareTakerNameLengthBiggerThanMinimumSize(CareTaker careTaker) throws InvalidCareTakerException {
		if (careTaker.getName().length() < 1) {
			throw new InvalidCareTakerException();
		}
	}

	private static void isCareTakerNotNull(CareTaker careTaker) throws InvalidCareTakerException {
		if (careTaker == null) {
			throw new InvalidCareTakerException();
		}
	}
}
