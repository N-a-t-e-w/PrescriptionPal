package cosc341.group4.prescriptionpal;

import android.content.Context;
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

public class AddPatient extends AppCompatActivity {
        EditText pnameET;
        EditText pageET;
        EditText pinfoET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);


    }


    public void Confirm(View view){
        String name,pinfo;
        int age;
        pnameET = findViewById(R.id.AddPatientNameET);
        pageET = findViewById(R.id.AddPatientAgeET);
        pinfoET = findViewById(R.id.AddPatientInfoET);
        name = pnameET.getText().toString();
        pinfo = pinfoET.getText().toString();
        age = Integer.parseInt(pageET.getText().toString());
        if(age < 1){
            Toast.makeText(getApplicationContext(),"Please enter a valid age",Toast.LENGTH_SHORT).show();
        }else if(name.length()<2){
            Toast.makeText(getApplicationContext(),"Please enter a name longer than 2 characters",Toast.LENGTH_SHORT).show();
        }else{
        writejson(name,pinfo,age);}

    }
    private void writejson(String name, String info, int age) {
        JSONObject newpatient = new JSONObject();
        try{
            newpatient.put("Name",name);
            newpatient.put("Age",age);
            newpatient.put("Additional Info",info);

            FileOutputStream outputStream;

            String filename = "patients.json";
            JSONObject Patients = getJsonObject(filename);
            JSONObject newObject = new JSONObject();
            JSONArray PatientArray = Patients.getJSONArray("Patients");
            PatientArray.put(newpatient);
            newObject.put("Patients",PatientArray);
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String contents = newObject.toString();
            outputStream.write(contents.getBytes());

            filename = name.toLowerCase().split(" ")[0]+"Prescriptions.json";
            newObject = new JSONObject();
            JSONArray newArray = new JSONArray();
            newObject.put("Prescriptions",newArray);
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            contents = newObject.toString();
            outputStream.write(contents.getBytes());


            finish();
        }catch(Exception e){
        }
    }
    private JSONObject getJsonObject(String filename) throws JSONException {
        String json;
        try{
            InputStream inputStream = getApplicationContext().openFileInput(filename);
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


    public void cancelAdd(View view){
        finish();
    }
}
