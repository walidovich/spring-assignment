package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	@Column(name = "firstname")
	private String firstName;
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	@Column(name = "lastname")
	private String lastName;
	@NotNull
	@NotEmpty
	@Column(unique = true)
	private String email;
	@NotNull
	@NotEmpty
	private String password;

	public User() {
	}

	public User(Long id, @NotNull @NotEmpty @Size(min = 2, max = 20) String firstName, @NotNull @NotEmpty @Size(min = 2, max = 20) String lastName, @NotNull @NotEmpty String email, @NotNull @NotEmpty String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}