package com.example.venkatesh.contactlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by venkatesh on 7/6/16.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder> {

    private ArrayList<DataContacts> dataSet;
    Context context;
    MyViewHolder myViewHolder;


    public CustomRecyclerAdapter(Context context ,ArrayList<DataContacts> data)
    {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public CustomRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
         myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerAdapter.MyViewHolder holder, final int position) {

        final DataContacts dataContacts = dataSet.get(position);

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewMobile;

        textViewName.setText(dataSet.get(position).dc_name);
        textViewVersion.setText(dataSet.get(position).dc_mobile);

        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("position:", position + "");
                PopupMenu popup = new PopupMenu(context, v);
                popup.inflate(R.menu.list_item_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.menu_del:
                                alertForDelete(position, dataContacts);
                                break;

                            case R.id.menu_edit:
                                DatabaseHandler dbh = new DatabaseHandler(context);
                                DataContacts data = dbh.editContact(dataContacts.dc_mobile) ;
                                popupForEdit(data,position);
                                break;
                        }
                        return true;
                    }

                });
            }
        });

    }

    public void alertForDelete(final int position, final DataContacts dataContacts)
    {
        AlertDialog.Builder alertBeforeDelete = new AlertDialog.Builder(context);
        View alertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        alertBeforeDelete.setView(alertView);
        alertBeforeDelete.setMessage("Are you sure you want to delete it ?");

        alertBeforeDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Deleted.. ", Toast.LENGTH_SHORT).show();
                             DatabaseHandler dbh = new DatabaseHandler(context);
                String selectedName = dataContacts.dc_name;
                                Log.d("selectedName:", selectedName);
                                dbh.deleteContact(selectedName);
                                dataSet.remove(position);
                                notifyDataSetChanged();
            }
        });

        alertBeforeDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialogCreated = alertBeforeDelete.create();
        dialogCreated.show();
    }


    public void popupForEdit(final DataContacts data, final int pos) {

            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    //        LayoutInflater inflater = context.getLayoutInflater();
    //        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
            final EditText edt1 =(EditText) dialogView.findViewById(R.id.edit1);
            final EditText edt2 =(EditText) dialogView.findViewById(R.id.edit2);

            dialogBuilder.setView(dialogView);
            dialogBuilder.setMessage("Editing...");
            edt1.setText(data.dc_name);
            edt2.setText(data.dc_mobile);
            dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String  name =  edt1.getText().toString();
                String number  =  edt2.getText().toString();

                Log.d("values", name + number);
                DatabaseHandler db = new DatabaseHandler(context);
                boolean isOk =   db.updateDataForContact(name, number, data.dc_id);
                if (isOk)
                {
                    Toast.makeText(context, "Contact Updated..", Toast.LENGTH_SHORT).show();
                    dataSet = db.getContactsData();
                    notifyDataSetChanged();
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName;
        TextView textViewMobile;
        View view1;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.name);
            this.textViewMobile = (TextView) itemView.findViewById(R.id.number);
            this.view1 = itemView.findViewById(R.id.list_item_menu);
        }
    }

}
