package mte.crasmonitoring.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Console;
import java.io.IOException;

import mte.crasmonitoring.utils.Constants;
import mte.crasmonitoring.utils.SharedPrefsUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
public final class ServiceGenerator {



    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, Context context) {

        final String userId = SharedPrefsUtils.getUserId(context);

        httpClient.addInterceptor(new Interceptor() {
                      @Override
                      public Response intercept(Interceptor.Chain chain) throws IOException {
                          Request original = chain.request();

                          if(userId != null)
                          {
                              Request request = original.newBuilder()
                                      .header("user_id", userId)
                                      .build();

                              return chain.proceed(request);
                          }
                          else
                          {
                              Request request = original.newBuilder()
                                      .build();

                              return chain.proceed(request);
                          }

                      }
                });


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(logging);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


}
