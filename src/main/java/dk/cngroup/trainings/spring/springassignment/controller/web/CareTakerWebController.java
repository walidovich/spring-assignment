package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.exception.InvalidCareTakerException;
import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/careTakers")
public class CareTakerWebController {

	private CareTakerService careTakerService;

	public CareTakerWebController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@GetMapping("")
	public ModelAndView addCareTakerView() {
		ModelAndView modelAndView = new ModelAndView("addcaretaker");
		modelAndView.addObject("careTakers", careTakerService.getCareTakers());
		modelAndView.addObject("careTaker", new CareTaker());
		System.out.print(">>>>>> My diagnostic: GET called.");
		return modelAndView;
	}

	@PostMapping("")
	public ModelAndView addCareTaker(@ModelAttribute @Valid CareTaker careTaker) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			careTakerService.addCareTaker(careTaker);
		} catch (InvalidCareTakerException e) {
			e.printStackTrace();
		}
		modelAndView.addObject("careTakers", careTakerService.getCareTakers());
		modelAndView.setViewName("caretakers");
		modelAndView.addObject("careTaker", new CareTaker());
		return modelAndView;
	}

	@GetMapping("/showList")
	public ModelAndView getCareTakersView() {
		ModelAndView modelAndView = new ModelAndView("caretakers");
		modelAndView.addObject("careTakers", careTakerService.getCareTakers());
		return modelAndView;
	}
}
