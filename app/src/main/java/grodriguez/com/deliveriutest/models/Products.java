package grodriguez.com.deliveriutest.models;

import com.parse.ParseFile;

import java.io.Serializable;

/**
 * Description Name and Image of the offered Products
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class Products implements Serializable {

    private String id;
    private String name;
    private String description;
    private ParseFile image;
    private int price;

    public Products(String id, String name, String description, ParseFile image, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
