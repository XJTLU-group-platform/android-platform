package com.example.testnetwork.util;

public class UserID_ExStorage_Method {
    String _name;
    String _account;
    String _password;
    String _gender;
    int _age;

    public UserID_ExStorage_Method(String name,String account, String password, String gender, int age) {
        this._name = name;
        this._account = account;
        this._password = password;
        this._gender = gender;
        this._age = age;
    }

    public String getAccount() {return this._account;};
    public void set_Account(String account) {this._account = account;}
    public String getName() {return this._name;}
    public void setName(String name) {this._name = name;}
    public String getPassword() {return this._password;}
    public String getGender() {return  this._gender;}
    public int getAge() {return this._age;}

}
