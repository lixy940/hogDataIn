package com.hog.bigdata.entity;

import java.util.Date;

public class SlaughterQuarantine {
    private Integer id;

    private Date quarantineDate;

    private Integer type;

    private String ownerName;

    private String weight;

    private String carNum;

    private String ownerPhone;

    private String unitPrice;

    private String producingProvince;

    private String producingCity;

    private String producingDistrict;

    private String destinationProvince;

    private String destinationCity;

    private String destinationDistrict;

    private String destinationTown;

    private Integer newFlag=1;

    private String townName;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String remarks;

    private String destinationDistrictCode;

    private String destinationTownCode;

    private String producingDistrictCode;

    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getQuarantineDate() {
        return quarantineDate;
    }

    public void setQuarantineDate(Date quarantineDate) {
        this.quarantineDate = quarantineDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum == null ? null : carNum.trim();
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone == null ? null : ownerPhone.trim();
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice == null ? null : unitPrice.trim();
    }

    public String getProducingProvince() {
        return producingProvince;
    }

    public void setProducingProvince(String producingProvince) {
        this.producingProvince = producingProvince == null ? null : producingProvince.trim();
    }

    public String getProducingCity() {
        return producingCity;
    }

    public void setProducingCity(String producingCity) {
        this.producingCity = producingCity == null ? null : producingCity.trim();
    }

    public String getProducingDistrict() {
        return producingDistrict;
    }

    public void setProducingDistrict(String producingDistrict) {
        this.producingDistrict = producingDistrict == null ? null : producingDistrict.trim();
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public void setDestinationProvince(String destinationProvince) {
        this.destinationProvince = destinationProvince == null ? null : destinationProvince.trim();
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity == null ? null : destinationCity.trim();
    }

    public String getDestinationDistrict() {
        return destinationDistrict;
    }

    public void setDestinationDistrict(String destinationDistrict) {
        this.destinationDistrict = destinationDistrict == null ? null : destinationDistrict.trim();
    }

    public String getDestinationTown() {
        return destinationTown;
    }

    public void setDestinationTown(String destinationTown) {
        this.destinationTown = destinationTown == null ? null : destinationTown.trim();
    }

    public Integer getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(Integer newFlag) {
        this.newFlag = newFlag;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName == null ? null : townName.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getDestinationDistrictCode() {
        return destinationDistrictCode;
    }

    public void setDestinationDistrictCode(String destinationDistrictCode) {
        this.destinationDistrictCode = destinationDistrictCode == null ? null : destinationDistrictCode.trim();
    }

    public String getDestinationTownCode() {
        return destinationTownCode;
    }

    public void setDestinationTownCode(String destinationTownCode) {
        this.destinationTownCode = destinationTownCode == null ? null : destinationTownCode.trim();
    }

    public String getProducingDistrictCode() {
        return producingDistrictCode;
    }

    public void setProducingDistrictCode(String producingDistrictCode) {
        this.producingDistrictCode = producingDistrictCode == null ? null : producingDistrictCode.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}