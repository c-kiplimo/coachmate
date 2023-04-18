package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.enums.WalletStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CoachWalletDTO {

    private Long id;
    private float currentBalance;
    private float reservedBalance;
    private float creditLimit;
    private WalletStatus status;
    private String mac;
    private String currency;
    private LocalDateTime lastTrxTime;
    private Long lastTrxId;
    private String code;
    private Long coachId;

    private CoachDTO coach;


}
