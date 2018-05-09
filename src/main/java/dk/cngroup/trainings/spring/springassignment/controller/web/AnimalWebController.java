package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.controller.dto.animal.AnimalCreateDTO;
import dk.cngroup.trainings.spring.springassignment.controller.dto.animal.AnimalEntityDtoConverter;
import dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker.CareTakerCreateDTO;
import dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker.CareTakerEntityDtoConverter;
import dk.cngroup.trainings.spring.springassignment.controller.dto.caretaker.CareTakerLinkIdDTO;
import dk.cngroup.trainings.spring.springassignment.exception.*;
import dk.cngroup.trainings.spring.springassignment.model.Animal;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.AnimalService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/animal")
public class AnimalWebController {

	private final AnimalService animalService;
	private final String VIEW_PATH = "views/animal/";

	public AnimalWebController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		List<Animal> animals = animalService.getAnimals();
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_list");
		modelAndView.addObject("animals", animals);
		return modelAndView;
	}

	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_form");
		AnimalCreateDTO animalCreateDTO = new AnimalCreateDTO();
		modelAndView.addObject("animalCreateDTO", animalCreateDTO);
		return modelAndView;
	}

	@PostMapping("/add")
	public ModelAndView add(@ModelAttribute("animalCreateDTO") @Valid AnimalCreateDTO animalCreateDTO,
							BindingResult bindingResult)
			throws InvalidAnimalException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_form");
			return modelAndView;
		} else {
			// add new
			Animal animal = AnimalEntityDtoConverter.toAnimalEntity(animalCreateDTO);
			animalService.addAnimal(animal);
			return new ModelAndView("redirect:/web/animal/list");
		}
	}

	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id) throws AnimalNotFoundException {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_update_form");
		Animal animal = animalService.getAnimalById(id);
		modelAndView.addObject("animal", animal);
		return modelAndView;
	}

	@PostMapping("/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id,
							   @ModelAttribute("animal") @Valid Animal animal,
							   BindingResult bindingResult)
			throws InvalidAnimalException, AnimalNotFoundException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_update_form");
			return modelAndView;
		} else {
			// add new
			animalService.updateAnimalById(id, animal);
			return new ModelAndView("redirect:/web/animal/list");
		}
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) throws AnimalNotFoundException {
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
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_details");
		modelAndView.addObject("animal", animal);
		CareTakerCreateDTO careTakerCreateDTO = new CareTakerCreateDTO();
		modelAndView.addObject("careTakerCreateDTO", careTakerCreateDTO);
		return modelAndView;
	}

	@PostMapping("/list/{id}")
	public ModelAndView addNewCareTakerToExistingAnimal(@PathVariable("id") Long id,
														@ModelAttribute("careTakerCreateDTO") @Valid CareTakerCreateDTO careTakerCreateDTO,
														BindingResult bindingResult)
			throws AnimalNotFoundException, InvalidCareTakerException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_details");
			Animal animal = animalService.getAnimalById(id);
			modelAndView.addObject("animal", animal);
			return modelAndView;
		} else {
			CareTaker careTaker = CareTakerEntityDtoConverter.toCareTakerEntity(careTakerCreateDTO);
			animalService.addNewCareTakerToExistingAnimal(id, careTaker);
			return new ModelAndView("redirect:/web/animal/list/" + id);
		}
	}

	@GetMapping("/list/{id}/link")
	public ModelAndView linkExistingCareTakerToExistingAnimalForm(@PathVariable("id") Long id)
			throws AnimalNotFoundException {
		Animal animal = animalService.getAnimalById(id);
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_link");
		modelAndView.addObject("animal", animal);
		CareTakerLinkIdDTO careTakerLinkIdDto = new CareTakerLinkIdDTO();
		modelAndView.addObject("careTakerLinkIdDto", careTakerLinkIdDto);
		return modelAndView;
	}

	@PostMapping("/list/{id}/link")
	public ModelAndView addExistingCareTakerToExistingAnimal(@PathVariable("id") Long id,
															 @ModelAttribute("careTakerLinkIdDto") @Valid CareTakerLinkIdDTO careTakerLinkIdDto,
															 BindingResult bindingResult)
			throws AnimalNotFoundException, AnimalAndCareTakerAlreadyLinked, CareTakerNotFoundException {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "animal_link");
			Animal animal = animalService.getAnimalById(id);
			modelAndView.addObject("animal", animal);
			return modelAndView;
		} else {
			animalService.addExistingCareTakerToExistingAnimal(id, careTakerLinkIdDto.getId());
			return new ModelAndView("redirect:/web/animal/list/" + id);
		}

	}
}