package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StatusDto implements Serializable {
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;
    private String attendNm;
    private String majorNm;
    private String deptNm;
    private String typeNm;
    private String attendHallCd;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date attendDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date attendTime;
    private String hallCd;
    private String headNm;
    private String bldgNm;
    private String hallNm;
    private Long examineeCnt;
    private Long currentCnt;
    private Long attendCnt;
    private Long otherHallCnt;
    private Long fromOtherCnt;
    private Long toOtherCnt;
    private Long absentCnt;
    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal attendPer;
    @JsonSerialize(using = PercentSerializer.class)
    private BigDecimal absentPer;
    private String groupNm;
    private Boolean isSend;
    private String attendCd;

    private String deviceId;
    private Long deviceNo;
    private Long paperCnt;
    private String firstPaperCd;
    private String lastPaperCd;
    private String paperList;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastDttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date signDttm;

    private Long paperCnt2;
    private String firstPaperCd2;
    private String lastPaperCd2;
    private String paperList2;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastDttm2;

    // examinee
    private String examineeCd;
    private String examineeNm;
    private String paperNo;
    private String paperCd;
    private Boolean isAttend;
    private Boolean isNoIdCard;
    private Boolean isChangePaper;
    private String attendHeadNm;
    private String attendBldgNm;
    private String attendHallNm;
    private Boolean isOtherHall;
    private Boolean isEtc;
    private String birth;
    private Boolean isCheck;
    private Boolean isSignature;

    // 검색용
    private String fromExamineeCd;
    private String toExamineeCd;


    private String uuid;
    private String phoneNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date logDttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date sendDttm;

    private String attendDT;
    private BufferedImage examineeImage;

    private Boolean isNoIdCardFilter;
    private Boolean isCheckFilter;

    private Boolean isCancel;
    private Boolean isExamineeCdScanner;
    private Boolean isPaperCdScanner;

    private Long cnt;
    private String actionCnt;
    private String duplicateCnt;
}