package com.example.obook.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.obook.adapter.anime.AnimeGenreAdapter
import com.example.obook.adapter.anime.CharacterAdapter
import com.example.obook.adapter.anime.RecommendAdapter
import com.example.obook.DetailActivity
import com.example.obook.model.animeModel.anime.animeCharacters.CharacterResponse
import com.example.obook.model.animeModel.anime.animeFullDetail.FullDetail
import com.example.obook.model.animeModel.anime.animeFullDetail.Genre
import com.example.obook.model.animeModel.anime.animeFullDetail.Trailer
import com.example.obook.model.animeModel.anime.recommendation.AnimeRecommendation
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.obook.R
import com.example.obook.model.animeModel.anime.animeFullDetail.Data
import com.example.obook.room.animeModel.AnimeDatabase
import com.example.obook.room.movieModel.MovieDatabase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_anime_detail.*

class AnimeDetail : Fragment() {
    private lateinit var animeId: String
    private var fav: Boolean = false
    private lateinit var animeTitle: TextView
    private lateinit var animeDescription: TextView
    private lateinit var animeRating: TextView
    private lateinit var animeStatus: TextView
    private lateinit var animeEpisodes: RecyclerView
    private lateinit var animeRecommended: RecyclerView
    private lateinit var animeCharacters: RecyclerView
    private lateinit var animeAired: TextView
    private lateinit var animeDuration: TextView
    private lateinit var animeGenres: RecyclerView
    private lateinit var animeImage: ImageView
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var genreList: List<Genre>
    private lateinit var genreAdapter: AnimeGenreAdapter
    private lateinit var episodes: TextView
    private lateinit var mContext: Context
    private lateinit var addFavourite: Button
    private lateinit var animeDatabase: AnimeDatabase
    val page = 1

    private val BTN_TEXT_REMOVE = "Remove from Favourites"
    private val BTN_TEXT_ADD = "Add to Favourites"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime_detail, container, false)
        animeTitle = view.findViewById(R.id.anime_name)
        animeDescription = view.findViewById(R.id.anime_synopsis)
        animeStatus = view.findViewById(R.id.anime_status)
        animeEpisodes = view.findViewById(R.id.anime_episodes_rv)
        animeCharacters = view.findViewById(R.id.anime_characters_rv)
        animeRecommended = view.findViewById(R.id.anime_recommended_rv)
        youTubePlayerView = view.findViewById(R.id.youtube_player_view)
        animeId = arguments?.getString("id").toString()
        fav = arguments?.getBoolean("fav")!!
        animeImage = view.findViewById(R.id.anime_image)
        animeGenres = view.findViewById(R.id.anime_genres_rv)
        genreList = ArrayList()
        episodes = view.findViewById(R.id.episodes)
        addFavourite = view.findViewById(R.id.addFavorite)

        animeDatabase = Room.databaseBuilder(requireContext(), AnimeDatabase::class.java, "animeDB")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()

        Log.d("id", animeId)

        getAnimeDetails(animeId.toInt())
        getGenreList(animeId.toInt())
        getRecommended(animeId.toInt())
        getCharacters(animeId.toInt())

        doDatabaseStuff(addFavourite, fav)

        lifecycle.addObserver(youTubePlayerView)


        return view
    }

    private fun doDatabaseStuff(favBtn: Button, fav: Boolean) {
        val animeDao = animeDatabase.animeDAO()
        var anime: Data? = null
        animeDao.getAnime().observe(this as LifecycleOwner) {
            for (element in it) {
                if (element.malId == animeId.toInt()) {
                    favBtn.text = BTN_TEXT_REMOVE
                }
            }
        }
        FullDetail.getAnimeData(animeId.toInt()) {
            anime = it.data
        }

        if (fav) {
            favBtn.text = BTN_TEXT_REMOVE
            favBtn.setOnClickListener {
                animeDao.deleteAnime(anime!!)
                favBtn.text = BTN_TEXT_ADD
                favBtn.setOnClickListener {
                    animeDao.insertAnime(anime!!)
                    favBtn.text = BTN_TEXT_REMOVE
                }
            }
        } else {
            favBtn.text = BTN_TEXT_ADD
            favBtn.setOnClickListener {
                animeDao.insertAnime(anime!!)
                favBtn.text = BTN_TEXT_REMOVE
                favBtn.setOnClickListener {
                    animeDao.deleteAnime(anime!!)
                    favBtn.text = BTN_TEXT_ADD
                }
            }
        }

    }

    private fun getCharacters(id: Int) {
        CharacterResponse.getAnimeCharacters(id) {
            animeCharacters.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            val characterAdapter = CharacterAdapter(it)
            animeCharacters.adapter = characterAdapter
        }
    }


    private fun getRecommended(id: Int) {
        AnimeRecommendation.getAnimeRecommended(page, id) { anime ->
            animeRecommended.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            val recommendAdapter =
                RecommendAdapter(anime as ArrayList<com.example.obook.model.animeModel.anime.recommendation.Data>)
            animeRecommended.adapter = recommendAdapter
            animeRecommended.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollHorizontally(1)) {
                        AnimeRecommendation.getAnimeRecommended(page + 1, id) {
                            recommendAdapter.addData(it as ArrayList<com.example.obook.model.animeModel.anime.recommendation.Data>)
                        }
                    }
                }
            })
            recommendAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    val image = anime[position].entry.images.jpg.toString()
//                            Log.d("ImageUri", image)
                    intent.putExtra("id", anime[position].entry.malId.toString())
                    intent.putExtra("image", image)
                    intent.putExtra("title", anime[position].entry.title)
                    intent.putExtra("anime", true)
                    startActivity(intent)
                }

            })
        }
    }

    private fun getGenreList(id: Int) {
        FullDetail.getAnimeDetails(id) {
            animeGenres.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            genreList = it.genres
            genreAdapter = AnimeGenreAdapter(genreList)
            animeGenres.adapter = genreAdapter
        }
    }

    private fun getAnimeDetails(id: Int) {
        FullDetail.getAnimeDetails(id) {
            animeTitle.text = it.title
            animeDescription.text = it.synopsis
            animeStatus.text = it.status
            episodes.text = it.episodes.toString()
            Glide.with(requireContext()).load(it.images.jpg.imageUrl).into(animeImage)
            Glide.with(requireContext()).load(it.images.jpg.imageUrl).into(anime_image1)
            getTrailers(it.trailer)
        }
    }

    private fun getTrailers(videos: Trailer) {
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            @SuppressLint("SetTextI18n")
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = videos.youtubeId
//                youTubePlayerView.addFullScreenListener()
                if (videoId == null) {
                    youTubePlayerView.visibility = View.GONE
                } else
                    youTubePlayer.cueVideo(videoId, 0.00F)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
