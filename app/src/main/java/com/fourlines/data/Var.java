package com.fourlines.data;


import com.fourlines.doctor.R;

public class Var {

    public static final int PAGE = 20;

    public static final String MY_PREFERENCES = "PRE_LOGIN";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FULLNAME = "fullname";
    public static final String TOKEN = "token";
    public static final String AVATAR = "avatar";

    public static final String ID = "_id";
    public static final String SICK_NAME = "name";
    public static final String SICK_TYPE = "type";
    public static final String SICK_REASON = "reason";
    public static final String SICK_FOODS = "foods";
    public static final String SICK_BAN_FOODS = "banfoods";
    public static final String SICK_SYMPTOMS = "symptoms";
    public static final String SICK_TREATMENT = "treatment";
    public static final String SICK_DESCRIPTION = "description";
    public static final String SICK_PREVENTION = "prevention";
    public static final String NOTIF_TITLE = "title";
    public static final String NOTIF_TOPIC = "topic";
    public static final String NOTIF_CONTENT = "content";
    public static final String SICKS = "sicks";

    public static final String URL_HOST = "http://54.148.158.218:8888";
    public static final String URL_LOGIN = URL_HOST + "/api/v1/login";
    public static final String URL_REGISTER = URL_HOST + "/api/v1/register";
    public static final String URL_GET_ALL_SICK = URL_HOST + "/api/v1/sicks";
    public static final String URL_GET_SICK_BY_NAME = URL_HOST + "/api/v1/sicks/name?sickName=";
    public static final String URL_GET_SICK_BY_TYPE = URL_HOST + "/api/v1/sicks/type?sickType=";
    public static final String URL_GET_DATA_ACCOUNT = URL_HOST + "/api/v1/user";
    public static final String URL_LOGOUT = URL_HOST + "/api/v1/logout";
    public static final String URL_GET_TIPS = URL_HOST + "/api/v1/tips?page=";
    public static final String URL_SEND_QUESTION = URL_HOST + "/api/v1/question?question=";
    public static final String URL_SEND_SICK = URL_HOST + "/api/v1/sicks/push";
    public static final String URL_VALIDATE_TOKEN = URL_HOST + "/api/v1/token/valid";

    public static final String CHAT_STATUS = "status";
    public static final String CHAT_TYPE = "type";
    public static final String CHAT_DATA = "data";
    public static final String CHAT_RESULT = "result";


    public static final String SICK_KEY = "SICK_KEY";
    public static final String SICK_TYPE_KEY = "SICK_TYPE_KEY";
    public static final String[] SICKTYPE = {"Tuần hoàn", "Xương khớp", "Tiêu hóa", "Ngoài da", "Hô hấp", "Thần kinh"};

    public static int drawList[] = {R.drawable.avatar, R.drawable.avatar1, R.drawable.avatar2,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar3};
    public static final String[] TRUE = {"có", "vâng", "đúng", "chuẩn", "ừ", "hơi hơi", "thỉnh thoảng", "hay bị"};
    public static final String[] FALSE = {"không", "chưa"};
    public static final String[] QUESTION_RADOM = {"bạn có bị", "bạn bị", "bạn thấy", "bạn cảm thấy"};
    public static final String[] ANSWER_RADOM = {"Bạn đang nói gì vậy? Tôi chẳng hiểu", "tôi chẳng hiểu bạn đang nói gì",
            "Tôi không biết đâu", "Nói chuyện khác đi bạn"};

    public static final String INTERNET_CHECK = "internet_check";

    public static final String SICKNAMETOSERVER = "sickName";

}
