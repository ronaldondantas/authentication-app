package br.com.polibrasnet.authenticatorapp;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AccountAuthenticatorActivity {

    private static final String PARAM_USER_PASS = "PARAM_USER_PASS";
    private static final String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";

    private static String mAuthTokenType = "AUTH_TOKEN_TYPE";

    AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.butLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = ((TextView) findViewById(R.id.metEmail)).getText().toString();
                final String userPass = ((TextView) findViewById(R.id.metPwd)).getText().toString();
                new AsyncTask<Void, Void, Intent>() {
                    @Override
                    protected Intent doInBackground(Void... params) {
//                        String authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);
                        String authToken = "authtoken";
                        final Intent res = new Intent();
                        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.account_type));
                        res.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
                        res.putExtra(PARAM_USER_PASS, userPass);
                        res.putExtra(ARG_IS_ADDING_NEW_ACCOUNT, true);
                        return res;
                    }
                    @Override
                    protected void onPostExecute(Intent intent) {
                        finishLogin(intent);
                    }
                }.execute();
            }
        });

        mAccountManager = AccountManager.get(MainActivity.this);
        Account[] accounts = mAccountManager.getAccountsByType(getString(R.string.account_type));
        // Use accounts[0] (or whatever number of account) after checking that accounts.length &gt; 1

        if (accounts.length > 0) {
            Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accounts[0].name);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account =
                new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = mAuthTokenType;
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
