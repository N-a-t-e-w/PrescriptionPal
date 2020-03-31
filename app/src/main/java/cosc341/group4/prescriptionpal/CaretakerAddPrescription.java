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
import java.util.Arrays;

public class CaretakerAddPrescription extends AppCompatActivity {
    String time;
    String fname;
    int dosage;
    String[] days;
    String info;
    String pname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_add_prescription);
        final TextView TimeTv = findViewById(R.id.EditPrescTimeTV);
        Intent intent = getIntent();
        pname = intent.getStringExtra("PATIENT").split(" ")[0];

        final Context mcontext = this;
        Button setttime = findViewById(R.id.editPrescTimeBtn);
        setttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TimeTv.setText(hourOfDay + ":" + minute);
                        time = hourOfDay + ":" + minute;
                    }
                },12,0,android.text.format.DateFormat.is24HourFormat(mcontext));
                tpd.show();
            }
        });
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
        days = daystring;
    }
    public void ConfirmAdd(View v){
        updatedateday();
        final TextView TimeTv = findViewById(R.id.EditPrescTimeTV);
        final EditText nameEdit = findViewById(R.id.EditNameEditText);
        final EditText dosEdit = findViewById(R.id.EditDosageEditTExt);
        final EditText infoEdit = findViewById(R.id.EditPrescInfoEditText);

        fname = nameEdit.getText().toString();
        info = infoEdit.getText().toString();
        dosage = Integer.parseInt(dosEdit.getText().toString());
        time = TimeTv.getText().toString();

        if (fname.length()<3){
            Toast.makeText(getApplicationContext(),"Name must be more than 3 characters long",Toast.LENGTH_SHORT).show();
        }else if (dosage<=0){
            Toast.makeText(getApplicationContext(),"Please enter a valid number",Toast.LENGTH_SHORT).show();
        }else {
            JSONObject prescript = new JSONObject();
            try{
                prescript.put("Name",fname);
                prescript.put("Dosage",dosage);
                prescript.put("Time", time);
                JSONArray aadays = new JSONArray((Arrays.asList(days)));
                prescript.put("Days", aadays);
                prescript.put("Info", info);
                prescript.put("Taken", false);
                FileOutputStream outputStream;

                String filename = pname + "Prescriptions.json";
                JSONObject userprescripts = getJsonObject();
                JSONObject newObject = new JSONObject();
                JSONArray prescriptsarray = userprescripts.getJSONArray("Prescriptions");
                JSONObject thisprescript = new JSONObject();
                thisprescript.put("Name",fname);
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

    public void cancel(View v){
    finish();
    }

    private JSONObject getJsonObject() throws JSONException {
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
