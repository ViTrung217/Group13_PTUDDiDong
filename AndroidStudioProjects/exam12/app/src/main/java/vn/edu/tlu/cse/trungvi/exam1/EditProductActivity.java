package vn.edu.tlu.cse.trungvi.exam1;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etProductName, etBrand, etPrice, etStock;
    Button btnUpdateProduct, btnDeleteProduct;
    int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        db = new DatabaseHelper(this);
        etProductName = findViewById(R.id.etProductName);
        etBrand = findViewById(R.id.etBrand);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        productId = getIntent().getIntExtra("product_id", -1);
        loadProductDetails();

        btnUpdateProduct.setOnClickListener(v -> updateProduct());
        btnDeleteProduct.setOnClickListener(v -> deleteProduct());
    }

    private void loadProductDetails() {
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            etProductName.setText(cursor.getString(1));
            etBrand.setText(cursor.getString(2));
            etPrice.setText(String.valueOf(cursor.getInt(3)));
            etStock.setText(String.valueOf(cursor.getInt(4)));
        }
        cursor.close();
    }

    private void updateProduct() {
        String name = etProductName.getText().toString();
        String brand = etBrand.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());
        int stock = Integer.parseInt(etStock.getText().toString());

        try {
            db.getWritableDatabase().execSQL(
                    "UPDATE products SET name = ?, brand = ?, price = ?, stock = ? WHERE id = ?",
                    new Object[]{name, brand, price, stock, productId}
            );
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteProduct() {
        try {
            db.getWritableDatabase().execSQL(
                    "DELETE FROM products WHERE id = ?",
                    new Object[]{productId}
            );
            Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi xóa!", Toast.LENGTH_SHORT).show();
        }
    }

}
