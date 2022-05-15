package com.jaffar.firebasedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Dao.firebasedatabase.DaoLayer;

public class mainPage extends AppCompatActivity {

    TextInputEditText name, position;
    MaterialButton submit, update, delete, search, retrieveAll, closeShowAll;
    private DaoLayer db;
    Employee emp;
    RecyclerView r;
    recyclerAdapter Tadapter;
    ConstraintLayout showAllRecycler;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        name = findViewById(R.id.inputName);
        position = findViewById(R.id.inputPosition);
        submit = findViewById(R.id.submitBtn);
        update = findViewById(R.id.updateBtn);
        delete = findViewById(R.id.deleteBtn);
        search = findViewById(R.id.searchBtn);
        pg = findViewById(R.id.progressBar);
        db = new DaoLayer(this);
        retrieveAll = findViewById(R.id.retrieveAllBtn);
        showAllRecycler = findViewById(R.id.ShowingAllLayout);
        closeShowAll = findViewById(R.id.closeShowAll);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                String getName = name.getText().toString();
                String getPosition = position.getText().toString();
                if(getName.isEmpty()||getPosition.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                }else{
                    emp = new Employee();
                    emp.setName(getName.toLowerCase(Locale.ROOT));
                    emp.setPosition(getPosition);
                    db.insert(emp);
                    pg.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    position.setText("");
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Dialog d = new Dialog(mainPage.this);
             d.requestWindowFeature(Window.FEATURE_ACTION_BAR);
             d.setContentView(R.layout.search_dialog);
             MaterialButton sumbitt = d.findViewById(R.id.SubmittBtn);
             EditText nameField = d.findViewById(R.id.storeSearchName);
             d.show();
             sumbitt.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (nameField.getText().toString().isEmpty()){
                         Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                     }else{
                         pg.setVisibility(View.VISIBLE);
                         new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 d.dismiss();
                                 Employee emp = db.retrieve(nameField.getText().toString().toLowerCase(Locale.ROOT));
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         Dialog d = new Dialog(mainPage.this);
                                         d.requestWindowFeature(Window.FEATURE_ACTION_BAR);
                                         d.setContentView(R.layout.retrieve_dialog);
                                         d.setTitle("Retrieved");
                                         MaterialButton OK = d.findViewById(R.id.OkBtn);
                                         TextView nameHere, PosHere;
                                         if (emp!=null) {
                                             nameHere = d.findViewById(R.id.storeName);
                                             PosHere = d.findViewById(R.id.storePosition);
                                             nameHere.setText(emp.name);
                                             PosHere.setText(emp.position);
                                             pg.setVisibility(View.GONE);
                                             d.show();
                                         }
                                         else {
                                             pg.setVisibility(View.GONE);
                                             Toast.makeText(getApplicationContext(), "Record not found", Toast.LENGTH_SHORT).show();
                                         }
                                         OK.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 d.dismiss();
                                             }
                                         });
                                     }
                                 });
                             }
                         }).start();
                     }

                 }
             });

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                String getName = name.getText().toString().toLowerCase(Locale.ROOT);
                String getPosition = position.getText().toString();
                Employee emp = new Employee();
                emp.setName(getName);
                emp.setPosition(getPosition);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      Boolean check = db.update(emp);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(check){
                                    pg.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Submitted Updated", Toast.LENGTH_SHORT).show();
                                }else {
                                    pg.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Record not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        retrieveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Employee> e = db.retrieveAll();
                        while (!db.complete);
                        Tadapter = new recyclerAdapter(mainPage.this, e);
                        r = findViewById(R.id.showAllRecycler);
                        r.setLayoutManager(new LinearLayoutManager(mainPage.this));
                        r.setAdapter(Tadapter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAllRecycler.setVisibility(View.VISIBLE);
                                pg.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
            }
        });

        closeShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllRecycler.setVisibility(View.GONE);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Dialog d = new Dialog(mainPage.this);
                    d.requestWindowFeature(Window.FEATURE_ACTION_BAR);
                    d.setContentView(R.layout.search_dialog);
                    MaterialButton sumbitt = d.findViewById(R.id.SubmittBtn);
                    EditText nameField = d.findViewById(R.id.storeSearchName);
                    d.show();
                    sumbitt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (nameField.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                            }else{
                                pg.setVisibility(View.VISIBLE);
                                new Thread(new Runnable() {
                                    boolean g;
                                    @Override
                                    public void run() {
                                        g = db.delete(nameField.getText().toString().toLowerCase(Locale.ROOT));
                                        d.dismiss();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (g){
                                                    pg.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Record deleted", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    pg.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Operation failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }
                    });
            }
        });

    }
}