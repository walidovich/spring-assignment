package dk.cngroup.trainings.spring.springassignment.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class LoginController {

	private final String VIEW_PATH = "views/authentication/";

	@GetMapping("/login")
	public ModelAndView loginForm() {
		return new ModelAndView(VIEW_PATH + "login");
	}

	@PostMapping("/logout")
	public ModelAndView logout() {
		return new ModelAndView("redirect:/web/login");
	}
}
