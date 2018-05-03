package dk.cngroup.trainings.spring.springassignment.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	private final String VIEW_PATH = "views/authentication";

	@GetMapping("/login")
	public ModelAndView loginForm() {
		return new ModelAndView(VIEW_PATH + "/login");
	}
}
