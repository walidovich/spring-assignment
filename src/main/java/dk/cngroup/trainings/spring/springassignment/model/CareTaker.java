package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "caretakers")
public class CareTaker {
	public final static int NAME_MINIMUM_SIZE = 1;
	@Id
	private Long id;
	@NotNull
	@Size(min = NAME_MINIMUM_SIZE)
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
		this.name = name;
	}

	@Override
	public String toString() {
		return "CareTaker{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
