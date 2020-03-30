package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public class HomepageActivity extends AppCompatActivity {

    public static boolean CARETAKER_MODE = false;
    public static final String CARETAKER = "CARETAKER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(CARETAKER_MODE) setContentView(R.layout.activity_homepage_c);
        else setContentView(R.layout.activity_homepage);

        initializeTodayJson();
        initializeHistoryJson();
        initializePatientsJson();
        initializePatientsTodayJson();

        //USED TO INITIALLY CREATE EMPTY JSON
        initializeUserPrescriptions();
    }

    @Override
    protected void onResume() {
        if (CARETAKER_MODE) setContentView(R.layout.activity_homepage_c);
        else setContentView(R.layout.activity_homepage);

        super.onResume();
    }

    public void goToday(View view){
        if(CARETAKER_MODE){
            Intent intent = new Intent(getApplicationContext(), PatientsActivity.class);
            intent.putExtra(CARETAKER, "Today");
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
            startActivity(intent);
        }
    }

    public void goHistory(View view){
        if(CARETAKER_MODE) {
            Intent intent = new Intent(getApplicationContext(), PatientsActivity.class);
            intent.putExtra(CARETAKER, "History");
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        }
    }

    public void goPrescriptions(View view){
        Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
        startActivity(intent);
    }

    public void goPatients(View view){
        Intent intent = new Intent(getApplicationContext(), PatientsActivity.class);
        intent.putExtra(CARETAKER, "Patients");
        startActivity(intent);
    }

    public void goSettings(View view){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void initializeTodayJson(){
        String json;
        try{
            InputStream inputStream = getAssets().open("today.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/today.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeHistoryJson(){
        String json;
        try{
            InputStream inputStream = getAssets().open("history.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/history.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializePatientsJson(){
        String json;
        try{
            InputStream inputStream = getAssets().open("patients.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/patients.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void initializeUserPrescriptions(){
        String json;
        try{
            InputStream inputStream = getAssets().open("UserPrescriptions.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/UserPrescriptions.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializePatientsTodayJson(){
        String json;
        try{
            InputStream inputStream = getAssets().open("patientstoday.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/patientstoday.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializePatientsHistoryJson(){
        String json;
        try{
            InputStream inputStream = getAssets().open("patientshistory.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/patientshistory.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
