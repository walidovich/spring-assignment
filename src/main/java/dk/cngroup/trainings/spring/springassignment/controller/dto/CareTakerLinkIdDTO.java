package dk.cngroup.trainings.spring.springassignment.controller.dto;

import javax.validation.constraints.NotNull;

public class CareTakerLinkIdDTO {

	@NotNull
	private Long id;

	public CareTakerLinkIdDTO() {
	}

	public CareTakerLinkIdDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CareTakerLinkIdDTO{" +
				"id=" + id +
				'}';
	}
}
