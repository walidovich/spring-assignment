package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caretakers")
public class CareTaker {
	public final static int NAME_MINIMUM_SIZE = 1;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Digits(integer = 6, fraction = 0, message = "id field must be a integer between 0 and 999999")
	@NotNull(message = "id field can''t be empty")
	private Long id;
	@Size(min = NAME_MINIMUM_SIZE, message = "name field must be at least 1 character")
	private String name;
	@ManyToMany
	@JoinTable(
			name = "caretaker_animal",
			joinColumns = @JoinColumn(name = "caretaker_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"))
	private List<Animal> animals;

	public CareTaker() {
		this.id = 0L;
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
