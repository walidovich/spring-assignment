package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/animal")
public class AnimalWebController implements WebMvcConfigurer {

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

	@GetMapping("/update/{id}")
	public ModelAndView updateAnimal(@PathVariable("id") Long id) throws AnimalNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_form");
		Animal animal = animalService.getAnimalById(id);
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView addAnimal() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_form");
		modelAndView.addObject("animal", new Animal());
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView saveAnimal(@ModelAttribute("animal") @Valid Animal animal,
								   BindingResult bindingResult)
			throws AnimalNotFoundException, InvalidAnimalException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_form");
			return modelAndView;
		} else {
			if (animal.getId() != null) {
				// update
				animalService.updateAnimalById(animal.getId(), animal);
			} else {
				// add new
				animalService.addAnimal(animal);
			}
			return new ModelAndView("redirect:/web/animal/list");
		}
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

	@GetMapping("/list/{id}")
	public ModelAndView getAnimalById(@PathVariable("id") Long id) throws AnimalNotFoundException {
		Animal animal = animalService.getAnimalById(id);
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_details");
		modelAndView.addObject("animal", animal);
		modelAndView.addObject("careTaker", new CareTaker());
		return modelAndView;
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewCareTakerToExistingAnimal(@PathVariable("id") Long id,
														@ModelAttribute("careTaker") @Valid CareTaker careTaker,
														BindingResult bindingResult)
			throws AnimalNotFoundException, InvalidCareTakerException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/animal_details");
			Animal animal = animalService.getAnimalById(id);
			modelAndView.addObject("animal", animal);
			return modelAndView;
		} else {
			animalService.addNewCareTakerToExistingAnimal(id, careTaker);
			return new ModelAndView("redirect:/web/animal/list/" + id);
		}
	}

	@PostMapping("/list/{id}/link")
	public ModelAndView addExistingCareTakerToExistingAnimal(@PathVariable("id") Long animalId,
															 @ModelAttribute("careTaker") CareTaker careTaker)
			throws AnimalNotFoundException, AnimalAndCareTakerAlreadyLinked, CareTakerNotFoundException {
		animalService.addExistingCareTakerToExistingAnimal(animalId, careTaker.getId());
		return new ModelAndView("redirect:/web/animal/list/" + animalId);
	}
}