package com.rent_it_app.rent_it.json_models;

/**
 * Created by Miz on 2/1/17.
 * http://www.jsonschema2pojo.org/
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("condition")
    @Expose
    private String condition;
    /*@SerializedName("location")
    @Expose
    private List<Double> location = null;*/
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zipcode")
    @Expose
    private Integer zipcode;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("visible")
    @Expose
    private Boolean visible;

    /*public Item()
    {
        id = "";
        uid = "";
        title = "";
        description = "";
        condition = "";
        location = new ArrayList<Double>();
        location.add(0.0);
        location.add(0.0);
        city = "";
        zipcode = 0;
        category = "";
        tags = new ArrayList<String>();
        tags.add("");
        value = 0.0;
        rate = 0.0;
        image = "";
        visible = false;
    }*/
    private final static long serialVersionUID = -3779499934553630833L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Item() {
    }

    /**
     *
     * @param tags
     * @param uid
     * @param visible
     * @param condition
     * @param zipcode
     * @param image
     * @param city
     * @param id
     * @param category
     * @param title
     * @param rate
     * @param description
     * @param value
     */
    public Item(String id, String category, String city, String condition, String description, String image, Double rate, String title, String uid, Double value, Boolean visible, Integer zipcode, List<String> tags) {
        super();
        this.id = id;
        this.category = category;
        this.city = city;
        this.condition = condition;
        this.description = description;
        this.image = image;
        this.rate = rate;
        this.title = title;
        this.uid = uid;
        this.value = value;
        this.visible = visible;
        this.zipcode = zipcode;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

   /* public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
*/
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String toString(){
        return getTitle();
    }
}
