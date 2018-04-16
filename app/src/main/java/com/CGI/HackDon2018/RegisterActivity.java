package com.CGI.HackDon2018;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.CGI.HackDon2018.R;

import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity  implements IQuestionListener{

    // Constants
    static final String CHAT_PREFS = "ChatPrefs";
    static final String DISPLAY_NAME_KEY = "username";

    static final QuestionUtils Question_utils  = new QuestionUtils();

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private RadioGroup radioGroupYesNo;
    private int CurrentPosition = 0;

    // Firebase instance variables
    private FirebaseAuth mAuth;

    private ListView listView;
    QuestionAdapter mQuestionAdapter;

    QuestionModel [] mQuestionListModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);



        // Get ListView object from xml
        listView = findViewById(R.id.question_list);

        // Defined Array values to show in ListView
   //     String[] values = new String[] { "Question 1",
   //             "Question 2"
   //     };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data






        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                                CurrentPosition = position;

                                            }
                                        });

        // TODO: Get hold of an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart(){
        super.onStart();
        Question_utils.Questionlistener = this;
        Question_utils.getQuestionList();
    }


    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
      //mic      createFirebaseUser();
            SendQuestionChoice();

        }
    }

    private void SendQuestionChoice() {

        ProfileModel pm = new ProfileModel();

        pm.email = mEmailView.getText().toString();
        pm.profileId = 0;
        pm.gender = "0";
        pm.affinities = new ArrayList<Integer>();

        radioGroupYesNo = (RadioGroup) findViewById(R.id.radioGroupYesNo);

        final int child=listView.getChildCount();
        for(int i=0;i<child;i++) {
            View rgg = listView.getChildAt(i);

            RadioGroup radioGroup = (RadioGroup) rgg.findViewById(R.id.radioGroupYesNo);

            int selectedId = radioGroup.getCheckedRadioButtonId();
            QuestionModel modelItem = mQuestionListModel[i];
            modelItem.selected = selectedId == R.id.radio_Yes;

//            RadioButton radioButton = (RadioButton) rgg.findViewById(selectedId);
        }


        // Check which radio button was clicked




        for (int i = 0; i < mQuestionListModel.length - 1; i++)
        {
            QuestionModel modelItem = mQuestionListModel[i];
            if (modelItem.selected)
            {
                pm.affinities.add(i);
            }
        }

        Question_utils.submitQuestion(this, pm);

    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length() > 4;
    }

    // TODO: Create a Firebase user
    private void createFirebaseUser() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();



        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FlashChat", "createUser onComplete: " + task.isSuccessful());

                if(!task.isSuccessful()){
                    Log.d("FlashChat", "user creation failed");
                    showErrorDialog("Registration attempt failed");
                } else {
                    saveDisplayName();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }


    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName() {
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }


    // TODO: Create an alert dialog to show in case registration failed
    private void showErrorDialog(String message){

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }




    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        //boolean checked = ((RadioButton) view).isChecked();
        radioGroupYesNo = (RadioGroup) findViewById(R.id.radioGroupYesNo);
      //  int idx = radioGroupYesNo.indexOfChild(view);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_Yes:
                //if (checked)
                    mQuestionListModel[CurrentPosition].selected = true;
                    // Pirates are the best
                    break;
            case R.id.radio_No:
//                if (checked)
                    // Ninjas rule
                    mQuestionListModel[CurrentPosition].selected = false;

                    break;
        }
    }


    @Override
    public void onReceiveList(QuestionModel[] model) {

        mQuestionListModel = model;
        ArrayList<String> list  = new ArrayList<>();

        for (int i = 0; i < model.length - 1; i++)
        {
               QuestionModel modelItem = model[i];
               list.add(modelItem.profileAffinityName);

        }

        mQuestionAdapter = new QuestionAdapter(this, list);

        // Assign adapter to ListView
        listView.setAdapter(mQuestionAdapter);


    }
}
