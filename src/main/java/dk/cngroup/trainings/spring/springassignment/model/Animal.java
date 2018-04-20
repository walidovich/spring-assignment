package dk.cngroup.trainings.spring.springassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animals")
public class Animal {
	public final static int NAME_MINIMUM_SIZE = 2;
	public final static int DESCRIPTION_MAXIMUM_SIZE = 10000;
	@Id
	private Long id;
	@NotNull
	@Size(min = NAME_MINIMUM_SIZE, message = "name field must be at least 2 characters")
	private String name;
	@Size(max = DESCRIPTION_MAXIMUM_SIZE, message = "description field must not exceed 10000 characters")
	private String description;
	@JsonIgnore
	@ManyToMany(mappedBy = "animals")
	private List<CareTaker> careTakers;

	public Animal() {
	}

	public Animal(Long id, String name, String description) {
		this.id = id;
		this.name = name.trim();
		this.description = description.trim();
		this.careTakers = new ArrayList<>();
	}

	public void addCareTaker(CareTaker careTaker) {
		careTakers.add(careTaker);
	}

	public List<CareTaker> getCareTakers() {
		return careTakers;
	}

	public void setCareTakers(List<CareTaker> careTakers) {
		this.careTakers = careTakers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Animal{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
