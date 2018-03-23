package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "careTakers")
public class CareTaker {
	public final static int NAME_MINIMUM_SIZE = 1;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotNull
	@Size(min = NAME_MINIMUM_SIZE)
	private String name;
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "careTaker_animal",
			joinColumns = @JoinColumn(name = "careTaker_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"))
	private List<Animal> animals;

	public CareTaker() {
	}

	public CareTaker(long id, String name) {
		this.id = id;
		this.name = name.trim();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}
}
