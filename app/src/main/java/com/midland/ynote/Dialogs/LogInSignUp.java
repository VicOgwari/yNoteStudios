package com.midland.ynote.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.midland.ynote.Activities.LoginActivity;
import com.midland.ynote.Activities.SignUpActivity;
import com.midland.ynote.R;

public class LogInSignUp extends Dialog {

    public LogInSignUp(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log_in_sign_up);
        initViewSnActions();
    }

    private void initViewSnActions() {
        Button login, register;
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        });

        register.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SignUpActivity.class);
            getContext().startActivity(intent);
        });
    }
}
