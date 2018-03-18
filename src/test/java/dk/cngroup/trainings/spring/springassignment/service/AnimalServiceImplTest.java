package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnimalServiceImplTest {

    private AnimalService animalService;
    private AnimalRepository animalRepositoryMock;
    private List<Animal> animals;
    private List<Animal> lions;

    @Before
    public void setUp(){

        animalRepositoryMock = Mockito.mock(AnimalRepository.class);
        animalService=new AnimalServiceImpl(animalRepositoryMock);

        Animal lion, shark, eagle, mountainLion;
        lion=new Animal(1L,"Lion","King of the jungle");
        shark=new Animal(2L,"Shark","Predator of the sea");
        eagle=new Animal(3L,"Eagle","F22 of the skies");
        mountainLion=new Animal(4L,"Lion","The mountain lion");

        animals= Arrays.asList(lion,shark,eagle,mountainLion);
        lions= Arrays.asList(lion,mountainLion);

        animalRepositoryMock.save(lion);
        animalRepositoryMock.save(shark);
        animalRepositoryMock.save(eagle);
        animalRepositoryMock.save(mountainLion);
    }

    @Test
    public void testGetAnimals(){

        // Arrange
        Mockito.when(animalRepositoryMock.findAll()).thenReturn(animals);

        // Act
        List<Animal> allAnimals= animalService.getAnimals();

        // Assert
        Assert.assertThat(allAnimals, Matchers.hasSize(4));
        Assert.assertEquals(allAnimals.get(2).getName(),"Eagle");
    }

    @Test
    public void testGetAnimalById(){
        // Arrange
        Mockito.when(animalRepositoryMock.findById(3L))
                .thenReturn(Optional.ofNullable(animals.get(2)));

        // Act
        ResponseEntity<Animal> responseEntity=animalService.getAnimalById(3L);

        // Assert
        System.out.println(responseEntity);
        Assert.assertTrue(responseEntity.hasBody());
        Assert.assertEquals(responseEntity.getBody().getName(), "Eagle");
        Assert.assertThat(responseEntity.getBody().getId(), Matchers.equalTo(3L));
    }

    @Test
    public void testGetAnimalsByName(){

        // Arrange
        Mockito.when(animalRepositoryMock.findAllByName("Lion")).thenReturn(lions);

        // Act
        List<Animal> allLions= animalService.getAnimalsByName("Lion");

        // Assert
        Assert.assertThat(allLions, Matchers.hasSize(2));
    }

    @Test
    public void testAddAnimal(){

        Animal animal= new Animal(5L,"Dauphin","Smartest aqua mammal");

        // Arrange
        Mockito.when(animalRepositoryMock.save(animal)).thenReturn(animal);

        // Act
        Animal dauphin = animalService.addAnimal(animal);

        // Assert
        Assert.assertTrue(dauphin!=null);
        Assert.assertEquals(dauphin.getName(), "Dauphin");
    }

    @Test
    public void deleteAnimal(){

        // Act
        ResponseEntity<Animal> responseEntity= animalService.deleteAnimalById(2L);

        // Assert
        Mockito.verify(animalRepositoryMock,Mockito.times(1)).deleteById(2L);
        Assert.assertEquals(responseEntity.getStatusCode(),ResponseEntity.ok().build());
    }
}
