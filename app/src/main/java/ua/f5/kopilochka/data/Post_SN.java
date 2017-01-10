package ua.f5.kopilochka.data;

import java.util.List;

/**
 * Created by NickNb on 26.10.2016.
 */
public class Post_SN {

    private int action_id;
    private String action_name;
    private int model_id;
    private String model_name;
    private long action_date_to;
    private int action_type_id;
    private int model_points;
    private List<String> serials;
    private long reg_date;
    private int reg_status;
    private String fail_reason;

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

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public long getAction_date_to() {
        return action_date_to;
    }

    public void setAction_date_to(long action_date_to) {
        this.action_date_to = action_date_to;
    }

    public int getAction_type_id() {
        return action_type_id;
    }

    public void setAction_type_id(int action_type_id) {
        this.action_type_id = action_type_id;
    }

    public int getModel_points() {
        return model_points;
    }

    public void setModel_points(int model_points) {
        this.model_points = model_points;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }

    public long getReg_date() {
        return reg_date;
    }

    public void setReg_date(long reg_date) {
        this.reg_date = reg_date;
    }

    public int getReg_status() {
        return reg_status;
    }

    public void setReg_status(int reg_status) {
        this.reg_status = reg_status;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }
}
