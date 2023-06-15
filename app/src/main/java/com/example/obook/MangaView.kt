package com.example.obook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.obook.fragments.MangaDetail
import com.example.obook.fragments.Popular
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manga_view.*
import kotlinx.android.synthetic.main.activity_manga_view.manga_image_view
import kotlinx.android.synthetic.main.activity_manga_view.next
import kotlinx.android.synthetic.main.activity_manga_view.prev
import kotlinx.android.synthetic.main.fragment_manga_view.*

class MangaView : AppCompatActivity() {
    private var id: String? = ""
    private var title: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_view)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)

        id = intent.getStringExtra("id")
        title = intent.getStringExtra("title")
        val chapter = intent.getStringExtra("chapter")!!
        supportActionBar?.title = "Chapter $chapter"
        getImages(id!!)
    }

    private fun getImages(id: String){
        var index = 0
        MangaResponse.getMangaImages(id){
            val images = it.chapter.data.toMutableList()
            val hash = it.chapter.hash
            for(i in images.indices){
                images[i] = "https://uploads.mangadex.org/data/${hash}/${images[i]}"
            }
            Picasso.get().load(images[0]).into(manga_image_view)
            next.setOnClickListener {
                if(index++ < images.size-1){
                    Picasso.get().load(images[index]).into(manga_image_view)
                }
                else{
                    index = images.size-1
                    Picasso.get().load(images[index]).into(manga_image_view)
                }
            }
            prev.setOnClickListener {
                if(index-- > 0){
                    Picasso.get().load(images[index]).into(manga_image_view)
                }
                else{
                    index = 0
                    Picasso.get().load(images[index]).into(manga_image_view)
                }
            }



//            Log.d("images", hash.toString())
//            mangaRecyclerView.adapter = MangaImageAdapter(images, hash)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
//        val intent = Intent(this, MangaDetail::class.java)
//        val bundle = Bundle()
//        bundle.putString("id", id)
//        val fragment = MangaDetail()
//        fragment.arguments = bundle
//        startActivity(intent)
//        this.finish()
    }
}