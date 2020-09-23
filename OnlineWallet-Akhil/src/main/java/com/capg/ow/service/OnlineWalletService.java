package com.capg.ow.service;

import java.util.Optional;

import com.capg.ow.entity.BankAccount;
import com.capg.ow.entity.OnlineWallet;

public interface OnlineWalletService {
	
	public OnlineWallet createOnlineWallet(OnlineWallet details);
	
	public BankAccount addBankAccount(BankAccount details);
	
	public Optional<OnlineWallet> findOnlineWallet(int id);

	public OnlineWallet updateOnlineWallet(OnlineWallet newWallet);
	
	public OnlineWallet addAmount(int id,int amount);

	public OnlineWallet transferAmount(int id1, int id2, int amount);
}
