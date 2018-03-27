package dk.cngroup.trainings.spring.springassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CareTakerControllerTest {

	MockMvc mockMvc;
	List<CareTaker> careTakers;
	List<Animal> animals;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private CareTakerService careTakerService;
	@MockBean
	private AnimalService animalService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
				build();

		CareTaker jane = new CareTaker(1L, "Jane Doe");
		CareTaker walid = new CareTaker(5L, "Walid CHEIKH");
		CareTaker adam = new CareTaker(12L, "Adam Kucera");
		CareTaker john = new CareTaker(1L, "John Doe");
		careTakers = Arrays.asList(jane, walid, adam, john);

		Animal lion = new Animal(1L, "Lion", "King of Africa");
		Animal eagle = new Animal(1L, "Eagle", "King of the skies");
		Animal dolphin = new Animal(1L, "Dolphin", "Cute and smart");
		animals = Arrays.asList(lion, eagle, dolphin);
	}

	@Test
	public void testGetCareTakers() throws Exception {
		// Always happy
		Mockito.when(careTakerService.getCareTakers()).thenReturn(careTakers);

		mockMvc.perform(get("/careTakers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[1].name", startsWith("Walid")));
	}

	@Test
	public void testAddValidCareTaker() throws Exception {
		// Happy path
		CareTaker martin = new CareTaker(1L, "Martin Dobias");
		ObjectMapper objectMapper = new ObjectMapper();
		String martinString = objectMapper.writeValueAsString(martin);

		Mockito.when(careTakerService.addCareTaker(any(CareTaker.class))).thenReturn(java.util.Optional.ofNullable(martin));

		mockMvc.perform(post("/careTakers")
				.content(martinString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", startsWith("Martin")));
	}

	@Test
	public void testAddCareTakerWithEmptyName() throws Exception {
		// Happy path
		CareTaker martin = new CareTaker(1L, "                    ");
		ObjectMapper objectMapper = new ObjectMapper();
		String martinString = objectMapper.writeValueAsString(martin);

		Mockito.when(careTakerService.addCareTaker(any(CareTaker.class))).thenReturn(Optional.empty());

		mockMvc.perform(post("/careTakers")
				.content(martinString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetCareTakerByExistingId() throws Exception {
		// Happy path
		CareTaker martin = new CareTaker(1L, "Martin Dobias");

		Mockito.when(careTakerService.getCareTakerById(any(Long.class)))
				.thenReturn(Optional.ofNullable(martin));

		mockMvc.perform(get("/careTakers/10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", startsWith("Martin")));
	}

	@Test
	public void testGetCareTakerByNonExistingId() throws Exception {
		// Sad path
		Mockito.when(careTakerService.getCareTakerById(any(Long.class)))
				.thenReturn(Optional.empty());

		mockMvc.perform(get("/careTakers/10"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateCareTakerByExistingIdAndValidCareTaker()
			throws Exception {
		// Happy path
		CareTaker updatedJohn = new CareTaker(7L, "John Cina");

		ObjectMapper objectMapper = new ObjectMapper();
		String updatedJohnString = objectMapper.writeValueAsString(updatedJohn);

		Mockito.when(careTakerService.getCareTakerById(any(Long.class)))
				.thenReturn(Optional.ofNullable(careTakers.get(3)));
		Mockito.when(careTakerService.addCareTaker(any(CareTaker.class)))
				.thenReturn(java.util.Optional.ofNullable(updatedJohn));
		Mockito.when(careTakerService.updateCareTakerById(any(Long.class), any(CareTaker.class)))
				.thenReturn(java.util.Optional.ofNullable(updatedJohn));

		mockMvc.perform(put("/careTakers/4")
				.content(updatedJohnString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", endsWith("Cina")));
	}

	@Test
	public void testUpdateCareTakerByNonExistingIdAndValidCareTaker()
			throws Exception {
		// Sad path
		CareTaker updatedJohn = new CareTaker(7L, "John Cina");

		ObjectMapper objectMapper = new ObjectMapper();
		String updatedJohnString = objectMapper.writeValueAsString(updatedJohn);

		Mockito.when(careTakerService.getCareTakerById(any(Long.class)))
				.thenReturn(Optional.empty());
		Mockito.when(careTakerService.updateCareTakerById(any(Long.class),
				any(CareTaker.class)))
				.thenReturn(Optional.empty());

		mockMvc.perform(put("/careTakers/9")
				.content(updatedJohnString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	/*
	Testing updating an existing careTaker id with an invalid careTaker is not
	necessary because it uses addCareTaker validation
	 */

	@Test
	public void testDeleteCareTakerByExistingId() throws Exception {
		// Happy path
		Mockito.when(careTakerService.deleteCareTakerById(3L))
				.thenReturn(true);

		MvcResult mvcResult = mockMvc.perform(delete("/careTakers/3"))
				.andExpect(status().isOk())
				.andReturn();

		Assert.assertTrue(mvcResult.getResponse()
				.getContentAsString().toLowerCase()
				.contains("success"));
	}

	@Test
	public void testDeleteCareTakerByNonExistingId() throws Exception {
		// Happy path
		Mockito.when(careTakerService.deleteCareTakerById(3L))
				.thenReturn(false);

		MvcResult mvcResult = mockMvc.perform(delete("/careTakers/3"))
				.andExpect(status().isNotFound())
				.andReturn();

		Assert.assertTrue(mvcResult.getResponse()
				.getContentAsString().toLowerCase()
				.contains("fail"));
	}

	@Test
	public void testAddAnimalToCareByExistingIdAndValidAnimal() throws Exception {
		// Happy path
		Animal monkey = new Animal(1L, "Monkey", "Smartest animal");

		ObjectMapper objectMapper = new ObjectMapper();
		String monkeyString = objectMapper.writeValueAsString(monkey);

		Mockito.when(careTakerService.getCareTakerById(any(Long.class)))
				.thenReturn(Optional.ofNullable(careTakers.get(1)));
		Mockito.when(animalService.addAnimal(any(Animal.class)))
				.thenReturn(Optional.ofNullable(monkey));
		Mockito.when(careTakerService.addNewAnimalToExistingCareTaker(any(Long.class),
				any(Animal.class))).thenReturn(Optional.ofNullable(monkey));

		mockMvc.perform(post("/careTakers/2/animals")
				.contentType(MediaType.APPLICATION_JSON)
				.content(monkeyString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Monkey")));
	}
}
