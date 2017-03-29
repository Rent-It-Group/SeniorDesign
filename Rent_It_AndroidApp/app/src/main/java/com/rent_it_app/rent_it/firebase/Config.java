package com.rent_it_app.rent_it.firebase;

/**
 * Created by Nagoya on 2/16/17.
 */

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String INSTANCE_ID_TOKEN_RETRIEVED = "iid_token_retrieved";
    public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";

    //Chat,Inventory
    public static final String EXTRA_DATA = "extraData";
    public static final String MORE_DATA = "moreData";
    public static final String THIS_RENTAL = "thisRental";

}
