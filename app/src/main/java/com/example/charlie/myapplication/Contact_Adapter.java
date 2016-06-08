package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlie on 08/06/2016.
 */
public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.UserHolder> {
    Activity activity;
    int layoutResourceId;

    private List<Contact> contacts;
    private int rowLayout;
    private Context context;

    ArrayList<Contact> data = new ArrayList<Contact>();

    public Contact_Adapter(Context context, List<Contact> contacts, int row) {
        this.contacts = contacts;
        this.context = context;
        this.rowLayout = rowLayout;

        notifyDataSetChanged();
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
       final Contact contact = contacts.get(position);
        holder.edit.setTag(contact.getID());
        holder.delete.setTag(contact.getID());
        holder.first_name.setText(contact.get_nameFirst());
        holder.last_name.setText(contact.get_nameLast());
        holder.dob_text.setText(contact.get_dob());
        holder.country_text.setText(contact.get_country());
        holder.gender_text.setText(contact.get_gender());
        //Decode Image
        byte[] imageOutput = Base64.decode(contact.get_picture().getBytes(),Base64.DEFAULT);
        Bitmap img = BitmapFactory.decodeByteArray(imageOutput,0,imageOutput.length);
        holder.profile_picture.setImageBitmap(img);

        holder.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Edit Button Clicked", ""+v.getTag());


                // com = (Interface)getActivity();
                // com.SelectItem(2, Integer.parseInt(v.getTag().toString()));

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

                            }
                        });
                adb.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView first_name;
        TextView last_name;
        TextView dob_text;
        TextView gender_text;
        TextView country_text;
        ImageView profile_picture;
        Button edit;
        Button delete;

        public UserHolder(View row) {
            super(row);

            first_name = (TextView) row.findViewById(R.id.user_f_name_txt);
            last_name = (TextView) row.findViewById(R.id.user_l_name_txt);
            dob_text = (TextView) row.findViewById(R.id.user_dob_txt);
            country_text = (TextView) row.findViewById(R.id.user_country_txt);
            gender_text = (TextView) row.findViewById(R.id.user_gender_txt);
            profile_picture = (ImageView) row.findViewById(R.id.user_picture);
            edit = (Button) row.findViewById(R.id.btn_edit);
            delete = (Button) row.findViewById(R.id.btn_delete);

        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

}
