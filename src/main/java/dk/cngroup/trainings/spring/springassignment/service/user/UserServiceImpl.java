package dk.cngroup.trainings.spring.springassignment.service.user;

import dk.cngroup.trainings.spring.springassignment.exception.UserEmailExistsException;
import dk.cngroup.trainings.spring.springassignment.model.MyUserDetails;
import dk.cngroup.trainings.spring.springassignment.model.User;
import dk.cngroup.trainings.spring.springassignment.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public User addUser(User user) throws UserEmailExistsException {
		if (!existsByEmail(user.getEmail())) {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		} else {
			throw new UserEmailExistsException(user.getEmail());
		}
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findByEmail(username);
		if (user.isPresent()) {
			return new MyUserDetails(user.get());
		} else {
			throw new UsernameNotFoundException(username + " doesn't exist");
		}
	}
}
