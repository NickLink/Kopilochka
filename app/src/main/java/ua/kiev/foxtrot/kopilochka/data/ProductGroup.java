package ua.kiev.foxtrot.kopilochka.data;

/**
 * Created by NickNb on 24.10.2016.
 */
public class ProductGroup {
    int group_id;
    String group_name;
    int models_count;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getModels_count() {
        return models_count;
    }

    public void setModels_count(int models_count) {
        this.models_count = models_count;
    }
}
