package com.example.osmarealtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.osmarealtest.emailsend.GMailSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static LinkedList<User> user = new LinkedList<>();
    private static String activeAccountUsername;
    String userInp, passInp, confirmPassInp, emailInp, account;
    String userCheck="", passCheck="";
    EditText usernameLogin, passwordLogin;
    String TAG="MainActivity";
    EditText username, password, confirmPassword, email;
    RadioGroup accountType;
    RadioButton accountChoice;
    Button signInButton, redirectLogin, redirectSignUp, loginButton, forgot_passButton, backToLoginButton;
    EditText input_key;
    String input_act_key;
    Button submit_key_button;
    EditText account_name, account_type, account_email, account_pass;
    Button edit_profile_button, logout_button, delete_account_button;
    EditText to_username, input_activation_key, new_password_inp, confirm_new_pass_inp;
    String recipient, new_pass, confirm_new_pass, act_key;
    Button submit_email_button, submit_new_password_button, navigation_button, navigation_button2;
    TextView account_name_cus, account_email_cus, account_name_sup, account_email_sup;
    TextView profile_button, chat_button, product_list_button;
    ImageView profile_image, chat_image, product_list_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_screen);
        onSignUp();
    }

    private void onSetData(String username, String password, String email, String account, String key) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);
        user.put("Email", email);
        user.put("Account", account);
        user.put("Activated",false);
        user.put("Activation Key",key);

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
                            if(res.exists() && res.get("Password").equals(passCheck) && res.get("Activated").equals(true)){
                                Toast.makeText(MainActivity.this,"Account login SUCCESS!",Toast.LENGTH_SHORT).show();
                                activeAccountUsername=userCheck;
                                onProfile();
                            } else if(res.exists() && res.get("Password").equals(passCheck) && res.get("Activated").equals(false)) {
                                setContentView(R.layout.activation_first_time);
                                submit_key_button = (Button) findViewById(R.id.submit_key_button);
                                submit_key_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        input_key = (EditText) findViewById(R.id.input_key);
                                        input_act_key = input_key.getText().toString();
                                        if(ValidityCheck.check(input_act_key)==1){
                                            Toast.makeText(MainActivity.this,"Account ACTIVATED!",Toast.LENGTH_SHORT).show();
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("Activated", true);
                                            db.collection("Users").document(userCheck).set(user, SetOptions.merge());
                                            activeAccountUsername=userCheck;
                                        } else if(ValidityCheck.check(input_act_key)==0) {
                                            Toast.makeText(MainActivity.this,"Activation key INVALID!",Toast.LENGTH_SHORT).show();
                                        } else if(ValidityCheck.check(input_act_key)==-1) {
                                            Toast.makeText(MainActivity.this,"Format INVALID!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                backToLogin();
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

        forgot_passButton = (Button) findViewById(R.id.forgot_passButton);
        forgot_passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPassword();
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

    private void onChatList() {
        tabActions();
    }

    /**
    private void navigateHome() {
        navigation_button2 = (Button) findViewById(R.id.navigation_button2);
        navigation_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot res = task.getResult();
                        if(res.get("Account").equals("Supplier")) { supplierTab(); }
                        else { customerTab(); }
                    }
                });
            }
        });
    }*/

    private void onProfile() {
        setContentView(R.layout.android_profile);
        readUserData();
        edit_profile_button = (Button) findViewById(R.id.edit_profile_button);
        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = account_name.getText().toString();
                String email = account_email.getText().toString();
                String pass = account_pass.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("Username", name);
                user.put("Email", email);
                user.put("Password", pass);
                db.collection("Users").document(activeAccountUsername).set(user, SetOptions.merge());
                Toast.makeText(MainActivity.this,"Profile details SAVED!",Toast.LENGTH_SHORT).show();
            }
        });

        logout_button = (Button) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
                Toast.makeText(MainActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                activeAccountUsername="";
            }
        });

        delete_account_button = (Button) findViewById(R.id.delete_account_button);
        delete_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
                FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername).delete();
                Toast.makeText(MainActivity.this,"Account successfully DELETED!",Toast.LENGTH_SHORT).show();
                activeAccountUsername="";
            }
        });
        tabActions();
    }

    private void tabActions() {
        navigation_button = (Button) findViewById(R.id.navigation_button);
        navigation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot res = task.getResult();
                        if(res.get("Account").equals("Supplier")) { supplierTab(); }
                        else { customerTab(); }
                    }
                });
            }
        });
    }

    private void customerTab() {
        setContentView(R.layout.customer_tab);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();

                account_name_cus = (TextView) findViewById(R.id.account_name_cus);
                account_email_cus = (TextView) findViewById(R.id.account_email_cus);

                account_name_cus.setText(res.get("Username").toString(), TextView.BufferType.EDITABLE);
                account_name_cus.setEnabled(false);
                account_email_cus.setText(res.get("Email").toString(), TextView.BufferType.EDITABLE);
                account_email_cus.setEnabled(false);
            }
        });

        commonTabActions();
    }

    private void supplierTab() {
        setContentView(R.layout.supplier_tab);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();

                account_name_sup = (TextView) findViewById(R.id.account_name_sup);
                account_email_sup = (TextView) findViewById(R.id.account_email_sup);

                account_name_sup.setText(res.get("Username").toString(), TextView.BufferType.EDITABLE);
                account_name_sup.setEnabled(false);
                account_email_sup.setText(res.get("Email").toString(), TextView.BufferType.EDITABLE);
                account_email_sup.setEnabled(false);
            }
        });
        commonTabActions();
    }

    private void commonTabActions() {
        profile_button = (TextView) findViewById(R.id.profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onProfile(); }
        });

        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onProfile(); }
        });

        chat_button = (TextView) findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onChatList(); }
        });

        chat_image = (ImageView) findViewById(R.id.chat_image);
        chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onChatList(); }
        });

        product_list_button = (TextView) findViewById(R.id.profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.product_list); }
        });

        product_list_image = (ImageView) findViewById(R.id.product_list_image);
        product_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.product_list); }
        });
    }

    private void readUserData() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(activeAccountUsername);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();

                account_name = (EditText) findViewById(R.id.account_name_sup);
                account_type = (EditText) findViewById(R.id.account_type);
                account_email = (EditText) findViewById(R.id.account_email_sup);
                account_pass = (EditText) findViewById(R.id.account_pass);

                account_name.setText(res.get("Username").toString(), TextView.BufferType.EDITABLE);
                account_type.setText(res.get("Account").toString(), TextView.BufferType.EDITABLE);
                account_type.setEnabled(false);
                account_type.setTextColor(Color.DKGRAY);
                account_email.setText(res.get("Email").toString(), TextView.BufferType.EDITABLE);
                account_pass.setText(res.get("Password").toString(), TextView.BufferType.EDITABLE);
            }
        });
    }

    private void onForgotPassword() {
        setContentView(R.layout.forgot_password);

        to_username = (EditText) findViewById(R.id.to_username);
        input_activation_key = (EditText) findViewById(R.id.input_activation_key);
        new_password_inp = (EditText) findViewById(R.id.new_password_inp);
        confirm_new_pass_inp = (EditText) findViewById(R.id.confirm_new_pass_inp);

        submit_email_button = (Button) findViewById(R.id.submit_email_button);
        submit_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipient = to_username.getText().toString();

                Toast.makeText(MainActivity.this,"Accessing account details...",Toast.LENGTH_SHORT).show();
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(recipient);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot res = task.getResult();
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        Toast.makeText(MainActivity.this,"Processing...",Toast.LENGTH_SHORT).show();
                        GMailSender sender = new GMailSender("osma.official.analysis@gmail.com", "Nylon1412.");
                        try {
                            sender.sendMail("Activation Key for OSMA",
                                    "Good day, "+recipient+"!\nThe activation key for your account is "+res.get("Activation Key").toString()+".\n" +
                                            "Note that you will be asked for your activation key on the\nfirst time that you will login.",
                                    "osma.official.analysis@gmail.com", String.valueOf(res.get("Email")));
                        } catch (Exception e) { e.printStackTrace(); }
                        Toast.makeText(MainActivity.this,"Account activation key SENT!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        submit_new_password_button = (Button) findViewById(R.id.submit_new_password_button);
        submit_new_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act_key = input_activation_key.getText().toString();
                new_pass = new_password_inp.getText().toString();
                confirm_new_pass = confirm_new_pass_inp.getText().toString();

                if(new_pass.equals(confirm_new_pass)){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("Password", new_pass);
                    db.collection("Users").document(recipient).set(user, SetOptions.merge());
                    Toast.makeText(MainActivity.this,"Password successfully changed!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"Password does not match!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        backToLogin();
    }

    private void backToLogin() {
        backToLoginButton = (Button) findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onLogin(); }
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
                    String activation_key = ActivationKey.mainKeyGenerate();
                    onSetData(userInp,passInp,emailInp,account,activation_key);

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        Toast.makeText(MainActivity.this,"Processing...",Toast.LENGTH_SHORT).show();
                        GMailSender sender = new GMailSender("osma.official.analysis@gmail.com", "Nylon1412.");
                        sender.sendMail("Activation Key for OSMA",
                                "Good day, "+userInp+"!\nThe activation key for your account is "+activation_key+".\n" +
                                        "Note that you will be asked for your activation key on the\nfirst time that you will login.",
                                "osma.official.analysis@gmail.com", emailInp);
                        Toast.makeText(MainActivity.this,"Account successfully registered!",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) { Log.e("SendMail", e.getMessage(), e); }
                } else {
                    Toast.makeText(MainActivity.this,"Different inputted passwords",Toast.LENGTH_SHORT).show();
                }
            }
        });
        redirectLogin = (Button) findViewById(R.id.redirectLogin);
        redirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onLogin(); }
        });
    }
}