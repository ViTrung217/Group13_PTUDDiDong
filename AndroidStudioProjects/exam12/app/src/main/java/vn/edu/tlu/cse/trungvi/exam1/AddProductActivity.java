package vn.edu.tlu.cse.trungvi.exam1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etProductName, etBrand, etPrice, etStock, etDescription;
    Button btnSaveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = new DatabaseHelper(this);
        etProductName = findViewById(R.id.etProductName);
        etBrand = findViewById(R.id.etBrand);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        etDescription = findViewById(R.id.etDescription);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        btnSaveProduct.setOnClickListener(v -> {
            String name = etProductName.getText().toString();
            String brand = etBrand.getText().toString();
            int price = Integer.parseInt(etPrice.getText().toString());
            int stock = Integer.parseInt(etStock.getText().toString());
            String description = etDescription.getText().toString();

            if (db.addProduct(name, brand, price, stock, description)) {
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi thêm sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

