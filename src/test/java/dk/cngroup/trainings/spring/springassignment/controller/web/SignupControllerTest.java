package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.controller.dto.user.UserSignupDTO;
import dk.cngroup.trainings.spring.springassignment.controller.helper.UserDtoValidatorService;
import dk.cngroup.trainings.spring.springassignment.service.user.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class SignupControllerTest {

	@MockBean
	private UserService userService;
	@MockBean
	private UserDtoValidatorService userDtoValidatorService;

	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	public void signupFormTest() throws Exception {
		// Always happy path:
		mockMvc.perform(get("/signup"))
				.andExpect(view().name("views/authentication/signup"))
				.andExpect(model().attribute("userSignupDto",
						Matchers.notNullValue(UserSignupDTO.class)));
	}

	@Test
	public void signupValidUserTest() throws Exception {
		// Happy path:

		mockMvc.perform(post("/signup")
				.param("firstName", "Walid")
				.param("lastName", "cheikh")
				.param("email", "walid@email.com")
				.param("password", "1234")
				.param("matchingPassword", "1234"))
				.andExpect(redirectedUrl("/web/login"));
	}

	// Sad paths still need to be implemented.
}
