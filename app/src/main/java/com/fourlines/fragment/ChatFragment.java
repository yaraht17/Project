package com.fourlines.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourlines.adapter.ChatAdapter;
import com.fourlines.data.DatabaseChat;
import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.model.ChatMessage;
import com.fourlines.model.SickItem;
import com.fourlines.volley.MySingleton;
import com.fourlines.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChatFragment extends Fragment implements View.OnClickListener {

    Typeface font_awesome;
    private View rootView;

    private static final int REQUEST_CODE = 1234;

    private ChatAdapter chatAdapter;
    private ListView listView;// list view message
    private EditText chatText;// input message send
    private TextView imgSend, imgVoice;// image send message
    private boolean side = false;
    private ArrayList<ChatMessage> items = new ArrayList<ChatMessage>();
    private ArrayList<SickItem> sickList = new ArrayList<>();
    private ArrayList<Integer> countYesSickList = new ArrayList<>();
    private int countYesSick = 0;
    private int countNoSick = 0;
    private String questionStart = "";
    private int locationSymptom = 0;
    private int locationSick = 0;
    private String symptom;
    private DatabaseChat db;
    private long lastId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat,
                container, false);

        font_awesome = Typeface.createFromAsset(rootView.getContext().getAssets(), "fontawesome-webfont.ttf");
        imgSend = (TextView) rootView.findViewById(R.id.btnSendMess);
        imgVoice = (TextView) rootView.findViewById(R.id.btnVoice);
        listView = (ListView) rootView.findViewById(R.id.msgview);
        chatText = (EditText) rootView.findViewById(R.id.edt_msg);


        imgSend.setTypeface(font_awesome);
        imgVoice.setTypeface(font_awesome);


        chatText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }

            private void hideKeyboard() {
                if (chatText != null) {
                    InputMethodManager imanager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imanager.hideSoftInputFromWindow(chatText.getWindowToken(), 0);

                }

            }
        });
        db = new DatabaseChat(getContext());
        lastId = db.getLastId();
        if (lastId <= Var.PAGE) {
            items = db.getChatHistoryList(1, lastId);
        } else {
            items = db.getChatHistoryList(lastId - Var.PAGE + 1, lastId);
        }
        db.closeBD();

        chatAdapter = new ChatAdapter(rootView.getContext(),
                R.layout.item_chat_right, items);
        listView.setAdapter(chatAdapter);
        imgSend.setOnClickListener(this);
        imgVoice.setOnClickListener(this);
        return rootView;


    }

    private void sendChatMessage(final String message) {// add message to list view
        if (message == null || message.equals("")) return;
        chatText.setText("");
        lastId++;
        items.add(new ChatMessage(lastId, false, message));
        addMessgeToDb(new ChatMessage(lastId, false, message));
        loadChatAdapter();
        if (sickList.size() == 0) {
            try {
                getMessageDoctor(message, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject respond) {
                        try {
                            if (respond.getString(Var.CHAT_STATUS).equals("success")) {
                                JSONObject object = respond.getJSONObject(Var.CHAT_RESULT);
                                if (object.getInt(Var.CHAT_TYPE) == 1) {
                                    questionStart = message.toLowerCase();
                                    sickList = convertResponseToArray(object);
                                    if (sickList.size() != 0) {
                                        while (true) {
                                            symptom = sickList.get(locationSick).getSymptoms().get(locationSymptom);
                                            if (questionStart.indexOf(symptom) == -1) {
                                                createQuestion();
                                                break;
                                            } else {
                                                locationSymptom++;
                                                countYesSick++;
                                                check();
                                            }
                                        }
                                    } else {
                                        lastId++;
                                        items.add(new ChatMessage(lastId, true, Var.ANSWER_RADOM[random(Var.ANSWER_RADOM.length)]));
                                        addMessgeToDb(new ChatMessage(lastId, true, Var.ANSWER_RADOM[random(Var.ANSWER_RADOM.length)]));
                                    }
                                } else if (object.getInt(Var.CHAT_TYPE) == 2) {
                                    lastId++;
                                    addMessgeToDb(new ChatMessage(lastId, true, object.getString(Var.CHAT_DATA)));
                                    items.add(new ChatMessage(lastId, true, object.getString(Var.CHAT_DATA)));
                                } else {
                                    lastId++;
                                    addMessgeToDb(new ChatMessage(lastId, true, object.getString(Var.CHAT_DATA)));
                                    items.add(new ChatMessage(lastId, true, object.getString(Var.CHAT_DATA)));
                                }
                                loadChatAdapter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            int tam = 0;
            for (int i = 0; i < Var.TRUE.length; i++) {
                if (message.toLowerCase().indexOf(Var.TRUE[i]) != -1) {
                    countYesSick++;
                    questionStart += " " + symptom;
                    tam = 1;
                    break;
                }
            }
            if (tam == 0) {
                for (int i = 0; i < Var.FALSE.length; i++) {
                    if (message.toLowerCase().indexOf(Var.FALSE[i]) != -1) {
                        tam = 1;
                        countNoSick++;
                        questionStart += " " + symptom;
                        nextSick(countNoSick);
                        break;
                    }
                }
                if (tam == 0) {
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, "Bạn nên trả lời đúng vào chủ đề của câu hỏi"));
                    items.add(new ChatMessage(lastId, true, "Bạn nên trả lời đúng vào chủ đề của câu hỏi"));
                } else {
                    locationSymptom++;
                    check();
                }
            } else {
                locationSymptom++;
                check();
            }
            if (sickList.size() != 0) {
                createQuestion();
                loadChatAdapter();
            }
        }
    }

    public void loadChatHistory() {
        long location = items.get(0).id;
        ArrayList<ChatMessage> messageList = new ArrayList<>();
        db = new DatabaseChat(getContext());
        messageList = db.getChatHistoryList(location - Var.PAGE + 1, location);
        ArrayList<ChatMessage> tam = new ArrayList<>();
        tam.addAll(messageList);
        tam.addAll(items);
        items = new ArrayList<>();
        items.addAll(tam);
    }

    public void check() {
        if (locationSymptom == sickList.get(locationSick).getSymptoms().size()) {
            countYesSickList.add(countYesSick);
            countYesSick = 0;
            countNoSick = 0;
            locationSymptom = 0;
            locationSick++;
            if (locationSick == sickList.size()) {
                int location = mathResult(countYesSickList, sickList);
                if (location == -1) {
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, "Bạn nên đến bệnh viện làm các xét nghiệm để " +
                            "biết rõ hơn về tình hình sức khỏe của mình dựa vào những gì bạn " +
                            "mô tả tôi không thể biết được bạn bị bệnh gì"));
                    items.add(new ChatMessage(lastId, true, "Bạn nên đến bệnh viện làm các xét nghiệm để " +
                            "biết rõ hơn về tình hình sức khỏe của mình dựa vào những gì bạn " +
                            "mô tả tôi không thể biết được bạn bị bệnh gì"));
                } else {
                    String result = "Bạn bị bệnh "
                            + sickList.get(location).getName();
                    String treatment = sickList.get(location).getTreatment();
                    String food = sickList.get(location).getFoods();
                    String banFoods = sickList.get(location).getBanFoods();
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, result));
                    items.add(new ChatMessage(lastId, true, result));
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, treatment));
                    items.add(new ChatMessage(lastId, true, treatment));
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, food));
                    items.add(new ChatMessage(lastId, true, food));
                    lastId++;
                    addMessgeToDb(new ChatMessage(lastId, true, banFoods));
                    items.add(new ChatMessage(lastId, true, banFoods));
                }
                sickList = new ArrayList<SickItem>();
                countYesSickList = new ArrayList<Integer>();
                locationSick = 0;
            }
        }
    }

    public void nextSick(int countNoSick) {
        double result = ((double) countNoSick / (double) sickList.get(locationSick).getSymptoms().size()) * 100;
        if (result > 50) {
            locationSymptom = sickList.get(locationSick).getSymptoms().size() - 1;
        }
    }

    public void addMessgeToDb(ChatMessage item) {
        db = new DatabaseChat(getContext());
        db.insertChatHistoryItem(item);
        db.closeBD();
    }

    public void createQuestion() {
        while (true) {
            symptom = sickList.get(locationSick).getSymptoms().get(locationSymptom);
            if (questionStart.indexOf(symptom) == -1) {
                lastId++;
                items.add(new ChatMessage(lastId, true, Var.QUESTION_RADOM[random(Var.QUESTION_RADOM.length)]
                        + " " + symptom + " không?"));
                addMessgeToDb(new ChatMessage(lastId, true, Var.QUESTION_RADOM[random(Var.QUESTION_RADOM.length)]
                        + " " + symptom + " không?"));
                break;
            } else {
                locationSymptom++;
                countYesSick++;
                check();
                if (sickList.size() == 0) {
                    break;
                }
            }
        }
    }

    public void loadChatAdapter() {

        chatAdapter = new ChatAdapter(rootView.getContext(), R.layout.item_chat_right, items);
        //chatText.setText("");

        // to scroll the list view to bottom on data change
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatAdapter);

    }

    public int mathResult(ArrayList<Integer> countYesSickList, ArrayList<SickItem> sickList) {
        double max = 0;
        int location = 0;
        for (int i = 0; i < countYesSickList.size(); i++) {
            double tmp = ((double) countYesSickList.get(i)) / ((double) sickList.get(i).getSymptoms().size());
            if (max < tmp) {
                max = tmp;
                location = i;
            }
        }
        if (max < 0.5) {
            return -1;
        }
        return location;
    }

    //convert json array to arraylist
    public ArrayList<String> convertToList(JSONArray a) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < a.length(); i++) {
            try {
                list.add(a.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    //convert json object to array list
    public ArrayList<SickItem> convertResponseToArray(JSONObject response) {
        ArrayList<SickItem> list = new ArrayList<>();

        try {
            JSONArray array = response.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                final SickItem sickItem = new SickItem(object.getString(Var.ID), object.getString(Var.SICK_NAME),
                        object.getString(Var.SICK_TYPE), object.getString(Var.SICK_REASON), object.getString(Var.SICK_FOODS),
                        object.getString(Var.SICK_BAN_FOODS), convertToList(object.getJSONArray(Var.SICK_SYMPTOMS)),
                        object.getString(Var.SICK_TREATMENT), object.getString(Var.SICK_DESCRIPTION), object.getString(Var.SICK_PREVENTION));
                list.add(sickItem);
                Log.d("TienDH", sickItem.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int random(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    private void getMessageDoctor(String message, final VolleyCallback callback) throws UnsupportedEncodingException {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.URL_SEND_QUESTION + URLEncoder.encode(message, "utf-8"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
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
            Toast.makeText(rootView.getContext(),
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

        }
    }


}