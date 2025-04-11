package vn.edu.tlu.cse.trungvi.exam;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mydb;
    EditText etproductId, etproductName, etproductBrand, etproductPrice, etproductStock, etproductDescription;
    Button insertbtn, showbtn, updatebtn, deletebtn, btnLogout;
    ListView lv;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);
        etproductId = findViewById(R.id.productId);
        etproductName = findViewById(R.id.productName);
        etproductBrand = findViewById(R.id.productBrand);
        etproductPrice = findViewById(R.id.productPrice);
        etproductStock = findViewById(R.id.productStock);
        etproductDescription = findViewById(R.id.productDescription);
        insertbtn = findViewById(R.id.btnInsert);
        showbtn = findViewById(R.id.btnShow);
        updatebtn = findViewById(R.id.btnUpdate);
        deletebtn = findViewById(R.id.btnDelete);
        lv = findViewById(R.id.productListView);
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

    // Thêm sản phẩm
    public void insertdata() {
        insertbtn.setOnClickListener(v -> {
            String productName = etproductName.getText().toString();
            String productBrand = etproductBrand.getText().toString();
            int productPrice = Integer.parseInt(etproductPrice.getText().toString());
            int productStock = Integer.parseInt(etproductStock.getText().toString());
            String productDescription = etproductDescription.getText().toString();

            boolean Inserted = mydb.insertData(productName, productBrand, productPrice, productStock, productDescription);
            if (Inserted) {
                Toast.makeText(MainActivity.this, "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị dữ liệu sản phẩm
    public void showdata() {
        showbtn.setOnClickListener(v -> {
            Cursor cursor = mydb.showData();
            if (cursor.getCount() == 0) {
                message("Lỗi", "Không có dữ liệu sản phẩm");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getString(0)).append("\n")
                        .append("Tên sản phẩm: ").append(cursor.getString(1)).append("\n")
                        .append("Hãng: ").append(cursor.getString(2)).append("\n")
                        .append("Giá: ").append(cursor.getString(3)).append("\n")
                        .append("Tồn kho: ").append(cursor.getString(4)).append("\n")
                        .append("Mô tả: ").append(cursor.getString(5)).append("\n");
            }
            message("Dữ liệu sản phẩm", buffer.toString());
        });
    }

    // Cập nhật thông tin sản phẩm
    public void update() {
        updatebtn.setOnClickListener(v -> {
            String id = etproductId.getText().toString();
            String productName = etproductName.getText().toString();
            String productBrand = etproductBrand.getText().toString();
            int productPrice = Integer.parseInt(etproductPrice.getText().toString());
            int productStock = Integer.parseInt(etproductStock.getText().toString());
            String productDescription = etproductDescription.getText().toString();

            boolean updated = mydb.update(id, productName, productBrand, productPrice, productStock, productDescription);
            if (updated) {
                Toast.makeText(MainActivity.this, "Sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa sản phẩm
    public void delete() {
        deletebtn.setOnClickListener(v -> {
            String id = etproductId.getText().toString();
            Integer delete = mydb.delete(id);
            if (delete > 0) {
                Toast.makeText(MainActivity.this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị thông báo
    public void message(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title)
                .setMessage(message)
                .show();
    }

    // Hiển thị danh sách sản phẩm
    public void showlist() {
        showbtn.setOnClickListener(v -> {
            list.clear();
            Cursor cursor = mydb.showData();
            if (cursor.getCount() == 0) {
                Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }
            while (cursor.moveToNext()) {
                list.add("ID: " + cursor.getString(0) + " | Tên sản phẩm: " + cursor.getString(1) +
                        " | Hãng: " + cursor.getString(2) + " | Giá: " + cursor.getString(3) +
                        " | Tồn kho: " + cursor.getString(4));
            }
            adapter.notifyDataSetChanged();
        });

        lv.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(MainActivity.this, list.get(i), Toast.LENGTH_SHORT).show()
        );
    }
}
