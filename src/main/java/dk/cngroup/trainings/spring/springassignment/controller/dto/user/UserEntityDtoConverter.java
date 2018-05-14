package dk.cngroup.trainings.spring.springassignment.controller.dto.user;

import dk.cngroup.trainings.spring.springassignment.model.User;
import org.modelmapper.ModelMapper;

public class UserEntityDtoConverter {

	public static User toUserEntity(UserSignupDTO userSignupDTO) {
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userSignupDTO, User.class);
		return user;
	}

	public static UserSignupDTO toUserSignupDTO(User user) {
		ModelMapper modelMapper = new ModelMapper();
		UserSignupDTO userSignupDTO = modelMapper.map(user, UserSignupDTO.class);
		return userSignupDTO;
	}
}
