package com.example.charlie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler{

//1 Connect xml to java file
    //2 Convert your xml objects to java objects: Typecasting
    //3 Initialize your UI views
    private static final int REQUEST_CODE = 1;
    TextView dob_Text;
    Button btn_Confirm;
    Button btn_DoB;
    int counter =0;
    String[] country_List;
    Spinner spinner;
    Bitmap bitmap;
    ImageView imageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
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
}







