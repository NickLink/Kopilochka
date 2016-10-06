package ua.kiev.foxtrot.kopilochka.database;

/**
 * Created by NickNb on 30.09.2016.
 */
public class Tables {

    public static final String table_name_bbs_news = "bbs_news";
    public static final String bbs_author = "author";
    public static final String bbs_title = "title";
    public static final String bbs_description = "description";
    public static final String bbs_url = "url";
    public static final String bbs_urlToImage = "urlToImage";
    public static final String bbs_publishedAt = "publishedAt";

    public static final String table_bbs_news = "create table " + table_name_bbs_news + " ("
            + "_id integer primary key autoincrement,"
            + "author text,"
            + "title text,"
            + "description text,"
            + "url text,"
            + "urlToImage text,"
            + "publishedAt text"
            + ");";


}
