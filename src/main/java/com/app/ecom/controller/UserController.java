package com.app.ecom.controller;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
      return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
    return  userService.fetchUsers(id).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/users")
    public ResponseEntity <String> createUser(@RequestBody UserRequest userRequestr){
        userService.addUser(userRequestr);
        return ResponseEntity.ok("User added successfully");
    }
  @PutMapping ("/api/users/{id}")
  public ResponseEntity <String> UpdateUser(@PathVariable long id, @RequestBody UserRequest updatedUserRequest) {
      boolean updated=  userService.UpdateUser(id,updatedUserRequest);
      if(updated){
        return ResponseEntity.ok("User updated successfully") ;
      }
         return ResponseEntity.notFound().build();
  }


}
