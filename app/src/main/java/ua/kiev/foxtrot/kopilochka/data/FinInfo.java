package ua.kiev.foxtrot.kopilochka.data;

import java.util.ArrayList;

/**
 * Created by NickNb on 05.10.2016.
 */
public class FinInfo {

    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_payment;
    private ArrayList<Charge> charges = new ArrayList<Charge>();
    private ArrayList<Payment> payments = new ArrayList<Payment>();

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_payment() {
        return user_payment;
    }

    public void setUser_payment(String user_payment) {
        this.user_payment = user_payment;
    }

    public ArrayList<Charge> getCharges() {
        return charges;
    }

    public void setCharges(ArrayList<Charge> charges) {
        this.charges = charges;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }
}
