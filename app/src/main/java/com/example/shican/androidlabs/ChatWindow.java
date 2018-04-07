package com.example.shican.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatView;
    EditText chatEditText;
    Button sendButton;
    ArrayList<String> chatMessage;
    FrameLayout frame;
    boolean isTablet;

    ContentValues cv;
    ChatAdapter messageAdapter;
    ChatDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor c;
    MessageFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatView = (ListView)findViewById(R.id.chatView);
        chatEditText = (EditText)findViewById(R.id.chatEditText);
        sendButton = (Button)findViewById(R.id.sendButton);
        frame = (FrameLayout)findViewById(R.id.frame);
        chatMessage = new ArrayList<>();
        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);
        cv = new ContentValues();
        helper = new ChatDatabaseHelper(this);
        db = helper.getWritableDatabase();

        if(frame==null){
            isTablet=false;
        }else{
            isTablet=true;
        }

        try{
            helper.onCreate(db);
        } catch (SQLiteException e){
            //helper.onUpgrade(db,helper.VERSION_NUM,helper.VERSION_NUM+1);
        }

        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID, helper.KEY_MESSAGE},
                null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            chatMessage.add(c.getString(c.getColumnIndex(helper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(helper.KEY_MESSAGE)));
            c.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
        for(int i=0; i<c.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,c.getColumnName(i));
        }

        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mf = new MessageFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("ID", messageAdapter.getItemId(i));
                bundle.putString("message", messageAdapter.getItem(i));
                bundle.putInt("position",i);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mf.setArguments(bundle);

                if(isTablet){
                    ft.replace(R.id.frame, mf);
                    ft.addToBackStack("");
                    ft.commit();
                }else{
                    Intent frameLayout = new Intent(ChatWindow.this, MessageDetails.class);
                    frameLayout.putExtras(bundle);
                    startActivityForResult(frameLayout, 77);
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatEditText.getText().toString();
                chatMessage.add(message);
                cv.put(helper.KEY_MESSAGE, message);
                db.insert(helper.TABLE_NAME, "null Replacement Value", cv);
                messageAdapter.notifyDataSetChanged();
                chatEditText.setText("");
                c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID, helper.KEY_MESSAGE},
                        null,null,null,null,null,null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 77){
            if(resultCode == RESULT_OK){
                //delete
                int position = data.getIntExtra("position",0);
                long ID = data.getLongExtra("ID", 0);
                this.deleteItem(position, ID);
            }
        }
    }

    public void deleteItem(int position, long ID){
        chatMessage.remove(position);
        db.delete(helper.TABLE_NAME,helper.KEY_ID+"="+ID, null );
        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID, helper.KEY_MESSAGE},
                null,null,null,null,null,null);
        this.messageAdapter.notifyDataSetChanged();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mf);
        ft.addToBackStack("");
        ft.commit();
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return chatMessage.size();
        }

        public String getItem(int position){
            return chatMessage.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 ==0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView)result.findViewById(R.id.messageText);
            message.setText(getItem(position));
            return result;
        }

        public long getId(int position){
            return position;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex(helper.KEY_ID));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
