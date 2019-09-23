package com.example.Moodle;

public class UserProfile {
    private String FName;
    private String LName;
    private String Email;
    private String std;
    private String DOB;
    private String Gender;

    public UserProfile(){

    }

    public UserProfile(String FName, String LName, String email, String std, String DOB,String Gender) {
        this.FName = FName;
        this.LName = LName;
        this.Email = email;
        this.std = std;
        this.DOB = DOB;
        this.Gender = Gender;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
