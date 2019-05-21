package com.softwaregiants.careertinder.utilities;

public interface Constants {
    String BASE_URL = "http://192.168.1.109:8080/" ;

    //API Methods
    String API_METHOD_LOGIN = "login";
    String API_METHOD_SIGNUP = "sign_up";
    String API_METHOD_ADD_NEW_JOB_OPENING = "add_job_opening";

    //Success Codes
    String SC_JOB_CREATED_SUCCESS = "job_opening_created";
    String SC_SUCCESS = "job_opening_created";

    //Messages
    String MSG_NETWORK_ERROR = "There is a technical error. Please try again.";
    String MSG_ERROR = "Error";

    //Preference Keys
    String PK_AUTH_CODE = "PK_AUTH_CODE";
    String PK_LOGIN_STATE = "PK_LOGIN_STATE";
    String PK_USER_TYPE = "PK_USER_TYPE";

    //Misc
    String USER_TYPE_JOB_SEEKER = "jobseeker";
    String USER_TYPE_EMPLOYER = "employer";
}
