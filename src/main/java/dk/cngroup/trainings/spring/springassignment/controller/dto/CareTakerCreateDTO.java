package dk.cngroup.trainings.spring.springassignment.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CareTakerCreateDTO {

	@NotNull
	@NotBlank(message = "name field cannot be empty")
	private String name;

	public CareTakerCreateDTO() {
	}

	public CareTakerCreateDTO(String name) {
		this.name = name.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}
}
