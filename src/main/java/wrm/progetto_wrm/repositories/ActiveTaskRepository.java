package wrm.progetto_wrm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import wrm.progetto_wrm.entities.ActiveTask;


public interface ActiveTaskRepository extends JpaRepository <ActiveTask, Integer>{
    ActiveTask findByTaskCode(int taskCode); 
    boolean existsByTaskCode (int taskCode);

}
