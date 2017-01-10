package ua.f5.kopilochka.data;

/**
 * Created by NickNb on 17.11.2016.
 */
public class MenuItem {
    private String name;
    private boolean viewed;
    private int resource;

    public MenuItem(String name, boolean viewed, int resource){
        this.name = name;
        this.viewed = viewed;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
