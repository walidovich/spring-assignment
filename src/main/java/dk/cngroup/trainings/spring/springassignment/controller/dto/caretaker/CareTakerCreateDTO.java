package dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker;

import javax.validation.constraints.Size;

public class CareTakerCreateDTO {

	@Size(min = 1, message = "name field must be at least 1 character")
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
