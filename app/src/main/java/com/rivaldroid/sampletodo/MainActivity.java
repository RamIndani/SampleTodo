package com.rivaldroid.sampletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rivaldroid.sampletodo.model.TodoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity is the entry point for the application
 */
public class MainActivity extends AppCompatActivity {
    //stores all the to do items
    List<String> items;
    // Adapter used with list view
    ArrayAdapter<String> itemsAdapter;
    //ListView to display the list of all the elements from to do list and bound with ListView defined in XML layout
    ListView lvItems;
    //REQUEST_CODE is used to verify that onActivityResult is for the startActivityForResult that we have started.
    private final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bind ListView defined in XML
        lvItems = (ListView) findViewById(R.id.lvItems);
        //Read items from todo.txt if there are any
        readItems();
        //Create new instance of the ArrayAdater to bind with ListView and display elements
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        //Set adapter to ListView
        lvItems.setAdapter(itemsAdapter);
        //Setup listeners on the ListView to handle events like setOnItemLongClickListener and setOnItemClickListener
        setupListViewListener();
    }

    /**
     * Listeners for ListView items are set here
     * OnItemLongClickListener is to delete the item from the list.
     * OnItemClickListener is to update the to do item in the list.
     */
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id){
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long id){
                TodoItem editTodoItem = new TodoItem(items.get(position), position);
                Intent editItemActivityIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemActivityIntent.putExtra("TodoItem",editTodoItem);
                startActivityForResult(editItemActivityIntent, REQUEST_CODE);
            }
        });
    }

    /**
     * Handle activity result once the EditItemActivity returns the updated results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data){
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            TodoItem updatedTodoItem = (TodoItem) data.getExtras().getSerializable("TodoItem");
            items.set(updatedTodoItem.getPosition(), updatedTodoItem.getTodoItemData());
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    /**
     * Handle Add new item action on button
     * To do item can not be empty
     * @param view
     */
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(!itemText.isEmpty()) {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
        }else{
            Toast.makeText(this, getResources().getString(R.string.todo_item_empty), Toast.LENGTH_SHORT).show();
            etNewItem.setError(getResources().getString(R.string.todo_item_empty));
        }
    }

    /**
     * Read all the records from todo.txt and create ArrayList to display on the screen
     */
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(IOException ioException){
            items = new ArrayList<String>();
        }
    }

    /**
     * Save the new to do item to todo.txt
     */
    private void writeItems(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
