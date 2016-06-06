package com.example.charlie.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

/**
 * Created by Charlie on 06/06/2016.
 */
public class Main_Fragment_Holder extends AppCompatActivity implements Interface, DatePickerDialogFragment.DatePickerDialogHandler{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout_holder);

       SelectItem(0,0);



    }


    public void Set_Dob(View v){
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setYearOptional(true);
        dpb.show();
    }

    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;
        TextView dob_Text = (TextView)findViewById(R.id.dob_textview);

        dob_Text.setText(getString(R.string.date_picker_result_value, year, monthOfYear, dayOfMonth));
    }

    public void SelectItem(int position, int user){
        Fragment fragment = null;
        Bundle args = new Bundle();
        Log.i("SelectItem",""+user);
        //Insert Switch Statement here
        switch(position){
            case 0:
                fragment = new List_Fragment();
                break;
            case 1:
                fragment = new Form_Fragment();
                args.putString(Form_Fragment.ENTRY_MODE, "add");
                break;
            case 2:
                fragment = new Form_Fragment();
                args.putString(Form_Fragment.ENTRY_MODE, "edit");
                args.putInt(Form_Fragment.ITEM_ID, user);
                break;

        }
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.fragment_layout_holder, fragment).commit();


    }


    public void onAddItem(View v){

        SelectItem(1, 0);
    }
}
