package com.outsider.masterofpredictionbackend.user.command.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_nickname")
    private String nickName;

    @Column(name = "user_age")
    private int age;

    @Column(name = "user_point")
    private int point;

    @Column(name = "user_gender")
    private Gender gender;

    @Column(name = "user_location")
    private Location lcation;

    public User() {
    }

    public User(String id, String password, String nickName, int age, int point, Gender gender, Location lcation) {
        this.id = id;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
        this.point = point;
        this.gender = gender;
        this.lcation = lcation;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public int getAge() {
        return age;
    }

    public int getPoint() {
        return point;
    }

    public Gender getGender() {
        return gender;
    }

    public Location getLcation() {
        return lcation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                ", point=" + point +
                ", gender=" + gender +
                ", lcation=" + lcation +
                '}';
    }
}
