package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/careTaker")
public class CareTakerWebController {

	private final CareTakerService careTakerService;
	private final String VIEW_PATH = "views/careTaker";

	public CareTakerWebController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_list");
		List<CareTaker> careTakers = careTakerService.getCareTakers();
		modelAndView.addObject("careTakers", careTakers);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_form");
		modelAndView.addObject("careTaker", new CareTaker());
		return modelAndView;
	}

	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_form");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		modelAndView.addObject("careTaker", careTaker);
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView save(@ModelAttribute("careTaker") @Valid CareTaker careTaker,
							 BindingResult bindingResult)
			throws CareTakerNotFoundException, InvalidCareTakerException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_form");
			return modelAndView;
		} else {
			if (careTaker.getId() != null) {
				// update
				careTakerService.updateCareTakerById(careTaker.getId(), careTaker);
			} else {
				// add new
				careTakerService.addCareTaker(careTaker);
			}
			return new ModelAndView("redirect:/web/careTaker/list");
		}
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		careTakerService.deleteCareTakerById(id);
		return new ModelAndView("redirect:/web/careTaker/list");
	}

	@GetMapping("/{careTakerId}/animal/{animalId}/remove")
	public ModelAndView delete(@PathVariable("careTakerId") Long careTakerId,
							   @PathVariable("animalId") Long animalId)
			throws CareTakerNotFoundException, AnimalNotFoundInCareTakerAnimalsListException,
			AnimalNotFoundException, AnimalsListEmptyException {
		careTakerService.removeAnimalFromAnimalsList(careTakerId, animalId);
		return new ModelAndView("redirect:/web/careTaker/list/" + careTakerId);
	}

	@GetMapping("/list/{id}")
	public ModelAndView careTakerDetails(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_details");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		modelAndView.addObject("careTaker", careTaker);
		modelAndView.addObject("animal", new Animal());
		return modelAndView;
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewAnimalToExistingCareTaker(@PathVariable("id") Long id,
														@ModelAttribute("animal") @Valid Animal animal,
														BindingResult bindingResult)
			throws InvalidAnimalException, CareTakerNotFoundException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "/careTaker_details");
			CareTaker careTaker = careTakerService.getCareTakerById(id);
			modelAndView.addObject("careTaker", careTaker);
			return modelAndView;
		} else {
			careTakerService.addNewAnimalToExistingCareTaker(id, animal);
			return new ModelAndView("redirect:/web/careTaker/list/" + id);
		}
	}

	@PostMapping("/list/{id}/link")
	public ModelAndView addExistingAnimalToExistingCareTaker(@PathVariable("id") Long careTakerId,
															 @ModelAttribute("animal") Animal animal)
			throws AnimalNotFoundException, AnimalAndCareTakerAlreadyLinked, CareTakerNotFoundException {
		careTakerService.addExistingAnimalToExistingCareTaker(careTakerId, animal.getId());
		return new ModelAndView("redirect:/web/careTaker/list/" + careTakerId);
	}
}
