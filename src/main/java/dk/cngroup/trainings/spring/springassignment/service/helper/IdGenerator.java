package dk.cngroup.trainings.spring.springassignment.service.helper;

public class IdGenerator {
	private static long id = 0L;

	public synchronized static long getId() {
		id++;
		return id;
	}
}