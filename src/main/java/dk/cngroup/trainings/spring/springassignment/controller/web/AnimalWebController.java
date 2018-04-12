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

	private AnimalService animalService;

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("animal/animal_page");
		List<Animal> animals = animalService.getAnimals();
		modelAndView.addObject("animals", animals);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView modelAndView = new ModelAndView("animal/animal_form");
		Animal animal = new Animal();
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id) throws AnimalNotFoundException {
		ModelAndView modelAndView = new ModelAndView("animal/animal_form");
		Animal animal = animalService.getAnimalById(id);
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView save(@ModelAttribute("animal") Animal animal)
			throws AnimalNotFoundException, InvalidAnimalException {
		if (animal != null && animal.getId() != null) {
			// update
			animalService.updateAnimalById(animal.getId(), animal);
		} else {
			// add new
			animalService.addAnimal(animal);
		}
		return new ModelAndView("redirect:/web/animal/list");
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) throws AnimalNotFoundException {
		animalService.deleteAnimalById(id);
		return new ModelAndView("redirect:/web/animal/list");
	}
}