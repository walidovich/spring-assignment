package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.controller.dto.animal.AnimalCreateDTO;
import dk.cngroup.trainings.spring.springassignment.controller.dto.animal.AnimalEntityDtoConverter;
import dk.cngroup.trainings.spring.springassignment.controller.dto.animal.AnimalLinkIdDTO;
import dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker.CareTakerCreateDTO;
import dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker.CareTakerEntityDtoConverter;
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
	private final String VIEW_PATH = "views/careTaker/";

	public CareTakerWebController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_list");
		List<CareTaker> careTakers = careTakerService.getCareTakers();
		modelAndView.addObject("careTakers", careTakers);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_form");
		CareTakerCreateDTO careTakerCreateDTO = new CareTakerCreateDTO();
		modelAndView.addObject("careTakerCreateDTO", careTakerCreateDTO);
		return modelAndView;
	}

	@PostMapping("/add")
	public ModelAndView add(@ModelAttribute("careTakerCreateDTO") @Valid CareTakerCreateDTO careTakerCreateDTO,
							BindingResult bindingResult)
			throws InvalidCareTakerException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_form");
			return modelAndView;
		} else {
			// add new
			CareTaker careTaker = CareTakerEntityDtoConverter.toCareTakerEntity(careTakerCreateDTO);
			careTakerService.addCareTaker(careTaker);
			return new ModelAndView("redirect:/web/careTaker/list");
		}
	}

	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_update_form");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		modelAndView.addObject("careTaker", careTaker);
		return modelAndView;
	}

	@PostMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id,
							   @ModelAttribute("careTaker") @Valid CareTaker careTaker,
							   BindingResult bindingResult)
			throws CareTakerNotFoundException, InvalidCareTakerException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_update_form");
			return modelAndView;
		} else {
			// add new
			careTakerService.updateCareTakerById(id, careTaker);
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
	public ModelAndView getCareTakerById(@PathVariable("id") Long id) throws CareTakerNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_details");
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		modelAndView.addObject("careTaker", careTaker);
		AnimalCreateDTO animalCreateDTO = new AnimalCreateDTO();
		modelAndView.addObject("animalCreateDTO", animalCreateDTO);
		return modelAndView;
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewAnimalToExistingCareTaker(@PathVariable("id") Long id,
														@ModelAttribute("animalCreateDTO") @Valid AnimalCreateDTO animalCreateDTO,
														BindingResult bindingResult)
			throws InvalidAnimalException, CareTakerNotFoundException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_details");
			CareTaker careTaker = careTakerService.getCareTakerById(id);
			modelAndView.addObject("careTaker", careTaker);
			return modelAndView;
		} else {
			Animal animal = AnimalEntityDtoConverter.toAnimalEntity(animalCreateDTO);
			careTakerService.addNewAnimalToExistingCareTaker(id, animal);
			return new ModelAndView("redirect:/web/careTaker/list/" + id);
		}
	}

	@GetMapping("/list/{id}/link")
	public ModelAndView linkExistingAnimalToExistingCareTakerForm(@PathVariable("id") Long id)
			throws CareTakerNotFoundException {
		CareTaker careTaker = careTakerService.getCareTakerById(id);
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_link");
		modelAndView.addObject("careTaker", careTaker);
		AnimalLinkIdDTO animalLinkDto = new AnimalLinkIdDTO();
		modelAndView.addObject("animalLinkDto", animalLinkDto);
		return modelAndView;
	}

	@PostMapping("/list/{id}/link")
	public ModelAndView addExistingAnimalToExistingCareTaker(@PathVariable("id") Long id,
															 @ModelAttribute("animalLinkDto") @Valid AnimalLinkIdDTO animalLinkDto,
															 BindingResult bindingResult)
			throws AnimalNotFoundException, AnimalAndCareTakerAlreadyLinked, CareTakerNotFoundException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "careTaker_link");
			CareTaker careTaker = careTakerService.getCareTakerById(id);
			modelAndView.addObject("careTaker", careTaker);
			return modelAndView;
		} else {
			careTakerService.addExistingAnimalToExistingCareTaker(id, animalLinkDto.getId());
			return new ModelAndView("redirect:/web/careTaker/list/" + id);
		}
	}
}
