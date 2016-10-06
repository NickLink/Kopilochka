package ua.kiev.foxtrot.kopilochka.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.data.BBS_News;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Parser {

    public static ArrayList<BBS_News> getNewsArray(String response){
        ArrayList<BBS_News> news = new ArrayList<BBS_News>();
        try{
            JSONObject data = new JSONObject(response);
            JSONArray articles = data.getJSONArray("articles");
            for (int i = 0;i<articles.length();i++){
                BBS_News item = get_News(articles.getJSONObject(i));
                news.add(item);
            }
            return news;
        }
        catch (Exception e){
            return null;
        }

    }

    public static BBS_News get_News(JSONObject news){
        BBS_News data = new BBS_News();
        data.setAuthor(news.optString("author"));
        data.setTitle(news.optString("title"));
        data.setDescription(news.optString("description"));
        data.setUrl(news.optString("url"));
        data.setUrlToImage(news.optString("urlToImage"));
        data.setPublishedAt(news.optString("publishedAt"));
        return data;
    }
}
