package com.cst2335.duon0065;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import org.w3c.dom.Text;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "CHATROOM_ACTIVITY";
    ArrayList<Message> messageList = new ArrayList<>();
    EditText msgText;
    ListAdapter adapter;
    SQLiteDatabase db;

    public static final String ITEM_ID = "ITEM ID";
    public static final String ITEM_SELECTED = "ITEM SELECTED";
    public static final String ITEM_POSITION = "ITEM POSITION";
    public static String IS_SEND = "IS SEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        boolean isTablet = findViewById(R.id.frameLayout) != null;
        loadDatabase();

        ListView chatLog = findViewById(R.id.messageList);
        chatLog.setAdapter(adapter = new ListAdapter());

        msgText = findViewById(R.id.editText3);
        Button sendButton = findViewById(R.id.sendbutton);
        Button receiveButton = findViewById(R.id.receivebutton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentTxtMsg = msgText.getText().toString();
                ContentValues sentContentText = new ContentValues();
                sentContentText.put(DatabaseOpener.COL_CONTENT, sentTxtMsg);
                sentContentText.put(DatabaseOpener.COL_SENT, 1);
                long id = db.insert(DatabaseOpener.TABLE_NAME, null, sentContentText);
                Message sendMsg = new Message(sentTxtMsg, 1, id);
                messageList.add(sendMsg);
                adapter.notifyDataSetChanged();
                msgText.setText("");
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedTxtMsg = msgText.getText().toString();
                ContentValues receivedContentText = new ContentValues();
                receivedContentText.put(DatabaseOpener.COL_CONTENT, receivedTxtMsg);
                receivedContentText.put(DatabaseOpener.COL_SENT, 0);
                long id = db.insert(DatabaseOpener.TABLE_NAME, null, receivedContentText);
                Message receiveMsg = new Message(receivedTxtMsg, 0, id);
                messageList.add(receiveMsg);
                adapter.notifyDataSetChanged();
                msgText.setText("");
            }
        });

        chatLog.setOnItemLongClickListener((p, b, pos, id) -> {

            long rowId = adapter.getItemId(pos);
            Message selectedMessage = messageList.get(pos);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("WARNING: Delete this row?")
                    .setMessage("The selected row number is: " + rowId +
                            "\nThe Database ID is: " + rowId)
                    .setPositiveButton("DELETE", (click, arg) -> {
                        deleteMessage(selectedMessage);
                        messageList.remove(messageList.get(pos));
                        adapter.notifyDataSetChanged();

                        if(isTablet) {
                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .remove(fragment)
                                    .commit();
                        }

                    })
                    .setNegativeButton("CANCEL", (click, arg) -> {
                    })
                    .create().show();
            return true;
        });

        chatLog.setOnItemClickListener((list, view, pos, id) -> {
            Message selectedMessage = messageList.get(pos);

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, selectedMessage.getMsg());
            dataToPass.putInt(ITEM_POSITION, pos);
            dataToPass.putLong(ITEM_ID, selectedMessage.getId());
            dataToPass.putInt(IS_SEND, selectedMessage.msgType);

            if(isTablet) {
                DetailsFragment dFragment = new DetailsFragment();
                dFragment.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, dFragment)
                        .commit();
            } else {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
        });

    }

    private void loadDatabase() {

        DatabaseOpener databaseOpener = new DatabaseOpener(this);
        db = databaseOpener.getWritableDatabase();

        String[] columns = {DatabaseOpener.COL_ID, DatabaseOpener.COL_CONTENT, DatabaseOpener.COL_SENT};
        Cursor results = db.query(false, DatabaseOpener.TABLE_NAME, columns, null,
                null, null, null, null, null);
        int contentIndex = results.getColumnIndex(DatabaseOpener.COL_CONTENT);
        int idIndex = results.getColumnIndex(DatabaseOpener.COL_ID);
        int isSendIndex = results.getColumnIndex(DatabaseOpener.COL_SENT);


        while (results.moveToNext()) {
            String content = results.getString(contentIndex);
            long id = results.getLong(idIndex);
            int isSend = results.getInt(isSendIndex);

            messageList.add(new Message(content, isSend, id));
        }
        printCursor(results, DatabaseOpener.VERSION_NUM);
    }

    protected void deleteMessage(Message msg) {
        db.delete(DatabaseOpener.TABLE_NAME, DatabaseOpener.COL_ID + "= ?",
                new String[]{Long.toString(msg.getId())});
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

            if (messageList.get(position).msgType == 1) {
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
            if (messageList.get(position).msgType == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class Message {
        String msg;
        int msgType;
        long id;

        public Message(String msg, int sent, long id) {
            this.msg = msg;
            this.msgType = sent;
            this.id = id;
        }

        public int getMsgType() {
            return msgType;
        }

        public long getId() {
            return id;
        }

        public String getMsg() {
            return msg;
        }
    }

    public void printCursor(Cursor cursor, int v) {
        cursor.moveToFirst();
        Log.e(TAG, "DATABASE VERSION: " + v);
        Log.e(TAG, "Columns Quantity in the cursor is: " + cursor.getColumnCount());
        Log.e(TAG, "Amount of rows in cursor is: " + cursor.getCount());
        while (cursor.moveToNext()) {
            Log.e(TAG, "Name of Column in cursor is: " + cursor.getString(0));
            Log.e(TAG, "List Row Message: " + cursor.getString(1));
            Log.e(TAG, "List row is a sent or receive message? (1 for send, 0 for receive): " + cursor.getString(2));
        }
    }
}