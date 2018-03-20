package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import liquibase.util.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
        List<Animal> actualList = animalService.getAnimals();

        // Assert
        Assert.assertThat(actualList, Matchers.hasSize(4));
        Assert.assertEquals(actualList.get(2).getName(), "Eagle");
    }

    @Test
    public void testGetAnimalById() {
        // Happy path:
        // Arrange
        Mockito.when(animalRepositoryMock.existsById(3L)).thenReturn(true);
        Mockito.when(animalRepositoryMock.findById(3L))
                .thenReturn(Optional.ofNullable(animals.get(2)));

        // Act
        Optional<Animal> animal = animalService.getAnimalById(3L);

        // Assert
        Assert.assertTrue(animal.isPresent());
        Assert.assertEquals("Eagle", animal.get().getName());
        Assert.assertThat(animal.get().getId(),
                Matchers.equalTo(3L));

        // Sad path:
        // Arrange
        Mockito.when(animalRepositoryMock.existsById(7L)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(7L))
                .thenReturn(null);

        // Act
        Optional<Animal> animal2 = animalService.getAnimalById(7L);

        // Assert
        Assert.assertNull(animal2);
    }

    @Test
    public void testGetAnimalsByName() {

        // Happy path:
        // Arrange
        Mockito.when(animalRepositoryMock.findAllByName("Lion")).thenReturn(lions);

        // Act
        List<Animal> animals =
                animalService.getAnimalsByName("Lion");

        // Assert
        Assert.assertThat(animals, Matchers.hasSize(2));

        // Sad path:
        // Arrange
        Mockito.when(animalRepositoryMock.findAllByName("Dauphin")).thenReturn(null);

        // Act
        List<Animal> animals2 =
                animalService.getAnimalsByName("Dauphin");

        // Assert
        Assert.assertNull(animals2);
    }

    @Test
    public void testAddAnimal() {

        // Happy path:
        Animal dauphin = new Animal(5L, "Dauphin", "Smartest aqua mammal");

        // Arrange
        Mockito.when(animalRepositoryMock.save(dauphin)).thenReturn(dauphin);

        // Act
        Optional<Animal> animal = animalService.addAnimal(dauphin);

        // Assert
        Assert.assertNotNull(animal);
        Assert.assertEquals("Dauphin", animal.get().getName());

        // Sad path: Adding an animal with short name, less than 2 characters
        Animal pigeon = new Animal(6L, "p", "Smartest aqua mammal");

        // Arrange
        Mockito.when(animalRepositoryMock.save(pigeon)).thenReturn(null);

        // Act
        Optional<Animal> animal2 = animalService.addAnimal(pigeon);

        // Assert
        Assert.assertNull(animal2);

        // Sad path: (Adding an animal with long description, more than 10000)
        // Arrange
        String longDescription = new String();
        longDescription += StringUtils.repeat("*", 10001);

        pigeon = new Animal(7L, "Pigeon",
                longDescription);

        Mockito.when(animalRepositoryMock.save(pigeon))
                .thenReturn(null);

        // Act
        Optional<Animal> animal3 =
                animalService.addAnimal(pigeon);

        // Assert
        Assert.assertNull(animal3);

        // Sad path: Adding an animal with description containing Penguin
        Animal penguin = new Animal(8L, "Penguin", "Royal Penguins of the north pole");

        // Arrange
        Mockito.when(animalRepositoryMock.save(penguin)).thenReturn(null);

        // Act
        Optional<Animal> animal4 = animalService.addAnimal(penguin);

        // Assert
        Assert.assertNull(animal4);
    }

    @Test
    public void deleteAnimal() {

        long id = 2L;
        //Happy path:
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(true);

        // Act
        boolean isDeleted = animalService.deleteAnimalById(2L);

        // Assert
        Assert.assertTrue(isDeleted);

        //Sad path:
        id = 10L;
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);

        // Act
        boolean isDeleted2 = animalService.deleteAnimalById(id);

        // Assert
        Assert.assertFalse(isDeleted2);
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
        Optional<Animal> animal =
                animalService.updateAnimalById(id, updatedAnimal);

        // Assert
        Assert.assertNotNull(animal);
        Assert.assertEquals("Falcon", animal.get().getName());

        // Sad path: Updating an animal with non existing id
        // Arrange
        id = 10L; // non existing
        updatedAnimal = new Animal(100L, "Falcon",
                "Fastest animal on earth");
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(id))
                .thenReturn(null);

        // Act
        Optional<Animal> animal2 =
                animalService.updateAnimalById(id, updatedAnimal);

        // Assert
        Assert.assertNull(animal2);
    }
}