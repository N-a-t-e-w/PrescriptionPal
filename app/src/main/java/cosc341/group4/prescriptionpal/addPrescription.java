package cosc341.group4.prescriptionpal;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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

public class addPrescription extends AppCompatActivity {
    String finalname = "";
    int finalDosage;
    String[] finaldays;
    String finalTime = "";
    String finalinfo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);

        TextView tv = findViewById(R.id.QuestionTextView);
        Button bbtn = findViewById(R.id.addPresccancelBtn);
        Button nbtn = findViewById(R.id.addprescnxtBtn);
        EditText answr = findViewById(R.id.addprescAnswerEditText);

        String[] questions = getResources().getStringArray(R.array.questions);
        prescName(tv, bbtn, nbtn, answr, questions);

    }
    private void prescName(final TextView tv, final Button bbtn, final Button nbtn, final EditText answr, final String[] question){
        answr.setVisibility(Button.VISIBLE);
        tv.setText(question[0]);
        answr.setInputType(InputType.TYPE_CLASS_TEXT);
        answr.setText("");
        bbtn.setText("Cancel");
        nbtn.setText("Next");
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = answr.getText().toString();
                if (name.length() < 3){
                    Toast.makeText(getApplicationContext(),"Name must be at least 3 characters long",Toast.LENGTH_SHORT).show();
                }else{
                finalname = name;
                prescDosage(tv,bbtn,nbtn,answr,question);
                }
            }
        });
    }

    private void prescDosage(final TextView tv, final Button bbtn, final Button nbtn, final EditText answr, final String[] question){
        answr.setVisibility(Button.VISIBLE);
        tv.setText(question[1]);
        answr.setInputType(InputType.TYPE_CLASS_NUMBER);
        bbtn.setText("Back");
        answr.setText("");
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescName(tv,bbtn,nbtn,answr,question);
            }
        });
        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int dosage = Integer.parseInt(answr.getText().toString());
                    finalDosage = dosage;
                    prescDay(tv,bbtn,nbtn,answr,question);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Please enter a valid number",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void prescDay(final TextView tv, final Button bbtn, final Button nbtn, final EditText answr, final String[] question){
        tv.setText(question[2]);
        answr.setVisibility(Button.INVISIBLE);
        final CheckBox mon = findViewById(R.id.addprescMondaychk);
        final CheckBox tues = findViewById(R.id.addprescTuesdaychk);
        final CheckBox wed = findViewById(R.id.addprescWednesdaychk);
        final CheckBox thurs = findViewById(R.id.addprescThursdaychk);
        final CheckBox fri = findViewById(R.id.addprescFridaychk);
        final CheckBox sat = findViewById(R.id.addprescSaturdaychk);
        final CheckBox sun = findViewById(R.id.addprescSundaychk);
        final CheckBox[] days = {mon,tues,wed,thurs,fri,sat,sun};
        for (CheckBox day : days){
            day.setVisibility(CheckBox.VISIBLE);
        }
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox day : days){
                    day.setVisibility(CheckBox.INVISIBLE);
                }
                prescDosage(tv,bbtn,nbtn,answr,question);
            }
        });

        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for (CheckBox day : days){
                    if (day.isChecked()){
                        count++;
                    }
                }
                String[] daystring = new String[count];
                count = 0;
                for (CheckBox day : days){
                    if (day.isChecked()){
                        daystring[count] = day.getText().toString();
                        count++;
                    }
                    day.setVisibility(CheckBox.INVISIBLE);
                }
                finaldays = daystring;
                prescTime(tv,bbtn,nbtn,answr,question);
            }
        });


    }

    private void prescTime(final TextView tv, final Button bbtn, final Button nbtn, final EditText answr, final String[] question){
        tv.setText(question[3]);
        final Button settime = findViewById(R.id.addprescselecttimeBtn);
        settime.setVisibility(View.VISIBLE);
        final TextView selectedtime = findViewById(R.id.addpresctimeTV);
        selectedtime.setVisibility(View.VISIBLE);
        final Context mcontext = this;
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedtime.setText(hourOfDay + ":" + minute);
                        finalTime = hourOfDay + ":" + minute;
                    }
                },12,0,android.text.format.DateFormat.is24HourFormat(mcontext));
                tpd.show();
            }
        });
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime.setVisibility(View.INVISIBLE);
                selectedtime.setVisibility(View.INVISIBLE);
                prescDay(tv,bbtn,nbtn,answr,question);
            }
        });
        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalTime.length()>1){
                    settime.setVisibility(View.INVISIBLE);
                    selectedtime.setVisibility(View.INVISIBLE);
                    prescInfo(tv,bbtn,nbtn,answr,question);
                }
            }
        });



    }
    private void prescInfo(final TextView tv, final Button bbtn, final Button nbtn, final EditText answr, final String[] question){
        tv.setText(question[4]);
        answr.setVisibility(View.VISIBLE);
        answr.setInputType(InputType.TYPE_CLASS_TEXT);
        answr.setText("");
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answr.setVisibility(View.INVISIBLE);
                prescTime(tv,bbtn,nbtn,answr,question);
            }
        });
        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalinfo = answr.getText().toString();
                writejson();
            }
        });

    }

    private void writejson() {
        JSONObject prescript = new JSONObject();
        try{
            prescript.put("Name",finalname);
            prescript.put("Dosage",finalDosage);
            prescript.put("Time", finalTime);
            JSONArray days = new JSONArray((Arrays.asList(finaldays)));
            prescript.put("Days", days);
            prescript.put("Info", finalinfo);
            FileOutputStream outputStream;
            String filename = finalname+"presc.json";
            String contents = prescript.toString();
            /*outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(contents.getBytes());
            outputStream.close();*/

            filename = "UserPrescriptions.json";
            JSONObject userprescripts = getJsonObject(filename);
            JSONObject newObject = new JSONObject();
            JSONArray prescriptsarray = userprescripts.getJSONArray("Prescriptions");
            JSONObject thisprescript = new JSONObject();
            thisprescript.put("Name",finalname);
            prescriptsarray.put(prescript);
            newObject.put("Prescriptions",prescriptsarray);
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

}
