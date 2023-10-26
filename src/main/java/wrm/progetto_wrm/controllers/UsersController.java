package wrm.progetto_wrm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.servlet.http.HttpServletRequest;
import wrm.progetto_wrm.UTILITIES.Configurations.AuthenticationRequest;
import wrm.progetto_wrm.UTILITIES.Configurations.RegisterRequest;
import wrm.progetto_wrm.repositories.UsersRepository;
import wrm.progetto_wrm.services.JwtService;
import wrm.progetto_wrm.services.UsersService;

@RestController
@RequestMapping ("/users")
public class UsersController {

    @Autowired
    UsersService us;

    @Autowired
    UsersRepository ur;

    @Autowired
    JwtService jwtService;
    
    @PostMapping ("/registerManager")
    public ResponseEntity registerManager (@RequestBody RegisterRequest regRequest){
        try {
            return new ResponseEntity (us.registerManager(regRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/registerWorker")
    public ResponseEntity registerWorker (@RequestBody RegisterRequest regRequest){
        try {
            return new ResponseEntity (us.registerWorker(regRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/authenticate")
    public ResponseEntity authenticate (@RequestBody AuthenticationRequest authRequest){
        try {
            return new ResponseEntity (us.authenticate(authRequest), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/upgradeRole")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity upgradeRole (@RequestParam ("email") String email){
        try {
            return new ResponseEntity (us.upgradeRole(email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/downgradeRole")
    @PreAuthorize ("hasAuthority ('MANAGER')")
    public ResponseEntity downgradeRole (@RequestParam ("email") String email){
        try {
            return new ResponseEntity (us.downgradeRole(email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/deleteUser")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity deleteUser (@RequestParam ("email") String email) {
        try {
            return new ResponseEntity (us.deleteUser(email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getUser")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity getUser (@RequestParam ("email") String email) {
        try {
            return new ResponseEntity (us.getUser (email), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllUsers")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity getAllUsers () {
        return new ResponseEntity (ur.findAll(), HttpStatus.OK);
    }

    @PutMapping ("/modifyUser")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity modifyUser (@RequestParam ("email") String email, @RequestParam ("firstname") String firstname, @RequestParam ("lastname") String lastname){
        try {
            return new ResponseEntity (us.modifyUser (email, firstname, lastname), HttpStatus.OK);
        } catch (RuntimeException e) {
             String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyUserEmail")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity modifyUserEmail (@RequestParam ("email") String email, @RequestParam ("newEmail") String newEmail){
        try {
            return new ResponseEntity (us.modifyUserEmail (email, newEmail), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/modifyUserPassword")
    @PreAuthorize ("hasAuthority('WORKER')")
    public ResponseEntity modifyUserPassword (HttpServletRequest servletRequest, @RequestParam ("password") String password, @RequestParam ("newPassword") String newPassword){
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.modifyUserPassword (email, password, newPassword), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
             return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/acceptTask")
    @PreAuthorize ("hasAnyAuthority('MANAGER','WORKER')")
    public ResponseEntity acceptTask (HttpServletRequest servletRequest, @RequestParam ("taskCode") Integer taskCode) {
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.acceptTask (email, taskCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/cancelAcceptedTask")
    @PreAuthorize ("hasAnyAuthority('MANAGER','WORKER')")
    public ResponseEntity cancelAcceptedTask (HttpServletRequest servletRequest, @RequestParam ("taskCode") Integer taskCode) {
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.cancelAcceptedTask(email, taskCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity (ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/addClosedTask")
    @PreAuthorize ("hasAnyAuthority('MANAGER','WORKER')")
    public ResponseEntity addClosedTask (HttpServletRequest servletRequest, @RequestParam ("taskCode")int taskCode){
        try {
            String email =jwtService.extractEmailFromRequest(servletRequest);
            return new ResponseEntity (us.addClosedTask(email, taskCode), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllActiveTasks")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity getAllActiveTasks (){
        try {
            return new ResponseEntity (us.getAllActiveTasks(), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/getAllClosedTasks")
    @PreAuthorize ("hasAuthority('MANAGER')")
    public ResponseEntity getAllClosedTasks (){
        try {
            return new ResponseEntity (us.getAllClosedTasks(), HttpStatus.OK);
        } catch (RuntimeException e) {
            String ex = e.getClass().getSimpleName();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
