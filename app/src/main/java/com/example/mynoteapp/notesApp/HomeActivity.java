package com.example.mynoteapp.notesApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mynoteapp.Constants;
import com.example.mynoteapp.R;
import com.example.mynoteapp.base.BaseActivity;
import com.example.mynoteapp.database.MyDataBase;
import com.example.mynoteapp.database.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    NotesRecyclerAdapter notesRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        setSupportActionBar(toolbar);
        swipeToDeleteAndUndo();
        initRecyclerView();
        openAddNoteActivity();
        openUpdateNoteActivity();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private void swipeToDeleteAndUndo() {
        // swipe to remove item from recyclerView
        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Note note = notes.remove(viewHolder.getAdapterPosition());
                MyDataBase.getInstance(HomeActivity.this).notesDao()
                        .deleteNote(note);
                notesRecyclerAdapter.notifyDataSetChanged();
                // coordinatorLayout (root)
                Snackbar.make(coordinatorLayout,"Item was removed from the list.",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notes.add(note);
                                MyDataBase.getInstance(HomeActivity.this).notesDao()
                                        .addNote(note);
                                notesRecyclerAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        };
    }

    List<Note> notes;
    @Override
    protected void onStart() {
        super.onStart();
        notes = MyDataBase.getInstance(this).notesDao()
                .getAllNotes();
        notesRecyclerAdapter.changeList(notes);
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        notesRecyclerAdapter = new NotesRecyclerAdapter(null);
        layoutManager = new LinearLayoutManager(this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(notesRecyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void openAddNoteActivity() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddNoteActivity.class));
            }
        });
    }
    public void openUpdateNoteActivity(){
        notesRecyclerAdapter.setOnItemClickListener(new NotesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, String title, String time, int id, String content) {
                Intent intent = new Intent(HomeActivity.this,UpdateNoteActivity.class);
                intent.putExtra(Constants.EXTRA_POSITION,pos);
                intent.putExtra(Constants.EXTRA_TITLE,title);
                intent.putExtra(Constants.EXTRA_TIME,time);
                intent.putExtra(Constants.EXTRA_ID,id);
                intent.putExtra(Constants.EXTRA_CONTENT,content);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        showMessage(R.string.do_you_want_to_exit, R.string.ok, R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
