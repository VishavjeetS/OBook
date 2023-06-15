package com.example.obook.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.obook.R
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
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.activity_stream.*
import java.io.File
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


class AnimeFragment : Fragment() {
    private lateinit var simplePlayer: SimpleExoPlayer
    private lateinit var torrentStream: TorrentStream
    private lateinit var mergeMediaSource: MergingMediaSource
    private lateinit var mediaSource: ProgressiveMediaSource
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime, container, false)
        simplePlayer = SimpleExoPlayer.Builder(requireContext()).build()
        val torrentOptions = TorrentOptions.Builder()
            .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
            .removeFilesAfterStop(true)
            .build()

        torrentStream = TorrentStream.init(torrentOptions)
        getStreamService("tt0111161")
        //watchMovie()
        return view
    }

    private fun getStreamService(id: String){
        YtsResponse.getMovieDetail(id) { movieDetails ->
//                val json = JSONObject(Gson().toJson(movieDetails))
//                val movieJson = json.getJSONObject("data").getJSONObject("movie")
//                if(movieJson.has("torrents")){
//
//                }
            val hash = movieDetails.data.movie.torrents[0].hash ?: ""
            val slug = movieDetails.data.movie.slug ?: ""
            val magnetUri =
                "magnet:?xt=urn:btih:$hash&dn=$slug&tr=udp%3A%2F%2Fopen.stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.tiny-vps.com%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.torrent.eu.org%3A451%2Fannounce&tr=udp%3A%2F%2Fexplodie.org%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.cyberia.is%3A6969%2Fannounce&tr=udp%3A%2F%2Fipv4.tracker.harry.lu%3A80%2Fannounce&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.birkenwald.de%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.moeking.me%3A6969%2Fannounce&tr=udp%3A%2F%2Fopentor.org%3A2710%2Fannounce&tr=udp%3A%2F%2Ftracker.dler.org%3A6969%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2970%2Fannounce&tr=https%3A%2F%2Ftracker.foreverpirates.co%3A443%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=http%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Fopentracker.i2p.rocks%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fcoppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337%2Fannounce"

            torrentStream.addListener(object : TorrentListener {
                override fun onStreamPrepared(torrent: Torrent?) {
                    if (torrent?.videoFile == null) {
                        Log.d("streamPrepared", "null")
                        return
                    } else {
                        Log.d("streamPrepared", torrent.videoFile.absolutePath.toString())
                    }
                }

                override fun onStreamStarted(torrent: Torrent?) {
                    Log.d("streamStarted", torrent?.videoFile.toString())

                }

                override fun onStreamError(torrent: Torrent?, e: Exception?) {
                    println("Failed to load torrent: Trying again")
                    torrentStream.startStream(magnetUri)
//                    torrentStream.addListener(this)
                }

                override fun onStreamReady(torrent: Torrent?) {
                    streamProgress.visibility = View.GONE
                    Log.d("streamReady", torrent?.videoFile.toString())
                    initPlayer(torrent!!.videoFile?.absolutePath!!)
                }

                override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
                    Log.d("streamProgress", status!!.progress.toString())
                }

                override fun onStreamStopped() {
                    Log.d("streamStopped", "stopped")
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).delete()
                }
            })
            torrentStream.startStream(magnetUri)
            println("waiting for stream to start")
            while(!torrentStream.isStreaming){
                streamProgress.visibility = View.VISIBLE
            }
        }
    }

    private fun initPlayer(path: String) {
        //toggleFullscreen(true)
        val factory: DefaultDataSourceFactory = DefaultDataSourceFactory(requireContext(), "ExoPlayer")
        mediaSource =
            ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(path))
        mergeMediaSource = MergingMediaSource(mediaSource)
        simplePlayer.prepare(mergeMediaSource)
        exo_player_view.player = simplePlayer
        simplePlayer.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if(state == Player.STATE_READY){
                    simplePlayer.removeListener(this)
                    simplePlayer.seekTo(simplePlayer.currentPosition)
                    simplePlayer.playWhenReady = true
                }
            }
        })
    }


}