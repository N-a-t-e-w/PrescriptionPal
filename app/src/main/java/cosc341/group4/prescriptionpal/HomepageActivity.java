package cosc341.group4.prescriptionpal;

import android.content.Context;
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

        initializeFiles();
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

    private void initializeFiles(){
        initializeFile("history.json");
        initializeFile("patients.json");
        initializeFile("patientshistory.json");
        initializeFile("patientstoday.json");
        initializeFile("UserPrescriptions.json");
    }

    private void initializeFile(String filename){
        String json;
        if(fileExists(getApplicationContext(), filename)) return;
        try{
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Writer output;
            File file = new File(getFilesDir()+"/"+filename);
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Code adapted from:
    // https://stackoverflow.com/questions/8867334/check-if-a-file-exists-before-calling-openfileinput
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
