package ua.kiev.foxtrot.kopilochka;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Const {
    public static final String BACKGROUND_SERVICE_BATTERY_CONTROL = new String("battery_service");
    public static final String BEER_DOWNLOAD_PROGRESS = new String("beer_download_progress");
    public static final String myAppPrefs = "myAppPrefs";

    public static final String BBS_NEWS_API_KEY = "a463fd41d3e94d9b8237df34320e6b9d";
    public static final String BBS_NEWS_API_PATH = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=";
    public static final String API_PATH = "http://dev.f5.ua/api/kopilochka/";


    public static final String TAG = "kopilochka";
    public static final String TAG_JSON = "TAG_JSON";
    //Preferences saves
    public static final String SAVED_LOG = "string_init";
    public static final String SAVED_PAS = "string_complete";
    public static final String SAVED_SES = "string_disabled";
    public static final String SAVED_NAME = "string_name";
    public static final String SAVED_EMAIL = "string_email";
    public static final String SAVED_PHONE = "string_phone";

    public static final String Fr_StP1 = "Fr_StP1";
    public static final String Fr_StP2 = "Fr_StP2";
    public static final String Fr_NtP1 = "Fr_NtP1";
    public static final String Fr_AcP1 = "Fr_AcP1";
    public static final String Fr_AcP2 = "Fr_AcP2";
    public static final String Fr_AcP3 = "Fr_AcP3";
    public static final String Fr_HsP1 = "Fr_HsP1";
    public static final String Fr_DtP1 = "Fr_DtP1";
    public static final String Fr_DtPL = "Fr_DtPL";
    public static final String Fr_DtPE = "Fr_DtPE";
    public static final String Fr_WtP1 = "Fr_WtP1";
    public static final String Fr_Scan = "Fr_Scan";

    //GetSession
    public static final int getSession = 1;
    public static final String GetSession = "GetToken";
    //-->in
    public static final String method = "method";
    public static final String login = "login";
    public static final String password = "password";
    //-->out
    public static final String session = "session";
    //public static final String session_id = "session_id";
    //public static final String hash = "hash";
    public static final String active = "active";
    public static final String user_name = "user_name";
    public static final String user_phone = "user_phone";
    public static final String user_email = "user_email";
    //---Error codes


    //GetNotices
    public static final int getNotices = 2;
    public static final String GetNotices = "GetNotices";
    //-->in
    //public static final String session = "session";
    //-->out
    public static final String notices = "notices";
    public static final String notice_id = "notice_id";
    public static final String notice_name = "notice_name";
    //public static final String notice_date_from = "notice_date_from";
    //public static final String notice_date_to = "notice_date_to";
    public static final String notice_text = "notice_text";
    //public static final String notice_image = "notice_image";
    public static final String notice_type_id = "notice_type_id";
    public static final String notice_type = "notice_type";


    //GetActions
    public static final int getActions = 3;
    public static final String GetActions = "GetActions";
    //-->in
    //public static final String session = "session";
    //--- actions
    public static final String actions = "actions";
    public static final String action_id = "action_id";
    public static final String action_name = "action_name";
    public static final String action_type_id = "action_type_id";
    public static final String action_type = "action_type";
    public static final String action_date_from = "action_date_from";
    public static final String action_date_to = "action_date_to";
    public static final String action_date_charge = "action_date_charge";
    public static final String action_description = "action_description";
    //--- models
    public static final String models = "models";
    public static final String model_id = "model_id";
    public static final String model_name = "model_name";
    public static final String model_points = "model_points";
    public static final String model_brand_id = "model_brand_id";
    public static final String model_group_id = "model_group_id";
    public static final String model_url = "model_url";
    public static final String model_brand_name = "model_brand_name";
    public static final String model_group_name = "model_group_name";
    public static final String model_sn_count = "model_sn_count";
    public static final String model_action = "model_action";
    public static final String models_count = "models_count";

    //PostSN
    public static final int postSN = 4;
    public static final String PostSN = "PostSN";
    //-->in
    //public static final String session = "session";
    //public static final String action_id = "action_id";
    //public static final String model_id = "model_id";
    public static final String data = "data";
    public static final String serials = "serials";
    //public static final String sn = "sn";
    public static final String reg_date = "reg_date";
    public static final String reg_status = "reg_status";
    public static final String fail_reason = "fail_reason";
    //-->out
    //public static final String 0 = "0"
    //public static final String action_id = "action_id";
    //public static final String model_id = "model_id";
    //public static final String serials = "serials";
    //public static final String sn = "sn";

    //GetFinInfo
    public static final int getFinInfo = 5;
    public static final String GetFinInfo = "GetFinInfo";
    //->in
    //public static final String session = "session";
    //-->out
//    public static final String user_name = "user_name";
//    public static final String user_phone = "user_phone";
//    public static final String user_email = "user_email";
    public static final String user_payment = "user_payment";
    public static final String charges = "charges";
    public static final String action_charge = "action_charge";
    public static final String date_charge = "date_charge";
    public static final String amount_charge = "amount_charge";
    public static final String payments = "payments";
    public static final String action_payment = "action_payment";
    public static final String date_payment = "date_payment";
    public static final String amount_payment = "amount_payment";
    public static final String comment_payment = "comment_payment";

    //PostQuestion
    public static final int postQuestion = 6;
    public static final String PostQuestion = "PostQuestion";
    //-->in
    //public static final String session = "session";
    public static final String fio = "fio";
    public static final String email = "email";
    public static final String question = "question";
    //-->out

    //==================Errors==========================
    public static final String JSON_Error = "error";
    public static final String JSON_Code = "code";
    public static final String JSON_Comment = "comment";
    public static final int JSON_Ok = -1;
}