package com.capg.ow.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.ow.entity.BankAccount;
import com.capg.ow.entity.OnlineWallet;
import com.capg.ow.security.exception.ResourceNotFoundException;
import com.capg.ow.service.OnlineWalletService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/onlinewallet" })
public class OnlineWalletRestController {

	@Autowired
	OnlineWalletService walletService;
	
	@PostMapping("/createwallet")
	public ResponseEntity<Boolean> createOnlineWallet(@Valid @RequestBody OnlineWallet details) {
		walletService.createOnlineWallet(details);
		ResponseEntity<Boolean> responseEntity = new ResponseEntity(true,HttpStatus.OK);
		return responseEntity;
	}
	
	@PostMapping("/addbankaccount")
	public ResponseEntity<Boolean> addBankAccount(@Valid @RequestBody BankAccount details) {
		walletService.addBankAccount(details);
		ResponseEntity<Boolean> responseEntity = new ResponseEntity(true,HttpStatus.OK);
		return responseEntity;
	}
	
	@GetMapping("getwallet/{id}")
	public Optional<OnlineWallet> getOnlineWallet(@PathVariable int id){
	
		Optional<OnlineWallet> onlineWallet = walletService.findOnlineWallet(id);
		if(onlineWallet==null) {
			throw new ResourceNotFoundException("OnlineWallet","id",id);
		}
		return onlineWallet;
	}
	
	@PutMapping("/updatewallet")
	public ResponseEntity<Boolean> updateOnlineWallet(@Valid @RequestBody OnlineWallet newWallet) {
		walletService.updateOnlineWallet(newWallet);
		ResponseEntity<Boolean> responseEntity = new ResponseEntity(true,HttpStatus.OK);
		return responseEntity;
	}
	
	@PutMapping("/addamount/{id}/{amount}")
	public ResponseEntity<String> addAmount(@PathVariable int id,@PathVariable int amount) {
		OnlineWallet response = walletService.addAmount(id,amount);
		if(response!=null)
		return new ResponseEntity("Amount of Rs."+amount+" successfully added to "+id,HttpStatus.OK);
		else
		return new ResponseEntity(false,HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/sendamount/{id1}/{id2}/{amount}")
	public ResponseEntity<String> sendAmount(@PathVariable int id1,@PathVariable int id2,@PathVariable int amount) {
		OnlineWallet response = walletService.transferAmount(id1,id2,amount);
		if(response!=null)
		return new ResponseEntity("Amount successfully transferred from "+id1+" to "+id2,HttpStatus.OK);
		else
		return new ResponseEntity(false,HttpStatus.BAD_REQUEST);
	}
}
