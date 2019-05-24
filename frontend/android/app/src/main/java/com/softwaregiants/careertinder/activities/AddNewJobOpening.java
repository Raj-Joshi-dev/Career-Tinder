package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.AddJobOpeningModel;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;

public class AddNewJobOpening extends ImagePickerActivity {

    //region class variables
    private Button btn;
    RetrofitClient mRetrofitClient;

    EditText ETCompanyName;
    EditText ETJobTitle;
    EditText ETJobDescription;
    EditText ETDesiredQualification;
    EditText ETDesiredWorkExperience;
    EditText ETPlaceOfWork;
    EditText ETSkill1;
    EditText ETSkill2;
    EditText ETSkill3;
    EditText ETLanguage1;
    EditText ETLanguage2;

    String CompanyName = "";
    String JobTitle = "";
    String JobDescription = "";
    String DesiredQualification = "";
    String DesiredWorkExperience = "";
    String PlaceOfWOrk = "";
    String Skill1 = "";
    String Skill2 = "";
    String Skill3 = "";
    String Language1 = "";
    String Language2 = "";

    String authCode = "";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_add_new_job_opening);

        init();
    }

    public void init() {
        authCode = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE, "");

        btn = findViewById(R.id.createJobOpeningBtn);
        btn.setOnClickListener(ocl);
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());

        ETCompanyName = (EditText)findViewById(R.id.ETCompanyName);
        ETJobTitle = (EditText)findViewById(R.id.ETJobTitle);
        ETJobDescription = (EditText)findViewById(R.id.ETJobDescription);
        ETDesiredQualification = (EditText)findViewById(R.id.ETDesiredQualification);
        ETDesiredWorkExperience = (EditText)findViewById(R.id.ETDesiredWorkExperience);
        ETPlaceOfWork = (EditText)findViewById(R.id.ETPlaceOfWork);
        ETSkill1 = (EditText)findViewById(R.id.ETSkill1);
        ETSkill2 = (EditText)findViewById(R.id.ETSkill2);
        ETSkill3 = (EditText)findViewById(R.id.ETSkill3);
        ETLanguage1 = (EditText)findViewById(R.id.ETLanguage1);
        ETLanguage2 = (EditText)findViewById(R.id.ETLanguage2);

        imageUser = findViewById(R.id.picture);
        updateImageButton = findViewById(R.id.updatePicture);
        updateImageButton.setOnClickListener(updateImageListener);
        requestMultiplePermissions();
    }

    //region onclick listener
    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CompanyName = ETCompanyName.getText().toString();
            JobTitle = ETJobTitle.getText().toString();
            JobDescription = ETJobDescription.getText().toString();
            DesiredQualification = ETDesiredQualification.getText().toString();
            DesiredWorkExperience = ETDesiredWorkExperience.getText().toString();
            PlaceOfWOrk = ETPlaceOfWork.getText().toString();
            Skill1 = ETSkill1.getText().toString();
            Skill2 = ETSkill2.getText().toString();
            Skill3 = ETSkill3.getText().toString();
            Language1 = ETLanguage1.getText().toString();
            Language2 = ETLanguage2.getText().toString();
            if (CompanyName.equals("")){
                Toast.makeText(mContext,"Please enter your Company Name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (JobTitle.equals("")){
                Toast.makeText(mContext,"Please enter Job Title", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (JobDescription.equals("")){
                Toast.makeText(mContext,"Please enter job description", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (DesiredQualification.equals("")){
                Toast.makeText(mContext,"Please enter desired qualification", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (DesiredWorkExperience.equals("") || !isNumeric(DesiredWorkExperience)){
                if (DesiredWorkExperience.equals("")) {
                    Toast.makeText(mContext, "Please enter desired Work Experience", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isNumeric(DesiredWorkExperience)){
                    Toast.makeText(mContext, "Please enter desired Work Experience (in Months)", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if (PlaceOfWOrk.equals("")){
                Toast.makeText(mContext,"Please enter a Place of Work", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (Skill1.equals("")){
                Toast.makeText(mContext,"Please enter a skill", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (Language1.equals("")){
                Toast.makeText(mContext,"Please enter a language", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                AddJobOpeningModel addJobOpeningModel = new AddJobOpeningModel();
                addJobOpeningModel.setCompanyName(CompanyName);
                addJobOpeningModel.setJobTitle(JobTitle);
                addJobOpeningModel.setJobDescription(JobDescription);
                addJobOpeningModel.setDesiredQualification(DesiredQualification);
                addJobOpeningModel.setDesiredWorkExperience(DesiredWorkExperience);
                addJobOpeningModel.setPlaceOfWork(PlaceOfWOrk);
                addJobOpeningModel.setSkill1(Skill1);
                addJobOpeningModel.setSkill2(Skill2);
                addJobOpeningModel.setSkill3(Skill3);
                addJobOpeningModel.setPreferredLanguage1(Language1);
                addJobOpeningModel.setPreferredLanguage2(Language2);
                mRetrofitClient.mApiInterface.addNewJobOpening(addJobOpeningModel, authCode).enqueue(mRetrofitClient);
            }
        }
    };
    //endregion

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getStatusCode().equals(Constants.SC_JOB_CREATED_SUCCESS)){
                Toast.makeText(mContext,"Job Opening Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext,CompanyDashboardActivity.class));
                finish();
            }
            else {
                Toast.makeText(mContext, baseBean.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(mContext,Constants.MSG_NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    };

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
