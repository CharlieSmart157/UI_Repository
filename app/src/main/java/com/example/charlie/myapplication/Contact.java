package com.example.charlie.myapplication;

/**
 * Created by Charlie on 03/06/2016.
 */
public class Contact {
    public int _id;
    public String _nameFirst;
    public String _nameLast;
    public String _dob;
    public String _country;
    public String _gender;
    public String _picture;

    public Contact(){
    }

    // constructor
    public Contact(int id, String _nameFirst, String _nameLast, String _country, String _dob, String _gender, String _picture){
    this._id = id;
    this._nameFirst = _nameFirst;
    this._nameLast = _nameLast;
    this._country = _country;
    this._dob = _dob;
    this._gender = _gender;
    this._picture = _picture;

    }

    //constructor
    public Contact(String _nameFirst, String _nameLast, String _country, String _dob, String _gender, String _picture){
        this._nameFirst = _nameFirst;
        this._nameLast = _nameLast;
        this._country = _country;
        this._dob = _dob;
        this._gender = _gender;
        this._picture = _picture;
    }

    // getting ID
    public int getID(){return this._id;}
    // setting ID
    public void setID(int id){this._id = id;}
    // getting First Name
    public String get_nameFirst(){return this._nameFirst;}
    // setting First Name
    public void set_nameFirst(String nameFirst){this._nameFirst = nameFirst;}
    // getting Second Name
    public String get_nameLast(){return this._nameLast;}
    // setting Second Name
    public void set_nameLast(String nameLast){this._nameLast = nameLast;}
    // getting Country
    public String get_country(){return this._country;}
    // setting Country
    public void set_country(String country){this._country = country;}
    // getting Date of Birth
    public String get_dob() {
        return _dob;
    }

    // setting Date of Birth

    public void set_dob(String _dob) {
        this._dob = _dob;
    }
    // getting Gender

    public String get_gender() {
        return _gender;
    }
    // setting Gender

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    // getting Picture
    public String get_picture() {
        return _picture;
    }
    // setting Picture


    public void set_picture(String _picture) {
        this._picture = _picture;
    }
}
