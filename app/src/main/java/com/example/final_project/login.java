package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity implements View.OnClickListener {
    EditText etemail, etpas;
    Button btnlog, btnnew;
    CheckBox checkBox;
    Button go;
    DBhelper DB;
    SharedPreferences sp;

    private static final String SHARED_PREF_EMAIL = "mypref";
    private static final String Key_EMAIL = "email";
    private static final String Key_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlog = findViewById(R.id.btnlog);
        btnnew = findViewById(R.id.btnnew);
        etemail = findViewById(R.id.etemail);
        etpas = findViewById(R.id.etpas);
        checkBox = findViewById(R.id.checkbox);

        btnlog.setOnClickListener(this);
        btnnew.setOnClickListener(this);

        DB = new DBhelper(this);
        sp = getSharedPreferences(SHARED_PREF_EMAIL, MODE_PRIVATE);

        if (sp.getString(Key_EMAIL,null)!=null) {
            etemail.setText(sp.getString(Key_EMAIL, null));
            etpas.setText(sp.getString(Key_PASSWORD, null));
        }
    }

    @Override
    public void onClick(View view) {

        if (view == btnlog) {
            etemail = this.etemail;
            etpas = this.etpas;
            if (DB.checkLogin(etemail.getText().toString(), etpas.getText().toString())) {
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Key_EMAIL, etemail.getText().toString());
                    editor.putString(Key_PASSWORD, etpas.getText().toString());
                    editor.apply();
                    Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Key_EMAIL, null);
                    editor.putString(Key_PASSWORD, null);
                    editor.apply();
                }
                //מסדר את האידי, 2 בודק ומסיים לבדוק על אתר ממנו תקח מידע , וכפתור התנתקות
                User user =DB.getUser(etemail.getText().toString(),etpas.getText().toString());
                if(user!=null) {
                    Intent intent = new Intent(this, Info.class);
                    //להעביר באינט משתמש לדפים הבאים כדי לשמור על התחשברות
                    intent.putExtra("user", user);
                    startActivity(intent);
                }

            } else
                Toast.makeText(this, "user was not found", Toast.LENGTH_SHORT).show();
        }

        if (view == btnnew) {
            NewUserDialog newUserDialog = new NewUserDialog();
            newUserDialog.show(getSupportFragmentManager(), "new user dialog");
        }
    }
}















