package ua.f5.kopilochka.data;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Charge {

    private String  action_charge;
    private String date_charge;
    private int amount_charges;

    public String getAction_charge() {
        return action_charge;
    }

    public void setAction_charge(String action_charge) {
        this.action_charge = action_charge;
    }

    public String getDate_charge() {
        return date_charge;
    }

    public void setDate_charge(String date_charge) {
        this.date_charge = date_charge;
    }

    public int getAmount_charges() {
        return amount_charges;
    }

    public void setAmount_charges(int amount_charges) {
        this.amount_charges = amount_charges;
    }
}
