package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;

public class CareTakerServiceFieldsTrimmer {

	public static void trimFields(CareTaker careTaker) {
		careTaker.setName(careTaker.getName().trim());
	}
}
