package dk.cngroup.trainings.spring.springassignment.controller;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        /*
        // objects helpers
        Animal shark= new Animal(6L,"Shark", "Predator of the sea");
        Animal penguin= new Animal(7L,"Penguin","Royal penguins of the north pole");
        Animal updatedLion=lion;
        updatedLion.setName("Mountain Lion");
        */

        Mockito.when(animalService.getAnimals())
                .thenReturn(new ResponseEntity<>(animals, HttpStatus.OK));
        /*
        Mockito.when(animalService.addAnimal(shark))
                .thenReturn(new ResponseEntity<>(shark,HttpStatus.OK));
        Mockito.when(animalService.deleteAnimalById(3L))
                .thenReturn(new ResponseEntity<>(shark,HttpStatus.OK));
        Mockito.when(animalService.addAnimal(penguin))
                .thenReturn(ResponseEntity.badRequest().build());
        Mockito.when(animalService.getAnimalById(2L))
                .thenReturn(new ResponseEntity<>(tiger,HttpStatus.OK));
        Mockito.when(animalService.getAnimalsByName("Lion"))
                .thenReturn(new ResponseEntity<>(Arrays.asList(lion,mountainLion), HttpStatus.OK));
        Mockito.when(animalService.getAnimalsByName("Penguin"))
                .thenReturn(ResponseEntity.badRequest().build());
        Mockito.when(animalService.updateAnimalById(5L, updatedLion))
                .thenReturn(new ResponseEntity<>(updatedLion, HttpStatus.OK));
        Mockito.when(animalService.deleteAnimalById(4L))
                .thenReturn(ResponseEntity.ok().build());
        Mockito.when(animalService.deleteAnimalById(10L))
                .thenReturn(ResponseEntity.notFound().build());
                */
    }

    @Test
    public void testGetAnimals() throws Exception {
        System.out.println("\n>>>>>>>>>>>>>>>> My Diagnostic:"
                +animalService.getAnimals().getStatusCode());

        mockMvc.perform(get("/")) // Also return 404 for /animals
                .andExpect(status().isOk()
                )
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[2].name", is("Elephant")));
    }

    /*
    @Test
    public void testAddAnimal() throws Exception {
        Animal anotherShark= new Animal(11L,"Shark", "Predator of the sea");
        ObjectMapper objectMapper = new ObjectMapper();
        String sharkString= objectMapper.writeValueAsString(anotherShark);

        System.out.println(animalService.addAnimal(anotherShark));

        mockMvc.perform(post("/") // Also return 404 for /animals
        .content(sharkString)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is("Shark")));
    }
    */
}
