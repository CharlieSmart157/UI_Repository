package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
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
    ListView lvItems;
    DatabaseHandler db;
    Interface com;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.second, container, false);
        lvItems = (ListView)view.findViewById(R.id.lvItems);
        lvItems.setItemsCanFocus(false);
        //insert UI init
        Set_refreshed_data();
        return view;
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
        itemsAdapter = new Contact_Adapter(getActivity(), R.layout.contact_list_view_expanded,
                items);
        lvItems.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
    }


    public class Contact_Adapter extends ArrayAdapter<Contact> {
        Activity activity;
        int layoutResourceId;
        Contact user;
        ArrayList<Contact> data = new ArrayList<Contact>();

        public Contact_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Contact> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.first_name = (TextView) row.findViewById(R.id.user_f_name_txt);
                holder.last_name = (TextView) row.findViewById(R.id.user_l_name_txt);
                holder.dob_text = (TextView) row.findViewById(R.id.user_dob_txt);
                holder.country_text = (TextView) row.findViewById(R.id.user_country_txt);
                holder.gender_text = (TextView) row.findViewById(R.id.user_gender_txt);
                holder.profile_picture = (ImageView) row.findViewById(R.id.user_picture);
                holder.edit = (Button) row.findViewById(R.id.btn_edit);
                holder.delete = (Button) row.findViewById(R.id.btn_delete);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.edit.setTag(user.getID());
            holder.delete.setTag(user.getID());
            holder.first_name.setText(user.get_nameFirst());
            holder.last_name.setText(user.get_nameLast());
            holder.dob_text.setText(user.get_dob());
            holder.country_text.setText(user.get_country());
            holder.gender_text.setText(user.get_gender());
            //Decode Image
            byte[] imageOutput = Base64.decode(user.get_picture().getBytes(),Base64.DEFAULT);
            Bitmap img = BitmapFactory.decodeByteArray(imageOutput,0,imageOutput.length);
            holder.profile_picture.setImageBitmap(img);

            holder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", ""+v.getTag());


                    com = (Interface)getActivity();
                    com.SelectItem(2, Integer.parseInt(v.getTag().toString()));

                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // show a message while loader is loading

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int user_id = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);
                                    DatabaseHandler dBHandler = new DatabaseHandler(
                                            activity.getApplicationContext());
                                    dBHandler.Delete_Contact(Integer.parseInt(v.getTag().toString()));
                                    Set_refreshed_data();
                                }
                            });
                    adb.show();
                }

            });
            return row;

        }

        class UserHolder {
            TextView first_name;
            TextView last_name;
            TextView dob_text;
            TextView gender_text;
            TextView country_text;
            ImageView profile_picture;
            Button edit;
            Button delete;
        }

    }
}
