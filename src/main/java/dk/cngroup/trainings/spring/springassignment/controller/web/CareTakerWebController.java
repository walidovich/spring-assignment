package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.service.CareTakerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/careTakers")
public class CareTakerWebController {
	
	private CareTakerService careTakerService;

	public CareTakerWebController(CareTakerService careTakerService) {
		this.careTakerService = careTakerService;
	}

	@GetMapping("")
	public String getCareTakers(Model model) {
		model.addAttribute("careTakers", careTakerService.getCareTakers());
		return "careTakers";
	}
}
