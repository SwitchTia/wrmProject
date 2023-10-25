package wrm.progetto_wrm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wrm.progetto_wrm.entities.Task;
import wrm.progetto_wrm.repositories.ActiveTaskRepository;
import wrm.progetto_wrm.repositories.ClosedTaskRepository;
import wrm.progetto_wrm.repositories.TaskRepository;
import wrm.progetto_wrm.services.TaskService;

@RestController
@RequestMapping ("/task")
public class TaskController {

    @Autowired
    TaskRepository tr;

    @Autowired
    TaskService ts;

    @Autowired
    ActiveTaskRepository atr;

    @Autowired
    ClosedTaskRepository ctr;

    @PostMapping ("/saveTask")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity saveTask (@RequestBody Task t){
        try {
            return new ResponseEntity (ts.saveTask (t), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/deleteTask")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity deleteTask (@RequestParam ("taskCode") int taskCode ) {
        try {
            return new ResponseEntity (ts.deleteTask (taskCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getTask")
    @PreAuthorize ("hasAnyAuthority ('MANAGER')")
    public ResponseEntity getTask (@RequestParam ("taskCode") int taskCode) {
        try {
            return new ResponseEntity (ts.getTask(taskCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllTasks")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity getAllTasks () {
        return new ResponseEntity (tr.findAll(), HttpStatus.OK);
    }

    @GetMapping ("/getAllActiveTasks")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity getAllActiveTasks () {
        return new ResponseEntity (atr.findAll(), HttpStatus.OK);
    }

    @GetMapping ("/getAllClosedTasks")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity getAllClosedTasks () {
        return new ResponseEntity (ctr.findAll(), HttpStatus.OK);
    }

    @PutMapping ("/modifyTask")
    @PreAuthorize ("hasAnyAuthority ('MANAGER')")
    public ResponseEntity modifyTask (@RequestParam ("taskName") String taskName, @RequestParam ("taskCode") int taskCode, @RequestParam ("taskBudget") int taskBudget, @RequestParam ("taskProfit") int taskProfit) {
        try {
            return new ResponseEntity (ts.modifyTask (taskName, taskCode, taskBudget, taskProfit), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    } 

    @GetMapping ("/taskSortPage")
    @PreAuthorize ("hasAnyAuthority ('MANAGER','WORKER')")
    public Page <Task> taskSortPage (@RequestParam ("pageNr") int pageNr, @RequestParam ("pageSize") int pageSize, @RequestParam ("sortDirection") String sortDirection){
        return ts.taskSortPage (pageNr, pageSize, sortDirection);
    }
}
