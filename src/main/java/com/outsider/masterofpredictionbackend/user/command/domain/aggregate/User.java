package com.outsider.masterofpredictionbackend.user.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outsider.masterofpredictionbackend.common.BaseEntity;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.*;
import com.outsider.masterofpredictionbackend.utils.BigDecimalDeserializer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "USER")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long id;
    @Column(name = "user_email")
    @JsonProperty("user_email")
    private String email;

    @Column(name = "user_password")
    @JsonProperty("user_password")
    private String password;
    @Column(name = "display_name")
    @JsonProperty("display_name")
    private String displayName;


    @Column(name = "user_name",unique = true)
    @JsonProperty("user_name")
    private String userName;

    @Column(name = "user_age")
    @JsonProperty("user_age")
    private int age;

    @Column(name = "user_point")
    @JsonProperty("user_point")
    @JsonSerialize(using = ToStringSerializer.class)  // 숫자를 문자열로 직렬화
    private BigDecimal points;

    @Column(name = "user_gender")
    @JsonProperty("user_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_location")
    @JsonProperty("user_location")
    private Location location;
    @Column(name = "user_withdrawal")
    @JsonProperty("user_withdrawal")
    private Boolean isWithdrawal;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_authority")
    @JsonProperty("user_authority")
    private Authority authority;

    @Column(name = "birthday")
    @JsonProperty("birthday")
    private LocalDate birthday;




    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Embedded
    private Tier tier;
    @Embedded
    private ProviderInfo provider; //어떤 OAuth인지(google, naver 등)
    private String provideId; // 해당 OAuth 의 key(id)
    @Column(name = "user_img",length = 1000)
    private String userImg;

    @Column(name = "joined_date")
    private LocalDate joinDate;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }


    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = Authority.fromString(authority);
    }

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

    public User(String email, String password,String displayName, String userName, Authority authority, ProviderInfo provider) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.userName = userName;
        this.tier = new Tier();
        this.points = new BigDecimal(3000);
        this.isWithdrawal = false;
        this.authority = authority;
        this.joinDate = LocalDate.now();
        this.provider = provider;
    }

    public User(String email, String password,String displayName, String userName, int age, Gender gender, Location lcation, Authority authority) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.location = lcation;
        this.tier = new Tier();
        this.points = new BigDecimal(3000);
        this.isWithdrawal = false;
        this.joinDate = LocalDate.now();
        this.authority = authority;


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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
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

    public void setUsername(String userName) {
        this.userName = userName;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                ", points=" + points +
                ", gender=" + gender +
                ", lcation=" + location +
                '}';
    }
}
