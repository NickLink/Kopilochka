package ua.kiev.foxtrot.kopilochka.data;

import java.util.ArrayList;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Action {

    private int action_id;
    private String action_name;
    private int action_type_id; //ИД типа акции 0 = Накопительная; 1 = Бонусная
    private String action_type;
    private long action_date_from;
    private long action_date_to;
    private long action_date_charge;
    private String action_description;
    private ArrayList<Model> models = new ArrayList<Model>();
    private int viewed;

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public int getAction_type_id() {
        return action_type_id;
    }

    public void setAction_type_id(int action_type_id) {
        this.action_type_id = action_type_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public long getAction_date_from() {
        return action_date_from;
    }

    public void setAction_date_from(long action_date_from) {
        this.action_date_from = action_date_from;
    }

    public long getAction_date_to() {
        return action_date_to;
    }

    public void setAction_date_to(long action_date_to) {
        this.action_date_to = action_date_to;
    }

    public long getAction_date_charge() {
        return action_date_charge;
    }

    public void setAction_date_charge(long action_date_charge) {
        this.action_date_charge = action_date_charge;
    }

    public String getAction_description() {
        return action_description;
    }

    public void setAction_description(String action_description) {
        this.action_description = action_description;
    }

    public ArrayList<Model> getModels() {
        return models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
