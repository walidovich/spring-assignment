package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

public class CareTakerValidationService {
	public static boolean isValid(CareTaker careTaker) {
		return careTaker != null
				&& careTaker.getName().length() >= CareTaker.NAME_MINIMUM_SIZE;
	}
}
