package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Main2Activity extends AppCompatActivity {
    ArrayList<Contact> items = new ArrayList<Contact>();
    Contact_Adapter itemsAdapter;
    ListView lvItems;
    Button add_btn;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
     //   readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setItemsCanFocus(false);
        add_btn = (Button)findViewById(R.id.add_btn);

        Set_refreshed_data();

    }

    /*public void onAddItem(View v){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
       // writeItems();

    }
/*
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,View item, int pos, long id){
            items.remove(pos);
            itemsAdapter.notifyDataSetChanged();
       //     writeItems();
            return true;
        }
        });
}
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile =  new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
*/
    private void Set_refreshed_data(){
        items.clear();
        db = new DatabaseHandler(this);
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
        itemsAdapter = new Contact_Adapter(Main2Activity.this, R.layout.contact_list_view_expanded,
    items);
        lvItems.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void  onResume(){
        super.onResume();
        Set_refreshed_data();

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
            holder.profile_picture.setImageURI(Uri.parse(user.get_picture()));


            holder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");

                    Intent update_user = new Intent(activity,
                            MainActivity.class);
                    update_user.putExtra("called", "update");
                    update_user.putExtra("USER_ID", v.getTag().toString());
                    activity.startActivity(update_user);

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
                                    dBHandler.Delete_Contact(user_id);
                                    Main2Activity.this.onResume();

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
