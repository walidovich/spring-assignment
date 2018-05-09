package dk.cngroup.trainings.spring.springassignment.service.user;

import dk.cngroup.trainings.spring.springassignment.model.User;
import dk.cngroup.trainings.spring.springassignment.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSreviceImpl implements UserService {

	private UserRepository userRepository;

	public UserSreviceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User addUser(User user) {
		if (!existsByEmail(user.getEmail())) {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			return userRepository.save(user);
		} else {
			return null;
		}
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
}
