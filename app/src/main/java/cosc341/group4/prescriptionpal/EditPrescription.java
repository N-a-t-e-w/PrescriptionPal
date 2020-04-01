package cosc341.group4.prescriptionpal;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class EditPrescription extends AppCompatActivity {
    JSONObject allPrescripts;
    String prescName;
    String info;
    int dosage;
    ArrayList<String> days = new ArrayList<String>();
    String time;
    String[] newdays;
    int pos = -1;
    String patientname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prescription);
        Intent intent =  getIntent();
        prescName = intent.getStringExtra("Name");
        patientname = intent.getStringExtra("PATIENT");

        populate();
        final TextView TimeTv = findViewById(R.id.EditPrescTimeTV);
        final EditText nameEdit = findViewById(R.id.EditNameEditText);
        final EditText dosEdit = findViewById(R.id.EditDosageEditTExt);
        final EditText infoEdit = findViewById(R.id.EditPrescInfoEditText);
        nameEdit.setText(prescName, TextView.BufferType.EDITABLE);
        dosEdit.setText(String.valueOf(dosage),TextView.BufferType.EDITABLE);
        infoEdit.setText(info,TextView.BufferType.EDITABLE);
        TimeTv.setText(time);
        final Context mcontext = this;


        Button setttime = findViewById(R.id.editPrescTimeBtn);
        setttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String tempmin;
                        if (minute < 10){
                            tempmin = "0"+minute;
                            TimeTv.setText(hourOfDay + ":" + tempmin);
                            time = hourOfDay + ":" + tempmin;
                        }else {
                            TimeTv.setText(hourOfDay + ":" + minute);
                            time = hourOfDay + ":" + minute;
                        }

                    }
                },12,0,android.text.format.DateFormat.is24HourFormat(mcontext));
                tpd.show();
            }
        });

        Button cancel = findViewById(R.id.editprescCancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
    private JSONObject getJsonObject(String pname) throws JSONException {
        String json;
        try{
            InputStream inputStream = getApplicationContext().openFileInput(pname +"Prescriptions.json");
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

        private void populate(){
            try {
                if (patientname != null){
                    allPrescripts = getJsonObject(patientname.toLowerCase().split(" ")[0]);
                }else{
                    allPrescripts = getJsonObject();
                }
                JSONArray prescriptionArray = allPrescripts.getJSONArray("Prescriptions");
                for (int i = 0; i < prescriptionArray.length(); i++) {
                    JSONObject prescriptionDetail = prescriptionArray.getJSONObject(i);

                    String name = prescriptionDetail.getString("Name");

                    if(name.equals(prescName)) {
                        pos = i;
                        dosage = prescriptionDetail.getInt("Dosage");
                        time = prescriptionDetail.getString("Time");
                        info = prescriptionDetail.getString("Info");
                        JSONArray jdays = prescriptionDetail.getJSONArray("Days");
                        if (jdays != null) {
                            for (int x=0;x<jdays.length();x++){
                                days.add(jdays.getString(x));
                            }
                            edDays();
                        }
                    }
                }

            }catch (Exception e){

            }
        }
        private void edDays() {
            final CheckBox mon = findViewById(R.id.addprescMondaychk2);
            final CheckBox tues = findViewById(R.id.addprescTuesdaychk2);
            final CheckBox wed = findViewById(R.id.addprescWednesdaychk2);
            final CheckBox thurs = findViewById(R.id.addprescThursdaychk2);
            final CheckBox fri = findViewById(R.id.addprescFridaychk2);
            final CheckBox sat = findViewById(R.id.addprescSaturdaychk2);
            final CheckBox sun = findViewById(R.id.addprescSundaychk2);
            final CheckBox[] dayarray = {mon, tues, wed, thurs, fri, sat, sun};
            for(CheckBox box : dayarray){
                box.setChecked(false);
            }
            for (String day : days
            ) {
                System.out.println(day);
                switch (day) {
                    case "Monday":
                        mon.setChecked(true);
                        break;
                    case "Tuesday":
                        tues.setChecked(true);
                        break;
                    case "Wednesday":
                        wed.setChecked(true);
                        break;
                    case "Thursday":
                        thurs.setChecked(true);
                        break;
                    case "Friday":
                        fri.setChecked(true);
                        break;
                    case "Saturday":
                        sat.setChecked(true);
                        break;
                    case "Sunday":
                        sun.setChecked(true);
                        break;
                    default:
                }
            }
            int count = 0;
            for (CheckBox day : dayarray) {
                if (day.isChecked()) {
                    count++;
                }
            }
            String[] daystring = new String[count];
            count = 0;
            for (CheckBox day : dayarray) {
                if (day.isChecked()) {
                    daystring[count] = day.getText().toString();
                    count++;
                }
            }
            newdays = daystring;
        }
        public void updatedateday(){
            final CheckBox mon = findViewById(R.id.addprescMondaychk2);
            final CheckBox tues = findViewById(R.id.addprescTuesdaychk2);
            final CheckBox wed = findViewById(R.id.addprescWednesdaychk2);
            final CheckBox thurs = findViewById(R.id.addprescThursdaychk2);
            final CheckBox fri = findViewById(R.id.addprescFridaychk2);
            final CheckBox sat = findViewById(R.id.addprescSaturdaychk2);
            final CheckBox sun = findViewById(R.id.addprescSundaychk2);
            final CheckBox[] dayarray = {mon, tues, wed, thurs, fri, sat, sun};
            int count = 0;
            for (CheckBox day : dayarray) {
                if (day.isChecked()) {
                    count++;
                }
            }
            String[] daystring = new String[count];
            count = 0;
            for (CheckBox day : dayarray) {
                if (day.isChecked()) {
                    daystring[count] = day.getText().toString();
                    count++;
                }
            }
            newdays = daystring;
        }


        public void confirmEdit(View view){

        updatedateday();
        final TextView TimeTv = findViewById(R.id.EditPrescTimeTV);
        final EditText nameEdit = findViewById(R.id.EditNameEditText);
        final EditText dosEdit = findViewById(R.id.EditDosageEditTExt);
        final EditText infoEdit = findViewById(R.id.EditPrescInfoEditText);

        prescName = nameEdit.getText().toString();
        info = infoEdit.getText().toString();
        String tempDosage = dosEdit.getText().toString();
        time = TimeTv.getText().toString();

        if (prescName.length()<3){
            Toast.makeText(getApplicationContext(),"Name must be more than 3 characters long",Toast.LENGTH_SHORT).show();
        }else if (tempDosage.length()<1){
            Toast.makeText(getApplicationContext(),"Please enter a valid dosage",Toast.LENGTH_SHORT).show();
        }else if(newdays.length < 1){
            Toast.makeText(getApplicationContext(),"Please select atleast 1 day",Toast.LENGTH_SHORT).show();
        }else{
            dosage = Integer.parseInt(tempDosage);
            JSONObject prescript = new JSONObject();
            try{
                prescript.put("Name",prescName);
                prescript.put("Dosage",dosage);
                prescript.put("Time", time);
                JSONArray aadays = new JSONArray((Arrays.asList(newdays)));
                prescript.put("Days", aadays);
                prescript.put("Info", info);
                prescript.put("Taken", false);
                FileOutputStream outputStream;
                String filename;
                if (patientname != null){
                    filename = patientname.toLowerCase().split(" ")[0] + "Prescriptions.json";
                }else{
                    filename = "UserPrescriptions.json";
                }
                JSONObject userprescripts = getJsonObject();
                JSONObject newObject = new JSONObject();

                JSONArray prescriptsarray = userprescripts.getJSONArray("Prescriptions");
                prescriptsarray.remove(pos);
                //JSONObject thisprescript = new JSONObject();
                //thisprescript.put("Name",prescName);
                prescriptsarray.put(prescript);
                newObject.put("Prescriptions",prescriptsarray);
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                String contents = newObject.toString();
                outputStream.write(contents.getBytes());
                finish();
            }catch(Exception e){
            }
        }
        }

        public void deletePrescription(View view){
        try{
            FileOutputStream outputStream;
            String filename;
            JSONObject userprescripts;
            if (patientname != null){
                filename = patientname.toLowerCase().split(" ")[0] + "Prescriptions.json";
                userprescripts = getJsonObject(patientname.toLowerCase().split(" ")[0]);
            }else{
                filename = "UserPrescriptions.json";
                userprescripts = getJsonObject();
            }

            JSONObject newObject = new JSONObject();
            JSONArray prescriptsarray = userprescripts.getJSONArray("Prescriptions");
            System.out.println(pos);
            prescriptsarray.remove(pos);
            newObject.put("Prescriptions",prescriptsarray);
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String contents = newObject.toString();
            outputStream.write(contents.getBytes());
            finish();

        }
    catch(Exception e){

        }
    }
        }

