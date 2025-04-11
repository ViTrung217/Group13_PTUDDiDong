package vn.edu.tlu.cse.trungvi.exam2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ListView listViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        dbHelper = new DatabaseHelper(this);
        listViewProducts = findViewById(R.id.listViewProducts);

        // Hiển thị sản phẩm
        Cursor cursor = dbHelper.showData();
        String[] fromColumns = {"product_name", "brand", "price"};
        int[] toViews = {R.id.txtProductName, R.id.txtBrand, R.id.txtPrice};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_product, cursor, fromColumns, toViews, 0);
        listViewProducts.setAdapter(adapter);
    }
}
