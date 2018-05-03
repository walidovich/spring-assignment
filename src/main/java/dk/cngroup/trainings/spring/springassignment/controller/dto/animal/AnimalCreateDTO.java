package dk.cngroup.trainings.spring.springassignment.controller.dto.animal;

import javax.validation.constraints.Size;

public class AnimalCreateDTO {

	@Size(min = 2, message = "name field must be at least 2 characters")
	private String name;
	@Size(max = 10000, message = "description field must not exceed 10000 characters")
	private String description;

	public AnimalCreateDTO() {
	}

	public AnimalCreateDTO(String name) {
		this.name = name.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}
}
