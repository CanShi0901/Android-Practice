package com.example.shican.androidlabs;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;

/**
 * Created by shican on 2018-03-26.
 */
public  class MessageFragment extends Fragment {
    String userText;
    String userID;
    Button deleteButton;

    public void onCreate(Bundle b){
        super.onCreate(b);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View gui = inflater.inflate(R.layout.fragment_message_details, container, false);
        Bundle infoPassed = getArguments();
        TextView ID = (TextView)gui.findViewById(R.id.senderID);
        TextView userMessage = (TextView)gui.findViewById(R.id.thisMessage);
        deleteButton = (Button)gui.findViewById(R.id.deleteButton);
        userText = infoPassed.getString("message");
        userID = Long.toString(infoPassed.getLong("ID"));

        ID.setText("ID: "+userID);
        userMessage.setText("Message: "+userText);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = infoPassed.getInt("position");
                ((ChatWindow)getActivity()).deleteItem(position, infoPassed.getLong("ID"));
            }
        });
        return gui;
    }

}