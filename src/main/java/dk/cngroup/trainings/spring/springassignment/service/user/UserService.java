package dk.cngroup.trainings.spring.springassignment.service.user;

import dk.cngroup.trainings.spring.springassignment.model.User;

public interface UserService {

	User addUser(User user);

	boolean existsByEmail(String email);
}
