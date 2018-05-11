package dk.cngroup.trainings.spring.springassignment.exception.user;

public class UserEmailExistsException extends Throwable {
	public UserEmailExistsException(String email) {
		super(email);
	}
}
