package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.AnimalNotFoundException;
import dk.cngroup.trainings.spring.springassignment.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/web/animal")
public class AnimalWebController {

	private final AnimalService animalService;

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("/list")
	public ModelAndView getAnimals() {
		List<Animal> animals = animalService.getAnimals();
		ModelAndView modelAndView = new ModelAndView("animal/animal_list");
		modelAndView.addObject("animals", animals);
		return modelAndView;
	}

	@GetMapping("/list/{id}")
	public ModelAndView getAnimalById(@PathVariable("id") Long id) throws AnimalNotFoundException {
		Animal animal = animalService.getAnimalById(id);
		ModelAndView modelAndView = new ModelAndView("animal/animal_details");
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView addAnimal() {
		ModelAndView modelAndView = new ModelAndView("animal/animal_form");
		Animal animal = new Animal();
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/update/{id}")
	public ModelAndView updateAnimal(@PathVariable("id") Long id) throws AnimalNotFoundException {
		ModelAndView modelAndView = new ModelAndView("animal/animal_form");
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
}