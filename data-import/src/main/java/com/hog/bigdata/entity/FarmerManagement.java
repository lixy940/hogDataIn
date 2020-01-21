package com.hog.bigdata.entity;

import java.util.Date;

public class FarmerManagement {
    private Integer id;

    private String farmerName;

    private String phone;

    private String addressPcdt;

    private String addressDetails;

    private String province;

    private String city;

    private String district;

    private String town;

    private Integer pigCunlan;

    private Integer newFlag=1;

    private String townName;

    private String createBy;

    private String createOffice;

    private Date createDate;

    private Date updateDate;

    private String updateBy;

    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName == null ? null : farmerName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddressPcdt() {
        return addressPcdt;
    }

    public void setAddressPcdt(String addressPcdt) {
        this.addressPcdt = addressPcdt == null ? null : addressPcdt.trim();
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails == null ? null : addressDetails.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim();
    }

    public Integer getPigCunlan() {
        return pigCunlan;
    }

    public void setPigCunlan(Integer pigCunlan) {
        this.pigCunlan = pigCunlan;
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

    public String getCreateOffice() {
        return createOffice;
    }

    public void setCreateOffice(String createOffice) {
        this.createOffice = createOffice == null ? null : createOffice.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}