package cosc341.group4.prescriptionpal;

import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        setDate();

        //Create the list view and hash map for storing the prescription info
        expandableListView = findViewById(R.id.today_expandableListView);
        HashMap<String, List<String>> item = new HashMap<>();
        Boolean[] checkBoxArray;

        JSONObject json;
        try {
            //Get the json object from the today.json file
            json = getJsonObject();
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Prescriptions");

            //Array list containing infoArrays for each prescription
            ArrayList<String[]> infoArrayList = new ArrayList<>();

            //Initialize the checkBoxArray
            checkBoxArray = new Boolean[prescriptionArray.length()];

            //Iterate through the json array and retrieve each prescription's info
            for (int i = 0; i < prescriptionArray.length(); i++){
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);

                String name = prescriptionDetail.getString("Name");
                String dosage = prescriptionDetail.getString("Dosage");
                String time = prescriptionDetail.getString("Time");
                String addInfo = prescriptionDetail.getString("Additional Info");

                Boolean taken = prescriptionDetail.getBoolean("Taken");
                checkBoxArray[i] = taken;


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

            TodayPrescriptionListAdapter adapter = new TodayPrescriptionListAdapter(item, checkBoxArray);
            expandableListView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Confirmed(View view){

        CheckBox checkBox = view.findViewById(R.id.today_confirm_checkbox);
        Boolean taken = checkBox.isChecked();
        String name = checkBox.getContentDescription().toString();

        JSONObject json;
        try {
            //Get the json object from the today.json file
            json = getJsonObject();
            //Get a json array of each prescription
            assert json != null;
            JSONArray prescriptionArray = json.getJSONArray("Prescriptions");

            for (int i = 0; i < prescriptionArray.length(); i++) {
                JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);
                if (prescriptionDetail.getString("Name").equals(name)){
                    //Replace old prescription with updated values
                    prescriptionArray.remove(i);
                    prescriptionDetail.put("Taken", taken);
                    prescriptionArray.put(prescriptionDetail);
                    json.put("Prescriptions", prescriptionArray);
                    break;
                }
            }
//TODO FIX THIS DUMB DUMB SHIT
//TODO GOTTA MAKE A LOCAL FILE TO WRITE TO
            Writer output = null;
            File file = new File("today.json");
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
        prescriptionInfo.add(infoArray[3]); //Additional Info

        item.put(infoArray[0], prescriptionInfo);
    }

    //Updates date textview to show the current date
    private void setDate(){
        TextView date = findViewById(R.id.today_date_textview);
        //Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE\nMMMM d");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        //Update the textview
        date.setText(dayOfTheWeek);
    }

    //CODE ADAPTED FROM: https://abhiandroid.com/programming/json
    private JSONObject getJsonObject() throws JSONException {
        String json;
        try{
            InputStream inputStream = getAssets().open("today.json");
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
