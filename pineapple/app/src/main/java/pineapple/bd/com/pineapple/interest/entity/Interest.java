package pineapple.bd.com.pineapple.interest.entity;

/**
 * Created by kevin on 16-4-10.
 */
public class Interest {

    String name;

    int imageResId;

    String actionUrl;

    int plateColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public void setPlateColor(int plateColor) {
        this.plateColor = plateColor;
    }

    public int getPlateColor() {
        return plateColor;
    }
}
