package com.booking.service;

import com.booking.dto.CustomerDTO;
import com.booking.dto.UserDTO;
import com.booking.entity.User;
import com.booking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    @Value("${user.url}")
    private String url;

    private UserRepository userRepository;

    private WebClient.Builder webClientBuilder;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public ResponseEntity<?> createUser(UserDTO dto){
        if (isUserByUsername(dto.getUserName())){
            logger.debug("This user name already used..");
            return new ResponseEntity<>(dto.getUserName()+" already used", HttpStatus.ALREADY_REPORTED);
        }
        Long userId = userRepository.save(new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getUserName(),
                dto.getRole(),
                dto.getEmail(),
                dto.getMobile(),
                dto.getPassword())).getUserId();

        logger.debug("user successfully created.. ID:"+userId);

        dto.setUserId(userId);
        this.synchronizeUser(dto);
        return new ResponseEntity<>(userId,HttpStatus.CREATED);
    }

    public ResponseEntity<?> findAllUsers(){
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            return new ResponseEntity<>("not found users",HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<>(users,HttpStatus.OK);
    }

    public ResponseEntity<?> findById(Long id){
        if (!userRepository.existsById(id)){
            return new ResponseEntity<>("not found users and check your input",HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findById(id).get();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    public ResponseEntity<?> UpdateUsers(UserDTO dto){
        logger.debug("start update user...");
        if (userRepository.existsById(dto.getUserId())){
            return new ResponseEntity<>("not found users and check your input",HttpStatus.NOT_FOUND);
        }
        User user = userRepository.save(new User(
                dto.getUserId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getUserName(),
                dto.getRole(),
                dto.getEmail(),
                dto.getMobile(),
                dto.getPassword()));

        logger.debug("user updated...");
        logger.debug("end update user...");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUsers(Long id){
        if (userRepository.existsById(id)){
            return new ResponseEntity<>("not found users and check your input",HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>(id+" deleted",HttpStatus.OK);
    }

    public boolean isUserByUsername(String userName){
        Boolean isExist = false;
        User user = userRepository.findByUserName(userName);
        if (user != null){
            isExist = true;
        }
        return isExist;
    }

    /**
     * This method use for user synchronize to customer.
     * @param dto
     */
    public void synchronizeUser(UserDTO dto){
        try {
            CustomerDTO dto1 = new CustomerDTO(dto.getUserId(), dto.getUserName(), dto.getEmail(), dto.getMobile());
            ResponseEntity<?> response = webClientBuilder.build()
                    .post()
                    .uri(url+"/customers")
                    .body(Mono.just(dto1), CustomerDTO.class)
                    .retrieve()
                    .bodyToMono(ResponseEntity.class)
                    .block();
           // System.out.println(response.getBody());
            logger.debug("customer successfully synchronized... ");
            //ResponseEntity<?> response = restTemplate.postForObject(url+"/customers",dto1,ResponseEntity.class);
        }catch (Exception e){
            logger.debug(e.getMessage());
        }

    }

    public User getUserByUserName(String userName) {
        if (!isUserByUsername(userName)){
            logger.debug("userName not found..");
            return null;
        }
        User user = userRepository.findByUserName(userName);
        return user;
    }
}