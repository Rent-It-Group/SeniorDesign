package com.rent_it_app.rent_it.json_models;

/**
 * Created by malhan on 3/16/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Claim {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("meeting_date")
    @Expose
    private String meetingDate;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("submitted_by")
    @Expose
    private String submittedBy;
    @SerializedName("rental_id")
    @Expose
    private String rentalId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    /**
     * No args constructor for use in serialization
     *
     */
    public Claim() {
    }

    /**
     *
     * @param id
     * @param status
     * @param reason
     * @param issue
     * @param item
     * @param rentalId
     * @param image
     * @param role
     * @param submittedBy
     * @param createdDate
     * @param meetingDate
     */
    public Claim(String id, Integer role, String meetingDate, String item, String reason, String image, String issue, Integer status, String submittedBy, String rentalId, String createdDate) {
        super();
        this.id = id;
        this.role = role;
        this.meetingDate = meetingDate;
        this.item = item;
        this.reason = reason;
        this.image = image;
        this.issue = issue;
        this.status = status;
        this.submittedBy = submittedBy;
        this.rentalId = rentalId;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}