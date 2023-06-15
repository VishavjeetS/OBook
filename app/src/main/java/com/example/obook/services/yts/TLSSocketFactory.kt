package com.example.obook.services.yts

import androidx.annotation.Nullable
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.*

class TLSSocketFactory() : SSLSocketFactory() {
    private val delegate: SSLSocketFactory
    private lateinit var trustManagers: Array<TrustManager>

    init {
        generateTrustManagers()
        val context: SSLContext = SSLContext.getInstance("TLS")
        context.init(null, trustManagers, null)
        delegate = context.getSocketFactory()
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class)
    private fun generateTrustManagers() {
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
        if (trustManagers.size != 1 || trustManagers.get(0) !is X509TrustManager) {
            throw IllegalStateException(
                "Unexpected default trust managers:"
                        + Arrays.toString(trustManagers)
            )
        }
        this.trustManagers = trustManagers
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket? {
        return enableTLSOnSocket(delegate.createSocket())
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket? {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String?, port: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress?, port: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int,
    ): Socket? {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    private fun enableTLSOnSocket(socket: Socket?): Socket? {
        if (socket != null && (socket is SSLSocket)) {
            (socket as SSLSocket).enabledProtocols = arrayOf<String>("TLSv1.1", "TLSv1.2")
        }
        return socket
    }

    val trustManager: X509TrustManager
        get() = trustManagers[0] as X509TrustManager
}