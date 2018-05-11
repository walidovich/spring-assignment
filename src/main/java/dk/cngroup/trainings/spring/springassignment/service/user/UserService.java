package dk.cngroup.trainings.spring.springassignment.service.user;

import dk.cngroup.trainings.spring.springassignment.exception.user.UserEmailExistsException;
import dk.cngroup.trainings.spring.springassignment.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	User addUser(User user) throws UserEmailExistsException;

	boolean existsByEmail(String email);
}
