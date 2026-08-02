package com.app.ecom.service;

import com.app.ecom.dto.AdressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
  //  public List<User> userList = new ArrayList<>();
   // private long nextId = 1L;
    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    public Optional<UserResponse> fetchUsers(Long id) {
      return  userRepository.findById(id).map(this::mapToUserResponse);
    }


    public void addUser( UserRequest userRequest){
       // user.setId(nextId++);
        User user   =new User();
        updateUserFromRequest(user,userRequest);
    userRepository.save(user ) ;
    }



    public Boolean UpdateUser(long id,UserRequest updatedUserRequest) {

        return userRepository.findById(id).map(existingUser->{
            updateUserFromRequest(existingUser,updatedUserRequest);

            userRepository.save(existingUser);
            return true;
        }).orElse(false);


      }

    private UserResponse mapToUserResponse(User user) {

        UserResponse userResponse = new UserResponse();

        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());

        if (user.getAddress() != null) {
            AdressDTO addressDTO = new AdressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddress(addressDTO);
        }

        return userResponse;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName((userRequest.getFirstName()));
        user.setLastName((userRequest.getLastName()));
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress()!=null){
            Address address=new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry()) ;
            user.setAddress(address);
        }
    }



}
