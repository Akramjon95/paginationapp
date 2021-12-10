package maxcoders.uz.aliftechapp.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import maxcoders.uz.aliftechapp.Constants
import maxcoders.uz.aliftechapp.data.DataRepository
import maxcoders.uz.aliftechapp.db.DataBase
import maxcoders.uz.aliftechapp.db.SampleDao
import maxcoders.uz.aliftechapp.network.RetrofitApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):RetrofitApi =
        retrofit.create(RetrofitApi::class.java)


    fun httpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        val client: OkHttpClient = builder.build()
        return client
    }

//    fun dataRepository(retrofitApi: RetrofitApi, sampleDaop: SampleDao): DataRepository{
//        return DataRepository(retrofitApi, sampleDaop)
//    }

    @Singleton
    @Provides
    fun getAppDatabase(context : Application): DataBase{
        return DataBase.invoke(context)
    }

    @Singleton
    @Provides
    fun sampleDao(dataBase: DataBase):SampleDao{
        return dataBase.getSampleDao()
    }
}