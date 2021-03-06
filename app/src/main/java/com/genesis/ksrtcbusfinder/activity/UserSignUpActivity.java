package com.genesis.ksrtcbusfinder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.task.DataTransfer;

public class UserSignUpActivity extends AppCompatActivity {
    EditText usernameSignEditText=null;
    EditText nameSignEditText=null;
    EditText passwordSignUpEditText=null;
    Button signInButton=null;
    EditText phoneEditText=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        usernameSignEditText=findViewById(R.id.usernameSignEditText);
        nameSignEditText=findViewById(R.id.nameSignEditText);
        passwordSignUpEditText=findViewById(R.id.passwordSignUpEditText);
        signInButton=findViewById(R.id.signInButton);
        phoneEditText=findViewById(R.id.phoneEditText);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameSignEditText.getText().toString().equals("")){
                    usernameSignEditText.setError("Please Enter Username");
                }else if(nameSignEditText.getText().toString().equals("")){
                    nameSignEditText.setError("Please Enter Name");
                } else if (passwordSignUpEditText.getText().toString().equals("")) {
                    passwordSignUpEditText.setError("Please Enter Password");
                }else if(phoneEditText.getText().toString().equals("")){
                    phoneEditText.setError("Please Enter Phone Number");
                }else{
                    String username= usernameSignEditText.getText().toString();
                    String name=nameSignEditText.getText().toString();
                    String password=passwordSignUpEditText.getText().toString();
                    String phone=phoneEditText.getText().toString();
                    DataTransfer dataTransfer=new DataTransfer(UserSignUpActivity.this);
                    dataTransfer.execute("user_SignUp",username,name,password,phone);
                }

            }
        });

    }
}
