package com.example.obook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.obook.Authentication.Login
import com.example.obook.Fragments.Favourite
import com.example.obook.Fragments.Popular
import com.example.obook.Fragments.SettingsFragment
import com.example.obook.Fragments.TopRated
import com.example.obook.Model.User
import com.example.obook.util.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private var obj = Constant.getInstance()
    private var userSign = false
    private var gSign = false
    @SuppressLint("SetTextI18n")
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
        gSign = intent.getBooleanExtra("gSign", false)
        obj.setInfo(userSign)
        obj.setGSign(gSign)


        println("True Value: $gSign")
        println("True Value: " + obj.getGSign())

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = nav_view.getHeaderView(0)
        val username = header.findViewById<TextView>(R.id.username)
        val email1 = header.findViewById<TextView>(R.id.email)
        val img = header.findViewById<ImageView>(R.id.ProfImg)

        if(!obj.getInfo()){
            nav_view.setNavigationItemSelectedListener {
                when(it.itemId){
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

        val FUser = Firebase.auth.currentUser
        FUser?.let {
            for (profile in it.providerData) {
                //(ex: google)
                val constObj = Constant.getInstance()
                constObj.setProviderId(profile.providerId)
                val uid = profile.uid
                if(constObj.getProviderId() == "password"){
                    val firebaseUid = FUser.uid
                    val refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUid)
                    refUsers.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val userObj = User.getInstance()
                                val MUser: User? = snapshot.getValue(User::class.java)
                                toolbarText.text = "Hey, ${MUser?.getName()}"
                                username.text = MUser?.getName()
                                email1.text = MUser?.getEmail()
                                userObj.setName(MUser?.getName().toString())
                                userObj.setUID(MUser?.getUID().toString())
                                userObj.setEmail(MUser?.getEmail().toString())
                                Log.d("UserObj - Password", "${userObj.getName()}")
                                toolbarText.setTextColor(Color.WHITE)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                else{
                    val constObj = Constant.getInstance()
                    constObj.setProviderId(profile.providerId)
                    val userObj = User.getInstance()
                    FUser.reload()
                    userObj.setName(profile.displayName.toString())
                    Log.d("Name", userObj.getName().toString())
                    userObj.setEmail(profile.email.toString())
                    userObj.setUri(profile.photoUrl.toString())
                    userObj.setUID(profile.uid)
                    toolbarText.text = "Hey, ${userObj.getName()}"
                    username.text = userObj.getName()
                    email1.text = userObj.getEmail()
                    Glide.with(this).load(Uri.parse(userObj.getUri())).into(img);
                    Log.d("UserObj ", "${constObj.getProviderId()} ${userObj.getName()} ${userObj.getUID()} ")
                    toolbarText.setTextColor(Color.WHITE)
                }
            }
            val const = Constant.getInstance()
            val userObj = User.getInstance()
            Log.d("GSignIn ", const.getProviderId())
            Log.d("UserObj ", "${const.getProviderId()} ${userObj.getName()} ${userObj.getUID()} ")
        }

        val popular = Popular()
        val favourite = Favourite()
        val topRated = TopRated()
        makeCurrentScreen(popular)
        nav_bar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.popular -> makeCurrentScreen(popular)
                R.id.topRated -> makeCurrentScreen(topRated)
                R.id.favorite -> makeCurrentScreen(favourite)
            }
            true
        }
        }

    private fun makeCurrentScreen(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.wrapper_frame, fragment)
        commit()
    }

    fun refresh() {
        val fragment = Favourite()
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.wrapper_frame, fragment).commit()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        if(obj.getInfo()){
            val register: MenuItem? = menu.findItem(R.id.logout_item)
            register?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        when (item.itemId) {
            R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT)
                .show()
            R.id.logout_item -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, Login::class.java))
                Toast.makeText(applicationContext,"Log out", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            R.id.settings -> makeCurrentScreen(SettingsFragment())
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}