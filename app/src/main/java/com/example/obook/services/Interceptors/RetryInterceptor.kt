package com.example.obook.services.Interceptors

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        var tryCount = 0

        while (!response.isSuccessful && tryCount < maxRetries) {
            response.close()
            tryCount++
            response = chain.proceed(chain.request())
        }
        return response
    }
}
