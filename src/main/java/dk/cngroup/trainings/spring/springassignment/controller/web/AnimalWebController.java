package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/web/animal")
public class AnimalWebController {

	private final AnimalService animalService;
	private final String VIEW_PATH = "views/animal";

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("/list")
	public ModelAndView getAnimals() {
		List<Animal> animals = animalService.getAnimals();
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_list");
		modelAndView.addObject("animals", animals);
		return modelAndView;
	}

	@GetMapping("/list/{id}")
	public ModelAndView getAnimalById(@PathVariable("id") Long id) throws AnimalNotFoundException {
		Animal animal = animalService.getAnimalById(id);
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_details");
		CareTaker careTaker = new CareTaker();
		modelAndView.addObject("animal", animal);
		modelAndView.addObject("careTaker", careTaker);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView addAnimal() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_form");
		Animal animal = new Animal();
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/update/{id}")
	public ModelAndView updateAnimal(@PathVariable("id") Long id) throws AnimalNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_form");
		Animal animal = animalService.getAnimalById(id);
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView saveAnimal(@ModelAttribute("animal") Animal animal)
			throws AnimalNotFoundException, InvalidAnimalException {
		if (animal.getId() != null) {
			// update
			animalService.updateAnimalById(animal.getId(), animal);
		} else {
			// add new
			animalService.addAnimal(animal);
		}
		return new ModelAndView("redirect:/web/animal/list");
	}

	@GetMapping("/delete/{id}")
	public ModelAndView deleteAnimal(@PathVariable("id") Long id) throws AnimalNotFoundException {
		animalService.deleteAnimalById(id);
		return new ModelAndView("redirect:/web/animal/list");
	}

	@GetMapping("/{animalId}/careTaker/{careTakerId}/remove")
	public ModelAndView delete(@PathVariable("animalId") Long animalId,
							   @PathVariable("careTakerId") Long careTakerId)
			throws CareTakerNotFoundException, AnimalNotFoundException,
			CareTakersListEmptyException, CareTakerNotInTheAnimalCareTakersListException {
		animalService.removeCareTakerFromCareTakersList(animalId, careTakerId);
		return new ModelAndView("redirect:/web/animal/list/" + animalId);
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewCareTakerToExistingAnimal(@PathVariable("id") Long id,
														@ModelAttribute("careTaker") CareTaker careTaker)
			throws AnimalNotFoundException, InvalidCareTakerException {
		animalService.addNewCareTakerToExistingAnimal(id, careTaker);
		return new ModelAndView("redirect:/web/animal/list/" + id);
	}

	@PostMapping("/list/{id}/link")
	public ModelAndView addExistingCareTakerToExistingAnimal(@PathVariable("id") Long animalId,
															 @ModelAttribute("careTaker") CareTaker careTaker)
			throws AnimalNotFoundException, AnimalAndCareTakerAlreadyLinked, CareTakerNotFoundException {
		animalService.addExistingCareTakerToExistingAnimal(animalId, careTaker.getId());
		return new ModelAndView("redirect:/web/animal/list/" + animalId);
	}
}