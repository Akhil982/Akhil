package com.capg.ow.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.ow.entity.BankAccount;
import com.capg.ow.entity.OnlineWallet;
import com.capg.ow.entity.Transaction;
import com.capg.ow.repository.BankAccountRepository;
import com.capg.ow.repository.OnlineWalletRepository;
import com.capg.ow.repository.TransactionRepository;

@Service
public class OnlineWalletServiceImpl implements OnlineWalletService {
	
	@Autowired
	OnlineWalletRepository walletRepository;
	
	@Autowired
	BankAccountRepository bankAccountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public OnlineWallet createOnlineWallet(OnlineWallet details) {
		return walletRepository.save(details);
	}

	@Override
	public BankAccount addBankAccount(BankAccount details) {
		return bankAccountRepository.save(details);
	}

	@Override
	public Optional<OnlineWallet> findOnlineWallet(int id) {
		return walletRepository.findById(id);
	}

	@Override
	public OnlineWallet updateOnlineWallet(OnlineWallet newWallet) {
		return walletRepository.saveAndFlush(newWallet);
	}
	
	@Override
	public OnlineWallet addAmount(int id, int amount) {
		Optional<OnlineWallet> result = walletRepository.findById(id);
		OnlineWallet newWallet = new OnlineWallet();
		newWallet.setId(result.get().getId());
		newWallet.setCustomerUserName(result.get().getCustomerUserName());
		newWallet.setCustomerPassword(result.get().getCustomerPassword());
		newWallet.setEmailId(result.get().getEmailId());
		newWallet.setPhno(result.get().getPhno());
		newWallet.setWalletBalance(result.get().getWalletBalance()+amount);
		BankAccount account = new BankAccount();
		account.setId(result.get().getBankAccount().getId());
		account.setAccountNumber(result.get().getBankAccount().getAccountNumber());
		account.setHolderName(result.get().getBankAccount().getHolderName());
		account.setIfscCode(result.get().getBankAccount().getIfscCode());
		account.setAccountBalance(result.get().getBankAccount().getAccountBalance()-amount);
		newWallet.setBankAccount(account);
		
		walletRepository.saveAndFlush(newWallet);
		return newWallet;
	}

	@Override
	public OnlineWallet transferAmount(int id1, int id2, int amount) {
		Optional<OnlineWallet> wallet1 = walletRepository.findById(id1);
		Optional<OnlineWallet> wallet2 = walletRepository.findById(id2);
		
		OnlineWallet newWallet1 = new OnlineWallet();
		newWallet1.setId(wallet1.get().getId());
		newWallet1.setCustomerUserName(wallet1.get().getCustomerUserName());
		newWallet1.setCustomerPassword(wallet1.get().getCustomerPassword());
		newWallet1.setEmailId(wallet1.get().getEmailId());
		newWallet1.setPhno(wallet1.get().getPhno());
		newWallet1.setWalletBalance(wallet1.get().getWalletBalance()-amount);
		newWallet1.setBankAccount(wallet1.get().getBankAccount());
		
		OnlineWallet newWallet2 = new OnlineWallet();
		newWallet2.setId(wallet2.get().getId());
		newWallet2.setCustomerUserName(wallet2.get().getCustomerUserName());
		newWallet2.setCustomerPassword(wallet2.get().getCustomerPassword());
		newWallet2.setEmailId(wallet2.get().getEmailId());
		newWallet2.setPhno(wallet2.get().getPhno());
		newWallet2.setWalletBalance(wallet2.get().getWalletBalance()-amount);
		newWallet2.setBankAccount(wallet2.get().getBankAccount());
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = now.format(format);  
		
        List<Transaction> list = new ArrayList<Transaction>();
        Transaction transaction = new Transaction();
		transaction.setFromAccountId(newWallet1.getId());
		transaction.setToAccountId(newWallet2.getId());
		transaction.setDescription("Amount of Rs."+amount+" transferred from "+id1+" to "+id2+" on "+formatDateTime);
		transaction.setAmount(amount);
		transaction.setDateOfTransaction(now);
		list.add(transaction);
		
		newWallet1.setTransactions(list);
		newWallet2.setTransactions(list);
		
		
		
		OnlineWallet updatedWallet1 = walletRepository.save(newWallet1);
		OnlineWallet updatedWallet2 = walletRepository.save(newWallet2);
		
		
		transactionRepository.save(transaction);
		
		return updatedWallet1;
	}
		
}
