package com.journaldev.recyclerviewcardview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginLogout extends AppCompatActivity {

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_login_logout);

        register = (TextView) findViewById(R.id.signuptextview);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.signuptextview: startActivity(new Intent(getApplicationContext(), RegisterUser.class)); break;
                }
            }
        });
    }
}
