package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();

        String[] infoArray = {"pill1", "pill2", "pill3", "pill4"};
        for (String string : infoArray){
            addPrescription(string, item);
        }


        TodayPrescriptionListAdapter adapter = new TodayPrescriptionListAdapter(item);
        expandableListView.setAdapter(adapter);
    }
    private void addPrescription(String infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        item.put(infoArray, prescriptionInfo);
    }
    public void goHome(View view){
        finish();
    }
    public void addPrescript(View view){
        Intent intent = new Intent(getApplicationContext(), addPrescription.class);
        startActivity(intent);
    }
}
