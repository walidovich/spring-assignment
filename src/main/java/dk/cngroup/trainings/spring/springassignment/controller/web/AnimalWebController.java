package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/animals")
public class AnimalWebController {

	private AnimalService animalService;

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("")
	public ModelAndView addAnimalView() {
		ModelAndView modelAndView = new ModelAndView("addanimal");
		modelAndView.addObject("animals", animalService.getAnimals());
		modelAndView.addObject("animal", new Animal());
		return modelAndView;
	}

	@PostMapping("")
	public ModelAndView addAnimal(@ModelAttribute @Valid Animal animal) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			animalService.addAnimal(animal);
		} catch (InvalidAnimalException e) {
			e.printStackTrace();
		}
		modelAndView.addObject("animals", animalService.getAnimals());
		modelAndView.setViewName("animals");
		modelAndView.addObject("animal", new Animal());
		return modelAndView;
	}

	@GetMapping("/showList")
	public ModelAndView getAnimalsView() {
		ModelAndView modelAndView = new ModelAndView("animals");
		modelAndView.addObject("animals", animalService.getAnimals());
		return modelAndView;
	}
}