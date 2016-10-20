package ua.kiev.foxtrot.kopilochka.database;

import ua.kiev.foxtrot.kopilochka.Const;

/**
 * Created by NickNb on 30.09.2016.
 */
public class Tables {

    public static final String table_name_bbs_news = "bbs_news";
    //=Fields=
    public static final String bbs_author = "author";
    public static final String bbs_title = "title";
    public static final String bbs_description = "description";
    public static final String bbs_url = "url";
    public static final String bbs_urlToImage = "urlToImage";
    public static final String bbs_publishedAt = "publishedAt";
    //=Table=
    public static final String table_bbs_news = "create table " + table_name_bbs_news + " ("
            + "_id integer primary key autoincrement,"
            + "author text,"
            + "title text,"
            + "description text,"
            + "url text,"
            + "urlToImage text,"
            + "publishedAt text"
            + ");";


    //======================NOTICES Part===========================
    public static final String table_name_notices = "notices";
    //=Fields from Const file=

    //=Table=
    public static final String table_notices = "create table " + table_name_notices + " ("
            + "_id integer primary key autoincrement,"
            + Const.notice_id + " text,"
            + Const.notice_name + " text,"
            + Const.notice_text + " text,"
            + Const.notice_type_id + " text,"
            + Const.notice_type + " text"
            + ");";

    //===========================MODELS Part==========================
    public static final String table_name_models = "models";
    //=Fields=

    //=Table=
    public static final String table_models = "create table " + table_name_models + " ("
            + "_id integer primary key autoincrement,"
            + Const.model_id + " integer,"
            + Const.model_name + " text,"
            + Const.model_points + " integer,"
            + Const.model_brand_id + " integer,"
            + Const.model_group_id + " integer,"
            + Const.model_url + " text,"
            + Const.model_brand_name + " text,"
            + Const.model_group_name + " text,"
            + Const.model_sn_count + " integer,"
            + Const.model_action + " integer"
            + ");";

    //===========================ACTIONS Part==========================
    public static final String table_name_actions = "actions";
    //=Fields=

    //=Table=
    public static final String table_actions = "create table " + table_name_actions + " ("
            + "_id integer primary key autoincrement,"
            + Const.action_id + " integer,"
            + Const.action_name + " text,"
            + Const.action_type_id + " integer,"
            + Const.action_type + " text,"
            + Const.action_date_from + " text,"
            + Const.action_date_to + " text,"
            + Const.action_date_charge + " text,"
            + Const.action_description + " text"
            + ");";
}
