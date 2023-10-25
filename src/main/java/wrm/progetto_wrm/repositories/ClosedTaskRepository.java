package wrm.progetto_wrm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import wrm.progetto_wrm.entities.ClosedTask;


public interface ClosedTaskRepository extends JpaRepository <ClosedTask, Integer>{
    ClosedTask findByTaskCode (int taskCode); 
    boolean existsByTaskCode (int taskCode);

}

