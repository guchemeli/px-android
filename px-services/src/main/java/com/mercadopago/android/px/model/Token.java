package com.mercadopago.android.px.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mercadopago.android.px.internal.util.TextUtil;
import java.util.Date;

public class Token implements CardInformation {

    private String id;
    private String publicKey;
    private String cardId;
    private String luhnValidation;
    private String status;
    private String usedDate;
    private Integer cardNumberLength;
    private Date creationDate;
    private String truncCardNumber;
    private Integer securityCodeLength;
    private Integer expirationMonth;
    private Integer expirationYear;
    private Date lastModifiedDate;
    private Date dueDate;
    private String firstSixDigits;
    private String lastFourDigits;
    private Cardholder cardholder;
    private String esc;

    @Override
    public Integer getSecurityCodeLength() {
        return securityCodeLength;
    }

    @Override
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    @Override
    public Integer getExpirationYear() {
        return expirationYear;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getCardId() {
        return cardId;
    }

    public String getLuhnValidation() {
        return luhnValidation;
    }

    public String getStatus() {
        return status;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public Integer getCardNumberLength() {
        return cardNumberLength;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getTruncCardNumber() {
        return truncCardNumber;
    }

    public String getId() {
        return id;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setSecurityCodeLength(Integer securityCodeLength) {
        this.securityCodeLength = securityCodeLength;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLuhnValidation(String luhnValidation) {
        this.luhnValidation = luhnValidation;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardNumberLength(Integer cardNumberLength) {
        this.cardNumberLength = cardNumberLength;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setTruncCardNumber(String truncCardNumber) {
        this.truncCardNumber = truncCardNumber;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String getFirstSixDigits() {
        return firstSixDigits;
    }

    @Override
    public String getLastFourDigits() {
        return lastFourDigits;
    }

    @Override
    public Cardholder getCardHolder() {
        return cardholder;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setFirstSixDigits(final String firstSixDigits) {
        this.firstSixDigits = firstSixDigits;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setLastFourDigits(final String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setCardholder(final Cardholder cardholder) {
        this.cardholder = cardholder;
    }

    public String getEsc() {
        return esc;
    }

    /**
     * @deprecated should be non-mutable DTO
     */
    @Deprecated
    public void setEsc(final String esc) {
        this.esc = esc;
    }

    /**
     * @return json string
     * @deprecated Should not be it's responsibility - will delete it.
     */
    @Deprecated
    public String toJson() {
        Gson gson =
            new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        return gson.toJson(this);
    }

    public boolean isTokenValid() {
        return getLastFourDigits() != null && !TextUtil.isEmpty(getLastFourDigits());
    }
}