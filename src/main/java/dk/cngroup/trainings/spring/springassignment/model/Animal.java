package dk.cngroup.trainings.spring.springassignment.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = NAME_MINIMUM_SIZE)
    @NotNull
    private String name;

    @Size(max = DESCRIPTION_MAXIMUM_SIZE)
    private String description;

    public final static int NAME_MINIMUM_SIZE=2;
    public final static int DESCRIPTION_MAXIMUM_SIZE=10000;

    public Animal() {
    }

    public Animal(Long id,String name, String description) {
        this.id=id;
        this.name = name;
        this.description = description;
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
