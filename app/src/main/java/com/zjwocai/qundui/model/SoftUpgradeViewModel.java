package com.zjwocai.qundui.model;

import java.util.List;

/**
 * Created by amituo on 16/11/7.
 */

public class SoftUpgradeViewModel {
//    private int versionNum; // 版本(数字)
//    private String version; // 版本(文字)
//    private String downloadUrl; // 下载地址
//    private boolean isForceUpdate; // 是否强制升级
//    private boolean isLatest; // 是否最新
//    private List<String> softUpgradeContents; // 升级内容
    private String id;
    private String version_name;
    private int version_number;
    private String version;
    private String download_url;
    private int force_update;
    private String type;
    private String description;
    private String size;


    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion_name() {
        return version_name;
    }

    public int getVersion_number() {
        return version_number;
    }

    public String getVersion() {
        return version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public int getForce_update() {
        return force_update;
    }

    public String getType() {
        return type;
    }



    public String getSize() {
        return size;
    }

    public void setForce_update(int force_update) {
        this.force_update = force_update;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public void setVersion_number(int version_number) {
        this.version_number = version_number;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setSize(String size) {
        this.size = size;
    }
}