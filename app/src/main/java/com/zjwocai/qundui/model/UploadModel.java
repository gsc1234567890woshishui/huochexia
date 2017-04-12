package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 图片上传返回值
 * Created by NieLiQin on 2016/8/4.
 */
public class UploadModel implements Serializable {

    /**
     * fieldName : file123
     * fileurl1 : http://localhost:8081/simplefileserver//download?file=68afb3f4-a44a-4f5d-88a9-c7fa22a7170d
     * fileurl2 : http://localhost:8081/simplefileserver//userfiles/qundui//2016/8/4//68afb3f4-a44a-4f5d-88a9-c7fa22a7170d.png
     * fileurl3 : userfiles/qundui//2016/8/4/68afb3f4-a44a-4f5d-88a9-c7fa22a7170d.png
     */

    private String fieldName;
    private String fileurl1;
    private String fileurl2;
    private String fileurl3;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFileurl1() {
        return fileurl1;
    }

    public void setFileurl1(String fileurl1) {
        this.fileurl1 = fileurl1;
    }

    public String getFileurl2() {
        return fileurl2;
    }

    public void setFileurl2(String fileurl2) {
        this.fileurl2 = fileurl2;
    }

    public String getFileurl3() {
        return fileurl3;
    }

    public void setFileurl3(String fileurl3) {
        this.fileurl3 = fileurl3;
    }
}
