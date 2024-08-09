package com.outsider.masterofpredictionbackend.user.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Location;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 또는 AUTO, SEQUENCE, TABLE 중 하나 선택
    private Long id;
    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_nickname")
    private String nickName;

    @Column(name = "user_age")
    private int age;

    @Column(name = "user_point")
    private BigDecimal points;

    @Column(name = "user_gender")
    private Gender gender;

    @Column(name = "user_location")
    private Location location;
    @Column(name = "user_withdrawal")
    private Boolean isWithdrawal;

    public void setWithdrawal(Boolean withdrawal) {
        isWithdrawal = withdrawal;
    }

    public Boolean getWithdrawal() {
        return isWithdrawal;
    }

    public User(Boolean isWithdrawal) {
        this.isWithdrawal = isWithdrawal;
    }

    public User() {
    }

    public User(String email, String password, String nickName, int age, Gender gender, Location lcation) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
        this.points = new BigDecimal(0);
        this.gender = gender;
        this.location = lcation;
        this.isWithdrawal = false;
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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

    public BigDecimal getPoints() {
        return points;
    }

    public Gender getGender() {
        return gender;
    }

    public Location getLocation() {
        return location;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPoints(BigDecimal point) {
        this.points = point;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setLocation(Location lcation) {
        this.location = lcation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                ", points=" + points +
                ", gender=" + gender +
                ", lcation=" + location +
                '}';
    }
}
