package com.example.mynoteapp.notesApp;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mynoteapp.R;
import com.example.mynoteapp.base.BaseActivity;
import com.example.mynoteapp.database.MyDataBase;
import com.example.mynoteapp.database.model.Note;

import java.util.Calendar;

public class AddNoteActivity extends BaseActivity implements View.OnClickListener {

    protected EditText title;
    protected EditText content;
    protected TextView time;
    protected Button addNote;

    String titleS;
    String contectS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_note);
        initView();
    }

    String choosedTime = "";
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.time) {
            // current hour and minute
            Calendar calendar = Calendar.getInstance();
            // TimePickerDialog to choose Note Date
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                // after time choose
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    time.setText(hourOfDay + ":" + minute);
                    choosedTime = hourOfDay + ":" + minute;
                }
            },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
            timePickerDialog.show();

        } else if (view.getId() == R.id.addNote) {
            titleS = title.getText().toString();
            contectS = content.getText().toString();

            if (checkValidated()){
                Note note = new Note(titleS,contectS,choosedTime);
                MyDataBase.getInstance(this).notesDao()
                        .addNote(note);
                showMessage(R.string.note_added_successfully, R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                },false);
            }
        }


    }

    private void initView() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        time.setOnClickListener(AddNoteActivity.this);
        addNote = findViewById(R.id.addNote);
        addNote.setOnClickListener(AddNoteActivity.this);
    }
    boolean full;
    public boolean checkValidated(){
        String titleS = title.getText().toString();
        String contentS = content.getText().toString();
        full = false;

        if (titleS.isEmpty()){
            title.setError("required");
            full = false;
        }else {
            title.setError(null);
            full=true;
        }
        if (contentS.isEmpty()){
            content.setError("required");
            full = false;
        }else {
            content.setError(null);
            full=true;
        }
        if (choosedTime.isEmpty()){
            time.setError("required");
            full = false;
        }else {
            time.setError(null);
            full=true;
        }

        return full;
    }
}
