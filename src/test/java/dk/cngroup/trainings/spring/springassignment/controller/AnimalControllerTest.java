package dk.cngroup.trainings.spring.springassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AnimalControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;
    List<Animal> animals;

    @MockBean
    private AnimalService animalService;

    @Before
    public void setUp(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).
                build();

        Animal lion= new Animal(1L,"Lion","King of Africa");
        Animal tiger=new Animal(2L,"Tiger","The Bengali predator");
        Animal elephant=new Animal(3L,"Elephant","Biggest terrestrial mammal");
        Animal dauphin=new Animal(4L,"Dauphin","Smartest aqua mammal");
        Animal mountainLion=new Animal(5L,"Lion","Mountain Lion, King of Atlas");
        animals= Arrays.asList(lion,tiger,elephant,dauphin,mountainLion);

        // objects helpers


        // Getting animals by non extsing name should return null;
        Mockito.when(animalService.getAnimalsByName("Penguin"))
                .thenReturn(null);
        // Deleting an animal by an existing id should return true
        Mockito.when(animalService.deleteAnimalById(dauphin.getId()))
                .thenReturn(true);
        // Deleting an animal by a non existing id should return false
        Mockito.when(animalService.deleteAnimalById(10L))
                .thenReturn(false);
    }

    @Test
    public void testAddAnimal() throws Exception {
        // Happy path
        Animal shark= new Animal(22L,"Shark", "Predator of the sea");
        ObjectMapper objectMapper = new ObjectMapper();
        String sharkString= objectMapper.writeValueAsString(shark);

        // Mock the isValid method
        Mockito.when(animalService.isValid(any(Animal.class))).thenReturn(true);
        // Adding a valid animal should return the animal itself
        Mockito.when(animalService.addAnimal(any(Animal.class)))
                .thenReturn(java.util.Optional.ofNullable(shark));

        mockMvc.perform(post("/animals/")
                .content(sharkString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Shark")));

        // Sad path: short name
        shark.setName("S");
        sharkString= objectMapper.writeValueAsString(shark);

        // Mock the isValid method to return false
        Mockito.when(animalService.isValid(any(Animal.class))).thenReturn(false);
        // Mocking addAnimal to return null
        Mockito.when(animalService.addAnimal(any(Animal.class)))
                .thenReturn(null);

        mockMvc.perform(post("/animals/")
                .content(sharkString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Sad path: Long description
        shark.setName("Shark");
        shark.setDescription(new String(StringUtils.repeat("*",10001)));
        sharkString= objectMapper.writeValueAsString(shark);

        mockMvc.perform(post("/animals/")
                .content(sharkString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Sad path: description containing penguin
        Animal penguin= new Animal(7L,"Penguin","Royal penguins of the north pole");
        String penguinString= objectMapper.writeValueAsString(penguin);

        mockMvc.perform(post("/animals/")
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
    public void testDeleteAnimalById() throws Exception{
        // Happy path
        // Deleting an existing animal id should return true
        Mockito.when(animalService.deleteAnimalById(3L))
                .thenReturn(true);

        MvcResult mvcResult=
                mockMvc.perform(delete("/animals/3")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertTrue(mvcResult.getResponse()
                .getContentAsString().toLowerCase()
                .contains("success"));

        // Sad path
        // Deleting a non existing animal id should return false
        Mockito.when(animalService.deleteAnimalById(19L))
                .thenReturn(false);

        mvcResult=
                mockMvc.perform(delete("/animals/19")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        Assert.assertTrue(mvcResult.getResponse()
                .getContentAsString().toLowerCase()
                .contains("fail"));
    }

    @Test
    public void testUpdateAnimalById() throws Exception{
        // Happy path
        Animal updatedLion=new Animal(8L,"Atlas Lion",
                "Morocco's king of the mountains");
        updatedLion.setId(animals.get(4).getId());

        // Mock the isValid method
        Mockito.when(animalService.isValid(any(Animal.class))).thenReturn(true);
        // Saving the updated animal should return itself
        Mockito.when(animalService.addAnimal(any(Animal.class)))
                .thenReturn(java.util.Optional.ofNullable(updatedLion));
        // Updating an animal by providing an existing id should return the updated animal
        Mockito.when(animalService.updateAnimalById(any(Long.class),
                any(Animal.class))).thenReturn(java.util.Optional.ofNullable(updatedLion));

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedLionString= objectMapper.writeValueAsString(updatedLion);
        mockMvc.perform(put("/animals/5")
                .content(updatedLionString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedLion.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedLion.getName())));

        // Sad path to be implemented in case of id non existing.
        // Sad path for invalid animal with existing id doesn't need testing because it
        // uses AddAnimal for validation every time.
    }

    @Test
    public void testGetAnimalById() throws Exception{
        // Happy path:
        // Getting an existing animal by id should return that animal
        Mockito.when(animalService.getAnimalById(any(Long.class)))
                .thenReturn(java.util.Optional.ofNullable(animals.get(3)));

        Animal searchedAnimal= animals.get(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String searchedAnimalString= objectMapper.writeValueAsString(searchedAnimal);

        mockMvc.perform(get("/animals/3")
                .content(searchedAnimalString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(animals.get(3).getId().intValue())))
                .andExpect(jsonPath("$.name", is(animals.get(3).getName())));

        // Sad path:
        // Getting an animal by a non existing id should return null
        Mockito.when(animalService.getAnimalById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/animals/22")
                .content(searchedAnimalString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAnimalsByName() throws Exception{
        // Happy path:
        List<Animal> searchedAnimals= Arrays.asList(animals.get(0),animals.get(4));
        // Getting animals by an existing name should return a list for existing name
        Mockito.when(animalService.getAnimalsByName("Lion")) // any(String.class)
                .thenReturn(searchedAnimals);

        ObjectMapper objectMapper = new ObjectMapper();
        String searchedAnimalsString= objectMapper.writeValueAsString(searchedAnimals);

        mockMvc.perform(get("/animals/search/Lion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].description",
                        Matchers.startsWith("Mountain")));

        // Sad path:
        // Getting animals by name should return 404
        Mockito.when(animalService.getAnimalsByName("Dog")) // any(String.class)
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/animals/search/Dog"))
                .andExpect(status().isNotFound());
    }
}
