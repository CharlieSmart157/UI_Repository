package com.example.charlie.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Charlie on 03/06/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    //Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME1 = "First_Name";
    private static final String KEY_NAME2 = "Last_Name";
    private static final String KEY_DOB = "Date_of_Birth";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_COUNTRY = "Country";
    private static final String KEY_PICTURE = "Picture";
    private static final ArrayList<Contact> contact_list = new ArrayList<Contact>();

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME1+ " TEXT,"
            + KEY_NAME2  + " TEXT," + KEY_DOB + " TEXT," + KEY_COUNTRY + " TEXT,"
            + KEY_GENDER + " TEXT," +KEY_PICTURE+ " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }



    public void Add_Contact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME1, contact.get_nameFirst());//First Name
        values.put(KEY_NAME2, contact.get_nameLast()); //Last Name
        values.put(KEY_COUNTRY, contact.get_country());//Country
        values.put(KEY_DOB, contact.get_dob());        //Date of Birth
        values.put(KEY_GENDER, contact.get_gender());  //Gender
        values.put(KEY_PICTURE, contact.get_picture());//Picture
        //Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();//Close database
    }

    //Getting single contact
    Contact Get_Contact(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_NAME1, KEY_NAME2, KEY_COUNTRY, KEY_DOB, KEY_GENDER, KEY_PICTURE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null) ;
        if(cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getString(2),cursor.getString(3),
                cursor.getString(4),cursor.getString(5),cursor.getString(6));
        //return contact
        cursor.close();
        db.close();

        return contact;
    }

    // Getting ALL contacts
    public ArrayList<Contact> Get_Contacts() {
        try {
            contact_list.clear();

            //Select ALL Query
            String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            //looping through all rows and adding to the list
            if (cursor.moveToFirst()) {
            do{
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.set_nameFirst(cursor.getString(1));
                contact.set_nameLast(cursor.getString(2));
                contact.set_country(cursor.getString(3));
                contact.set_dob(cursor.getString(4));
                contact.set_gender(cursor.getString(5));
                contact.set_picture(cursor.getString(6));
                //adding contacts to list
                contact_list.add(contact);
            }while(cursor.moveToNext());
            }
            //Return contact list
            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e){// TODO: handle exception
            Log.e("all_contact",""+e);
        }
        return contact_list;
    }

    //Updating single contact
    public int Update_Contact(Contact contact){
        SQLiteDatabase db = this .getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME1, contact.get_nameFirst());
        values.put(KEY_NAME2, contact.get_nameLast());
        values.put(KEY_COUNTRY, contact.get_country());
        values.put(KEY_DOB, contact.get_dob());
        values.put(KEY_GENDER, contact.get_picture());

        //Updating Rows
        return db.update(TABLE_CONTACTS,values, KEY_ID + " = ?", new String[]{String.valueOf(contact.getID())});


    }

    // Deleting single contact
    public void Delete_Contact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


}
