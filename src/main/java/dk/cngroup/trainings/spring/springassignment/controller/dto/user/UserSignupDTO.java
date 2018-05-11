package dk.cngroup.trainings.spring.springassignment.controller.dto.user;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserSignupDTO {

	@NotNull
	@Size(min = 2, max = 20, message = "first name must be between 2 and 20 characters")
	private String firstName;
	@NotNull
	@Size(min = 2, max = 20, message = "last name must be between 2 and 20 characters")
	private String lastName;
	@NotNull
	@Column(unique = true)
	@Email
	@Pattern(regexp = ".+@.+\\.[a-z]+", message = "invalid email address")
	private String email;
	@NotNull
	@Size(min = 4, message = "password must be at least 4 characters")
	private String password;
	private String matchingPassword;

	public UserSignupDTO() {
	}

	public UserSignupDTO(String firstName, String lastName, String email, String password, String matchingPassword) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
}
