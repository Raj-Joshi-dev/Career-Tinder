package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.adapters.JobOpeningsAdapter;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.JobOpeningsListModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;


public class JobOpeningsListActivity extends BaseActivity {


    private Button btn;
    Intent nextActivity;
    Context mContext;
    RetrofitClient mRetrofitClient;

    public String authCode;

    private RecyclerView recyclerView;
    private JobOpeningsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private JobOpeningsListModel jobOpeningsListModel;
    TextView TVNoItems;

    Intent companyDashboardIntent;
    Intent editJobOpeningIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_openings_list);

        init();
        btn = findViewById(R.id.addJobOpeningBtn);
        btn.setOnClickListener(onClickListener);
        nextActivity = new Intent(this, AddNewJobOpening.class);
        companyDashboardIntent = new Intent(this, CompanyDashboardActivity.class);
        editJobOpeningIntent = new Intent(this, EditJobOpeningActivity.class);


    }

    public void init(){
        authCode = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE, "");

        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());
        TVNoItems = findViewById(R.id.TVNoItems);

        if ( UtilityMethods.isConnected(mContext) ) {
            mRetrofitClient.mApiInterface.getJobOpenings(authCode).enqueue(mRetrofitClient.createProgress(mContext));
        }
        addDrawer("Your Vacancies",0);
    }

    public void buildRV(){
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new JobOpeningsAdapter(jobOpeningsListModel.getJobOpeningModelList());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new JobOpeningsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivityForResult(companyDashboardIntent.putExtra("job", jobOpeningsListModel.getJobOpeningModelList().get(position)),
                        Constants.NEED_RESULT_COMPANY_DASHBOARD);
            }

            @Override
            public void onEditClick(int position) {
                startActivityForResult(editJobOpeningIntent.putExtra("jobOpeningObject", jobOpeningsListModel.getJobOpeningModelList().get(position)),
                        Constants.NEED_RESULT_EDIT_COMPANY);
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getStatusCode().equals("Success")) {
                jobOpeningsListModel = (JobOpeningsListModel) baseBean;
                if ( jobOpeningsListModel != null && jobOpeningsListModel.getJobOpeningModelList() != null &&
                        !jobOpeningsListModel.getJobOpeningModelList().isEmpty()){
                    TVNoItems.setVisibility(View.INVISIBLE);
                    buildRV();
                }
            }
            else {
                Toast.makeText(mContext, Constants.MSG_ERROR,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if ( mRetrofitClient != null ) {
            mRetrofitClient.cancelProgress();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(nextActivity,Constants.NEED_RESULT_JOB_OPENING_CREATION);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.NEED_RESULT_EDIT_COMPANY:
                case Constants.NEED_RESULT_JOB_OPENING_CREATION:
                    if (UtilityMethods.isConnected(mContext)) {
                        mRetrofitClient.mApiInterface.getJobOpenings(authCode).enqueue(mRetrofitClient.createProgress(mContext));
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case Constants.NEED_RESULT_EDIT_COMPANY:
                case Constants.NEED_RESULT_COMPANY_DASHBOARD:
                    finish();
            }
        }
    }
}
