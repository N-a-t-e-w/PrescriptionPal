package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientsActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    private String from;

    public static final String PATIENT = "PATIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        Intent intent = getIntent();
        from = intent.getStringExtra(HomepageActivity.CARETAKER);

        if(!from.equals("Patients")){
            TextView textView = findViewById(R.id.patients_title_textview);
            textView.setText(R.string.choosepatient);
        }
        update();

    }
    protected void onResume(){
        update();
        super.onResume();
    }

    private void update(){
        //Create the list view and hash map for storing the prescription info
        expandableListView = findViewById(R.id.patients_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();
        HashMap<String, Boolean> check = new HashMap<>();

        JSONObject json;

        try {
            //Get the json object from the prescriptions.json file
            json = getJsonObject();
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Patients");

            //Array list containing infoArrays for each prescription
            ArrayList<String[]> infoArrayList = new ArrayList<>();

            //Iterate through the json array and retrieve each prescription's info
            for (int i = 0; i < prescriptionArray.length(); i++) {
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);

                String name = prescriptionDetail.getString("Name");
                String age = prescriptionDetail.getString("Age");
                String addInfo = prescriptionDetail.getString("Additional Info");


                String[] infoArray = {
                        name,
                        "Age: " + age,
                        "Additional Information:\n" + addInfo
                };
                //Add the infoArray to the Array list
                infoArrayList.add(infoArray);
            }

            for (String[] infoArray : infoArrayList) addPatient(infoArray, item);

            PatientsListAdapter adapter = new PatientsListAdapter(item);
            expandableListView.setAdapter(adapter);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private void addPatient(String[] infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        prescriptionInfo.add(infoArray[1]); //Age
        prescriptionInfo.add(infoArray[2]); //Additional Info

        item.put(infoArray[0], prescriptionInfo);
    }

    //CODE ADAPTED FROM: https://abhiandroid.com/programming/json
    private JSONObject getJsonObject() throws JSONException {
        String json;
        try{
            InputStream inputStream = getApplicationContext().openFileInput("patients.json");
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

    public void ViewPatient(View view){

        if(from.equals("Patients")) {
            Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
            Button b = view.findViewById(R.id.patients_prescription_button);
            String name = b.getContentDescription().toString();
            intent.putExtra(PATIENT, name.toLowerCase());
            startActivity(intent);
        }else if(from.equals("Today")){

            Button b = view.findViewById(R.id.patients_prescription_button);
            String name = b.getContentDescription().toString();

            Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
            intent.putExtra(PATIENT, name);

            startActivity(intent);
        }else if(from.equals("History")){

            Button b = view.findViewById(R.id.patients_prescription_button);
            String name = b.getContentDescription().toString();

            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            intent.putExtra(PATIENT, name);

            startActivity(intent);
        }
    }
    public void addPatient(View view){
        Intent intent = new Intent(getApplicationContext(),AddPatient.class);
        startActivity(intent);

    }
    public void goHome(View view){
        finish();
    }

    public void editPatient(View view){
        Button b = view.findViewById(R.id.patients_edit_button);
        String name = b.getContentDescription().toString();

        Intent intent = new Intent(getApplicationContext(), EditPatient.class);
        intent.putExtra(PATIENT, name);
        startActivity(intent);
    }
}
