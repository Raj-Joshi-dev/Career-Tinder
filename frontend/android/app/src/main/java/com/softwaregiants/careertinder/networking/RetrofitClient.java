package com.softwaregiants.careertinder.networking;

import android.util.Log;

import com.google.gson.Gson;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.LoginSuccessModel;
import com.softwaregiants.careertinder.utilities.Constants;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient implements Callback<ResponseBody> {

    private static Retrofit retrofit;
    private ApiResponseCallback mApiResponseCallBack;
    public ApiInterface mApiInterface;
    private String TAG = RetrofitClient.class.getSimpleName();

    public static RetrofitClient getRetrofitClient(ApiResponseCallback mApiResponseCallBack) {
        if ( retrofit == null ) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            // set your desired log level
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RetrofitClient mRetrofitClient = new RetrofitClient();
        mRetrofitClient.mApiInterface = retrofit.create(ApiInterface.class);
        mRetrofitClient.mApiResponseCallBack = mApiResponseCallBack;
        return mRetrofitClient;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            Log.d(TAG, response.toString());
            try {
                String rawResponse = response.body().string();
                Log.i(TAG, "onResponse: JSON\n\n" + rawResponse + "\n\n"  );
                BaseBean baseBean = new Gson().fromJson( rawResponse, BaseBean.class);
                switch (baseBean.getApiMethod()) {
                    case Constants.API_METHOD_LOGIN: {
                        LoginSuccessModel loginSuccessModel = new Gson().fromJson(rawResponse, LoginSuccessModel.class);
                        mApiResponseCallBack.onSuccess(loginSuccessModel);
                        break;
                    }
                    case Constants.API_METHOD_SIGNUP: {
                        mApiResponseCallBack.onSuccess(baseBean);
                        break;
                    }
                    case Constants.API_METHOD_ADD_NEW_JOB_OPENING: {
                        mApiResponseCallBack.onSuccess(baseBean);
                        break;
                    }
                    case Constants.API_METHOD_POST_SIGNUP:
                        //TODO remove default
                    default:{
                        mApiResponseCallBack.onSuccess(baseBean);
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e(TAG, t.toString());
        mApiResponseCallBack.onFailure(t);
    }

}
