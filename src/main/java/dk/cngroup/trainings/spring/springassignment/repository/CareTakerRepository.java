package dk.cngroup.trainings.spring.springassignment.repository;

import dk.cngroup.trainings.spring.springassignment.model.CareTaker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareTakerRepository extends CrudRepository<CareTaker,Long>{

}
