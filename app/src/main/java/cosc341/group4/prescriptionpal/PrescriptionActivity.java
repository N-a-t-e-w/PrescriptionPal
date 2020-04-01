package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrescriptionActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    String patname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        Intent patient = getIntent();
        patname = patient.getStringExtra("PATIENT");
        if(patname != null){
            updatePrescriptions(patname.split(" ")[0]);
        }else{
            updatePrescriptions();

        }
    }
    //private void addPrescription(String infoArray,  HashMap<String, List<String>> item){
    //    ArrayList<String> prescriptionInfo = new ArrayList<>();
    //    item.put(infoArray, prescriptionInfo);
    //}

    @Override
    protected void onResume() {
        Intent patient = getIntent();
        patname = patient.getStringExtra("PATIENT");
        if(patname != null){
            updatePrescriptions(patname.split(" ")[0]);
        }else{
            updatePrescriptions();
        }
        super.onResume();
    }

    private void addPrescription(String[] infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        prescriptionInfo.add(infoArray[1]); //Dosage
        prescriptionInfo.add(infoArray[2]); //Time
        prescriptionInfo.add(infoArray[3]); //Additional Info

        item.put(infoArray[0], prescriptionInfo);
    }

    public void goHome(View view){
        finish();
    }
    public void addPrescript(View view){
        if(patname != null){
        Intent intent = new Intent(getApplicationContext(), CaretakerAddPrescription.class);
        intent.putExtra("PATIENT",patname);
        startActivity(intent);
    }else{
        Intent intent = new Intent(getApplicationContext(), addPrescription.class);
        startActivity(intent);
    }}

    //CODE ADAPTED FROM: https://abhiandroid.com/programming/json
    private JSONObject getJsonObject() throws JSONException {
        String json;

        try{
            InputStream inputStream = getApplicationContext().openFileInput("UserPrescriptions.json");
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
    public void editPrescript(View view){
        Button btn = view.findViewById(R.id.editPrescriptBtn);
        Intent intent = new Intent(getApplicationContext(), EditPrescription.class);
        if(patname != null){
            intent.putExtra("PATIENT",patname);
        }
        String prescriptname = btn.getContentDescription().toString();
        intent.putExtra("Name",prescriptname);
        startActivity(intent);
    }

    private void updatePrescriptions(){
        expandableListView = findViewById(R.id.prescriptions_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();

        JSONObject json;

        try {
            //Get the json object from the prescriptions.json file
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
                String addInfo = prescriptionDetail.getString("Info");
                //Put whether the current medication has been taken into the check hashmap

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


            PrescriptionListAdapter adapter = new PrescriptionListAdapter(item);
            expandableListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updatePrescriptions(String pname){
        expandableListView = findViewById(R.id.prescriptions_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();

        JSONObject json;

        try {
            //Get the json object from the prescriptions.json file
            json = getJsonObject(pname);
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
                String addInfo = prescriptionDetail.getString("Info");
                //Put whether the current medication has been taken into the check hashmap

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


            PrescriptionListAdapter adapter = new PrescriptionListAdapter(item);
            expandableListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //CODE ADAPTED FROM: https://abhiandroid.com/programming/json
    private JSONObject getJsonObject(String pname) throws JSONException {
        String json;

        try{
            InputStream inputStream = getApplicationContext().openFileInput(pname + "Prescriptions.json");
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
}
