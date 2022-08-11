package com.cst2335.duon0065;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID);

        View result = inflater.inflate(R.layout.fragment_details, container, false);

        TextView message = result.findViewById(R.id.textView4);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        TextView idView = result.findViewById(R.id.textView3);
        idView.setText("ID=" + id);

        CheckBox isSent = result.findViewById(R.id.checkBox);
        if(dataFromActivity.getInt(ChatRoomActivity.IS_SEND) == 1) {
            isSent.setChecked(true);
        }

        Button hideBtn = result.findViewById(R.id.button);

        hideBtn.setOnClickListener( v -> {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity) context;
    }
}