package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import dk.cngroup.trainings.spring.springassignment.service.exception.InvalidAnimalException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/animals")
public class AnimalWebController {

	private AnimalService animalService;

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("")
	public ModelAndView addAnimalView() {
		ModelAndView modelAndView = new ModelAndView("addAnimal");
		modelAndView.addObject("animal", new Animal(1L, "Lion", "The Lion"));
		System.out.println(">>>>>>>>>>>>>>>>>> GET called");
		return modelAndView;
	}

	@PostMapping("")
	public String addAnimal(@ModelAttribute Animal animal) {
		try {
			animalService.addAnimal(animal);
		} catch (InvalidAnimalException e) {
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>>>>>>>> POST called");
		ModelAndView modelAndView = new ModelAndView("animals");
		modelAndView.addObject("animals", animalService.getAnimals());
		return "animals";
	}
}