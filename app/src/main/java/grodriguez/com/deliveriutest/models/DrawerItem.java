package grodriguez.com.deliveriutest.models;

import android.media.Image;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class DrawerItem {

    private String title;
    private Image image;

    public DrawerItem(String title) {
        this.title = title;
    }

    public DrawerItem(String title, Image image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "DrawerItem{" +
                "title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
