package dk.cngroup.trainings.spring.springassignment.controller.web;

import dk.cngroup.trainings.spring.springassignment.controller.dto.user.UserEntityDtoConverter;
import dk.cngroup.trainings.spring.springassignment.controller.dto.user.UserSignupDTO;
import dk.cngroup.trainings.spring.springassignment.controller.helper.UserDtoValidatorService;
import dk.cngroup.trainings.spring.springassignment.exception.user.UserEmailExistsException;
import dk.cngroup.trainings.spring.springassignment.exception.user.UserPasswordsNotMatchingException;
import dk.cngroup.trainings.spring.springassignment.model.User;
import dk.cngroup.trainings.spring.springassignment.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private final String VIEW_PATH = "views/authentication/";

	private UserService userService;

	public SignupController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("")
	public ModelAndView signupForm() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "signup");
		modelAndView.addObject("userSignupDto", new UserSignupDTO());
		return modelAndView;
	}

	@PostMapping("")
	public ModelAndView signup(@ModelAttribute("userSignupDto") @Valid UserSignupDTO userSignupDTO,
							   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "signup");
			return modelAndView;
		} else {
			try {
				UserDtoValidatorService.checkMatchingPasswords(userSignupDTO);
				User user = UserEntityDtoConverter.toUserEntity(userSignupDTO);
				userService.addUser(user);
			} catch (UserPasswordsNotMatchingException e) {
				return specialError("password", "passwords don't match", bindingResult);
			} catch (UserEmailExistsException e) {
				return specialError("email", "email already used", bindingResult);
			}
			return new ModelAndView("redirect:/web/login");

		}
	}

	private ModelAndView specialError(String field, String errorMessage, BindingResult bindingResult) {
		bindingResult.rejectValue(field, "error.user", errorMessage);
		ModelAndView modelAndView = new ModelAndView(VIEW_PATH + "signup");
		modelAndView.addAllObjects(bindingResult.getModel());
		return modelAndView;
	}
}
