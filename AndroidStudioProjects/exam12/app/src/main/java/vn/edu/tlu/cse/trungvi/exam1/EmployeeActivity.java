package vn.edu.tlu.cse.trungvi.exam1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeActivity extends AppCompatActivity {
    Button btnManageProducts, btnViewOrders, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageProducts.setOnClickListener(v ->
                startActivity(new Intent(EmployeeActivity.this, ManageProductsActivity.class)));

        btnViewOrders.setOnClickListener(v ->
                startActivity(new Intent(EmployeeActivity.this, ViewOrdersActivity.class)));

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(EmployeeActivity.this, MainActivity.class));
            finish();
        });
    }
}
