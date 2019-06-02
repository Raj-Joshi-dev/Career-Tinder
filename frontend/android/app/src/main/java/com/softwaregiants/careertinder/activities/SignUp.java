package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.SignUpModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import org.apache.commons.validator.routines.EmailValidator;

public class SignUp extends AppCompatActivity {

    private Button createMyAccount;
    Context mContext;
    RetrofitClient mRetrofitClient;

    EditText fullName;
    EditText emailAddress;
    EditText password;
    EditText confirmPassword;

    RadioButton jobseeker;
    RadioButton employer;

    String userTypeString = "not set";
    String name = "";
    String email = "";
    String pass = "";
    String confirmPass = "";
    SignUpModel signUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        createMyAccount = findViewById(R.id.createMyAccountBtn);
        createMyAccount.setOnClickListener(ocl);
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());

        fullName = (EditText)findViewById(R.id.fullName);
        emailAddress = (EditText)findViewById(R.id.emailAddress);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);

        jobseeker = (RadioButton)findViewById(R.id.jobSeekerRadioButton);
        jobseeker.setOnClickListener(js_radio_listener);
        employer = (RadioButton)findViewById(R.id.employerRadioButton);
        employer.setOnClickListener(e_radio_listener);
    }

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = fullName.getText().toString();
            email = emailAddress.getText().toString();
            pass = password.getText().toString();
            confirmPass = confirmPassword.getText().toString();
            if(name.equals("")){
                Toast.makeText(mContext,"Please enter your Full Name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (email.equals("") || (!validateEmail(email))) {
                if(email.equals("")){
                    Toast.makeText(mContext, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateEmail(email)) {
                    Toast.makeText(mContext, "Your Email address is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if (!checkPasswordLength(pass) || pass.equals("")){
                    if(pass.equals("")){
                        Toast.makeText(mContext,"Please enter a password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkPasswordLength(pass)){
                        Toast.makeText(mContext,"Password should be at least 8 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
            }
            else if (!matchPasswords(pass, confirmPass) || confirmPass.equals("")){
                if (confirmPass.equals("")){
                    Toast.makeText(mContext,"Please re-enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matchPasswords(pass, confirmPass)){
                    Toast.makeText(mContext,"Password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(userTypeString.equals("not set")){
                    Toast.makeText(mContext,"SELECT ONE : Job Seeker or Employer?", Toast.LENGTH_SHORT).show();
                    return;
            }
            else{
                signUpModel = new SignUpModel();
                signUpModel.setName(name);
                signUpModel.setEmail(email);
                signUpModel.setPassword(UtilityMethods.sha1Hash(pass));
                signUpModel.setUserType(userTypeString);
                if ( UtilityMethods.isConnected(mContext) ) {
                    mRetrofitClient.mApiInterface.signUp(signUpModel).enqueue(mRetrofitClient);
                }
            }
        }

    };

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getStatusCode().equals("email_exists")){
                Toast.makeText(mContext,"Account already exists with this email",Toast.LENGTH_SHORT).show();
            }
            else if (baseBean.getStatusCode().equals("account_created")) {
                Toast.makeText(mContext,"Account Created",Toast.LENGTH_SHORT).show();
//                if (signUpModel.getUserType().equals(Constants.USER_TYPE_JOB_SEEKER)) {
//
//                }
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    View.OnClickListener js_radio_listener = new View.OnClickListener(){
        public void onClick(View view) {
            userTypeString = Constants.USER_TYPE_JOB_SEEKER;
        }
    };
    View.OnClickListener e_radio_listener = new View.OnClickListener(){
        public void onClick(View view) {
            userTypeString = Constants.USER_TYPE_EMPLOYER;
        }
    };

    public boolean matchPasswords(String p, String c){
        if(p.equals(c)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkPasswordLength(String pas){
        if(pas.length() < 8){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

}
