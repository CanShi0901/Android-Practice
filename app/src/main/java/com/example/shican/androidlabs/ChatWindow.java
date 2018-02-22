package com.example.shican.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView chatView;
    EditText chatEditText;
    Button sendButton;
    ArrayList<String> chatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatView = (ListView)findViewById(R.id.chatView);
        chatEditText = (EditText)findViewById(R.id.chatEditText);
        sendButton = (Button)findViewById(R.id.sendButton);
        chatMessage = new ArrayList<>();
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMessage.add(chatEditText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                chatEditText.setText("");
            }
        });



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

    }


}
