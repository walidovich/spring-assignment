package dk.cngroup.trainings.spring.springassignment.exception;

public class UserEmailExistsException extends Throwable {
	public UserEmailExistsException(String email) {
		super(email);
	}
}
