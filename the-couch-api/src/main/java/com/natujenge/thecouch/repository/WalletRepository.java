package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<ClientWallet, Long> {
}
