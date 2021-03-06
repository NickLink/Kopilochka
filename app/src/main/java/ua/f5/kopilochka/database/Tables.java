package ua.f5.kopilochka.database;

import ua.f5.kopilochka.Const;

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
            + Const.notice_id + " text, "
            + Const.notice_name + " text, "
            + Const.notice_text + " text, "
            + Const.notice_type_id + " integer, "
            + Const.notice_type + " text, "
            + Const.viewed + " integer, "
            + Const.notice_hash + " text"
            + ");";

    //===========================MODELS Part==========================
    public static final String table_name_models = "models";
    //=Fields=

    //=Table=
    public static final String table_models = "create table " + table_name_models + " ("
            + Const.model_id + " integer, "
            + Const.model_name + " text, "
            + Const.model_points + " integer, "
            + Const.model_brand_id + " integer, "
            + Const.model_group_id + " integer, "
            + Const.model_url + " text, "
            + Const.model_brand_name + " text, "
            + Const.model_group_name + " text, "
            + Const.model_sn_count + " integer, "
            + Const.model_action + " integer, "
            + Const.viewed + " integer default 0, "
            + "PRIMARY KEY (" + Const.model_action + ", " + Const.model_id + ") "
            + ");";

    //===========================ACTIONS Part==========================
    public static final String table_name_actions = "actions";
    //=Fields=

    //=Table=
    public static final String table_actions = "create table " + table_name_actions + " ("
            + Const.action_id + " integer, "
            + Const.action_name + " text, "
            + Const.action_type_id + " integer, "
            + Const.action_type + " text, "
            + Const.action_date_from + " integer, "
            + Const.action_date_to + " integer, "
            + Const.action_date_charge + " integer, "
            + Const.action_description + " text, "
            + Const.viewed + " integer, "
            + Const.action_hash + " text, "
            + "PRIMARY KEY (" + Const.action_id + ") "
            + ");";

    //========================GROUPS=======================================
    public static final String table_name_groups = "groups";
    //=Fields=

    //=Table=
    public static final String table_groups = "create table " + table_name_groups + " ("
            + Const.group_id + " integer, "
            + Const.group_name + " text, "
            + Const.viewed + " integer default 0, "
            + Const.group_hash + " text"
            + ");";

    //=============================POST_SN==================================
    public static final String table_name_postsn = "postsn";
    //=Fields=

    //=Table=
    public static final String table_postsn = "create table " + table_name_postsn + " ("
            + "_id integer primary key autoincrement,"
            + Const.action_id + " integer,"
            + Const.action_name + " text,"
            + Const.model_id + " integer,"
            + Const.model_name + " text,"
            + Const.action_date_to + " integer,"
            + Const.action_type_id + " integer,"
            + Const.model_points + " integer,"
            + Const.serials + " text,"
            + Const.reg_date + " integer,"
            + Const.reg_status + " integer,"
            + Const.fail_reason + " text"
            + ");";

}
