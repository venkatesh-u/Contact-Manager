package com.example.venkatesh.contactlist;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edt1,edt2;
    String name,number;
    DatabaseHandler dbh;
   public static ArrayList<DataContacts> contactsData;
    CustomRecyclerAdapter adapter;
    public static RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        dbh = new DatabaseHandler(this);

//        myOnClickListener = new MyOnClickListener(this);
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContact();
            }
        });
        contactsData = dbh.getContactsData();
        Log.d("contactsDataSize:", contactsData.size() + "");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomRecyclerAdapter(MainActivity.this, contactsData);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);



    }

    private void createContact()
    {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
       dialogView.setBackgroundColor(Color.DKGRAY);

       edt1 =(EditText) dialogView.findViewById(R.id.edit1);
         edt2 =(EditText) dialogView.findViewById(R.id.edit2);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setMessage("Add Contact..");

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                name =  edt1.getText().toString();
                    number    =  edt2.getText().toString();

                Log.d("values", name + number);
                boolean isOk =   dbh.insertDataForContact(name, number);
                if (isOk)
                {
                    Toast.makeText(MainActivity.this, "Added..", Toast.LENGTH_SHORT).show();
                    contactsData = dbh.getContactsData();
                    adapter = new CustomRecyclerAdapter(MainActivity.this, contactsData);
                    recyclerView.setAdapter(adapter);
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }


//    private class MyOnClickListener implements View.OnClickListener
//    {
//        private final Context context;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            removeItem(v);
//        }
//
//        private void removeItem(View v)
//        {
//            int selectedItemPosition = recyclerView.getChildPosition(v);
//            Log.d("selectedPosition:", selectedItemPosition+"");
//
//            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.name);
//            String selectedName = (String) textViewName.getText();
//
//           Log.d("selectedName:", selectedName);
//
//            dbh.deleteContact(selectedName);
//
//            contactsData.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
//        }
//    }


//    private class MyOnClickListener implements View.OnClickListener
//    {
//
//        private final Context context;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            Log.d("clicked","menu ok");
//            removeItem(v);
//        }
//
//        private void removeItem(View v)
//        {
//           Log.d(" v.getId():", recyclerView.getChildPosition(v) + "" );
////            int selectedItemPosition = recyclerView.getChildPosition(v);
////            Log.d("selectedPosition:", selectedItemPosition+"");
//
//            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.name);
//            String selectedName = (String) textViewName.getText();
//
//            Log.d("selectedName:", selectedName);
//
//            dbh.deleteContact(selectedName);
//
//            contactsData.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            case  R.id.add_Contact:
                createContact();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
