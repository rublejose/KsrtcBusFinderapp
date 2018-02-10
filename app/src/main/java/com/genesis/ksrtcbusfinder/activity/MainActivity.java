package com.genesis.ksrtcbusfinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.task.DataTransfer;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText=null;
    EditText passwordEditText=null;
    TextView newuserTextView=null;
    Button loginButton=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        newuserTextView=findViewById(R.id.newuserTextView);
        newuserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,UserSignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton=findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().equals("")){
                    usernameEditText.setError("please enter the name");
                }else if (passwordEditText.getText().toString().equals("")){
                    passwordEditText.setError("please enter password");
                }else{
                    String username=usernameEditText.getText().toString();
                    String password=passwordEditText.getText().toString();
                    DataTransfer dataTransfer=new DataTransfer(MainActivity.this);
                    dataTransfer.execute("user_login",username,password);
                }
            }
        });
    }
}
