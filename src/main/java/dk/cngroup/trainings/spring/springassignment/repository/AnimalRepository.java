package dk.cngroup.trainings.spring.springassignment.repository;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Long> {

	@Override
	List<Animal> findAll();

	@Override
	Optional<Animal> findById(Long id);

	List<Animal> findAllByName(String name);

	@Override
	Animal save(Animal animal);

	void deleteById(long id);

	boolean existsById(long id);
}
