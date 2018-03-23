package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CareTakerControllerTest {

	MockMvc mockMvc;
	List<CareTaker> careTakers;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private CareTakerService careTakerService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
				build();

		CareTaker jane = new CareTaker(1L, "Jane Doe");
		CareTaker walid = new CareTaker(5L, "Walid CHEIKH");
		CareTaker adam = new CareTaker(12L, "Adam Kucera");
		CareTaker john = new CareTaker(1L, "John Doe");
		careTakers = Arrays.asList(jane, walid, adam, john);
	}
}
