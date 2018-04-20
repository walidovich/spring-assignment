package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.AnimalNotFoundException;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnimalWebControllerTest {

	private final String URL_PATH = "/web/animal";
	private final String viewPackage = "views/animal/";
	private List<Animal> animals;
	private MockMvc mockMvc;
	@MockBean
	private AnimalService animalService;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		Animal lion = new Animal(1L, "Lion", "King of Africa");
		Animal tiger = new Animal(2L, "Tiger", "The Bengali predator");
		Animal elephant = new Animal(3L, "Elephant", "Biggest terrestrial mammal");
		Animal dauphin = new Animal(4L, "Dauphin", "Smartest aqua mammal");
		Animal mountainLion = new Animal(5L, "Lion", "Mountain Lion, King of Atlas");
		animals = Arrays.asList(lion, tiger, elephant, dauphin, mountainLion);
	}

	@Test
	public void getAnimalsTest() throws Exception {
		// Happy URL_PATH
		when(animalService.getAnimals()).thenReturn(animals);

		mockMvc.perform(get(URL_PATH + "/list"))
				.andExpect(status().isOk())
				.andExpect(view().name(viewPackage + "animal_list"))
				.andExpect(model().attribute("animals", hasSize(5)));

		verify(animalService, times(1)).getAnimals();
		verifyNoMoreInteractions(animalService);
	}

	@Test
	public void getAnimalByExistingIdTest() throws Exception {
		// Happy URL_PATH
		when(animalService.getAnimalById(3L)).thenReturn(animals.get(2));

		mockMvc.perform(get(URL_PATH + "/list/3"))
				.andExpect(status().isOk())
				.andExpect(view().name(viewPackage + "animal_details"))
				.andExpect(model().attribute("animal", is(animals.get(2))));

		verify(animalService, times(1)).getAnimalById(3L);
		verifyNoMoreInteractions(animalService);
	}

	@Test
	public void getAnimalByNonExistingIdTest() throws Exception {
		// Sad URL_PATH
		when(animalService.getAnimalById(6L)).thenThrow(AnimalNotFoundException.class);

		mockMvc.perform(get(URL_PATH + "/list/6"))
				.andExpect(status().isNotFound());

		verify(animalService, times(1)).getAnimalById(6L);
		verifyNoMoreInteractions(animalService);
	}

	@Test
	public void addAnimalTest() throws Exception {
		// Happy URL_PATH
		mockMvc.perform(get(URL_PATH + "/add"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("animal", hasProperty("id", nullValue())))
				.andExpect(view().name(viewPackage + "animal_form"));

		verifyZeroInteractions(animalService);
	}

	// There is no Sad URL_PATH for requesting the "/add"

	@Test
	public void saveNewAnimalTest() throws Exception {
		// Happy URL_PATH
		Animal animal = new Animal(7L, "Leopard", "Lone cat");

		when(animalService.addAnimal(any(Animal.class))).thenReturn(animal);

		mockMvc.perform(post(URL_PATH + "/save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", animal.getName())
				.param("description", animal.getDescription())
				.sessionAttr("animal", new Animal()))
				.andExpect(redirectedUrl(URL_PATH + "/list"))
				.andExpect(view().name("redirect:" + URL_PATH + "/list"));

		verify(animalService, times(1)).addAnimal(any(Animal.class)); // I HATE TESTING hhhh
		verifyNoMoreInteractions(animalService);
	}
}
