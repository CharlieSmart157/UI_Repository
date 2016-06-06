package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler{

//1 Connect xml to java file
    //2 Convert your xml objects to java objects: Typecasting
    //3 Initialize your UI views
    private static final int REQUEST_CODE = 1;
    EditText first_Name, last_Name;
    TextView dob_Text;
    Button btn_Confirm, btn_DoB;
    int counter =0, USER_ID;
    boolean update = false;
    String[] country_List;
    Spinner spinner;
    Bitmap bitmap;
    ImageView imageBtn;
    String imageAddress;
    RadioButton m_rad_btn, f_rad_btn, o_rad_btn;
    RadioGroup r_group;
    String  valid_profile_pic = null, valid_f_name = null, valid_l_name= null, valid_dob= null, valid_country= null, valid_gender = null;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();

        String called_from = getIntent().getStringExtra("called");

        if (called_from.equalsIgnoreCase("update")) {


          USER_ID = Integer.parseInt(getIntent().getStringExtra("USER_ID"));
            update = true;
            Contact c = dbHandler.Get_Contact(USER_ID);

            first_Name.setText(c.get_nameFirst());
            last_Name.setText(c.get_nameLast());
            dob_Text.setText(c.get_dob());
            byte[] imageOutput = Base64.decode(c.get_picture().getBytes(),Base64.DEFAULT);
            Bitmap img = BitmapFactory.decodeByteArray(imageOutput,0,imageOutput.length);
            imageBtn.setImageBitmap(img);
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

        Log.i("Activity_Cycle", "onCreate CALLED");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity_Cycle", "onPause CALLED");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity_Cycle", "onStop CALLED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity_Cycle", "onResume CALLED");
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.i("Activity_Cycle", "onDestroy CALLED");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("Activity_Cycle", "onStart CALLED");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("Activity_Cycle", "onRestart CALLED");
    }


    public void initializeUI(){

        btn_Confirm=(Button)findViewById(R.id.btnClick);
        btn_DoB = (Button)findViewById(R.id.DBtn_1);
        dob_Text = (TextView)findViewById(R.id.dob_textview);
        country_List = getResources().getStringArray(R.array.list_of_countries);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.list_of_countries,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        imageBtn = (ImageView)findViewById(R.id.profile_Image);
        first_Name = (EditText)findViewById(R.id.editText_first_name);
        last_Name = (EditText)findViewById(R.id.editText_last_name);
        m_rad_btn = (RadioButton)findViewById(R.id.Rbtn_Male);
        f_rad_btn = (RadioButton)findViewById(R.id.Rbtn_Female);
        o_rad_btn = (RadioButton)findViewById(R.id.Rbtn_Other);
        r_group = (RadioGroup)findViewById(R.id.r_Group_1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }


                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0,stream2);
                byte[] inputData = stream2.toByteArray();
                imageAddress = Base64.encodeToString(inputData, Base64.DEFAULT);
                imageBtn.setImageBitmap(bitmap);

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

    public void Increment(View view){
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setMessage("Done?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
                    }
                });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    }
                });


        dialog.show();
    }



    public void Set_Dob(View v){
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setYearOptional(true);
        dpb.show();
    }

@Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;
        dob_Text.setText(getString(R.string.date_picker_result_value, year, monthOfYear, dayOfMonth));
    }

    public void add_entry(View view){
        valid_f_name = first_Name.getText().toString();
        valid_l_name = last_Name.getText().toString();
        valid_country = spinner.getSelectedItem().toString();
        valid_dob = dob_Text.getText().toString();
        RadioButton selected = (RadioButton)findViewById(r_group.getCheckedRadioButtonId());
        valid_gender = (String)selected.getText().toString();
        valid_profile_pic = imageAddress;
        if(update == false) {
            dbHandler.Add_Contact(new Contact(valid_f_name, valid_l_name, valid_country,
                    valid_dob, valid_gender, valid_profile_pic));
            Toast.makeText(getApplicationContext(), "Contact Created", Toast.LENGTH_LONG).show();
        }
        else {
            dbHandler.Update_Contact(new Contact(USER_ID, valid_f_name, valid_l_name, valid_country,
                    valid_dob, valid_gender, valid_profile_pic));
            Toast.makeText(getApplicationContext(), "Contact Updated", Toast.LENGTH_LONG).show();
        }
        Intent view_user = new Intent(MainActivity.this,
                Main2Activity.class);
        view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(view_user);
        finish();
    }


}







