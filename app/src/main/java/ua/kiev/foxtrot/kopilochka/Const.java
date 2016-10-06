package ua.kiev.foxtrot.kopilochka;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Const {
    public static final String BACKGROUND_SERVICE_BATTERY_CONTROL = new String("battery_service");
    public static final String BEER_DOWNLOAD_PROGRESS = new String("beer_download_progress");

    public static final String BBS_NEWS_API_KEY = "a463fd41d3e94d9b8237df34320e6b9d";
    public static final String BBS_NEWS_API_PATH = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=";


    public static final String TAG = "kopilochka";
    public static final String TAG_JSON = "TAG_JSON";


    public static final String Fr_StP1 = "Fr_StP1";
    public static final String Fr_NtP1 = "Fr_NtP1";
    public static final String Fr_AcP1 = "Fr_AcP1";
    public static final String Fr_HsP1 = "Fr_HsP1";
    public static final String Fr_DtP1 = "Fr_DtP1";
    public static final String Fr_WtP1 = "Fr_WtP1";
    public static final String Fr_Scan = "Fr_Scan";

    //GetToken
    public static final int GetToken = 1;
    //-->in
    public static final String user = "user";
    public static final String password = "password";
    //-->out
    public static final String session = "session";
    public static final String active = "active";
    public static final String user_name = "user_name";
    public static final String user_phone = "user_phone";
    public static final String user_email = "user_email";
    //---Error codes


    //GetNotices
    public static final int GetNotices = 2;
    //-->in
    //public static final String session = "session";
    //-->out
    public static final String notice_id = "notice_id";
    public static final String notice_name = "notice_name";
    public static final String notice_date_from = "notice_date_from";
    public static final String notice_date_to = "notice_date_to";
    public static final String notice_text = "notice_text";
    public static final String notice_image = "notice_image";

    //GetActions
    public static final int GetActions = 3;
    //-->in
    //public static final String session = "session";
    //--- actions
    public static final String action_id = "action_id";
    public static final String action_name = "action_name";
    public static final String action_type_id = "action_type_id";
    public static final String action_type = "action_type";
    public static final String action_date_from = "action_date_from";
    public static final String action_date_to = "action_date_to";
    public static final String action_date_charge = "action_date_charge";
    public static final String action_description = "action_description";
    public static final String action_image = "action_image";
    //--- models
    public static final String model_id = "model_id";
    public static final String model_name = "model_name";
    public static final String model_sn_count = "model_sn_count";
    public static final String model_points = "model_points";
    public static final String model_brand_id = "model_brand_id";
    public static final String model_brand = "model_brand";
    public static final String model_group_id = "model_group_id";
    public static final String model_group = "model_group";
    public static final String model_url = "model_url";
    public static final String model_image = "model_image";

    //PostSN
    public static final int PostSN = 4;
    //-->in
    //public static final String session = "session";
    //public static final String action_id = "action_id";
    //public static final String model_id = "model_id";
    public static final String data = "data";
    public static final String serials = "serials";
    public static final String sn = "sn";
    //-->out
    //public static final String 0 = "0"
    //public static final String action_id = "action_id";
    //public static final String model_id = "model_id";
    //public static final String serials = "serials";
    //public static final String sn = "sn";

    //GetFinInfo
    public static final int GetFinInfo = 5;
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
    public static final String amount_charges = "amount_charges";
    public static final String payments = "payments";
    public static final String action_payment = "action_payment";
    public static final String date_payment = "date_payment";
    public static final String amount_payment = "amount_payment";
    public static final String comment_payment = "comment_payment";

    //PostQuestion
    public static final int PostQuestion = 6;
    //-->in
    //public static final String session = "session";
    public static final String fio = "fio";
    public static final String e_mail = "e-mail";
    public static final String question = "question";
    //-->out


}