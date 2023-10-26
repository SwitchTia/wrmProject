package wrm.progetto_wrm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import wrm.progetto_wrm.UTILITIES.Exceptions.DataNotCorrectException;
import wrm.progetto_wrm.UTILITIES.Exceptions.TaskAlreadyExistsException;
import wrm.progetto_wrm.UTILITIES.Exceptions.TaskCostInferiorTaskProfitException;
import wrm.progetto_wrm.UTILITIES.Exceptions.TaskDoesNotExistException;
import wrm.progetto_wrm.entities.Task;
import wrm.progetto_wrm.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository tr;

    public Task saveTask (Task t) throws RuntimeException {
        if (tr.existsByTaskCode(t.getTaskCode())){
            throw new TaskAlreadyExistsException ();
        }
        if (t.getTaskCost() > t.getTaskProfit()){
            throw new TaskCostInferiorTaskProfitException ();
        }
        return tr.save(t);
    }

    @Transactional
    public String deleteTask (int taskCode) throws RuntimeException {
        Task t = tr.findByTaskCode (taskCode);
        if (t == null) {
            throw new DataNotCorrectException();
        }
        tr.delete (t);
        String del = "The task with task code " + taskCode + " has been deleted";
        return del;
    }

    public Task getTask (int taskCode) {
        Task t = tr.findByTaskCode (taskCode);
        if (!tr.existsByTaskCode(taskCode)){
            throw new TaskDoesNotExistException();
        }
        return t;
    }
    
    @Transactional
    public Task modifyTask (String taskName, int taskCode, double taskCost, double taskProfit) throws RuntimeException {
        Task t = tr.findByTaskCode(taskCode);
        if (t == null){
            throw new TaskDoesNotExistException();
        }
        t.setTaskName(taskName);
        t.setTaskCode(taskCode);
        if (taskCost > taskProfit){
            throw new TaskCostInferiorTaskProfitException ();
        }
        else {
            t.setTaskCost(taskCost);
            t.setTaskProfit(taskProfit);
        }
        tr.save (t);
        return t;
    } 

    public Page <Task> taskSortPage (int pageNr, int pageSize, String sortDirection) {
        Sort sort;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc("taskCode"));
        } else {
            sort = Sort.by(Sort.Order.asc("taskCode"));
        }
        PageRequest pageReq = PageRequest.of(pageNr, pageSize, sort);
        return tr.findAll(pageReq);
    }
}
