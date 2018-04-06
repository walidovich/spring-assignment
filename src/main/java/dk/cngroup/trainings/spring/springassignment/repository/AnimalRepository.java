package dk.cngroup.trainings.spring.springassignment.repository;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Long> {

	@Override
	List<Animal> findAll();

	List<Animal> findAllByName(String name);

	@Override
	Animal save(Animal animal);
}
