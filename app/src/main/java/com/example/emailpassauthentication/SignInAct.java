package com.example.emailpassauthentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInAct extends AppCompatActivity {

    private EditText emailET,passwordET;
    private Button signInUserBtn,Signout;

    private ProgressBar objectProgressBar;
    private FirebaseAuth objectFirebaseAuth;

    private TextView recoveryTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        connectXMLToJava();
        objectFirebaseAuth= FirebaseAuth.getInstance();

        signInUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        recoveryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrecoverdialogbox();
            }
        });
    }

    private void showrecoverdialogbox()
    {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);

        final EditText emailText=new EditText(this);
        emailText.setHint("Email");
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(emailText);

        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email=emailText.getText().toString().trim();

                beginRecovery(email);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecovery(String email)
    {
        objectFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(SignInAct.this, "Recovery Email is send", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignInAct.this, "Could'nt send Recovery Email", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {

                Toast.makeText(SignInAct.this, "Failed to recover"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void connectXMLToJava()
    {
        try
        {
            emailET=findViewById(R.id.userEmailET);
            passwordET=findViewById(R.id.userPasswordET);

            signInUserBtn=findViewById(R.id.signInUser);
            objectProgressBar=findViewById(R.id.signInUserProgressBar);

            recoveryTV=findViewById(R.id.Forgetpassword);
            Signout=findViewById(R.id.signout_btn);

            signInUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signInUser();
                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(this, "connectXMLToJava:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInUser()
    {
        try
        {
            if(!emailET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty())
            {
                if(objectFirebaseAuth!=null)
                {
                    if(objectFirebaseAuth.getCurrentUser()!=null)
                    {
                        objectFirebaseAuth.signOut();
                        Toast.makeText(this, "Sign Out Successfully", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        objectProgressBar.setVisibility(View.VISIBLE);
                        signInUserBtn.setEnabled(false);

                        objectFirebaseAuth.signInWithEmailAndPassword(emailET.getText().toString(),
                                passwordET.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(SignInAct.this, "User sign in successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInAct.this,MainContentPage.class));

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        objectProgressBar.setVisibility(View.INVISIBLE);
                                        signInUserBtn.setEnabled(true);

                                        Toast.makeText(SignInAct.this, "Fails to sign in user:"+
                                                e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
            else if(emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            }
            else if(passwordET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
                passwordET.requestFocus();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "signInUser:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
