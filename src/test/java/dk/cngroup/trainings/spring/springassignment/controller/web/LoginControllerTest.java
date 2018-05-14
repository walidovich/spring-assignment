package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.config.WebSecurityConfig;
import dk.cngroup.trainings.spring.springassignment.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebSecurityConfig.class})
@WebAppConfiguration
@SpringBootTest
public class LoginControllerTest {

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private UserService userService;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	public void nonLoggedUserRedirectionTest() throws Exception {
		mockMvc.perform(get("/web/animal/list"))
				.andExpect(redirectedUrl("http://localhost/web/login"));
	}

	@Test
	public void redirectLoggedOffUserTest() throws Exception {
		mockMvc.perform(logout("/web/logout"))
				.andExpect(redirectedUrl("/web/login?logout"))
				.andExpect(unauthenticated());
	}
}
