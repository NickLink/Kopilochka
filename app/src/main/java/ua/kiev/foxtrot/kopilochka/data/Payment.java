package ua.kiev.foxtrot.kopilochka.data;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Payment {

    private String action_payment;
    private String date_payment;
    private int amount_payment;
    private String comment_payment;

    public String getAction_payment() {
        return action_payment;
    }

    public void setAction_payment(String action_payment) {
        this.action_payment = action_payment;
    }

    public String getDate_payment() {
        return date_payment;
    }

    public void setDate_payment(String date_payment) {
        this.date_payment = date_payment;
    }

    public int getAmount_payment() {
        return amount_payment;
    }

    public void setAmount_payment(int amount_payment) {
        this.amount_payment = amount_payment;
    }

    public String getComment_payment() {
        return comment_payment;
    }

    public void setComment_payment(String comment_payment) {
        this.comment_payment = comment_payment;
    }
}
