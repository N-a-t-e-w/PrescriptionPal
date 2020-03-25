package cosc341.group4.prescriptionpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        HashMap<String, List<String>> item = new HashMap<>();

        String[] infoArray = {"chicken pills", "Desc", "Amount", "Dosage"};
        addPrescription(infoArray, item);

        String[] infoArray2 = {"chungus pills", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non leo vel metus tempus lobortis nec sit amet ex. Etiam at congue mi, non condimentum est. In egestas eget augue eget condimentum. Etiam in cursus ipsum, non finibus ante. Integer molestie mattis quam eu scelerisque. Duis euismod interdum auctor. Proin pellentesque, ipsum vitae accumsan lacinia, diam libero iaculis dui, sit amet blandit ligula tellus sed massa. Duis volutpat consectetur tempus. Duis eget velit luctus, ornare risus quis, iaculis ante.", "69420", "Dosage"};
        addPrescription(infoArray2, item);

        TodayPrescriptionListAdapter adapter = new TodayPrescriptionListAdapter(item);
        expandableListView.setAdapter(adapter);

    }

    private void addPrescription(String[] infoArray,  HashMap<String, List<String>> item){
        ArrayList<String> prescriptionInfo = new ArrayList<>();
        prescriptionInfo.add(infoArray[1]);
        prescriptionInfo.add(infoArray[2]);
        prescriptionInfo.add(infoArray[3]);

        item.put(infoArray[0], prescriptionInfo);
    }
}
