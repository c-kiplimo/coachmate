package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.repository.ClientWalletRepository;

import java.time.LocalDateTime;

public interface ClientWalletDto {
    Long getId();

    String getExtPaymentRef();

    Float getAmountDeposited();

    Float getWalletBalance();

    ModeOfPayment getModeOfPayment();

    String getDescription();

    PaymentCurrency getpaymentCurrency();


    LocalDateTime getCreatedAt();

    ClientView getClient();

    CoachView getCoach();

    OrganizationView getOrganization();

    // client details to send to coach
    public interface ClientView {
        Long getId();

        String getFirstName();

        String getMsisdn();
    }

    // coach details to send
    public interface CoachView {
        Long getId();

        String getBusinessName();
    }


    public interface OrganizationView {
        Long getId();

        String getOrgName();

    }
}
