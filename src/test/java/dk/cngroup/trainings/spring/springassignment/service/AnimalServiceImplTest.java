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
    }

    @Test
    public void testGetAnimals() {
        Mockito.when(animalRepositoryMock.findAll()).thenReturn(animals);

        List<Animal> actualList = animalService.getAnimals();

        Assert.assertThat(actualList, Matchers.hasSize(4));
        Assert.assertEquals(actualList.get(2).getName(), "Eagle");
    }

    @Test
    public void testGetAnimalByExistingId() {
        // Happy path:
        Mockito.when(animalRepositoryMock.existsById(3L)).thenReturn(true);
        Mockito.when(animalRepositoryMock.findById(3L))
                .thenReturn(Optional.ofNullable(animals.get(2)));

        Optional<Animal> animal = animalService.getAnimalById(3L);

        Assert.assertTrue(animal.isPresent());
        Assert.assertEquals("Eagle", animal.get().getName());
        Assert.assertThat(animal.get().getId(),
                Matchers.equalTo(3L));
    }

    @Test
    public void testGetAnimalByNonExistingId() {
        // Sad path:
        Mockito.when(animalRepositoryMock.existsById(7L)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(7L))
                .thenReturn(null);

        Optional<Animal> animal2 = animalService.getAnimalById(7L);

        Assert.assertNull(animal2);
    }

    @Test
    public void testGetAnimalsByExistingName() {
        // Happy path:
        Mockito.when(animalRepositoryMock.findAllByName("Lion")).thenReturn(lions);

        List<Animal> animals =
                animalService.getAnimalsByName("Lion");

        Assert.assertThat(animals, Matchers.hasSize(2));
    }

    @Test
    public void testGetAnimalsByNonExistingName() {
        // Sad path: searching non existing animals name
        Mockito.when(animalRepositoryMock.findAllByName("Dauphin"))
                .thenReturn(Arrays.asList());

        List<Animal> animals2 =
                animalService.getAnimalsByName("Dauphin");

        Assert.assertTrue(animals2.isEmpty());
    }

    @Test
    public void testAddValidAnimal() {
        // Happy path:
        Animal dauphin = new Animal(5L, "Dauphin", "Smartest aqua mammal");

        Mockito.when(animalRepositoryMock.save(dauphin)).thenReturn(dauphin);

        Assert.assertTrue(animalService.addAnimal(dauphin).isPresent());
        Assert.assertEquals("Dauphin",
                animalService.addAnimal(dauphin).get().getName());
    }

    @Test
    public void testAddAnimalWithShortName() {
        // Sad path: Adding an animal with short name, less than 2 characters
        Animal pigeon = new Animal(6L, "p", "Smartest aqua mammal");

        Mockito.when(animalRepositoryMock.save(pigeon)).thenReturn(null);

        Assert.assertFalse(animalService.addAnimal(pigeon).isPresent());
    }

    @Test
    public void testAddAnimalWithLongDescription() {
        // Sad path: (Adding an animal with long description, more than 10000)
        Animal pigeon = new Animal(6L, "p",
                StringUtils.repeat("*", 10001));

        Mockito.when(animalRepositoryMock.save(pigeon))
                .thenReturn(null);
        Assert.assertFalse(animalService.addAnimal(pigeon).isPresent());
    }

    @Test
    public void testAddAnimalWithDescriptionContainingPenguin() {
        // Sad path: Adding an animal with description containing Penguin
        Animal penguin = new Animal(8L, "Penguin",
                "Royal Penguins of the north pole");

        Mockito.when(animalRepositoryMock.save(penguin)).thenReturn(null);

        Assert.assertFalse(animalService.addAnimal(penguin).isPresent());
    }

    @Test
    public void deleteAnimalByExistingId() {
        //Happy path:
        long id = 2L;
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(true);

        Assert.assertTrue(animalService.deleteAnimalById(id));
    }

    @Test
    public void deleteAnimalByNonExistingId() {
        //Sad path:
        long id = 10L; // Non existing id
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);

        Assert.assertFalse(animalService.deleteAnimalById(id));
    }

    @Test
    public void testUpdateAnimalByExistingId() {
        // Happy path:
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

        updatedAnimal.setId(100L);
        Optional<Animal> animal =
                animalService.updateAnimalById(id, updatedAnimal);

        Assert.assertNotNull(animal);
        Assert.assertEquals("Falcon", animal.get().getName());
    }

    @Test
    public void testUpdateAnimalByNonExistingId() {
        // Sad path: Updating an animal with non existing id
        long id = 10L; // non existing
        Animal updatedAnimal = new Animal(100L, "Falcon",
                "Fastest animal on earth");
        Mockito.when(animalRepositoryMock.existsById(id)).thenReturn(false);
        Mockito.when(animalRepositoryMock.findById(id))
                .thenReturn(Optional.empty());

        Optional<Animal> animal2 =
                animalService.updateAnimalById(id, updatedAnimal);

        Assert.assertFalse(animal2.isPresent());
    }

    /*
    Testing updating an animal by existing id and an invalid animal is not needed because
     it uses addAnimal method in which adding invalid animal is already tested
     */
}