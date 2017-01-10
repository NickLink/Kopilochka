package ua.f5.kopilochka.interfaces;

/**
 * Created by NickNb on 05.10.2016.
 */
public interface HttpRequest {
    void http_result(int type, String result);
    void http_error(int type, String error);
}
