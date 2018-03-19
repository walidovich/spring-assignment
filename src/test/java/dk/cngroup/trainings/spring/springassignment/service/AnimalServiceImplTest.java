package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import liquibase.util.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnimalServiceImplTest {

    private AnimalService animalService;
    private AnimalRepository animalRepositoryMock;
    private List<Animal> animals;
    private List<Animal> lions;

    @Before
    public void setUp() {

        animalRepositoryMock = Mockito.mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepositoryMock);

        Animal lion, shark, eagle, mountainLion;
        lion = new Animal(1L, "Lion", "King of the jungle");
        shark = new Animal(2L, "Shark", "Predator of the sea");
        eagle = new Animal(3L, "Eagle", "F22 of the skies");
        mountainLion = new Animal(4L, "Lion", "The mountain lion");

        animals = Arrays.asList(lion, shark, eagle, mountainLion);
        lions = Arrays.asList(lion, mountainLion);

        animalRepositoryMock.save(lion);
        animalRepositoryMock.save(shark);
        animalRepositoryMock.save(eagle);
        animalRepositoryMock.save(mountainLion);
    }

    @Test
    public void testGetAnimals() {

        // Arrange
        Mockito.when(animalRepositoryMock.findAll()).thenReturn(animals);

        // Act
        ResponseEntity<List<Animal>> responseEntityActual = animalService.getAnimals();

        // Assert
        Assert.assertThat(responseEntityActual.getBody(), Matchers.hasSize(4));
        Assert.assertEquals(responseEntityActual.getBody().get(2).getName(), "Eagle");
    }

    @Test
    public void testGetAnimalById() {
        // Happy path:
        // Arrange
        Mockito.when(animalRepositoryMock.existsById(3L)).thenReturn(true);
        Mockito.when(animalRepositoryMock.findById(3L))
                .thenReturn(Optional.ofNullable(animals.get(2)));

        // Act
        ResponseEntity<Animal> responseEntity1 = animalService.getAnimalById(3L);

        // Assert
        Assert.assertTrue(responseEntity1.hasBody());
        Assert.assertEquals("Eagle", responseEntity1.getBody().getName());
        Assert.assertThat(responseEntity1.getBody().getId(),
                Matchers.equalTo(3L));

        // Sad path:
        // Arrange
        Mockito.when(animalRepositoryMock.existsById(7L)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(7L))
                .thenReturn(null);

        // Act
        ResponseEntity<Animal> responseEntity2 = animalService.getAnimalById(7L);

        // Assert
        Assert.assertFalse(responseEntity2.hasBody());
        Assert.assertEquals(ResponseEntity.notFound().build().getStatusCode(),
                responseEntity2.getStatusCode());
    }

    @Test
    public void testGetAnimalsByName() {

        // Happy path:
        // Arrange
        Mockito.when(animalRepositoryMock.findAllByName("Lion")).thenReturn(lions);

        // Act
        ResponseEntity<List<Animal>> responseEntity1 =
                animalService.getAnimalsByName("Lion");

        // Assert
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode()
                , responseEntity1.getStatusCode());
        Assert.assertThat(responseEntity1.getBody(), Matchers.hasSize(2));

        // Sad path:
        // Arrange
        Mockito.when(animalRepositoryMock.findAllByName("Dauphin")).thenReturn(null);

        // Act
        ResponseEntity<List<Animal>> responseEntity2 =
                animalService.getAnimalsByName("Dauphin");

        // Assert
        Assert.assertFalse(responseEntity2.hasBody());
    }

    @Test
    public void testAddAnimal() {

        // Happy path:
        Animal dauphin = new Animal(5L, "Dauphin", "Smartest aqua mammal");

        // Arrange
        Mockito.when(animalRepositoryMock.save(dauphin)).thenReturn(dauphin);

        // Act
        ResponseEntity<Animal> responseEntity = animalService.addAnimal(dauphin);

        // Assert
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode()
                , responseEntity.getStatusCode());
        Assert.assertEquals("Dauphin", responseEntity.getBody().getName());

        // Sad path: Adding an animal with short name, less than 2 characters
        Animal pigeon = new Animal(6L, "p", "Smartest aqua mammal");

        // Arrange
        Mockito.when(animalRepositoryMock.save(pigeon)).thenReturn(null);

        // Act
        ResponseEntity<Animal> responseEntity2 = animalService.addAnimal(pigeon);

        // Assert
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode()
                , responseEntity2.getStatusCode());


        // Sad path: (Adding an animal with long description, more than 10000)
        // Arrange
        String longDescription = new String();
        longDescription += StringUtils.repeat("*", 10001);
        System.out.println(longDescription);

        pigeon = new Animal(7L, "Pigeon",
                longDescription);

        Mockito.when(animalRepositoryMock.save(pigeon))
                .thenReturn(null);

        // Act
        ResponseEntity<Animal> responseEntity3 =
                animalService.addAnimal(pigeon);

        // Assert
        Assert.assertFalse(responseEntity3.hasBody());
        Assert.assertEquals(ResponseEntity.badRequest().build().getStatusCode(),
                responseEntity3.getStatusCode());
    }

    @Test
    public void deleteAnimal() {

        long id = 2L;
        //Happy path:
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(true);

        // Act
        ResponseEntity<Animal> responseEntity1 = animalService.deleteAnimalById(2L);

        // Assert
        Mockito.verify(animalRepositoryMock, Mockito.times(1)).deleteById(2L);
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(),
                responseEntity1.getStatusCode());

        //Sad path:
        id = 10L;
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<Animal> responseEntity2 = animalService.deleteAnimalById(id);

        // Assert
        Mockito.verify(animalRepositoryMock, Mockito.times(1)).deleteById(2L);
        Assert.assertEquals(ResponseEntity.notFound().build().getStatusCode(),
                responseEntity2.getStatusCode());
    }

    @Test
    public void testUpdateAnimalById() {
        // Happy path:
        // Arrange
        long id = 3L;
        Animal oldAnimal = animals.get(2);
        Animal updatedAnimal = new Animal(100L, "Falcon",
                "Fastest animal on earth");
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(true);
        Mockito.when(animalRepositoryMock.findById(id))
                .thenReturn(Optional.ofNullable(oldAnimal));
        updatedAnimal.setId(oldAnimal.getId());
        Mockito.when(animalRepositoryMock.save(updatedAnimal))
                .thenReturn(updatedAnimal);

        // Act
        updatedAnimal.setId(100L);
        ResponseEntity<Animal> responseEntity1 =
                animalService.updateAnimalById(id, updatedAnimal);

        // Assert
        Assert.assertTrue(responseEntity1.hasBody());
        Assert.assertEquals(ResponseEntity.ok().build().getStatusCode(),
                responseEntity1.getStatusCode());
        Assert.assertEquals("Falcon", responseEntity1.getBody().getName());

        // Sad path: Updating an animal with non existing id
        // Arrange
        id = 10L; // non existing
        updatedAnimal = new Animal(100L, "Falcon",
                "Fastest animal on earth");
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(id))
                .thenReturn(null);

        // Act
        ResponseEntity<Animal> responseEntity2 =
                animalService.updateAnimalById(id, updatedAnimal);

        // Assert
        Assert.assertFalse(responseEntity2.hasBody());
        Assert.assertEquals(ResponseEntity.notFound().build().getStatusCode(),
                responseEntity2.getStatusCode());


    }
}