package com.capg.ow.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Entity
@Table(name = "onlinewallets")
public class OnlineWallet {
	
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int id;

@NotEmpty(message = "first name must not be empty")
@Size(min = 5, message = "Name should have atleast 5 characters")
@Column(name = "customer_username", nullable = false)
private String customerUserName;

@NotEmpty(message = "first name must not be empty")
@Size(min = 5, message = "Password should have atleast 6 characters")
@Column(name = "customer_password", nullable = false)
private String customerPassword;

@NotEmpty(message = "email must not be empty")
@Email(message = "email should be valid")
@Column(name = "email_address", nullable = false)
private String emailId;

@Pattern(regexp="(^$|[0-9]{10})" , message = "phone number should be valid")
@Column(name = "phone_number", nullable = false)
private String phno;

private int walletBalance;

@OneToOne(cascade=CascadeType.ALL)
@JoinColumn(name="bank_FK")
private BankAccount bankAccount;

@OneToMany(cascade = CascadeType.ALL)
private List<Transaction> transactions;

public OnlineWallet() {
	
}

public OnlineWallet(int id, String customerUserName, String customerPassword,String phno,int walletBalance) {
	this.id = id;
	this.customerUserName = customerUserName;
	this.customerPassword = customerPassword;
	this.phno = phno;
	this.walletBalance = walletBalance;
}


public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getCustomerUserName() {
	return customerUserName;
}

public String getPhno() {
	return phno;
}

public void setPhno(String phno) {
	this.phno = phno;
}

public String getEmailId() {
	return emailId;
}

public void setEmailId(String emailId) {
	this.emailId = emailId;
}

public BankAccount getBankAccount() {
	return bankAccount;
}

public void setBankAccount(BankAccount bankAccount) {
	this.bankAccount = bankAccount;
}

public void setCustomerUserName(String customerUserName) {
	this.customerUserName = customerUserName;
}

public String getCustomerPassword() {
	return customerPassword;
}

public void setCustomerPassword(String customerPassword) {
	this.customerPassword = customerPassword;
}

public int getWalletBalance() {
	return walletBalance;
}

public void setWalletBalance(int walletBalance) {
	this.walletBalance = walletBalance;
}

public List<Transaction> getTransactions() {
	return transactions;
}

public void setTransactions(List<Transaction> transactions) {
	this.transactions = transactions;
}

}