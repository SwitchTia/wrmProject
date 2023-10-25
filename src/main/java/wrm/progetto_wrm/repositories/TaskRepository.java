package wrm.progetto_wrm.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import wrm.progetto_wrm.entities.Task;


public interface TaskRepository extends JpaRepository <Task, Integer>{
    Task findByTaskCode (int taskCode);
    boolean existsByTaskCode (int taskCode);

    Page <Task> findByTaskCode (int taskCode, PageRequest pageRequest);
}

