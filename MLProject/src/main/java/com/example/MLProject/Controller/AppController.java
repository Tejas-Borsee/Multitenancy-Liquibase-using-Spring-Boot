package com.example.MLProject.Controller;

import com.example.MLProject.Model.UserTable;
import com.example.MLProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ml")
public class AppController {

    @Autowired
    private UserService userServ;


    @GetMapping("/test")
    public String test(){
        return "API is working.";
    }


    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserTable users) {
        try {
            userServ.saveMainUser(users);
            userServ.createTenant(users.getTenant_name());
            return ResponseEntity.ok("Tenant " + users.getTenant_name() + " created.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> createTenant(@RequestParam String name) {
//        try {
//            userServ.createTenant(name);
//            return ResponseEntity.ok("Tenant " + name + " created.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error: " + e.getMessage());
//        }
//    }


}




