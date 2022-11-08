package com.example.obook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.obook.Model.User
import com.example.obook.util.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var refUsers: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private var obj = Constant()
    private var userSign = false
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

        userSign = intent.getBooleanExtra("Not Sign", false)
        obj.setInfo(userSign)

        println("True Value: " + userSign)
        println("True Value: " + obj.getInfo())

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = nav_view.getHeaderView(0)
        val username = header.findViewById<TextView>(R.id.username)

        if(!obj.getInfo()){
            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

            refUsers.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user: User? = snapshot.getValue(User::class.java)
                        val name = user!!.getName()
                        toolbarText.text = ("Hey, $name")
                        username.text = "Hey, $name"
                        toolbarText.setTextColor(Color.WHITE)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            nav_view.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.fav -> {
                        makeCurrentScreen(Favourite())
                        drawer.closeDrawer(GravityCompat.START)
                    }
                    R.id.logout -> {
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(this, Login::class.java))
                        Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show()
                        this.finish()
                    }
                }
                true
            }
        }
        else{
            hideItem()
        }

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

    private fun hideItem() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val nav_Menu: Menu = navigationView.getMenu()
        nav_Menu.findItem(R.id.logout).isVisible = false
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        if(obj.getInfo()){
            val register: MenuItem? = menu!!.findItem(R.id.logout_item)
            print("reg " + register)
            register?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        if(Constant().getInfo()){
            when (item.itemId) {
                R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT)
                    .show()
                R.id.logout_item -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(applicationContext, Login::class.java))
                    Toast.makeText(applicationContext,"Log out", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                R.id.favourite -> makeCurrentScreen(Favourite())
            }
        }
        else{
            when (item.itemId) {
                R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT).show()
                R.id.favourite -> makeCurrentScreen(Favourite())
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}