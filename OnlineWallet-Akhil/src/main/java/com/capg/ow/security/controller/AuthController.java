package com.capg.ow.security.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capg.ow.entity.BankAccount;
import com.capg.ow.entity.OnlineWallet;
import com.capg.ow.security.exception.AppException;
import com.capg.ow.security.model.entity.Role;
import com.capg.ow.security.model.entity.RoleName;
import com.capg.ow.security.model.entity.User;
import com.capg.ow.security.model.payload.ApiResponse;
import com.capg.ow.security.model.payload.SignInRequest;
import com.capg.ow.security.model.payload.SignUpRequest;
import com.capg.ow.security.repository.RoleRepository;
import com.capg.ow.security.repository.UserRepository;
import com.capg.ow.security.util.JwtUtil;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {
	
	@Autowired
    RestTemplate restTemplate;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
    RoleRepository roleRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value= "/welcomeuser")
	public ResponseEntity<String> welcomeUser() {
		return new ResponseEntity<String>("Welcome User!", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value= "/welcomeadmin")
	public ResponseEntity<String> welcomeAdmin() {
		return new ResponseEntity<String>("Welcome Admin!", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value= "/allusers")
	public List<User> viewAllUsers() {
		return repository.findAll();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("deleteuser/{id}")
	public ResponseEntity<Boolean> deleteBooking(@PathVariable int id){
		repository.deleteById(id);
		ResponseEntity<Boolean> responseEntity = new ResponseEntity(true,HttpStatus.OK);
		return responseEntity;
	}

	@PostMapping("/signin")
	public String generateToken(@Valid @RequestBody SignInRequest user){
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		user.getUsernameOrEmail(),
                		user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateToken(authentication);
        return jwt;
    }
	
	 @PostMapping("/signup")
	    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		 
		 if(repository.existsByUsername(signUpRequest.getUsername())) {
	            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
	                    HttpStatus.BAD_REQUEST);
	        }

	        if(repository.existsByEmail(signUpRequest.getEmail())) {
	            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
	                    HttpStatus.BAD_REQUEST);
	        }

	        // Creating user's account
	        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
	                signUpRequest.getEmail(), signUpRequest.getPassword() , signUpRequest.getDob() , signUpRequest.getPhno());

	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new AppException("User Role not set."));

	        user.setRoles(Collections.singleton(userRole));

	        User result = repository.save(user);
	        
	        OnlineWallet newWallet = new OnlineWallet();
	        newWallet.setCustomerUserName(signUpRequest.getName());
	        newWallet.setCustomerPassword(signUpRequest.getPassword());
	        newWallet.setEmailId(signUpRequest.getEmail());
	        newWallet.setPhno(signUpRequest.getPhno());
	        newWallet.setWalletBalance(0);
	        
	        BankAccount newBank = new BankAccount();
	        newBank.setAccountNumber(signUpRequest.getAccountNumber());
	        newBank.setHolderName(signUpRequest.getHolderName());
	        newBank.setIfscCode(signUpRequest.getIfscCode());
	        newBank.setAccountBalance(signUpRequest.getAccountBalance());
	        
	        newWallet.setBankAccount(newBank);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<OnlineWallet> entity = new HttpEntity<OnlineWallet>(newWallet,headers);
	        
	        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("http://localhost:8070/onlinewallet/createwallet", HttpMethod.POST, entity, Boolean.class);

	        System.out.println(responseEntity);
	        
	        URI location = ServletUriComponentsBuilder
	                .fromCurrentContextPath().path("/users/{username}")
	                .buildAndExpand(result.getUsername()).toUri();

	        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	    }
	 }