package mte.crasmonitoring.rest;

import android.content.Context;
import android.text.TextUtils;

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

                          if(!TextUtils.isEmpty(userId))
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
