package com.example.final_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewUserDialog extends AppCompatDialogFragment {

    EditText etdialogemail, etdialogpass, etdialogname,etdialogrepass;
    DBhelper db;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db = new DBhelper(getContext());
                        long id;
                        db.getWritableDatabase();
                        String name = etdialogname.getText().toString();
                        String email = etdialogemail.getText().toString();
                        String pass = etdialogpass.getText().toString();
                        String repass = etdialogrepass.getText().toString();
                        System.out.println(db.CREATE_TABLE);
                        if (name.equals("") || email.equals("") || pass.equals(""))
                            Toast.makeText(getContext(), "please enter all the fields", Toast.LENGTH_SHORT).show();

                        else {
                            if (etdialogpass.getText().toString().equals(etdialogrepass.getText().toString()))
                                id = db.insertUser(new User(name, email, pass));
                        }
                    }
                });

        etdialogname=view.findViewById(R.id.etdialogname);
        etdialogemail=view.findViewById(R.id.etdialogemail);
        etdialogpass=view.findViewById(R.id.etdialogpass);
        etdialogrepass=view.findViewById(R.id.etdialogrepass);

        return builder.create();


    }
}
