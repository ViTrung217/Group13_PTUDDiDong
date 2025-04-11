package com.example.crudapp1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mydb;
    EditText etname, etmarks, etid;
    Button insertbtn, showbtn, updatebtn, deletebtn, btnLogout;
    ListView lv;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);
        etname = findViewById(R.id.name);
        etmarks = findViewById(R.id.marks);
        etid = findViewById(R.id.ids);
        insertbtn = findViewById(R.id.Button);
        showbtn = findViewById(R.id.show);
        updatebtn = findViewById(R.id.Update);
        deletebtn = findViewById(R.id.delete);
        lv = findViewById(R.id.Listview);
        btnLogout = findViewById(R.id.btnLogout);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
        insertdata();
        showdata();
        update();
        delete();
        showlist();
    }

    public void insertdata() {
        insertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean Inserted = mydb.insertData(etname.getText().toString(), etmarks.getText().toString());
                if (Inserted) {
                    Toast.makeText(MainActivity.this, "Data is inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showdata() {
        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = mydb.Showdata();
                if (cursor.getCount() == 0) {
                    message("Error", "No data");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("Id : ").append(cursor.getString(0)).append("\n")
                            .append("Name : ").append(cursor.getString(1)).append("\n")
                            .append("Marks : ").append(cursor.getString(2)).append("\n");
                }
                message("Data", buffer.toString());
            }
        });
    }

    public void update() {
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updated = mydb.update(etid.getText().toString(), etname.getText().toString(),
                        etmarks.getText().toString());
                if (updated) {
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error in Updating", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void delete() {
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer delete = mydb.delete(etid.getText().toString());
                if (delete > 0) {
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title)
                .setMessage(message)
                .show();
    }

    public void showlist() {
        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                Cursor cursor = mydb.Showdata();
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                while (cursor.moveToNext()) {
                    list.add("ID: " + cursor.getString(0) + " | Name: " + cursor.getString(1) + " | Marks: " + cursor.getString(2));
                }
                adapter.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, list.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}