package ua.kiev.foxtrot.kopilochka;

/**
 * Created by NickNb on 29.09.2016.
 */
public interface Interfaces {
    void OpenClose();

    void ScannStart();
    void ScannResult(String result);

    void LoginSuccess(String token);
    void ActionSelected(int action_id);
}
