package dk.cngroup.trainings.spring.springassignment.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("redirect:/web/login");
	}
}
