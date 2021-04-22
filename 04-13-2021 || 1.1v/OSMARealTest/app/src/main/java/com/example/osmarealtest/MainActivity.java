package com.example.osmarealtest;

import androidx.annotation.NonNull;
import androidx.annotation.XmlRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static LinkedList<User> Users = new LinkedList<>();
    String userInp, passInp, confirmPassInp, emailInp, account;
    String userCheck="", passCheck="";
    EditText usernameLogin, passwordLogin;
    String TAG = "MainActivity";
    EditText username, password, confirmPassword, email;
    RadioGroup accountType;
    RadioButton accountChoice;
    Button signInButton, redirectLogin, redirectSignUp, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_activity);
        onSignUp();
    }

    private void onSetData(String username, String password, String email, String account) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);
        user.put("Email", email);
        user.put("Account", account);

        db.collection("Users").document(username)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data successfully set!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    protected void onLogin() {
        setContentView(R.layout.login_activity);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameLogin = (EditText) findViewById(R.id.usernameLogin);
                passwordLogin = (EditText) findViewById(R.id.passwordLogin);
                userCheck = usernameLogin.getText().toString();
                passCheck = passwordLogin.getText().toString();

                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(userCheck);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot res = task.getResult();
                            if(res.exists() && res.get("Password").equals(passCheck)){
                                Toast.makeText(MainActivity.this,"Account login SUCCESS!",Toast.LENGTH_SHORT).show();
                                System.out.println(res.getData().toString());
                            } else {
                                Toast.makeText(MainActivity.this,"Username or Password INCORRECT!",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this,"Account login FAILED!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        redirectSignUp = (Button) findViewById(R.id.redirectSignUp);
        redirectSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUp();
            }
        });
    }

    protected void onSignUp() {
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        email = (EditText) findViewById(R.id.email);
        accountType = (RadioGroup) findViewById(R.id.accountType);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInp = username.getText().toString();
                passInp = password.getText().toString();
                confirmPassInp = confirmPassword.getText().toString();
                emailInp = email.getText().toString();

                int selectedType = accountType.getCheckedRadioButtonId();
                accountChoice = (RadioButton) findViewById(selectedType);
                account = (String) accountChoice.getText();

                if(passInp.equals(confirmPassInp)){
                    Users.add(new User(userInp,passInp,emailInp,account));
                    onSetData(userInp,passInp,emailInp,account);
                    Toast.makeText(MainActivity.this,"Account successfully registered!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"Different inputted passwords",Toast.LENGTH_SHORT).show();
                }
            }
        });
        redirectLogin = (Button) findViewById(R.id.redirectLogin);
        redirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }
}