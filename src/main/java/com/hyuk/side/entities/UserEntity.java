package com.hyuk.side.entities;

import java.util.Date;
import java.util.Objects;

public class UserEntity {
    private String email;
    private String pwd;
    private String name;
    private String phone;
    private String status;
    private Date registerAt;
    private String profileImage;
    private Date birth;

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public UserEntity setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public UserEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getRegisterAt() {
        return registerAt;
    }

    public UserEntity setRegisterAt(Date registerAt) {
        this.registerAt = registerAt;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public UserEntity setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public Date getBirth() {
        return birth;
    }

    public UserEntity setBirth(Date birth) {
        this.birth = birth;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
