package com.example.mynoteapp.notesApp;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mynoteapp.Constants;
import com.example.mynoteapp.R;
import com.example.mynoteapp.base.BaseActivity;
import com.example.mynoteapp.database.MyDataBase;
import com.example.mynoteapp.database.model.Note;

import java.util.Calendar;

public class UpdateNoteActivity extends BaseActivity implements View.OnClickListener {

    protected EditText title;
    protected EditText content;
    protected TextView time;
    protected Button updateNote;
    String titleS;
    String timeS;
    String oldContent;
    int pos , id;
    String choosedTime = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_update_note);
        initView();
        Intent intent = getIntent();
        titleS = intent.getStringExtra(Constants.EXTRA_TITLE);
        timeS = intent.getStringExtra(Constants.EXTRA_TIME);
        pos = intent.getIntExtra(Constants.EXTRA_POSITION,-1);
        id = intent.getIntExtra(Constants.EXTRA_ID,0);
        oldContent = intent.getStringExtra(Constants.EXTRA_CONTENT);
        title.setText(titleS);
        content.setText(oldContent);
        time.setText(timeS);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.time) {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                        choosedTime = hourOfDay + ":" + minute;
                }
            },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
            timePickerDialog.show();
        } else if (view.getId() == R.id.update_note) {
            String newTitle , newContent;
            newTitle = title.getText().toString();
            newContent = time.getText().toString();

            if (checkValidated()){
                Note note = new Note(id,newTitle,newContent,choosedTime);
                MyDataBase.getInstance(this).notesDao()
                        .updateNote(note);
                showMessage(R.string.note_updated_successfully, R.string.ok, new DialogInterface.OnClickListener() {
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
        time.setOnClickListener(UpdateNoteActivity.this);
        updateNote = findViewById(R.id.update_note);
        updateNote.setOnClickListener(UpdateNoteActivity.this);
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
