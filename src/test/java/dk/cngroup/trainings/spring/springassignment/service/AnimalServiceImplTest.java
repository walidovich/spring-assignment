package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import liquibase.util.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AnimalServiceImplTest {

	private AnimalService animalService;
	private AnimalRepository animalRepositoryMock;
	private CareTakerRepository careTakerRepository;
	private List<Animal> animals;
	private List<Animal> lions;

	@Before
	public void setUp() {

		animalRepositoryMock = Mockito.mock(AnimalRepository.class);
		careTakerRepository = Mockito.mock(CareTakerRepository.class);
		animalService = new AnimalServiceImpl(animalRepositoryMock, careTakerRepository);

		Animal lion, shark, eagle, mountainLion;
		lion = new Animal(1L, "Lion", "King of the jungle");
		shark = new Animal(2L, "Shark", "Predator of the sea");
		eagle = new Animal(3L, "Eagle", "F22 of the skies");
		mountainLion = new Animal(4L, "Lion", "The mountain lion");

		animals = new LinkedList<>(Arrays.asList(lion, shark, eagle, mountainLion));
		lions = Arrays.asList(lion, mountainLion);
	}

	@Test
	public void testGetAnimals() {
		when(animalRepositoryMock.findAll()).thenReturn(animals);

		List<Animal> actualList = animalService.getAnimals();

		Assert.assertThat(actualList, Matchers.hasSize(4));
		Assert.assertEquals(actualList.get(2).getName(), "Eagle");
	}

	@Test
	public void testGetAnimalByExistingId() throws AnimalNotFoundException {
		// Happy path:
		when(animalRepositoryMock.existsById(3L)).thenReturn(true);
		when(animalRepositoryMock.findById(3L))
				.thenReturn(Optional.ofNullable(animals.get(2)));

		Animal animal = animalService.getAnimalById(3L);

		Assert.assertNotNull(animal);
		Assert.assertEquals("Eagle", animal.getName());
		Assert.assertThat(animal.getId(),
				Matchers.equalTo(3L));
	}

	@Test(expected = AnimalNotFoundException.class)
	public void testGetAnimalByNonExistingId() throws AnimalNotFoundException {
		// Sad path:
		when(animalRepositoryMock.findById(7L))
				.thenReturn(Optional.empty());

		Animal animal2 = animalService.getAnimalById(7L);

		Assert.assertNull(animal2);
	}

	@Test
	public void testGetAnimalsByExistingName() {
		// Happy path:
		when(animalRepositoryMock.findAllByName("Lion")).thenReturn(lions);

		List<Animal> animals =
				animalService.getAnimalsByName("Lion");

		Assert.assertThat(animals, Matchers.hasSize(2));
	}

	@Test
	public void testGetAnimalsByNonExistingName() {
		// Sad path: searching non existing animals name
		when(animalRepositoryMock.findAllByName("Dauphin"))
				.thenReturn(Arrays.asList());

		List<Animal> animals2 =
				animalService.getAnimalsByName("Dauphin");

		Assert.assertTrue(animals2.isEmpty());
	}

	@Test
	public void testAddValidAnimal() throws InvalidAnimalException {
		// Happy path:
		Animal dauphin = new Animal(5L, "Dauphin", "Smartest aqua mammal");

		when(animalRepositoryMock.save(dauphin)).thenReturn(dauphin);

		Assert.assertNotNull(animalService.addAnimal(dauphin));
		Assert.assertEquals("Dauphin",
				animalService.addAnimal(dauphin).getName());
	}

	@Test(expected = InvalidAnimalException.class)
	public void testAddAnimalWithShortName() throws InvalidAnimalException {
		// Sad path: Adding an animal with short name, less than 2 characters
		Animal pigeon = new Animal(6L, "p", "Smartest aqua mammal");

		animalService.addAnimal(pigeon);
	}

	@Test(expected = InvalidAnimalException.class)
	public void testAddAnimalWithLongDescription() throws InvalidAnimalException {
		// Sad path: (Adding an animal with long description, more than 10000)
		Animal pigeon = new Animal(6L, "p",
				StringUtils.repeat("*", 10001));

		animalService.addAnimal(pigeon);
	}

	@Test(expected = InvalidAnimalException.class)
	public void testAddAnimalWithDescriptionContainingPenguin() throws InvalidAnimalException {
		// Sad path: Adding an animal with description containing Penguin
		Animal penguin = new Animal(8L, "Penguin",
				"Royal Penguins of the north pole");

		animalService.addAnimal(penguin);
	}

	@Test
	public void deleteAnimalByExistingId() throws AnimalNotFoundException {
		//Happy path:
		long id = 2L;
		when(animalRepositoryMock.existsById(id)).thenReturn(true);
		when(animalRepositoryMock.findById(id))
				.thenReturn(Optional.ofNullable(animals.get(1)));

		animalService.deleteAnimalById(id);
	}

	@Test(expected = AnimalNotFoundException.class)
	public void deleteAnimalByNonExistingId() throws AnimalNotFoundException {
		//Sad path:
		long id = 10L; // Non existing id
		when(animalRepositoryMock.existsById(id)).thenReturn(false);

		animalService.deleteAnimalById(id);
	}

	@Test
	public void testUpdateAnimalByExistingId() throws InvalidAnimalException, AnimalNotFoundException {
		// Happy path:
		long id = 3L;
		Animal oldAnimal = animals.get(2);
		Animal updatedAnimal = new Animal(100L, "Falcon",
				"Fastest animal on earth");

		when(animalRepositoryMock.existsById(id)).thenReturn(true);
		when(animalRepositoryMock.findById(id))
				.thenReturn(Optional.ofNullable(oldAnimal));
		updatedAnimal.setId(oldAnimal.getId());
		when(animalRepositoryMock.save(updatedAnimal))
				.thenReturn(updatedAnimal);

		updatedAnimal.setId(100L);
		Animal animal = animalService.updateAnimalById(id, updatedAnimal);

		Assert.assertNotNull(animal);
		Assert.assertEquals("Falcon", animal.getName());
	}

	@Test(expected = AnimalNotFoundException.class)
	public void testUpdateAnimalByNonExistingId() throws InvalidAnimalException, AnimalNotFoundException {
		// Sad path: Updating an animal with non existing id
		long id = 10L; // non existing
		Animal updatedAnimal = new Animal(100L, "Falcon",
				"Fastest animal on earth");
		when(animalRepositoryMock.existsById(id)).thenReturn(false);
		when(animalRepositoryMock.findById(id))
				.thenReturn(Optional.empty());

		Animal animal2 = animalService.updateAnimalById(id, updatedAnimal);

		Assert.assertNull(animal2);
	}

	/*
   Testing updating an animal by existing id and an invalid animal is not needed because
	it uses addAnimal method in which adding invalid animal is already tested
	*/

	@Test
	public void testRemoveCareTakerFromCareTakersListWithLinkedExistingCareTakerIdAndAnimalId()
			throws CareTakerNotFoundException, CareTakerNotInTheAnimalCareTakersListException,
			AnimalNotFoundException, CareTakersListEmptyException {
		// Happy path:
		CareTaker walid = new CareTaker(1L, "Walid");
		CareTaker adam = new CareTaker(2L, "Adam");
		CareTaker martin = new CareTaker(3L, "Martin");
		animals.get(1).setCareTakers(new LinkedList<>(Arrays.asList(walid, adam, martin)));
		walid.setAnimals(animals);
		adam.setAnimals(animals);
		martin.setAnimals(animals);

		when(animalRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(animals.get(1)));
		when(careTakerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(martin));

		List<CareTaker> subCareTakersList =
				animalService.removeCareTakerFromCareTakersList(2L, 3L);

		Assert.assertThat(subCareTakersList, Matchers.hasSize(2));
	}

	@Test(expected = CareTakerNotInTheAnimalCareTakersListException.class)
	public void testRemoveCareTakerFromCareTakersListWithNonLinkedExistingCareTakerIdAndAnimalId()
			throws CareTakerNotFoundException, CareTakerNotInTheAnimalCareTakersListException,
			AnimalNotFoundException, CareTakersListEmptyException {
		// Happy path:
		CareTaker walid = new CareTaker(1L, "Walid");
		CareTaker adam = new CareTaker(2L, "Adam");
		CareTaker martin = new CareTaker(3L, "Martin");
		animals.get(1).setCareTakers(new LinkedList<>(Arrays.asList(walid, martin)));
		walid.setAnimals(new LinkedList<>(Arrays.asList(animals.get(1))));
		martin.setAnimals(new LinkedList<>(Arrays.asList(animals.get(1))));

		Animal dolphin = new Animal(8L, "Dolphin", "Smartest aqua mammal");
		dolphin.setCareTakers(new LinkedList<>(Arrays.asList(adam)));
		adam.setAnimals(new LinkedList<>(Arrays.asList(dolphin)));

		when(animalRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(animals.get(1)));
		when(careTakerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(martin));

		animalService.removeCareTakerFromCareTakersList(2L, 2L);
	}
}