package techy.ap.com;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button lock, disable, enable;
    public static final int RESULT_ENABLED = 11;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);
        lock = (Button) findViewById(R.id.lock);
        disable = (Button) findViewById(R.id.disablebtn);
        enable = (Button) findViewById(R.id.enablebtn);
        lock.setOnClickListener(this);
        disable.setOnClickListener(this);
        enable.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        if (v == lock) {
            boolean active = devicePolicyManager.isAdminActive(componentName);


            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Toast.makeText(this, "You need to enable Admin device features", Toast.LENGTH_SHORT).show();
            }
        } else if (v == enable) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Explanation comes here");
            startActivityForResult(intent, RESULT_ENABLED);
        } else if (v == disable) {
            devicePolicyManager.removeActiveAdmin(componentName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ENABLED:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "You have enabled the Admin Device Feature", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "problem to enabled the Admin Device Feature", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
