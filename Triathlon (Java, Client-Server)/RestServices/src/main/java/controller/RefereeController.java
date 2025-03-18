package controller;

import Domain.Referee;
import Repository.DBRepositories.RefereeDBRepository;
import Repository.Interfaces.IRefereeRepository;
import controller.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.ServicesException;

@CrossOrigin
@RestController
@RequestMapping("/triathlon/referees")
public class RefereeController {

    private static final String template = "Hello, %s!";

    @Autowired
    private IRefereeRepository userRepository;

    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }


    @RequestMapping( method= RequestMethod.GET)
    public Referee[] getAll(){
        System.out.println("Get all users ...");
        return userRepository.getAll().toArray(new Referee[0]);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("Get by id "+id);
        Referee user=userRepository.findById(Integer.parseInt(id));
        if (user==null)
            return new ResponseEntity<String>("Referee not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Referee>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Referee create(@RequestBody Referee user){
        userRepository.add(user);
        return user;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Referee update(@RequestBody Referee user) {
        System.out.println("Updating user ...");
        userRepository.update(user, user.getId());
        return user;

    }
    // @CrossOrigin(origins = "http://localhost:3000")
//    @RequestMapping(method= RequestMethod.DELETE)
//    public ResponseEntity<?> delete(@RequestBody Referee referee){
//        System.out.println("Deleting user ... "+ referee);
//        userRepository.delete(referee);
//        return new ResponseEntity<Referee>(HttpStatus.OK);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting user ... " + id);
        Referee referee = userRepository.findById(id);
        userRepository.delete(referee);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @RequestMapping("/{user}/name")
    public String name(@PathVariable String user){
        Referee result=userRepository.getRefereeByName(user);
        System.out.println("Result ..."+result);

        return result.getName();
    }



    @ExceptionHandler(ServicesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(ServicesException e) {
        return e.getMessage();
    }
}