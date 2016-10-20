package ua.kiev.foxtrot.kopilochka.data;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Notice {

    private int notice_id;
    private String notice_name;
    private String notice_text;
    private int notice_type_id;
    private String notice_type;

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public String getNotice_name() {
        return notice_name;
    }

    public void setNotice_name(String notice_name) {
        this.notice_name = notice_name;
    }

    public String getNotice_text() {
        return notice_text;
    }

    public void setNotice_text(String notice_text) {
        this.notice_text = notice_text;
    }

    public int getNotice_type_id() {
        return notice_type_id;
    }

    public void setNotice_type_id(int notice_type_id) {
        this.notice_type_id = notice_type_id;
    }

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }
}
