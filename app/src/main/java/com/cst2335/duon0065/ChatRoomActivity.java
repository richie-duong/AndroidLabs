package com.cst2335.duon0065;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import org.w3c.dom.Text;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> messageList = new ArrayList<>( );
    EditText msgText;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView chatLog = findViewById(R.id.messageList);
        chatLog.setAdapter(adapter = new ListAdapter());
        msgText = findViewById(R.id.editText3);
        Button sendButton = findViewById(R.id.sendbutton);
        Button receiveButton = findViewById(R.id.receivebutton);

        sendButton.setOnClickListener(v -> {
            Message sendMsg = new Message(msgText.getText().toString());
            messageList.add(sendMsg);
            sendMsg.sent = true;
            adapter.notifyDataSetChanged();
            msgText.setText("");
        });

        receiveButton.setOnClickListener(v -> {
            Message receiveMsg = new Message(msgText.getText().toString());
            messageList.add(receiveMsg);
            receiveMsg.sent = false;
            adapter.notifyDataSetChanged();
            msgText.setText("");
        });

        chatLog.setOnItemLongClickListener( (p, b, pos, id) -> {

            long rowId = adapter.getItemId(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete row?")

                    .setMessage("Selected row: " + rowId +
                            "\nDatabase id: " + rowId)

                    .setPositiveButton("Yes", (click, arg) -> {
                        messageList.remove(messageList.get(pos));
                        adapter.notifyDataSetChanged();
                    })

                    .setNegativeButton("No", (click, arg) -> { })
                    .create().show();
            return true;
        });

    }

    private class ListAdapter extends BaseAdapter {

        public int getCount() {
            return messageList.size();
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            if (messageList.get(position).sent) {
                convertView = inflater.inflate(R.layout.sendlayout, parent, false);
                TextView textView = convertView.findViewById(R.id.textPlacement);
                textView.setText(messageList.get(position).getMsg());
            } else {
                convertView = inflater.inflate(R.layout.reclayout, parent, false);
                TextView textView = convertView.findViewById(R.id.textPlacement);
                getViewTypeCount();
                textView.setText(messageList.get(position).getMsg());
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(messageList.get(position).sent) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class Message {
        String msg;
        boolean sent;

        public Message(String message) {
            this.msg = message;
        }

        public String getMsg() {
            return msg;
        }
    }


}