package dk.cngroup.trainings.spring.springassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import liquibase.util.StringUtils;
import org.hamcrest.Matchers;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AnimalControllerTest {

	MockMvc mockMvc;
	List<Animal> animals;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@MockBean
	private AnimalService animalService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
				build();

		Animal lion = new Animal(1L, "Lion", "King of Africa");
		Animal tiger = new Animal(2L, "Tiger", "The Bengali predator");
		Animal elephant = new Animal(3L, "Elephant", "Biggest terrestrial mammal");
		Animal dauphin = new Animal(4L, "Dauphin", "Smartest aqua mammal");
		Animal mountainLion = new Animal(5L, "Lion", "Mountain Lion, King of Atlas");
		animals = Arrays.asList(lion, tiger, elephant, dauphin, mountainLion);
	}

	@Test
	public void testAddValidAnimal() throws Exception {
		// Happy path:
		Animal shark = new Animal(22L, "Shark", "Predator of the sea");
		ObjectMapper objectMapper = new ObjectMapper();
		String sharkString = objectMapper.writeValueAsString(shark);

		// Adding a valid animal should return the animal itself
		Mockito.when(animalService.addAnimal(any(Animal.class)))
				.thenReturn(java.util.Optional.ofNullable(shark));

		mockMvc.perform(post("/animals")
				.content(sharkString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Shark")));
	}

	@Test
	public void testAddAnimalWithShortName() throws Exception {
		Animal shark = new Animal(22L, "S", "Predator of the sea");
		ObjectMapper objectMapper = new ObjectMapper();
		String sharkString = objectMapper.writeValueAsString(shark);

		mockMvc.perform(post("/animals")
				.content(sharkString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddAnimalWithLongDescription() throws Exception {
		// Sad path: Long description
		Animal shark = new Animal(22L, "Shark", StringUtils.repeat("*", 10001));
		ObjectMapper objectMapper = new ObjectMapper();
		String sharkString = objectMapper.writeValueAsString(shark);

		mockMvc.perform(post("/animals")
				.content(sharkString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddAnimalWithDescriptionContainingPenguin() throws Exception {
		// Sad path: description containing penguin
		Animal penguin = new Animal(7L, "Penguin", "Royal penguin of the north pole");
		ObjectMapper objectMapper = new ObjectMapper();
		String penguinString = objectMapper.writeValueAsString(penguin);

		Mockito.when(animalService.addAnimal(any(Animal.class))).thenThrow(InvalidAnimalException.class);

		mockMvc.perform(post("/animals")
				.content(penguinString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetAnimals() throws Exception {
		// Getting all animals should return list of animals empty or not
		Mockito.when(animalService.getAnimals())
				.thenReturn(animals);
		mockMvc.perform(get("/animals"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[2].name", is("Elephant")));
	}

	@Test
	public void testDeleteAnimalByExistingId() throws Exception {
		// Happy path
		// Deleting an existing animal id should return true
		Mockito.when(animalService.deleteAnimalById(3L))
				.thenReturn(true);

		MvcResult mvcResult =
				mockMvc.perform(delete("/animals/3")
						.content("")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andReturn();

		Assert.assertTrue(mvcResult.getResponse()
				.getContentAsString().toLowerCase()
				.contains("success"));
	}

	@Test
	public void testDeleteAnimalByNonExistingId() throws Exception {
		// Sad path
		// Deleting a non existing animal id should return false
		Mockito.when(animalService.deleteAnimalById(19L))
				.thenReturn(false);

		MvcResult mvcResult =
				mockMvc.perform(delete("/animals/19")
						.content("")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound())
						.andReturn();

		Assert.assertTrue(mvcResult.getResponse()
				.getContentAsString().toLowerCase()
				.contains("fail"));
	}

	@Test
	public void testUpdateAnimalByExistingIdAndValidAnimal() throws Exception {
		// Happy path
		Animal updatedLion = new Animal(8L, "Atlas Lion",
				"Morocco's king of the mountains");
		updatedLion.setId(animals.get(4).getId());

		Mockito.when(animalService.getAnimalById(any(Long.class)))
				.thenReturn(Optional.ofNullable(animals.get(4)));
		Mockito.when(animalService.addAnimal(any(Animal.class)))
				.thenReturn(java.util.Optional.ofNullable(updatedLion));
		Mockito.when(animalService.updateAnimalById(any(Long.class),
				any(Animal.class))).thenReturn(java.util.Optional.ofNullable(updatedLion));

		ObjectMapper objectMapper = new ObjectMapper();
		String updatedLionString = objectMapper.writeValueAsString(updatedLion);

		mockMvc.perform(put("/animals/5")
				.content(updatedLionString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(updatedLion.getId().intValue())))
				.andExpect(jsonPath("$.name", is(updatedLion.getName())));
	}

	@Test
	public void testUpdateAnimalByNonExistingIdAndValidAnimal() throws Exception {
		// Sad path: update an animal by non existing id should return 404.
		Animal updatedLion = new Animal(8L, "Atlas Lion",
				"Morocco's king of the mountains");
		updatedLion.setId(animals.get(4).getId());

		Mockito.when(animalService.getAnimalById(any(Long.class)))
				.thenReturn(Optional.empty());
		Mockito.when(animalService.updateAnimalById(any(Long.class),
				any(Animal.class))).thenReturn(Optional.empty());

		ObjectMapper objectMapper = new ObjectMapper();
		String updatedLionString = objectMapper.writeValueAsString(updatedLion);

		mockMvc.perform(put("/animals/8")
				.content(updatedLionString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

    /*
    Testing updating an animal by existing id and an invalid animal is not needed because
     it uses addAnimal method in which adding invalid animal is already tested
     */

	@Test
	public void testGetAnimalByExistingId() throws Exception {
		// Happy path:
		// Getting an existing animal by id should return that animal
		Mockito.when(animalService.getAnimalById(any(Long.class)))
				.thenReturn(java.util.Optional.ofNullable(animals.get(3)));

		Animal searchedAnimal = animals.get(3);
		ObjectMapper objectMapper = new ObjectMapper();
		String searchedAnimalString = objectMapper.writeValueAsString(searchedAnimal);

		mockMvc.perform(get("/animals/3")
				.content(searchedAnimalString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(animals.get(3).getId().intValue())))
				.andExpect(jsonPath("$.name", is(animals.get(3).getName())));
	}


	@Test
	public void testGetAnimalByNonExistingId() throws Exception {
		// Sad path:
		// Getting an animal by a non existing id should return Optional.empty()

		Mockito.when(animalService.getAnimalById(any(Long.class)))
				.thenReturn(Optional.empty());

		mockMvc.perform(get("/animals/22")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetAnimalsByExistingName() throws Exception {
		// Happy path:
		List<Animal> searchedAnimals = Arrays.asList(animals.get(0), animals.get(4));
		// Getting animals by an existing name should return a list for existing name
		Mockito.when(animalService.getAnimalsByName("Lion"))
				.thenReturn(searchedAnimals);

		mockMvc.perform(get("/animals/name?name=Lion"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].description",
						Matchers.startsWith("Mountain")));
	}

	@Test
	public void testGetAnimalsByNonExistingName() throws Exception {
		// Sad path:
		// Getting animals by name should return 404
		Mockito.when(animalService.getAnimalsByName("Dog"))
				.thenReturn(Arrays.asList());

		mockMvc.perform(get("/animals/search/Dog"))
				.andExpect(status().isNotFound());
	}
}
