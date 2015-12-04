package grodriguez.com.deliveriutest.models;

import java.io.Serializable;

/**
 * Name of category and products associated with it
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class Categories implements Serializable {

    private String id;
    private String name;

    public Categories(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
