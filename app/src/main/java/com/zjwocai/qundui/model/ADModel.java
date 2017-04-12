package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 广告实体类
 * Created by NieLiQin on 2016/6/24.
 */
public class ADModel implements Serializable {

    /**
     * id : 23c4b477e82a418ab85d5f81c542bf54
     * image : userfiles/1/images/banner/2016/07/javascript.gif
     * h5url : gggggggggg
     */

    private String id;
    private String image;
    private String h5url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getH5url() {
        return h5url;
    }

    public void setH5url(String h5url) {
        this.h5url = h5url;
    }
}
