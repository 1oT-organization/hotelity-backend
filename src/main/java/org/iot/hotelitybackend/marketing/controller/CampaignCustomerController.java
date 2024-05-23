package org.iot.hotelitybackend.marketing.controller;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.marketing.dto.CampaignCustomerDTO;
import org.iot.hotelitybackend.marketing.service.CampaignCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/marketing")
public class CampaignCustomerController {

    private final CampaignCustomerService campaignCustomerService;

    @Autowired
    public CampaignCustomerController(CampaignCustomerService campaignCustomerService) {
        this.campaignCustomerService = campaignCustomerService;
    }

    @GetMapping("/campaigns/{campaignSentCustomerCodePk}/campaign")
    public CampaignCustomerDTO selectCampaignByCampaignSentCustomerCodePk(@PathVariable int campaignSentCustomerCodePk) {
        return campaignCustomerService.selectCampaignByCampaignSentCustomerCodePk(campaignSentCustomerCodePk);
    }

    @GetMapping("/campaigns/search/page")
    public ResponseEntity<ResponseVO> selectSearchedCampaignCustomersList(
        @RequestParam(required = false) Integer campaignCodeFk,
        @RequestParam(required = false) String campaignSendType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime campaignSentDate,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String campaignTitle,
        @RequestParam(required = false) Integer campaignSentStatus,
        @RequestParam(required = false) Integer templateCodeFk,
        @RequestParam(required = false) String templateName,
        @RequestParam int pageNum
    ) {
        Map<String, Object> campaignPageInfo = campaignCustomerService.selectSearchedCampaignsList(
            pageNum, campaignCodeFk, campaignSendType, campaignSentDate
            , customerName, campaignTitle, campaignSentStatus,
            templateCodeFk, templateName
        );

        ResponseVO response = ResponseVO.builder()
            .data(campaignPageInfo)
            .resultCode(HttpStatus.OK.value())
            .build();

        return ResponseEntity.status(response.getResultCode()).body(response);
    }
}
