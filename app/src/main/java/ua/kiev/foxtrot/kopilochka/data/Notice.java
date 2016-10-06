package ua.kiev.foxtrot.kopilochka.data;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Notice {

    private String notice_id;
    private String notice_name;
    private String notice_date_from;
    private String notice_date_to;
    private String notice_text;
    private String notice_image;

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getNotice_name() {
        return notice_name;
    }

    public void setNotice_name(String notice_name) {
        this.notice_name = notice_name;
    }

    public String getNotice_date_from() {
        return notice_date_from;
    }

    public void setNotice_date_from(String notice_date_from) {
        this.notice_date_from = notice_date_from;
    }

    public String getNotice_date_to() {
        return notice_date_to;
    }

    public void setNotice_date_to(String notice_date_to) {
        this.notice_date_to = notice_date_to;
    }

    public String getNotice_text() {
        return notice_text;
    }

    public void setNotice_text(String notice_text) {
        this.notice_text = notice_text;
    }

    public String getNotice_image() {
        return notice_image;
    }

    public void setNotice_image(String notice_image) {
        this.notice_image = notice_image;
    }
}
