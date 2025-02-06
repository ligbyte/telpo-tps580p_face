package com.stkj.cashier.cbgfacepass.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 人脸用户数据
 */
@Entity
public class FacePassPeopleInfo {

    @Id(autoincrement = true)
    private Long id;
    private String Unique_number;
    private Integer Card_state;
    private String UID;
    private String Full_Name;
    private String DepNameType;
    private String IdentityCard;
    private String Phone;

    @Unique
    private String UserNumber;
    private String Card_Number;
    private String CardType;
    private double Balance;
    private String Opening_date;
    private Integer limitTimes1;
    private Integer limitTimes2;
    private Integer limitTimes3;
    private Integer limitTimes4;
    private double Consumption_quota;
    private String imgData;
    private String AccountType;
    private Boolean callBack;

    //旷视人脸识别face token
    private String CBGFaceToken;
    private int CBGCheckFaceResult;

    public FacePassPeopleInfo() {
    }

    @Generated(hash = 432542799)
    public FacePassPeopleInfo(Long id, String Unique_number, Integer Card_state,
            String UID, String Full_Name, String DepNameType, String IdentityCard,
            String Phone, String UserNumber, String Card_Number, String CardType,
            double Balance, String Opening_date, Integer limitTimes1,
            Integer limitTimes2, Integer limitTimes3, Integer limitTimes4,
            double Consumption_quota, String imgData, String AccountType,
            Boolean callBack, String CBGFaceToken, int CBGCheckFaceResult) {
        this.id = id;
        this.Unique_number = Unique_number;
        this.Card_state = Card_state;
        this.UID = UID;
        this.Full_Name = Full_Name;
        this.DepNameType = DepNameType;
        this.IdentityCard = IdentityCard;
        this.Phone = Phone;
        this.UserNumber = UserNumber;
        this.Card_Number = Card_Number;
        this.CardType = CardType;
        this.Balance = Balance;
        this.Opening_date = Opening_date;
        this.limitTimes1 = limitTimes1;
        this.limitTimes2 = limitTimes2;
        this.limitTimes3 = limitTimes3;
        this.limitTimes4 = limitTimes4;
        this.Consumption_quota = Consumption_quota;
        this.imgData = imgData;
        this.AccountType = AccountType;
        this.callBack = callBack;
        this.CBGFaceToken = CBGFaceToken;
        this.CBGCheckFaceResult = CBGCheckFaceResult;
    }

    public String getUnique_number() {
        return Unique_number;
    }

    public void setUnique_number(String unique_number) {
        Unique_number = unique_number;
    }

    public Integer getCard_state() {
        return Card_state;
    }

    public void setCard_state(Integer card_state) {
        Card_state = card_state;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getDepNameType() {
        return DepNameType;
    }

    public void setDepNameType(String depNameType) {
        DepNameType = depNameType;
    }

    public String getIdentityCard() {
        return IdentityCard;
    }

    public void setIdentityCard(String identityCard) {
        IdentityCard = identityCard;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }

    public String getCard_Number() {
        return Card_Number;
    }

    public void setCard_Number(String card_Number) {
        Card_Number = card_Number;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getOpening_date() {
        return Opening_date;
    }

    public void setOpening_date(String opening_date) {
        Opening_date = opening_date;
    }

    public String getImgData() {
        return imgData;
    }

    public void setImgData(String imgData) {
        this.imgData = imgData;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCBGFaceToken() {
        return this.CBGFaceToken;
    }

    public void setCBGFaceToken(String CBGFaceToken) {
        this.CBGFaceToken = CBGFaceToken;
    }

    public int getCBGCheckFaceResult() {
        return this.CBGCheckFaceResult;
    }

    public void setCBGCheckFaceResult(int CBGCheckFaceResult) {
        this.CBGCheckFaceResult = CBGCheckFaceResult;
    }

    public double getBalance() {
        return this.Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getConsumption_quota() {
        return this.Consumption_quota;
    }

    public void setConsumption_quota(double Consumption_quota) {
        this.Consumption_quota = Consumption_quota;
    }

    public Boolean getCallBack() {
        return this.callBack;
    }

    public void setCallBack(Boolean callBack) {
        this.callBack = callBack;
    }

    public Integer getLimitTimes1() {
        return limitTimes1;
    }

    public void setLimitTimes1(Integer limitTimes1) {
        this.limitTimes1 = limitTimes1;
    }

    public Integer getLimitTimes2() {
        return limitTimes2;
    }

    public void setLimitTimes2(Integer limitTimes2) {
        this.limitTimes2 = limitTimes2;
    }

    public Integer getLimitTimes3() {
        return limitTimes3;
    }

    public void setLimitTimes3(Integer limitTimes3) {
        this.limitTimes3 = limitTimes3;
    }

    public Integer getLimitTimes4() {
        return limitTimes4;
    }

    public void setLimitTimes4(Integer limitTimes4) {
        this.limitTimes4 = limitTimes4;
    }
}
