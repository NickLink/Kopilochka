package ua.kiev.foxtrot.kopilochka;

/**
 * Created by NickNb on 29.09.2016.
 */
public interface Interfaces {
    void OpenClose();

    void ScannStart(int id);
    void ScannResult(int id, String result);

    void LoginSuccess();
    void ShowDataExtra();
    void LogOut();
    void ActionSelected(int action_id);
    void ModelSelected(int action_id, int model_id);
    void ProductGroupSelected(int group_id, String group_name, int action_type);
    void EditExistPostSN(int action_id, int model_id, String serials);
    void DeleteExistPostSN();

}
