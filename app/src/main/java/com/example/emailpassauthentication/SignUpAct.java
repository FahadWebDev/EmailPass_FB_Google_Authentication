package com.example.emailpassauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class SignUpAct extends AppCompatActivity {

    private EditText emailET,passwordET;
    private Button signUpUserBtn;

    private ProgressBar objectProgressBar;
    private FirebaseAuth objectFirebaseAuth;   //Step 1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        connectXMLToJava();
        objectFirebaseAuth= FirebaseAuth.getInstance(); //Step 2
    }

    private void connectXMLToJava()
    {
        try
        {
            emailET=findViewById(R.id.emailET);
            passwordET=findViewById(R.id.passwordET);

            signUpUserBtn=findViewById(R.id.signUpUserBtn);
            objectProgressBar=findViewById(R.id.signUpProgressBar);

            signUpUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    signUpUser();
                    checkIfUserExists();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "connectXMLToJava:"
                    +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfUserExists()
    {
        try
        {
            if(!emailET.getText().toString().isEmpty())
            {
                if(objectFirebaseAuth!=null)
                {
                    objectProgressBar.setVisibility(View.VISIBLE);
                    signUpUserBtn.setEnabled(false);

                    objectFirebaseAuth.fetchSignInMethodsForEmail(emailET.getText().toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            boolean check=task.getResult().getSignInMethods().isEmpty();
                                            if(check)
                                            {
                                                signUpUser();
                                            }
                                            else
                                            {
                                                objectProgressBar.setVisibility(View.INVISIBLE);
                                                signUpUserBtn.setEnabled(true);

                                                Toast.makeText(SignUpAct.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }
                            )
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    objectProgressBar.setVisibility(View.INVISIBLE);
                                    signUpUserBtn.setEnabled(true);

                                    Toast.makeText(SignUpAct.this, "Fails to check if user exists:"
                                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "Firebase Auth object is null", Toast.LENGTH_SHORT).show();
                }
            }
            else if(emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the email first", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "checkIfUserExists"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void signUpUser()
    {
        try
        {
            if(!emailET.getText().toString().isEmpty()
            && !passwordET.getText().toString().isEmpty())
            {
                if(objectFirebaseAuth!=null)
                {
//                    objectProgressBar.setVisibility(View.VISIBLE);
//                    signUpUserBtn.setEnabled(false);

                    objectFirebaseAuth.createUserWithEmailAndPassword(emailET.getText().toString(),
                            passwordET.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    objectProgressBar.setVisibility(View.INVISIBLE);
                                    signUpUserBtn.setEnabled(true);

                                    Toast.makeText(SignUpAct.this, "User signed up successfully", Toast.LENGTH_SHORT).show();
                                    if(authResult.getUser()!=null)
                                    {
                                        objectFirebaseAuth.signOut();
                                        startActivity(new Intent(SignUpAct.this,MainActivity.class));

                                        finish();

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    objectProgressBar.setVisibility(View.INVISIBLE);
                                    signUpUserBtn.setEnabled(true);

                                    Toast.makeText(SignUpAct.this, "Fails to sign up user:"
                                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "Firebase object is not initialized", Toast.LENGTH_SHORT).show();
                }
            }
            else if(emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            }
            else if(passwordET.getText().toString().isEmpty())
            {
                objectProgressBar.setVisibility(View.INVISIBLE);
                signUpUserBtn.setEnabled(true);

                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                passwordET.requestFocus();
            }
        }
        catch (Exception e)
        {
            objectProgressBar.setVisibility(View.INVISIBLE);
            signUpUserBtn.setEnabled(true);

            Toast.makeText(this, "signUpUser:"
                    +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
