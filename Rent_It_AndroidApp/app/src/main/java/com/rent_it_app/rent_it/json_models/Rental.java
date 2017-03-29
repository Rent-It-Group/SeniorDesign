package com.rent_it_app.rent_it.json_models;

/**
 * Created by malhan on 3/23/17.
 */

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rental implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("rental_id")
    @Expose
    private String rentalId;
    @SerializedName("renter")
    @Expose
    private String renter;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("item")
    @Expose
    //private String item;
    private Item item;
    @SerializedName("rental_status")
    @Expose
    private Integer rentalStatus;
    @SerializedName("booked_start_date")
    @Expose
    private String bookedStartDate;
    @SerializedName("booked_end_date")
    @Expose
    private String bookedEndDate;
    @SerializedName("booked_period")
    @Expose
    private Long bookedPeriod;
    @SerializedName("estimated_total")
    @Expose
    private Double estimatedTotal;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("rental_started_date")
    @Expose
    private String rentalStartedDate;
    @SerializedName("rental_end_date")
    @Expose
    private String rentalEndDate;
    @SerializedName("payment_status")
    @Expose
    private Long paymentStatus;
    @SerializedName("daily_rate")
    @Expose
    private Double dailyRate;
    @SerializedName("rental_period")
    @Expose
    private Double rentalPeriod;
    @SerializedName("service_fee")
    @Expose
    private Double serviceFee;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("total")
    @Expose
    private Double total;
    private final static long serialVersionUID = -978368248115006438L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Rental() {
    }

    /**
     *
     * @param total
     * @param rentalStatus
     * @param serviceFee
     * @param renter
     * @param dailyRate
     * @param image
     * @param paymentStatus
     * @param bookedStartDate
     * @param rentalStartedDate
     * @param id
     * @param tax
     * @param item
     * @param owner
     * @param bookedPeriod
     * @param rentalId
     * @param rentalPeriod
     * @param bookedEndDate
     * @param rentalEndDate
     * @param estimatedTotal
     */
    public Rental(String id, String rentalId, String renter, String owner, /*String item*/Item item, Integer rentalStatus, String bookedStartDate, String bookedEndDate, Long bookedPeriod, Double estimatedTotal, String image, String rentalStartedDate, String rentalEndDate, Long paymentStatus, Double dailyRate, Double rentalPeriod, Double serviceFee, Double tax, Double total) {
        super();
        this.id = id;
        this.rentalId = rentalId;
        this.renter = renter;
        this.owner = owner;
        this.item = item;
        this.rentalStatus = rentalStatus;
        this.bookedStartDate = bookedStartDate;
        this.bookedEndDate = bookedEndDate;
        this.bookedPeriod = bookedPeriod;
        this.estimatedTotal = estimatedTotal;
        this.image = image;
        this.rentalStartedDate = rentalStartedDate;
        this.rentalEndDate = rentalEndDate;
        this.paymentStatus = paymentStatus;
        this.dailyRate = dailyRate;
        this.rentalPeriod = rentalPeriod;
        this.serviceFee = serviceFee;
        this.tax = tax;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /*public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }*/

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(Integer rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public String getBookedStartDate() {
        return bookedStartDate;
    }

    public void setBookedStartDate(String bookedStartDate) {
        this.bookedStartDate = bookedStartDate;
    }

    public String getBookedEndDate() {
        return bookedEndDate;
    }

    public void setBookedEndDate(String bookedEndDate) {
        this.bookedEndDate = bookedEndDate;
    }

    public Long getBookedPeriod() {
        return bookedPeriod;
    }

    public void setBookedPeriod(Long bookedPeriod) {
        this.bookedPeriod = bookedPeriod;
    }

    public Double getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(Double estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRentalStartedDate() {
        return rentalStartedDate;
    }

    public void setRentalStartedDate(String rentalStartedDate) {
        this.rentalStartedDate = rentalStartedDate;
    }

    public String getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(String rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public Long getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Long paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Double getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(Double rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}