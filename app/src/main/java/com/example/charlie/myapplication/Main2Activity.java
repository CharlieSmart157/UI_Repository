package com.example.charlie.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
ArrayList<String> items;
    ArrayAdapter<String>itemsAdapter;
    ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
     //   items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
       // items.add("Second Item");
        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();

    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,View item, int pos, long id){
            items.remove(pos);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
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
}
