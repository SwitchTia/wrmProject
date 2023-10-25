package wrm.progetto_wrm.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import wrm.progetto_wrm.UTILITIES.Configurations.AuthenticationRequest;
import wrm.progetto_wrm.UTILITIES.Configurations.AuthenticationResponse;
import wrm.progetto_wrm.UTILITIES.Configurations.RegisterRequest;
import wrm.progetto_wrm.UTILITIES.Exceptions.DataNotCorrectException;
import wrm.progetto_wrm.UTILITIES.Exceptions.LimitOfFiveActiveTaskEccededException;
import wrm.progetto_wrm.UTILITIES.Exceptions.TaskDoesNotExistException;
import wrm.progetto_wrm.UTILITIES.Exceptions.UserAlreadyExistsException;
import wrm.progetto_wrm.UTILITIES.Exceptions.UserBudgetEccededException;
import wrm.progetto_wrm.UTILITIES.Exceptions.UserDoesNotExistException;
import wrm.progetto_wrm.UTILITIES.Exceptions.UserHasAlreadyWorkerRoleException;
import wrm.progetto_wrm.UTILITIES.Exceptions.UserHasAlreadyManagerRoleException;
import wrm.progetto_wrm.entities.ActiveTask;
import wrm.progetto_wrm.entities.ClosedTask;
import wrm.progetto_wrm.entities.Role;
import wrm.progetto_wrm.entities.Task;
import wrm.progetto_wrm.entities.Users;
import wrm.progetto_wrm.repositories.ActiveTaskRepository;
import wrm.progetto_wrm.repositories.ClosedTaskRepository;
import wrm.progetto_wrm.repositories.TaskRepository;
import wrm.progetto_wrm.repositories.UsersRepository;

@Service
@RequiredArgsConstructor
public class UsersService {
    
    
    final UsersRepository ur;

    @Autowired
    TaskRepository tr;

    @Autowired
    ClosedTaskRepository ctr;

    @Autowired
    ActiveTaskRepository atr;
    
    
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public boolean isValidEmail (String email) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return email.matches(regex);
    }

    public AuthenticationResponse registerManager (RegisterRequest regRequest){
        Users u = new Users();
        u.setFirstname(regRequest.getFirstname().toUpperCase());
        u.setLastname (regRequest.getLastname().toUpperCase());
        u.setEmail(regRequest.getEmail().toLowerCase());
        u.setPassword (passwordEncoder.encode(regRequest.getPassword()));
        u.setRole(Role.MANAGER);
        if (ur.existsByEmail(u.getEmail())){
            throw new UserAlreadyExistsException();
        }
        else {
            ur.save(u);
            var jwtToken = jwtService.generateToken(u);
            return new AuthenticationResponse(jwtToken);
        } 
    }

    public AuthenticationResponse registerWorker (RegisterRequest regRequest){
        Users u = new Users();
        u.setFirstname(regRequest.getFirstname().toUpperCase());
        u.setLastname (regRequest.getLastname().toUpperCase());
        u.setEmail(regRequest.getEmail().toLowerCase());
        u.setPassword (passwordEncoder.encode(regRequest.getPassword()));
        u.setRole(Role.WORKER);
        if (ur.existsByEmail(u.getEmail())){
            throw new UserAlreadyExistsException();
        }
        else {
            ur.save(u);
            var jwtToken = jwtService.generateToken(u);
            return new AuthenticationResponse(jwtToken);
        } 
    }

    public AuthenticationResponse authenticate (AuthenticationRequest authRequest) throws RuntimeException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authRequest.getEmail().toLowerCase(), 
            authRequest.getPassword()));
        Users u = ur.findByEmail(authRequest.getEmail().toLowerCase());
        var jwtToken = jwtService.generateToken(u);

        return new AuthenticationResponse (jwtToken);
    }

    public Users upgradeRole (String email) throws RuntimeException {
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null) {
            throw new UserDoesNotExistException();
        }
        if (u.getRole() == Role.MANAGER) {
            throw new UserHasAlreadyManagerRoleException();
        }
        u.setRole(Role.MANAGER);
        ur.save(u);
        return u;
    }

    public Users downgradeRole (String email) throws RuntimeException {
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null) {
            throw new UserDoesNotExistException();
        }
        if (u.getRole() == Role.WORKER) {
            throw new UserHasAlreadyWorkerRoleException();
        }
        u.setRole(Role.WORKER);
        ur.save(u);
        return u;
    }

    public String deleteUser (String email) throws RuntimeException{
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        ur.delete(u);
        String del = "The user " + email + " has been deleted";
        return del;
    }

    public Users getUser (String email) throws RuntimeException {
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        return (u);
    }

    public Users modifyUser (String email, String firstname, String lastname) throws RuntimeException {
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException ();
        }
        u.setFirstname (firstname);
        u.setLastname (lastname);
        u.setEmail(email);
        ur.save (u);
        return u;
    }

    public Users modifyUserEmail (String email, String newEmail){
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException();
        }
        u.setEmail(newEmail);
        ur.save (u);
        return u;
    }

    public Users modifyUserPassword (String email, String password, String newPassword){
        Users u = ur.findByEmail(email);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException();
        }
        u.setPassword(newPassword);
        ur.save (u);
        return u;
    }

    @Transactional
    public Users acceptTask (String email, Integer taskCode) throws RuntimeException {
        Users u = ur.findByEmail(email);
        Task t = tr.findByTaskCode(taskCode);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (t == null){
            throw new TaskDoesNotExistException();
        }
        if (u.getActiveTaskList().size() >= 5)  {
            throw new LimitOfFiveActiveTaskEccededException ();
        }
        if (u.getUserBudget() < t.getTaskBudget()){
            throw new UserBudgetEccededException ();
        }
        u.setUserBudget (u.getUserBudget() - t.getTaskBudget());

        ActiveTask at = new ActiveTask();
        at.setTaskName(t.getTaskName());
        at.setTaskCode(t.getTaskCode());
        at.setTaskBudget(t.getTaskBudget());
        at.setTaskProfit(t.getTaskProfit());
        at.setVersion(t.getVersion());
        at = atr.save(at);
        tr.delete(t);

        u.getActiveTaskList().add(at); 

        return u = ur.save(u);
    }

    public Users cancelAcceptedTask (String email, Integer taskCode) throws RuntimeException {
        Users u = ur.findByEmail(email);
        ActiveTask at = atr.findByTaskCode(taskCode);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (at == null){
            throw new TaskDoesNotExistException ();
        }
        Task t = new Task();
        t.setTaskName(at.getTaskName());
        t.setTaskCode(at.getTaskCode());
        t.setTaskBudget(at.getTaskBudget());
        t.setTaskProfit(at.getTaskProfit());
        t.setVersion(at.getVersion());

        tr.save(t);
        u.setUserBudget (u.getUserBudget() + at.getTaskBudget());
        atr.delete(at);
        u.getActiveTaskList().remove(at);
        
        return u = ur.save(u);
    }

    public Users addClosedTask (String email, Integer taskCode) throws RuntimeException{
        Users u = ur.findByEmail(email);
        ActiveTask at = atr.findByTaskCode(taskCode);
        if (!isValidEmail(email)){
            throw new DataNotCorrectException ();
        }
        if (u == null){
            throw new UserDoesNotExistException();
        }
        if (at == null){
            throw new TaskDoesNotExistException ();
        }
        ClosedTask ct  = new ClosedTask();
        ct.setTaskName(at.getTaskName());
        ct.setTaskCode(at.getTaskCode());
        ct.setTaskBudget(at.getTaskBudget());
        ct.setTaskProfit(at.getTaskProfit());
        ct.setVersion(at.getVersion());

        ct = ctr.save(ct);
        atr.delete(at);

        u.getClosedTaskList().add(ct);
        u.setUserBudget (u.getUserBudget() + ct.getTaskProfit());
        u.setUserProfit(ct.getTaskProfit());

        return u = ur.save(u);
    }

    /*public List <ClosedTask> getAllClosedTask  () {
        List <ClosedTask> allClosedTaskList = new ArrayList <> ();
        List <Users> usersList = ur.findAll();
        for (Users users : usersList ){
            allClosedTaskList.add(closedTaskList);
        }
        return allClosedTaskList;
    }*/

}
