package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.exception.user.UserEmailExistsException;
import dk.cngroup.trainings.spring.springassignment.model.User;
import dk.cngroup.trainings.spring.springassignment.repository.UserRepository;
import dk.cngroup.trainings.spring.springassignment.service.user.UserService;
import dk.cngroup.trainings.spring.springassignment.service.user.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

	private UserService userService;
	private UserRepository userRepository;
	private User walid;

	@Before
	public void before() {
		userRepository = Mockito.mock(UserRepository.class);
		userService = new UserServiceImpl(userRepository);
		walid = new User(1L, "Walid", "Cheikh", "walid@email.com",
				"0123");
	}

	@Test
	public void addValidUserTest() throws UserEmailExistsException {
		// Happy path:

		User walidInDb = new User(1L, "Walid", "Cheikh", "walid@email.com",
				new BCryptPasswordEncoder().encode("1230"));
		Mockito.when(userRepository.existsByEmail(walid.getEmail()))
				.thenReturn(false);
		Mockito.when(userRepository.save(walid))
				.thenReturn(walidInDb);

		User user = userService.addUser(walid);

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getEmail(), walid.getEmail());
	}

	@Test(expected = UserEmailExistsException.class)
	public void addExistingUserTest() throws UserEmailExistsException {
		// Sad path: User email already exists

		Mockito.when(userRepository.existsByEmail(walid.getEmail()))
				.thenReturn(true);

		User user = userService.addUser(walid);
	}

	@Test
	public void loadUserByExistingUsernameTest() {
		// Happy path:

		User walidInDb = new User(1L, "Walid", "Cheikh", "walid@email.com",
				new BCryptPasswordEncoder().encode("1230"));

		Mockito.when(userRepository.findByEmail(walid.getEmail()))
				.thenReturn(java.util.Optional.ofNullable(walidInDb));

		UserDetails userDetails = userService.loadUserByUsername(walid.getEmail());

		Assert.assertNotNull(userDetails);
		Assert.assertEquals(userDetails.getUsername(), walid.getEmail());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByNonExistingUsernameTest() {
		// Sad path: username non existing

		Mockito.when(userRepository.findByEmail(walid.getEmail()))
				.thenReturn(Optional.empty());

		userService.loadUserByUsername(walid.getEmail());
	}
}
