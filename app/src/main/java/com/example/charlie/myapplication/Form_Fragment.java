package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Charlie on 06/06/2016.
 */
public class Form_Fragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    public static final String ENTRY_MODE = "entry_mode";
    public static  final String ITEM_ID = "item_id";
    private AppCompatActivity myContext;
    EditText first_Name, last_Name;
    TextView dob_Text;
    Button btn_Confirm, btn_DoB, imageBtn;
    int counter =0;
    int USER_ID;
    boolean update = false;
    String[] country_List;
    Spinner spinner;
    Bitmap bitmap;
    ImageView imageHolder;
    View view;
    Interface com;
    String imageAddress;
    RadioButton m_rad_btn, f_rad_btn, o_rad_btn;
    RadioGroup r_group;
    String  valid_profile_pic = null, valid_f_name = null, valid_l_name= null, valid_dob= null, valid_country= null, valid_gender = null;
    DatabaseHandler dbHandler;
    public android.support.v4.app.FragmentManager OldFManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.contact_fragment,container,false);
        String called_from = getArguments().getString(ENTRY_MODE);
        dbHandler = new DatabaseHandler(getActivity());
        initializeUI();

        //OldFManager = myContext.getSupportFragmentManager();
        if(called_from.equalsIgnoreCase("edit")){
            USER_ID = getArguments().getInt(ITEM_ID);
            Log.i("USER_ID",""+getArguments().getInt("ITEM_ID"));
            update = true;
            Contact c = dbHandler.Get_Contact(USER_ID);

            first_Name.setText(c.get_nameFirst());
            last_Name.setText(c.get_nameLast());
            dob_Text.setText(c.get_dob());
            byte[] imageOutput = Base64.decode(c.get_picture().getBytes(),Base64.DEFAULT);
            Bitmap img = BitmapFactory.decodeByteArray(imageOutput,0,imageOutput.length);
            imageHolder.setImageBitmap(img);
            imageAddress = c.get_picture();

            if(c.get_gender().equalsIgnoreCase("M")){
                m_rad_btn.setChecked(true);
            }
            else
            if(c.get_gender().equalsIgnoreCase("F")){
                f_rad_btn.setChecked(true);
            }
            else
            if(c.get_gender().equalsIgnoreCase("O")){
                o_rad_btn.setChecked(true);
            }


            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(c.get_country()));




        }

        return view;
    }

    public void initializeUI(){

        btn_Confirm=(Button)view.findViewById(R.id.btnClick);

        btn_Confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

              add_entry(v);

            }
        });
        btn_DoB = (Button)view.findViewById(R.id.DBtn_1);
       /* btn_DoB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Set_Dob(v);

            }
        });*/
        dob_Text = (TextView)view.findViewById(R.id.dob_textview);
        country_List = getResources().getStringArray(R.array.list_of_countries);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(getActivity(), R.array.list_of_countries,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        imageHolder = (ImageView)view.findViewById(R.id.profile_Image);
        imageBtn = (Button)view.findViewById(R.id.btn_Image);
        imageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pickImage(v);
            }

        });
        first_Name = (EditText)view.findViewById(R.id.editText_first_name);
        last_Name = (EditText)view.findViewById(R.id.editText_last_name);
        m_rad_btn = (RadioButton)view.findViewById(R.id.Rbtn_Male);
        f_rad_btn = (RadioButton)view.findViewById(R.id.Rbtn_Female);
        o_rad_btn = (RadioButton)view.findViewById(R.id.Rbtn_Other);
        r_group = (RadioGroup)view.findViewById(R.id.r_Group_1);
    }


    public void pickImage(View View) {
        Log.i("Activity_Cycle", "pickImage");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }


                stream = getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0,stream2);
                byte[] inputData = stream2.toByteArray();
                imageAddress = Base64.encodeToString(inputData, Base64.DEFAULT);
                imageHolder.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }

    public void add_entry(View view){
        valid_f_name = first_Name.getText().toString();
        valid_l_name = last_Name.getText().toString();
        valid_country = spinner.getSelectedItem().toString();
        valid_dob = dob_Text.getText().toString();
        RadioButton selected = null;
        if ((RadioButton)getActivity().findViewById(r_group.getCheckedRadioButtonId()) != null){
        selected = (RadioButton)getActivity().findViewById(r_group.getCheckedRadioButtonId());
            valid_gender = (String)selected.getText().toString();
        }

        valid_profile_pic = imageAddress;
        if(valid_f_name !=null && valid_country != null && valid_l_name != null && valid_dob != null && valid_gender != null && valid_profile_pic != null) {

            if (update == false) {
                dbHandler.Add_Contact(new Contact(valid_f_name, valid_l_name, valid_country,
                        valid_dob, valid_gender, valid_profile_pic));
                Toast.makeText(getActivity(), "Contact Created", Toast.LENGTH_LONG).show();
            } else {
                dbHandler.Update_Contact(new Contact(USER_ID, valid_f_name, valid_l_name, valid_country,
                        valid_dob, valid_gender, valid_profile_pic));
                Toast.makeText(getActivity(), "Contact Updated", Toast.LENGTH_LONG).show();
            }

            com = (Interface) getActivity();
            com.SelectItem(0, 0);
        }
        else
        {
            Toast.makeText(getActivity(), "Incomplete form, try again.", Toast.LENGTH_LONG).show();
        }
    }

}

