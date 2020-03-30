package cosc341.group4.prescriptionpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private TextView date;
    private boolean showingDay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        String patient = intent.getStringExtra(PatientsActivity.PATIENT);
        if(patient != null){
            TextView textView = findViewById(R.id.history_title_textview);
            String titletext = patient +"'s Prescription History";
            textView.setText(titletext);

            Button button = findViewById(R.id.history_home_button);
            button.setText(R.string.back);
        }

        date = findViewById(R.id.history_date_textview);

        setDate();

        compactCalendar = findViewById(R.id.history_compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                showDay(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date.setText(sdf.format(firstDayOfNewMonth));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(showingDay){
            showCalendar();
        }else super.onBackPressed();
    }

    public void Back(View view){
        showCalendar();
    }

    private void setDate(){
        //Format the date
        Date d = new Date();
        String monthYear = sdf.format(d);
        //Update the textview
        date.setText(monthYear);
    }

    private void showCalendar(){
        showingDay = false;

        LinearLayout ll = findViewById(R.id.history_linearlayout);
        ll.setVisibility(View.INVISIBLE);

        Button b = findViewById(R.id.history_back_button);
        b.setVisibility(View.INVISIBLE);

        compactCalendar.setVisibility(View.VISIBLE);

        setDate();
    }
    
    private void showDay(Date dateClicked){
        showingDay = true;

        LinearLayout ll = findViewById(R.id.history_linearlayout);
        ll.setVisibility(View.VISIBLE);

        Button b = findViewById(R.id.history_back_button);
        b.setVisibility(View.VISIBLE);

        compactCalendar.setVisibility(View.INVISIBLE);
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE\nMMMM d");
        date.setText(sdfDay.format(dateClicked));

        ExpandableListView expandableListView = findViewById(R.id.history_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();
        HashMap<String, Boolean> check = new HashMap<>();

        JSONObject json;

        try {
            //Get the json object from the history.json file
            json = getJsonObject();
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Prescriptions");

            //Array list containing infoArrays for each prescription
            ArrayList<String[]> infoArrayList = new ArrayList<>();

            //Iterate through the json array and retrieve each prescription's info
            for (int i = 0; i < prescriptionArray.length(); i++){
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);

                String name = prescriptionDetail.getString("Name");
                String dosage = prescriptionDetail.getString("Dosage");
                String time = prescriptionDetail.getString("Time");
                String addInfo = prescriptionDetail.getString("Additional Info");

                Boolean taken = prescriptionDetail.getBoolean("Taken");
                //Put whether the current medication has been taken into the check hashmap
                check.put(name, taken);


                String[] infoArray = {
                        name,
                        "Dosage: " + dosage,
                        "When: " + time,
                        "Additional Information:\n" + addInfo
                };
                //Add the infoArray to the Array list
                infoArrayList.add(infoArray);
            }

            for (String[] infoArray : infoArrayList) addPrescription(infoArray, item);


            TodayListAdapter adapter = new TodayListAdapter(item, check);
            expandableListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPrescription(String[] infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        prescriptionInfo.add(infoArray[1]); //Dosage
        prescriptionInfo.add(infoArray[2]); //Time
        prescriptionInfo.add(infoArray[3]); //Additional Info

        item.put(infoArray[0], prescriptionInfo);
    }

    private JSONObject getJsonObject() throws JSONException {
        String json;
        try{
            InputStream inputStream;

            if(HomepageActivity.CARETAKER_MODE) inputStream = getApplicationContext().openFileInput("history.json");
            else inputStream = getApplicationContext().openFileInput("patienthistory.json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new JSONObject(json);
    }

    public void Confirmed(View view){

        CheckBox checkBox = view.findViewById(R.id.today_confirm_checkbox);
        Boolean taken = checkBox.isChecked();
        String name = checkBox.getContentDescription().toString();

        System.out.println("NAME: " + name + " TAKEN: " + taken);

        JSONObject json;
        try {
            //Get the json object from the today.json file
            json = getJsonObject();
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Prescriptions");
            JSONArray newPrescriptionArray = new JSONArray();

            for (int i = 0; i < prescriptionArray.length(); i++) {
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);
                //Replace old prescription with updated values

                if (prescriptionDetail.getString("Name").equals(name)) {
                    prescriptionDetail.put("Taken", taken);

                }

                newPrescriptionArray.put(prescriptionDetail);
                json.put("Prescriptions", newPrescriptionArray);
            }

            //Rewrite updated json to today.json
            Writer output;
            File file;
            if(HomepageActivity.CARETAKER_MODE) file = new File(getFilesDir()+"/patientshistory.json");
            else file = new File(getFilesDir()+"/history.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json.toString());
            output.close();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }


    public void goHome(View view){
        finish();
    }
}

