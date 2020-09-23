package com.capg.ow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.ow.entity.OnlineWallet;


@Repository
public interface OnlineWalletRepository extends JpaRepository<OnlineWallet,Integer>{
	
}
