//package com.example.obook
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Environment
//import android.util.Log
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.obook.adapter.yts.DownloadAdapter
//import com.example.obook.model.ytsModel.DownloadItem
//import com.github.se_bastiaan.torrentstream.StreamStatus
//import com.github.se_bastiaan.torrentstream.Torrent
//import com.github.se_bastiaan.torrentstream.TorrentOptions
//import com.github.se_bastiaan.torrentstream.TorrentStream
//import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
//import kotlinx.android.synthetic.main.activity_stream.*
//import java.lang.Exception
//
//class DownloadActivity : AppCompatActivity(), TorrentListener {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var downloadInfo: ArrayList<DownloadItem>
//    private var torrentName: String? = ""
//    private var speed: String? = ""
//    private var torrentSize: String? = ""
//    private var downloadStatus: String? = ""
//    private var seeds: String? = ""
//    private var peers: String? = ""
//    private var progress: Int = 0
//    private var hash: String? = ""
//    private var slug: String? = ""
//    private var sizeIntent: String? = ""
//    private var magnetUrl: String? = ""
//    private var obj: DownloadItem? = null
//    private lateinit var torrentStream: TorrentStream
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_download)
//        downloadInfo = ArrayList()
//        getIntents()
//        recyclerView = findViewById(R.id.download_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//        getDownloadInfo()
//    }
//
//    private fun getIntents(){
//        hash = intent.getStringExtra("hash")
//        slug = intent.getStringExtra("slug")
//        sizeIntent = if(intent.getStringExtra("size") == null) "0" else intent.getStringExtra("peers")
//    }
//
//    private fun getDownloadInfo(){
//        val torrentOptions = TorrentOptions.Builder()
//            .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
//            .removeFilesAfterStop(false)
//            .build()
//
//        magnetUrl = "magnet:?xt=urn:btih:$hash&dn=$slug&tr=udp://tracker.coppersurfer.tk:6969/announce&tr=udp://tracker.leechers-paradise.org:6969/announce&tr=udp://tracker.opentrackr.org:1337/announce&tr=udp://9.rarbg.me:2710/announce&tr=udp://9.rarbg.to:2710/announce&tr=udp://tracker.internetwarriors.net:1337/announce&tr=udp://tracker.cyberia.is:6969/announce"
//
//        torrentStream = TorrentStream.init(torrentOptions)
//        torrentStream.startStream(magnetUrl)
//        torrentStream.addListener(this)
//        downloadInfo.add(obj!!)
//        val downloadAdapter = DownloadAdapter(downloadInfo)
//        recyclerView.adapter = downloadAdapter
//    }
//
//    override fun onStreamPrepared(torrent: Torrent?) {
//        Log.d("TAG", "onStreamPrepared: ")
//    }
//
//    override fun onStreamStarted(torrent: Torrent?) {
//        Log.d("TAG", "onStreamStarted: ")
//    }
//
//    override fun onStreamError(torrent: Torrent?, e: Exception?) {
//        Log.d("TAG", "onStreamError: ")
//        torrentStream.startStream(magnetUrl)
//    }
//
//    override fun onStreamReady(torrent: Torrent?) {
//        Toast.makeText(this@DownloadActivity, "Download Started", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
//        torrentName = torrent!!.torrentHandle.name
//        speed = if(status!!.downloadSpeed.toDouble() / 1000 < 1.0){
//            "${String.format("%.2f", status.downloadSpeed.toDouble() / 1000)} KB/s"
//        } else if(status.downloadSpeed.toDouble() / 1000 > 1000.0){
//            "${String.format("%.2f", status.downloadSpeed.toDouble() / 1000000)} MB/s"
//        } else{
//            "${String.format("%.2f", status.downloadSpeed.toDouble() / 1000)} KB/s"
//        }
//        torrentSize = if(torrentStream.sessionManager.totalDownload().toString() > "1000000000"){
//            "${String.format("%.2f", torrentStream.sessionManager.totalDownload().toDouble() / 1000000000)} GB"
//        } else if(torrentStream.sessionManager.totalDownload().toString() > "1000000"){
//            "${String.format("%.2f", torrentStream.sessionManager.totalDownload().toDouble() / 1000000)} MB"
//        } else{
//            "${String.format("%.2f", torrentStream.sessionManager.totalDownload().toDouble() / 1000)} KB"
//        }
//        downloadStatus = String.format("%.2f", status.progress * 100) + "%"
//        seeds = status.seeds.toString()
//        progress = (status.progress * 100).toInt()
//        obj = DownloadItem(torrentName!!, speed!!, torrentSize!!, downloadStatus!!, seeds!!, progress)
//        Log.d("TAG", "onStreamProgress: $torrentName $speed $torrentSize $downloadStatus $seeds $progress")
//    }
//
//    override fun onStreamStopped() {
//        Log.d("TAG", "onStreamStopped: ")
//    }
//}