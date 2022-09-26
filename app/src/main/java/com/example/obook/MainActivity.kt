package com.example.obook


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.obook.Model.User
import com.example.obook.util.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var refUsers: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = null

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: User? = snapshot.getValue(User::class.java)
                    val name = user!!.getName()
                    toolbarText.text = ("Hey, $name")
                    toolbarText.setTextColor(Color.WHITE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val popular = Popular()
        val settings = SettingsFragment()
        val favorite = TopRated()
        makeCurrentScreen(popular)
        nav_bar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeCurrentScreen(popular)
                R.id.favourite -> makeCurrentScreen(favorite)
                R.id.settings -> makeCurrentScreen(settings)
            }
            true
        }
        }
    private fun makeCurrentScreen(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.wrapper_frame, fragment)
        commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT)
                .show()
            R.id.logout_item -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, Welcome::class.java))
                Toast.makeText(applicationContext,"Log out", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }

}