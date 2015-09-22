package com.fourlines.data;

public class Var {

    public static final String MY_PREFERENCES = "PRE_LOGIN";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FULLNAME = "fullname";

    public static final String ID = "_id";
    public static final String SICK_NAME = "name";
    public static final String SICK_TYPE = "type";
    public static final String SICK_REASON = "reason";
    public static final String SICK_FOODS = "foods";
    public static final String SICK_BAN_FOODS = "banfoods";
    public static final String SICK_SYMPTOMS = "symptoms";
    public static final String SICKS = "sicks";

    public static final String URL_HOST = "http://54.148.158.218:8888";
    public static final String URL_LOGIN = URL_HOST + "/api/v1/login";
    public static final String URL_REGISTER = URL_HOST + "/api/v1/register";
    public static final String URL_GET_ALL_SICK = URL_HOST + "/api/v1/sicks";
    public static final String URL_GET_SICK_BY_NAME = URL_HOST + "/api/v1/sicks/name?sickName=";
    public static final String URL_GET_SICK_BY_TYPE = URL_HOST + "/api/v1/sicks/type?sickType=";
    public static final String URL_GET_DATA_ACCOUNT = URL_HOST + "/api/v1/user";
    public static final String URL_LOGOUT = URL_HOST + "/api/v1/logout";

    public static final String SICK_KEY = "SICK_KEY";
    public static final String SICK_TYPE_KEY = "SICK_TYPE_KEY";
    public static final String DOCTOR_KEY = "DOCTOR_KEY";
    public static final String[] SICKTYPE = {"Hô Hấp", "Tuần Hoàn", "Tiêu Hóa", "Tiết Niệu", "Xương Khớp", "Thần Kinh"};


}
