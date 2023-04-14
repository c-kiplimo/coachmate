package ke.natujenge.baked.service.util;

import ke.natujenge.baked.service.dto.*;

public class OnBoardBakerUtil {

    public static BakerNotificationSettingsDTO extractNotificationSettings(OnBoardBakerDTO onBoardBakerDTO, BakerDTO bakerDTO, BakerNotificationSettingsDTO bakerNotificationSettingsDTO){
        bakerNotificationSettingsDTO.setTillNumber(onBoardBakerDTO.getTillNumber());
        bakerNotificationSettingsDTO.setNewOrderTemplate(onBoardBakerDTO.getNewOrderTemplate());
        bakerNotificationSettingsDTO.setPartialOrderPaymentTemplate(onBoardBakerDTO.getPartialOrderPaymentTemplate());
        bakerNotificationSettingsDTO.setFullOrderPaymentTemplate(onBoardBakerDTO.getFullOrderPaymentTemplate());
        bakerNotificationSettingsDTO.setDeliverOrderTemplate(onBoardBakerDTO.getDeliverOrderTemplate());
        bakerNotificationSettingsDTO.setCancelOrderTemplate(onBoardBakerDTO.getCancelOrderTemplate());
        bakerNotificationSettingsDTO.setBaker(bakerDTO);

        return bakerNotificationSettingsDTO;
    }

    public static BakerLocationDTO extractLocationData(OnBoardBakerDTO onBoardBakerDTO, BakerDTO bakerDTO,BakerLocationDTO bakerLocationDTO){
        bakerLocationDTO.setCounty(onBoardBakerDTO.getCounty());
        bakerLocationDTO.setPhysicalAddress(onBoardBakerDTO.getPhysicalAddress());
        bakerLocationDTO.setPostalAddress(onBoardBakerDTO.getPostalAddress());
        bakerLocationDTO.setBaker(bakerDTO);

        return bakerLocationDTO;
    }

    public static BakerPaymentDetailsDTO extractPaymentData(OnBoardBakerDTO onBoardBakerDTO, BakerDTO bakerDTO, BakerPaymentDetailsDTO bakerPaymentDetailsDTO){
        bakerPaymentDetailsDTO.setPaymentType(onBoardBakerDTO.getPaymentType());
        bakerPaymentDetailsDTO.setMpesaPaymentType(onBoardBakerDTO.getMpesaPaymentType());
        bakerPaymentDetailsDTO.setTillNumber(onBoardBakerDTO.getTillNumber());
        bakerPaymentDetailsDTO.setAccountNumber(onBoardBakerDTO.getAccountNumber());
        bakerPaymentDetailsDTO.setMsisdn(onBoardBakerDTO.getMsisdn());
        bakerPaymentDetailsDTO.setBusinessNumber(onBoardBakerDTO.getBusinessNumber());
        bakerPaymentDetailsDTO.setBaker(bakerDTO);

        return bakerPaymentDetailsDTO;
    }

    public static BakerSettingsDTO extractSettingsData(OnBoardBakerDTO onBoardBakerDTO, BakerDTO bakerDTO, BakerSettingsDTO bakerSettingsDTO){
        bakerSettingsDTO.setLogo(onBoardBakerDTO.getFilename());
        bakerSettingsDTO.setBaker(bakerDTO);

        return bakerSettingsDTO;
    }
}

