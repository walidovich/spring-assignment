package dk.cngroup.trainings.spring.springassignment.service;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.repository.AnimalRepository;
import dk.cngroup.trainings.spring.springassignment.repository.CareTakerRepository;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalServiceFieldsTrimmer;
import dk.cngroup.trainings.spring.springassignment.service.helper.AnimalValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.CareTakerValidationService;
import dk.cngroup.trainings.spring.springassignment.service.helper.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AnimalServiceImpl implements AnimalService {

	private final AnimalRepository animalRepository;
	private final CareTakerRepository careTakerRepository;

	public AnimalServiceImpl(AnimalRepository animalRepository, CareTakerRepository careTakerRepository) {
		this.animalRepository = animalRepository;
		this.careTakerRepository = careTakerRepository;
	}

	@Override
	public List<Animal> getAnimals() {
		return animalRepository.findAll();
	}

	@Override
	public Animal getAnimalById(long id) throws AnimalNotFoundException {
		Optional<Animal> animal = animalRepository.findById(id);
		if (animal.isPresent()) {
			return animal.get();
		} else {
			throw new AnimalNotFoundException(id);
		}
	}

	@Override
	public List<Animal> getAnimalsByName(String name) {
		return animalRepository.findAllByName(name);
	}

	@Override
	public Animal addAnimal(Animal animal) throws InvalidAnimalException {
		AnimalServiceFieldsTrimmer.trimFields(animal);
		AnimalValidationService.validate(animal);
		animal.setId(IdGenerator.getId());
		return animalRepository.save(animal);
	}

	@Override
	public void deleteAnimalById(long id) throws AnimalNotFoundException {
		this.checkAnimalExistsById(id);
		Animal theAnimal = this.getAnimalById(id);
		if (theAnimal.getCareTakers() != null) {
			for (CareTaker careTaker : this.getAnimalById(id).getCareTakers()) {
				careTaker.getAnimals().remove(this.getAnimalById(id));
				careTakerRepository.save(careTaker);
			}
		}
		animalRepository.deleteById(id);
	}

	@Override
	public void checkAnimalExistsById(long id) throws AnimalNotFoundException {
		if (!animalRepository.existsById(id)) {
			throw new AnimalNotFoundException(id);
		}
	}

	@Override
	public Animal updateAnimalById(long id, Animal animal)
			throws InvalidAnimalException, AnimalNotFoundException {
		AnimalServiceFieldsTrimmer.trimFields(animal);
		AnimalValidationService.validate(animal);
		this.checkAnimalExistsById(id);
		animal.setId(id);
		return animalRepository.save(animal);
	}

	@Override
	public CareTaker addNewCareTakerToExistingAnimal(long id, CareTaker careTaker)
			throws InvalidCareTakerException, AnimalNotFoundException {
		CareTakerValidationService.validate(careTaker);
		Animal animal = this.getAnimalById(id);
		careTaker.setId(IdGenerator.getId());
		List<Animal> animals = Arrays.asList(animal);
		careTaker.setAnimals(animals);
		CareTaker addedCareTaker = careTakerRepository.save(careTaker);
		return addedCareTaker;
	}

	@Override
	public CareTaker addExistingCareTakerToExistingAnimal(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException, AnimalAndCareTakerAlreadyLinked {
		Animal animal = this.getAnimalById(animalId);
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		// Animal service can't use CareTakerService because we will have recursive dependencies
		if (!careTaker.isPresent()) {
			throw new CareTakerNotFoundException(careTakerId);
		} else {
			if (animal.getCareTakers().stream().noneMatch(c -> c.getId() == careTakerId)) {
				List<Animal> animals = careTaker.get().getAnimals();
				animals.add(animal);
				careTaker.get().setAnimals(animals);
				careTakerRepository.save(careTaker.get());
				return careTaker.get();
			} else {
				throw new AnimalAndCareTakerAlreadyLinked(animalId, careTakerId);
			}
		}
	}

	@Override
	public List<CareTaker> removeCareTakerFromCareTakersList(long animalId, long careTakerId)
			throws AnimalNotFoundException, CareTakerNotFoundException,
			CareTakersListEmptyException, CareTakerNotInTheAnimalCareTakersListException {
		Optional<Animal> animal = animalRepository.findById(animalId);
		List<CareTaker> careTakers;
		Optional<CareTaker> careTaker = careTakerRepository.findById(careTakerId);
		if (!animal.isPresent()) {
			throw new AnimalNotFoundException(animalId);
		}
		careTakers = animal.get().getCareTakers();
		if (!careTaker.isPresent()) {
			throw new CareTakerNotFoundException(careTakerId);
		} else if (careTakers.size() == 0) {
			throw new CareTakersListEmptyException(animalId);
		} else if (careTakers.stream().noneMatch(careTakerTmp -> careTakerTmp.getId().equals(careTakerId))) {
			throw new CareTakerNotInTheAnimalCareTakersListException(animalId, careTakerId);
		} else {
			careTaker.get().getAnimals().removeIf(animalTmp -> animalTmp.getId().equals(animalId));
			animal.get().getCareTakers().removeIf(careTakerTmp -> careTakerTmp.getId().equals(careTakerId));
			careTakerRepository.save(careTaker.get());
			animalRepository.save(animal.get());
		}
		return animal.get().getCareTakers();
	}
}
