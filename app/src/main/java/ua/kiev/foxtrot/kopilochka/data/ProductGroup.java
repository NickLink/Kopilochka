package ua.kiev.foxtrot.kopilochka.data;

/**
 * Created by NickNb on 24.10.2016.
 */
public class ProductGroup {
    private int group_id;
    private String group_name;
    private int models_count;
    private String group_hash;

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

    public String getGroup_hash() {
        return group_hash;
    }

    public void setGroup_hash(String group_hash) {
        this.group_hash = group_hash;
    }
}
