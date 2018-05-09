package dk.cngroup.trainings.spring.springassignment.service.helper;

import dk.cngroup.trainings.spring.springassignment.controller.dto.user.UserSignupDTO;
import dk.cngroup.trainings.spring.springassignment.exception.UserPasswordsNotMatchingException;

import javax.validation.Valid;

public class UserDtoValidatorService {

	public static void checkMatchingPasswords(@Valid UserSignupDTO userSignupDTO) throws UserPasswordsNotMatchingException {
		if (!userSignupDTO.getPassword().equals(userSignupDTO.getMatchingPassword())) {
			throw new UserPasswordsNotMatchingException();
		}
	}
}
