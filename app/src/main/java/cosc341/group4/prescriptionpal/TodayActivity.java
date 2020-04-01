package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class TodayActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    String patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        Intent intent = getIntent();
        patient = intent.getStringExtra(PatientsActivity.PATIENT);
        if(patient != null){
            TextView textView = findViewById(R.id.today_title_textview);
            String titletext = patient +"'s Prescriptions for Today";
            textView.setText(titletext);

            Button button = findViewById(R.id.today_home_button);
            button.setText(R.string.back);
        }

        final String day = setDate();

        //Create the list view and hash map for storing the prescription info
        expandableListView = findViewById(R.id.today_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();
        HashMap<String, Boolean> check = new HashMap<>();

        JSONObject json;

        try {
            //Get the json object from the prescriptions.json file
            if (patient != null){
                json = getJsonObject(patient.toLowerCase().split(" ")[0]);
            }else{
                json = getJsonObject();
            }
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Prescriptions");

            //Array list containing infoArrays for each prescription
            ArrayList<String[]> infoArrayList = new ArrayList<>();

            //Iterate through the json array and retrieve each prescription's info
            for (int i = 0; i < prescriptionArray.length(); i++){
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);

                String name = prescriptionDetail.getString("Name");
                String dosage = String.valueOf(prescriptionDetail.getInt("Dosage"));
                String time = prescriptionDetail.getString("Time");

                String days = "";
                JSONArray jDays = prescriptionDetail.getJSONArray("Days");
                for(int j = 0; j<jDays.length(); j++){
                    days += jDays.getString(j);
                    if(j + 1 != jDays.length()) days+=", ";
                }

                String addInfo = prescriptionDetail.getString("Info");

                Boolean taken = prescriptionDetail.getBoolean("Taken");
                //Put whether the current medication has been taken into the check hashmap
                check.put(name, taken);


                String[] infoArray = {
                        name,
                        "Dosage: " + dosage,
                        "Time: " + time,
                        "Days" + days,
                        "Additional Information:\n" + addInfo
                };
                //Add the infoArray to the Array list
                //System.out.println("DAY:" + day);
                //System.out.println("DAYs:" + days);
                if (days.contains(day)){
                    infoArrayList.add(infoArray);
                }
            }

            for (String[] infoArray : infoArrayList) addPrescription(infoArray, item);


            TodayListAdapter adapter = new TodayListAdapter(item, check);
            expandableListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Called when a checkbox is ticked/unticked on the today page
    //Updates prescriptions.json 'taken' values for medications
    public void Confirmed(View view){

        CheckBox checkBox = view.findViewById(R.id.today_confirm_checkbox);
        Boolean taken = checkBox.isChecked();
        String name = checkBox.getContentDescription().toString();

        System.out.println("NAME: " + name + " TAKEN: " + taken);

        JSONObject json;
        try {
            //Get the json object from the prescriptions.json file
            if (patient != null){
                json = getJsonObject(patient.toLowerCase().split(" ")[0]);
            }else{
                json = getJsonObject();
            }
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

            //Rewrite updated json to prescriptions.json
            Writer output;
            File file;

            if (patient != null){
                file = new File(getFilesDir()+"/" + patient.toLowerCase().split(" ")[0] + "Prescriptions.json");
            }else{
                file = new File(getFilesDir()+"/UserPrescriptions.json");
            }


            output = new BufferedWriter(new FileWriter(file));
            output.write(json.toString());
            output.close();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    private void addPrescription(String[] infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        prescriptionInfo.add(infoArray[1]); //Dosage
        prescriptionInfo.add(infoArray[2]); //Time
        prescriptionInfo.add(infoArray[3]); //Days
        prescriptionInfo.add(infoArray[4]); //Additional Info

        item.put(infoArray[0], prescriptionInfo);
    }

    //Updates date textview to show the current date
    private String setDate(){
        TextView date = findViewById(R.id.today_date_textview);
        //Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE\nMMMM d");
        SimpleDateFormat theday = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        String day = theday.format(d);
        //Update the textview
        date.setText(dayOfTheWeek);
        return day;
    }

    //CODE ADAPTED FROM: https://abhiandroid.com/programming/json
    private JSONObject getJsonObject() throws JSONException {
        String json;
        try{
            InputStream inputStream;
            inputStream = getApplicationContext().openFileInput("UserPrescriptions.json");
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
    private JSONObject getJsonObject(String name) throws JSONException {
        String json;
        try{
            InputStream inputStream;
            inputStream = getApplicationContext().openFileInput(name +"Prescriptions.json");
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

    public void goHome(View view){
        finish();
    }
}
