package com.example.charlie.myapplication;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

/**
 * Created by Charlie on 06/06/2016.
 */
public class Main_Fragment_Holder extends AppCompatActivity implements Interface, DatePickerDialogFragment.DatePickerDialogHandler{
    // action bar
    private ActionBar actionBar;
    private boolean isEditing = false;
    MenuItem add_btn, save_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout_holder);
        actionBar = getActionBar();
        //actionBar.setIcon(R.drawable.ic_launcher);
       SelectItem(0,0);



    }

    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        add_btn=menu.findItem(R.id.action_add);

        return super.onCreateOptionsMenu(menu);
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


    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_add:
                isEditing = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.invalidateOptionsMenu();
                }
                SelectItem(1,0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isEditing) {
            add_btn.setVisible(false); // hide Add button
           // save_btn.setVisible(true); // show the Save button
        } else if (!isEditing) {
            add_btn.setVisible(true); // show Add button
          //  save_btn.setVisible(false); // hide the Save button
        }

        return true;
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
