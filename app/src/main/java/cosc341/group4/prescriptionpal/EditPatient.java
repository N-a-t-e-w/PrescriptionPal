package cosc341.group4.prescriptionpal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditPatient extends AppCompatActivity {
        String patientName,pinfo;
        EditText pnameET, pageET, pinfoET;
        int pos,page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        pnameET = findViewById(R.id.EditPatientNameET);
        pinfoET = findViewById(R.id.EditPatientInfoET);
        pageET = findViewById(R.id.EditPatientAgeET);
        Intent intent = getIntent();
        patientName = intent.getStringExtra("PATIENT");

        populate();

    }

    private void populate(){
        try {
            JSONObject patientsJSON = getJsonObject();
            JSONArray PatientArray = patientsJSON.getJSONArray("Patients");
            for (int i = 0; i < PatientArray.length(); i++) {
                JSONObject PatientDetail = PatientArray.getJSONObject(i);
                String name = PatientDetail.getString("Name");

                if(name.equals(patientName)) {
                    pos = i;
                    page = PatientDetail.getInt("Age");
                    pinfo = PatientDetail.getString("Additional Info");
                }
                pnameET.setText(name);
                pinfoET.setText(pinfo);
                pageET.setText(String.valueOf(page));
            }

        }catch (Exception e){

        }
    }

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

    public void confirm(View view){
        patientName = pnameET.getText().toString();
        page = Integer.parseInt(pageET.getText().toString());
        pinfo = pinfoET.getText().toString();
        if (patientName.length()<2){
            Toast.makeText(getApplicationContext(),"Name must be more than 2 characters long",Toast.LENGTH_SHORT).show();
        }else if (page<0){
            Toast.makeText(getApplicationContext(),"Please enter a valid age",Toast.LENGTH_SHORT).show();
        }else {
            JSONObject newpatient = new JSONObject();
            try{
                newpatient.put("Name",patientName);
                newpatient.put("Age",page);
                newpatient.put("Additional Info", pinfo);
                FileOutputStream outputStream;
                String filename = "patients.json";

                JSONObject Patients = getJsonObject();
                JSONObject newObject = new JSONObject();

                JSONArray Patientsarray = Patients.getJSONArray("Patients");
                Patientsarray.remove(pos);

                Patientsarray.put(newpatient);
                newObject.put("Patients",Patientsarray);
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                String contents = newObject.toString();
                outputStream.write(contents.getBytes());
                finish();
            }catch(Exception e){
            }
        }
    }


    public void delete(View view){

    }
    public void cancel(View view){
        finish();
    }
}
