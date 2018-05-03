package dk.cngroup.trainings.spring.springassignment.controller.dto.animal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AnimalLinkIdDTO {

	@Digits(integer = 6, fraction = 0, message = "id field must be a integer between 0 and 999999")
	@NotNull(message = "id field can''t be empty")
	private Long id;

	public AnimalLinkIdDTO() {
	}

	public AnimalLinkIdDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
