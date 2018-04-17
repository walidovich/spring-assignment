package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.CareTakerNotFoundException;
import dk.cngroup.trainings.spring.springassignment.exception.InvalidAnimalException;
import dk.cngroup.trainings.spring.springassignment.exception.InvalidCareTakerException;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/web/careTaker")
public class CareTakerWebController {

	private final CareTakerService careTakerService;

	public CareTakerWebController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("careTaker/careTaker_list");
		List<CareTaker> careTakers = careTakerService.getCareTakers();
		modelAndView.addObject("careTakers", careTakers);
		return modelAndView;
	}

	@GetMapping("/list/{id}")
	public ModelAndView careTakerDetails(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView("careTaker/careTaker_details");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		Animal animal = new Animal();
		modelAndView.addObject("careTaker", careTaker);
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView modelAndView = new ModelAndView("careTaker/careTaker_form");
		CareTaker careTaker = new CareTaker();
		modelAndView.addObject("careTaker", careTaker);
		return modelAndView;
	}

	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView("careTaker/careTaker_form");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		modelAndView.addObject("careTaker", careTaker);
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView save(@ModelAttribute("careTaker") CareTaker careTaker)
			throws CareTakerNotFoundException, InvalidCareTakerException {
		if (careTaker.getId() != null) {
			// update
			careTakerService.updateCareTakerById(careTaker.getId(), careTaker);
		} else {
			// add new
			careTakerService.addCareTaker(careTaker);
		}
		return new ModelAndView("redirect:/web/careTaker/list");
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		careTakerService.deleteCareTakerById(id);
		return new ModelAndView("redirect:/web/careTaker/list");
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewAnimalToExistingCareTaker(@PathVariable("id") Long id,
														@ModelAttribute("animal") Animal animal)
			throws InvalidAnimalException, CareTakerNotFoundException {
		careTakerService.addNewAnimalToExistingCareTaker(id, animal);
		return new ModelAndView("redirect:/web/careTaker/list/" + id);
	}
}
