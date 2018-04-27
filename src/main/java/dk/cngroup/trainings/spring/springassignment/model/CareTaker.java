package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caretakers")
public class CareTaker {

	@Id
	private Long id;
	@NotNull
	@NotBlank(message = "name field can not be empty")
	private String name;
	@ManyToMany
	@JoinTable(
			name = "caretaker_animal",
			joinColumns = @JoinColumn(name = "caretaker_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"))
	private List<Animal> animals;

	public CareTaker() {
	}

	public CareTaker(Long id, String name) {
		this.id = id;
		this.name = name.trim();
		this.animals = new ArrayList<>();
	}

	public void addAnimalToCareTaker(Animal animal) {
		animals.add(animal);
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
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
		this.name = name.trim();
	}

	@Override
	public String toString() {
		return "CareTaker{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
