package com.example.obook.services.yts

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class NetworkHandler {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://yts.mx/api/v2/"
    fun getInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit!!
    }

    private val DEFAULT_WAIT_TIME = 2000L
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> =
                trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager


            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder.addInterceptor { chain ->
                var response = chain.proceed(chain.request())
                var tryCount = 0
                while (response.code() == 525 || response.code() == 503 && tryCount < 20) {
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
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun calculateWaitTime(response: Response): Long {
        val retryAfterHeader = response.header("Retry-After")
        return if (response.code() == 525) {
            if (retryAfterHeader != null) {
                retryAfterHeader.toLong() * 1000L // Convert seconds to milliseconds
            } else {
                // If no Retry-After header is present, wait for a default amount of time
                DEFAULT_WAIT_TIME
            }
        } else {
            0
        }
    }


}