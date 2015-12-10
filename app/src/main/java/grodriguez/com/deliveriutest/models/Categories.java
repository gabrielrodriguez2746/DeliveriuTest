package grodriguez.com.deliveriutest.models;

import java.io.Serializable;
import java.util.List;

/**
 * Name of category and products associated with it
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class Categories implements Serializable {

    private int index;
    private String id;
    private String name;
    private List<Products> products;

    public Categories(int index, String id, String name) {
        this.index = index;
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

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "index=" + index +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
