package com.example.emailpassauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainContentPage extends AppCompatActivity {

    private FirebaseAuth objectFirebaseAuth;
    private Button Signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content_page);

        objectFirebaseAuth= FirebaseAuth.getInstance();
        if(objectFirebaseAuth.getCurrentUser()!=null)
        {
            Toast.makeText(this, "You are sign in:"+objectFirebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "No user is sign in", Toast.LENGTH_SHORT).show();
        }
        Signout=findViewById(R.id.signout_btn);

    }

    public void moveToMainPage(View view)
    {
        try
        {
                    objectFirebaseAuth.signOut();
                    Signout.setVisibility(View.INVISIBLE);

                    Toast.makeText(MainContentPage.this, "SignOut Sucessfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,MainActivity.class));
        }
        catch (Exception e)
        {
            Toast.makeText(this, "moveToMainPage:"
                    +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (objectFirebaseAuth.getCurrentUser()!=null)
        {
            objectFirebaseAuth.signOut();
        }
    }
}
