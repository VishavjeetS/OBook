package com.example.obook.services.yts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.X509Certificate

class YtsApiService: BroadcastReceiver() {

    companion object {
        private const val BASE_URL = "https://yts.mx/api/v2/"
        private var retrofit: Retrofit? = null
        private var client:  OkHttpClient? = null
        private const val DEFAULT_WAIT_TIME = 2000L

        fun getInstance(): Retrofit {
//            return NetworkHandler().getInstance()
//            if (retrofit == null || client == null) {
//                try {
//                    val tlsSocketFactory = TLSSocketFactory()
//                    client = OkHttpClient.Builder()
//                        .addInterceptor { chain ->
//                            var response = chain.proceed(chain.request())
//                            var tryCount = 0
//                            while (response.code() == 525 || response.code() == 503 && tryCount < 20) {
//                                Log.d("YtsApiService", "Retrying: ${tryCount + 1}")
//                                // Wait for a certain amount of time before retrying the request
//                                val waitTime = calculateWaitTime(response)
//                                Thread.sleep(waitTime)
//
//                                // Retry the request
//                                tryCount++
//                                response.close()
//                                response = chain.proceed(chain.request())
//                            }
//                            response
//                        }
////                    .dns { listOf(InetAddress.getByName("176.103.130.130")) }
//                        .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager)
//                        .build()
//                } catch (e: KeyManagementException) {
//                    e.printStackTrace()
//                } catch (e: NoSuchAlgorithmException) {
//                    e.printStackTrace()
//                } catch (e: KeyStoreException) {
//                    e.printStackTrace()
//                }
//                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
//                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
//                    .client(client!!)
//                    .addConverterFactory(GsonConverterFactory.create()).build()
//            }
            return retrofit!!
        }

        private fun calculateWaitTime(response: Response): Long {
            val retryAfterHeader = response.header("Retry-After")
            return if(response.code() == 525) {
                if (retryAfterHeader != null) {
                    retryAfterHeader.toLong() * 1000L
                } else {
                    DEFAULT_WAIT_TIME
                }
            } else{
                0
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val tlsSocketFactory = TLSSocketFactory()

        val trustAllCerts = (object : X509TrustManager {
            override fun checkClientTrusted(
                p0: Array<out java.security.cert.X509Certificate>?,
                p1: String?,
            ) {

            }

            override fun checkServerTrusted(
                p0: Array<out java.security.cert.X509Certificate>?,
                p1: String?,
            ) {

            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("TLSv1.2")
        val trustAllCertss = arrayOf<TrustManager>(trustAllCerts)
        sslContext.init(null, trustAllCertss, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
            Log.d("network", "wifi or cellular")
            // Create a new OkHttpClient and Retrofit instance with a new ConnectionPool
            client = OkHttpClient.Builder()
                .hostnameVerifier{ _, _ -> true }
                .connectionPool(ConnectionPool())
//                .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager)
//                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .addInterceptor { chain ->
                    var response = chain.proceed(chain.request())
                    var tryCount = 0
                    while (response.code() == 525 && tryCount < 20) {
                        Log.d("YtsApiService", "Retrying: ${tryCount + 1}")
                        // Wait for a certain amount of time before retrying the request
                        val waitTime = calculateWaitTime(response)
                        Thread.sleep(waitTime)

                        // Retry the request
                        tryCount++
                        response.close()
                        response = chain.proceed(chain.request())
                    }
                    response
                }
                .build()
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client!!)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        else {
            Log.d("network", "no wifi or cellular")
            client = OkHttpClient.Builder()
                .hostnameVerifier{ _, _ -> true }
                .sslSocketFactory(sslSocketFactory, trustAllCerts)
                .addInterceptor { chain ->
                    var response = chain.proceed(chain.request())
                    var tryCount = 0
                    while (response.code() == 525 && tryCount < 20) {
                        Log.d("YtsApiService", "Retrying: ${tryCount + 1}")
                        // Wait for a certain amount of time before retrying the request
                        val waitTime = calculateWaitTime(response)
                        Thread.sleep(waitTime)

                        // Retry the request
                        tryCount++
                        response.close()
                        response = chain.proceed(chain.request())
                    }
                    response
                }
                .build()
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client!!)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}