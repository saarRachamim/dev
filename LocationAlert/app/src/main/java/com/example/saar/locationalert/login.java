package com.example.saar.locationalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView registerLink;
    UserStore userStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = (EditText)findViewById(R.id.loginUsername);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.loginButton);
        registerLink = (TextView)findViewById(R.id.registerLink);
        userStore = new UserStore(this);
        loginButton.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginButton:
                String userName = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                User newUser = new User(userName, password);

                authenticate(newUser);



                break;
            case R.id.registerLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User newUser) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.getUserData(newUser, new GetUserCallBack() {
            @Override
            public void done(User user) {
                if (user == null)
                    showError("Wrong user name or password");
                else
                    loginUser(user);
            }
        });
    }

    private void showError(String s) {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(login.this);
        errorDialog.setMessage(s);
        errorDialog.create().show();
    }

    private void loginUser(User user) {
        userStore.saveUserInLocalDB(user);
        userStore.setIsUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }
}
