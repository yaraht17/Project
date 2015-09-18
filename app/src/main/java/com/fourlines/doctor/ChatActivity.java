package com.fourlines.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourlines.adapter.ChatAdapter;
import com.fourlines.data.Var;
import com.fourlines.model.ChatMessage;
import com.fourlines.model.DoctorItem;

import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1234;

    private ChatAdapter chatAdapter;
    private ListView listView;// list view message
    private EditText chatText;// input message send
    private ImageView imgSend, imgVoice;// image send message
    private boolean side = false;
    private ArrayList<ChatMessage> items = new ArrayList<ChatMessage>();
    private Button btnBack;
    private TextView txtDoctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imgSend = (ImageView) findViewById(R.id.btnSendMess);
        imgVoice = (ImageView) findViewById(R.id.btnVoice);
        listView = (ListView) findViewById(R.id.msgview);
        chatText = (EditText) findViewById(R.id.edt_msg);
        btnBack = (Button) findViewById(R.id.btnBack);
        txtDoctorName = (TextView) findViewById(R.id.txtDoctorName);

        DoctorItem doctorItem = (DoctorItem) getIntent().getSerializableExtra(Var.DOCTOR_KEY);
        txtDoctorName.setText(doctorItem.getDoctorName());

        chatAdapter = new ChatAdapter(this,
                R.layout.item_chat_right, items);
        listView.setAdapter(chatAdapter);
        imgSend.setOnClickListener(this);
        imgVoice.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void sendChatMessage(String message) {// add message to list view
        // message
        if (message == null || message.equals("")) return;
        items.add(new ChatMessage(false, message, ""));
        items.add(new ChatMessage(true, message, ""));
        chatAdapter = new ChatAdapter(this, R.layout.item_chat_right, items);
        chatText.setText("");

        // to scroll the list view to bottom on data change
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatAdapter);


    }

    private void startVoiceRecognitionActivity() {// use google
        // voice in
        // phone
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bạn cần tư vấn....");
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Điện thoại của bạn không hỗ trợ Google Voice", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition
            // engine thought it heard
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() != 0) {
                sendChatMessage(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSendMess:
                sendChatMessage(chatText.getText().toString());
                break;
            case R.id.btnVoice:
                startVoiceRecognitionActivity();
                break;
            case R.id.btnBack:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
                break;

        }
    }
}
