package com.example.obook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.obook.services.yts.YtsResponse
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.Torrent
import com.github.se_bastiaan.torrentstream.TorrentOptions
import com.github.se_bastiaan.torrentstream.TorrentStream
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_stream.*
import org.json.JSONObject
import org.libtorrent4j.AlertListener
import org.libtorrent4j.Priority
import org.libtorrent4j.alerts.Alert
import java.io.File
import java.security.AccessController.getContext
import java.util.*
import kotlin.math.abs


class StreamActivity : AppCompatActivity(), TorrentListener, Player.Listener {
    private lateinit var simplePlayer: SimpleExoPlayer
    private lateinit var torrentStream: TorrentStream
    private lateinit var mergeMediaSource: MergingMediaSource
    private lateinit var mediaSource: ProgressiveMediaSource
    private lateinit var torrentStreamListener: TorrentListener
    private var imdb: String = ""
    private var id: String = ""
    private var magnetUri: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)
        supportActionBar?.hide()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)


        simplePlayer = SimpleExoPlayer.Builder(this).build()
        imdb = intent.getStringExtra("imdb") ?: ""
        id = intent.getStringExtra("id") ?: ""
        val torrentOptions = TorrentOptions.Builder()
            .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
            .removeFilesAfterStop(true)
            .build()

        torrentStream = TorrentStream.init(torrentOptions)
        getStreamService(imdb)
        torrentStream.addListener(this)
    }


    private fun getStreamService(id: String) {
        YtsResponse.getMovieDetail(id){
            if(it.status == "ok"){
                streamTitle.text = "Connecting to stream service..."
                val hash = intent.getStringExtra("hash")
                val slug = intent.getStringExtra("slug")
                if(hash != null && slug != null){
                    magnetUri =
                        "magnet:?xt=urn:btih:$hash&dn=$slug&tr=udp%3A%2F%2Fopen.stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.tiny-vps.com%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.torrent.eu.org%3A451%2Fannounce&tr=udp%3A%2F%2Fexplodie.org%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.cyberia.is%3A6969%2Fannounce&tr=udp%3A%2F%2Fipv4.tracker.harry.lu%3A80%2Fannounce&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.birkenwald.de%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.moeking.me%3A6969%2Fannounce&tr=udp%3A%2F%2Fopentor.org%3A2710%2Fannounce&tr=udp%3A%2F%2Ftracker.dler.org%3A6969%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2970%2Fannounce&tr=https%3A%2F%2Ftracker.foreverpirates.co%3A443%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=http%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Fopentracker.i2p.rocks%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fcoppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337%2Fannounce"
                    torrentStream.startStream(magnetUri)
                }
            }
        }

    }

    private fun initPlayer(path: String) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val factory: DefaultDataSourceFactory = DefaultDataSourceFactory(this, "ExoPlayer")
        exo_player_view.player = simplePlayer
        mediaSource =
            ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(path))
        mergeMediaSource = MergingMediaSource(mediaSource)
        simplePlayer.prepare(mergeMediaSource)
        simplePlayer.addListener(this)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onSeekProcessed()",
        "com.google.android.exoplayer2.Player.Listener"
    )
    )
    override fun onSeekProcessed() {
        super.onSeekProcessed()
        Log.d("onSeekProcessed", "onSeekProcessed")
        Log.d("torrent",torrentStream.currentTorrent?.piecesToPrepare.toString())
        val currentPosition = simplePlayer.currentPosition.toInt()
        val torrentHandle = torrentStream.currentTorrent.torrentHandle
        val pieceSize = torrentStream.currentTorrent.piecesToPrepare.toInt()
        val pieceIndex = ((currentPosition * pieceSize) / simplePlayer.duration).toInt()
        Log.d("pieceIndex",pieceIndex.toString())
        torrentHandle.piecePriority(pieceIndex, Priority.TOP_PRIORITY)
        print(torrentStream.currentTorrent.interestedPieceIndex)
//        for (i in pieceIndex until pieceSize) {
//            torrentHandle.piecePriority(i, Priority.TOP_PRIORITY)
//        }
        Log.d("pieceAvailable", Arrays.toString(torrentHandle.pieceAvailability()).toString())


//        val availablePieces = torrentHandle.pieceAvailability()
//        val currentPieceIndex = abs((currentPosition*availablePieces.size)/simplePlayer.duration.toInt())
//        Log.d("currentPieceIndex",currentPieceIndex.toString())
//        Log.d("currentPosition",availablePieces[currentPieceIndex].toString())
//        if(availablePieces[currentPieceIndex] > 0){
//            torrentHandle.piecePriority(currentPieceIndex, Priority.TOP_PRIORITY)
//            Log.d("pieceAvailable", Arrays.toString(torrentHandle.pieceAvailability()).toString())
//        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                streamProgress.visibility = View.VISIBLE
                simplePlayer.pause()
            }
            Player.STATE_READY -> {
                streamProgress.visibility = View.GONE
            }
            Player.STATE_ENDED -> {
                simplePlayer.release()
                torrentStream.stopStream()
            }
            Player.STATE_IDLE -> {
                streamProgress.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (torrentStream.currentTorrent != null)
            torrentStream.currentTorrent.resume()

    }

    override fun onPause() {
        super.onPause()
        if (torrentStream.currentTorrent != null)
            torrentStream.currentTorrent.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (torrentStream.isStreaming) {
            torrentStream.removeListener(this)
            torrentStream.stopStream()
            simplePlayer.stop()
            simplePlayer.release()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("isMovie", true)
        intent.putExtra("id", id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        if(torrentStream.isStreaming) {
            torrentStream.stopStream()
            simplePlayer.stop()
            simplePlayer.release()
            torrentStream.removeListener(this)
            startActivity(intent)
            this.finish()
        }
        else{
            startActivity(intent)
            this.finish()
        }
    }

    override fun onStreamPrepared(torrent: Torrent?) {
        if (torrent?.videoFile == null) {
            Log.d("streamPrepared", "null")
            return
        } else {
            Log.d("streamPrepared", torrent.videoFile.absolutePath.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStreamStarted(torrent: Torrent?) {
        Log.d("streamStarted", torrent?.videoFile.toString())
        streamTitle.text = "Collecting meta data..."
    }

    override fun onStreamError(torrent: Torrent?, e: Exception?) {
        println("Failed to load torrent: Trying again")
        torrentStream.startStream(magnetUri)
    }

    override fun onStreamReady(torrent: Torrent?) {
        streamProgress.visibility = View.GONE
        streamTitle.visibility = View.GONE
        Log.d("streamReady", torrent?.videoFile.toString())
        initPlayer(torrent!!.videoFile?.absolutePath!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
        streamSpeed.visibility = View.VISIBLE
        streamTitle.text = "Collecting meta data...  - ${String.format("%.2f", status!!.progress.toDouble())}%"
        if(status.downloadSpeed.toDouble() / 1000 < 1.0){
            streamSpeed.text = "Speed: ${String.format("%.2f", status.downloadSpeed.toDouble() / 1000)} KB/s"
        }
        else if(status.downloadSpeed.toDouble() / 1000 > 1000.0){
            streamSpeed.text = "Speed: ${String.format("%.2f", status.downloadSpeed.toDouble() / 1000000)} MB/s"
        }
        else{
            streamSpeed.text = "Speed: ${String.format("%.2f", status.downloadSpeed.toDouble() / 1000)} KB/s"
        }
    }

    override fun onStreamStopped() {
        Log.d("streamStopped", "stopped")
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).delete()
    }
}