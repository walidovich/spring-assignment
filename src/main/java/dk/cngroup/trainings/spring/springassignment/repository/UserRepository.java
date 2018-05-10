package dk.cngroup.trainings.spring.springassignment.repository;

import dk.cngroup.trainings.spring.springassignment.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@Override
	User save(User user);

	boolean existsByEmail(String email);
}