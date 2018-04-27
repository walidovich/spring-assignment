package dk.cngroup.trainings.spring.springassignment.controller.dto;

import javax.validation.constraints.NotNull;

public class CareTakerLinkIdDTO {

	@NotNull
	private Long careTakerId;

	public CareTakerLinkIdDTO() {
	}

	public CareTakerLinkIdDTO(Long id) {
		this.careTakerId = id;
	}

	public Long getId() {
		return careTakerId;
	}

	public void setId(Long id) {
		this.careTakerId = id;
	}

	@Override
	public String toString() {
		return "CareTakerLinkIdDTO{" +
				"id=" + careTakerId +
				'}';
	}
}
