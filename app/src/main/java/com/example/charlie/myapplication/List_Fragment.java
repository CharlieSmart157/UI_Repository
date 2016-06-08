package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Charlie on 06/06/2016.
 */
public class List_Fragment extends Fragment {
    ArrayList<Contact> items = new ArrayList<Contact>();
    Contact_Adapter itemsAdapter;
   // ListView lvItems;
    DatabaseHandler db;
    Interface com;
    private RecyclerView lvItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        //lvItems = (ListView)view.findViewById(R.id.lvItems);
        //lvItems.setItemsCanFocus(false);
        //insert UI init
        initializeRecyclerView()
        return view;
    }
    public void initializeRecyclerView(){
        lvItems = (RecyclerView)findViewById(R.id.recyclerView);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.setItemAnimator(new DefaultItemAnimator());

        Set_refreshed_data();

    }
    private void Set_refreshed_data(){
        items.clear();
        db = new DatabaseHandler(getActivity());
        ArrayList<Contact> contact_array_from_db = db.Get_Contacts();
        Log.i("Retrieving Data", "No of Entries: "+contact_array_from_db.size());
        for(int i =0; i < contact_array_from_db.size();i++){
            int idno = contact_array_from_db.get(i).getID();
            String nameFirst = contact_array_from_db.get(i).get_nameFirst();
            Log.i("Retrieving Data", nameFirst);
            String nameLast = contact_array_from_db.get(i).get_nameLast();
            String country = contact_array_from_db.get(i).get_country();
            String dob = contact_array_from_db.get(i).get_dob();
            String gender = contact_array_from_db.get(i).get_gender();
            String picture = contact_array_from_db.get(i).get_picture();

            Contact cnt = new Contact();
            cnt.setID(idno);
            cnt.set_nameFirst(nameFirst);
            cnt.set_nameLast(nameLast);
            cnt.set_country(country);
            cnt.set_dob(dob);
            cnt.set_gender(gender);
            cnt.set_picture(picture);
            items.add(cnt);
        }
        db.close();
        itemsAdapter = new Contact_Adapter(getActivity(), items,R.layout.contact_list_view_expanded);
        lvItems.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
    }



}
