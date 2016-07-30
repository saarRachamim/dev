package com.example.saar.locationalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText registerUsername, registerPassword, registerNickName;
    Button registerButton;
    UserStore userStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerUsername = (EditText)findViewById(R.id.registerUsername);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerNickName = (EditText)findViewById(R.id.registerNickName);
        registerButton = (Button)findViewById(R.id.registerButton);
        userStore = new UserStore(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registerButton:
                String name = registerNickName.getText().toString();
                String userName = registerUsername.getText().toString();
                String password = registerPassword.getText().toString();

                User newUser = new User(name, userName, password);
                authenticate(newUser);
                break;
        }
    }

    private void authenticate(final User newUser) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.getUserData(newUser, new GetUserCallBack() {
            @Override
            public void done(User user) {
                if (user == null)
                {
                    pushUserToDataBase(newUser);
                }
                else
                    showError("User is taken");
            }
        });
    }

    private void showError(String s) {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(Register.this);
        errorDialog.setMessage(s);
        errorDialog.create().show();
    }

    private void pushUserToDataBase(User user)
    {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserData(user, new GetUserCallBack() {
            @Override
            public void done(User user) {
//                userStore.saveUserInLocalDB(user);
                startActivity(new Intent(Register.this, login.class));
            }
        });
    }
}
