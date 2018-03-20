package dk.cngroup.trainings.spring.springassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Animal penguin= new Animal(7L,"Penguin","Royal penguins of the north pole");
        Animal updatedLion=new Animal(8L,"Atlas Lion","King of Africa");

        // Deleting an existing animal should return true
        Mockito.when(animalService.deleteAnimalById(3L))
                .thenReturn(true);
        // Adding an animal containing penguin should return null
        Mockito.when(animalService.addAnimal(penguin))
                .thenReturn(null);
        // Getting an existing animal by id should return that animal
        Mockito.when(animalService.getAnimalById(tiger.getId()))
                .thenReturn(java.util.Optional.ofNullable(tiger));
        // Getting animals by name should return a list for existing name
        Mockito.when(animalService.getAnimalsByName("Lion"))
                .thenReturn(Arrays.asList(lion,mountainLion));
        // Getting animals by non extsing name should return null;
        Mockito.when(animalService.getAnimalsByName("Penguin"))
                .thenReturn(null);
        // Updating an animal by providing an existing id should return the updated animal
        Mockito.when(animalService.updateAnimalById(mountainLion.getId(),
                updatedLion)).thenReturn(java.util.Optional.ofNullable(updatedLion));
        // Deleting an animal by an existing id should return true
        Mockito.when(animalService.deleteAnimalById(dauphin.getId()))
                .thenReturn(true);
        // Deleting an animal by a non existing id should return false
        Mockito.when(animalService.deleteAnimalById(10L))
                .thenReturn(false);
    }


    @Test
    public void testGetAnimals() throws Exception {
        // Getting all animals should return list of animals empty or not
        Mockito.when(animalService.getAnimals())
                .thenReturn(animals);
        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk()
                )
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[2].name", is("Elephant")));
    }

    @Test
    public void testAddAnimal() throws Exception {
        Animal shark= new Animal(22L,"Shark", "Predator of the sea");
        ObjectMapper objectMapper = new ObjectMapper();
        String sharkString= objectMapper.writeValueAsString(shark);

        Mockito.when(animalService.isValid(shark)).thenReturn(true);
        // Adding a valid animal should return the animal itself
        Mockito.when(animalService.addAnimal(shark))
                .thenReturn(java.util.Optional.ofNullable(shark));

        System.out.println(">>>>>>>>>>"+animalService.isValid(shark));

        mockMvc.perform(post("/animals")
        .content(sharkString)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is("Shark")));
    }

}
