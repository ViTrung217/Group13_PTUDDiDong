package vn.edu.tlu.cse.trungvi.exam1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ManageProductsActivity extends AppCompatActivity {
    DatabaseHelper db;
    ListView lvProducts;
    Button btnAddProduct;
    ArrayList<String> productList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        db = new DatabaseHelper(this);
        lvProducts = findViewById(R.id.lvProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Handle "Add Product" button click
        btnAddProduct.setOnClickListener(v ->
                startActivity(new Intent(ManageProductsActivity.this, AddProductActivity.class)));

        // Load products into the ListView
        loadProducts();

        // Set OnItemClickListener for the ListView
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected product and extract its ID
                String selectedItem = productList.get(position);
                int productId = Integer.parseInt(selectedItem.split("\\.")[0]);

                // Pass the product ID to EditProductActivity
                Intent intent = new Intent(ManageProductsActivity.this, EditProductActivity.class);
                intent.putExtra("product_id", productId);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM products", null);
        while (cursor.moveToNext()) {
            // Assuming the product's ID is in the first column (index 0), name in the second column (index 1), and price in the fourth column (index 3)
            productList.add(cursor.getInt(0) + ". " + cursor.getString(1) + " - " + cursor.getInt(3) + " VNĐ");
        }
        cursor.close();

        // Set up the adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        lvProducts.setAdapter(adapter);
    }
}
