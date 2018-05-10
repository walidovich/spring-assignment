package dk.cngroup.trainings.spring.springassignment.service.user;

import dk.cngroup.trainings.spring.springassignment.exception.UserEmailExistsException;
import dk.cngroup.trainings.spring.springassignment.model.User;

public interface UserService {

	User addUser(User user) throws UserEmailExistsException;

	boolean existsByEmail(String email);
}
