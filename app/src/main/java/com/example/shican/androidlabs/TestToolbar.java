package com.example.shican.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    String item1Message = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "My message!!!!!!!!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()){
            case R.id.toolOne:
                Snackbar.make(findViewById(R.id.toolbar), item1Message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.toolTwo:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialogTitle);
                builder.setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.toolThree:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View newMessageView = inflater.inflate(R.layout.dialog_new_message, null);
                builder2.setView(newMessageView);
                builder2.setPositiveButton(R.string.newMessage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText newMessage = (EditText)newMessageView.findViewById(R.id.new_message);
                                item1Message = newMessage.getText().toString();
                                Toast.makeText(TestToolbar.this,"Message saved in item 1",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                builder2.setNegativeButton(R.string.negativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder2.create().show();
                break;
            case R.id.about:
                Toast.makeText
                        (TestToolbar.this, "Version 1.0, by Csn Shi",
                                Toast.LENGTH_LONG).show();
            default:
        }
        return true;
    }

}
