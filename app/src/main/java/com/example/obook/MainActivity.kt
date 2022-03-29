package com.example.obook


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.obook.util.BrowseFragment
import com.example.obook.util.SettingsFragment
import com.example.obook.Model.User
import com.example.obook.util.Login
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

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: User? = snapshot.getValue(User::class.java)
                    val name = user!!.getName()
                    toolbarText.text = ("Hey, $name")
                    toolbarText.setTextColor(Color.BLACK)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

            val toolbar: Toolbar = findViewById(R.id.toolbar_main)
            setSupportActionBar(toolbar)
            supportActionBar!!.title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbar.navigationIcon = null

            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.back)

            val tabLayout: TabLayout = findViewById(R.id.tablayout)
            val viewPager: ViewPager = findViewById(R.id.viewpager)
            val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

            viewPagerAdapter.addfragment(BrowseFragment(), "Browse Movies")
            viewPagerAdapter.addfragment(SettingsFragment(), "Settings")

            viewPager.adapter = viewPagerAdapter
            tablayout.setupWithViewPager(viewPager)
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
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments: ArrayList<Fragment>
    private val titles: ArrayList<String>

    init {
        fragments = ArrayList<Fragment>()
        titles = ArrayList<String>()
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    fun addfragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

}