package com.rent_it_app.rent_it.json_models;

/**
 * Created by malhan on 3/22/17.
 */

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("braintree_customer_id")
    @Expose
    private String braintreeCustomerId;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    private final static long serialVersionUID = -8081711464658773400L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param id
     * @param lastName
     * @param braintreeCustomerId
     * @param email
     * @param fcmToken
     * @param firstName
     * @param displayName
     */
    public User(String id, String email, String displayName, String firstName, String lastName, String braintreeCustomerId, String fcmToken) {
        super();
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.braintreeCustomerId = braintreeCustomerId;
        this.fcmToken = fcmToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBraintreeCustomerId() {
        return braintreeCustomerId;
    }

    public void setBraintreeCustomerId(String braintreeCustomerId) {
        this.braintreeCustomerId = braintreeCustomerId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
