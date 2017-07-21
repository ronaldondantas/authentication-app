package br.com.polibrasnet.authenticatorapp;

import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        result = (TextView) findViewById(R.id.tvResult);

        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                result.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            } else {
                result.setText("FALHOU!");
            }
        }
    }
}
