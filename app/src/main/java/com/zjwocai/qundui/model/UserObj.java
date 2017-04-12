package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 用户类型
 *
 * @author lanyan
 */
@SuppressWarnings("serial")
public class UserObj implements Serializable {

    protected boolean isAuto;// 是否记住密码
    protected String recognizecode;// "vip001",
    protected String truename;// "策腾学生12",
    protected String nickname;// "舒扬",
    protected String avatar;
    protected String userid;// "vip001",
    protected String password;
    protected String usertype;
    protected String gender;// "1",
    protected String areaid;// "110108",
    protected String cellphone;// "13501381665",
    protected String telephone;// "0571-82176228",
    protected String email;// "340244525@qq.com",
    protected String online;// "",
    protected String schoolid;// "110108000001",
    protected String schoolname;// "策腾中学",
    protected String classid;// "110108000001",
    protected String classname;// "策腾中学初一一班",
    protected String areaname;
    protected String remainderday;
    protected String token;
    protected String welcome;
    protected String avatarkey;

    public boolean isAuto() {
        return isAuto;
    }

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public String getRecognizecode() {
        return recognizecode;
    }

    public void setRecognizecode(String recognizecode) {
        this.recognizecode = recognizecode;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getRemainderday() {
        return remainderday;
    }

    public void setRemainderday(String remainderday) {
        this.remainderday = remainderday;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarkey() {
        return avatarkey;
    }

    public void setAvatarkey(String avatarkey) {
        this.avatarkey = avatarkey;
    }

}
