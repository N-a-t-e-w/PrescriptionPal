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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initializeTodayJson();
    }

    public void goToday(View view){
        Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
        startActivity(intent);
    }

    public void goPrescriptions(View view){
        Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
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
}
