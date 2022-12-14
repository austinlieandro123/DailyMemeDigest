package com.austin.dailymemedigest

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    val fragments:ArrayList<Fragment> = ArrayList()

    override fun onResume() {
        super.onResume()
        var pack = "com.austin.dailymemedigest"
        var shared = getSharedPreferences(pack, Context.MODE_PRIVATE)
        var uname =  shared.getString(LoginActivity.SHARED_USERNAME, null)
        var firstName =  shared.getString(LoginActivity.FIRST_NAME, null)
        var lastName =  shared.getString(LoginActivity.LAST_NAME, null)
        var URLavatar =  shared.getString(LoginActivity.URL_AVATAR, null)

        val navView: NavigationView = findViewById(R.id.navView)
        val headerView: View = navView.getHeaderView(0)

        val unameheader = headerView.findViewById<View>(R.id.txtUsernameHeader) as? TextView
        unameheader?.setText(uname)

        val fullnameheader = headerView.findViewById<View>(R.id.txtFullNameHeader) as? TextView
        val imgheader = headerView.findViewById<View>(R.id.imgProfile) as? ImageView

        if (firstName=="null"&&lastName=="null"){
            fullnameheader?.setText("User")
        }else if(firstName!="null"&&lastName=="null"){
            fullnameheader?.setText(firstName.toString())
        }else {
            fullnameheader?.setText(firstName.toString() + " " + lastName.toString())
        }
        Picasso.get().load(URLavatar).into(imgheader)

    val adapter = MyViewPagerAdapter(this, fragments)

    viewPagerMain.adapter = adapter

    viewPagerMain.registerOnPageChangeCallback(
        object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                val itemId = bottomNav.menu.getItem(position).itemId
                bottomNav.selectedItemId = itemId
                changeSelected(when(itemId){
                    R.id.ItemHomeBot -> 0
                    R.id.ItemMyCreationBot -> 1
                    R.id.ItemLeaderboardBot -> 2
                    R.id.ItemSavedBot -> 3
                    R.id.ItemSettingsBot -> kesetting(bottomNav,navView)
                    else -> 0
                }, viewPagerMain, navView, bottomNav)
            }
        }
    )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var pack = "com.austin.dailymemedigest"
        var shared = getSharedPreferences(pack, Context.MODE_PRIVATE)
        var uname =  shared.getString(LoginActivity.SHARED_USERNAME, null)
        var firstName =  shared.getString(LoginActivity.FIRST_NAME, null)
        var lastName =  shared.getString(LoginActivity.LAST_NAME, null)
        var URLavatar =  shared.getString(LoginActivity.URL_AVATAR, null)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Daily Meme Digest"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        var drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name, R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        fragments.add(HomeFragment())
        fragments.add(SelfCreationFragment())
        fragments.add(LeaderboardFragment())
        fragments.add(SavedFragment())

        val adapter = MyViewPagerAdapter(this, fragments)

        viewPagerMain.adapter = adapter

        viewPagerMain.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    val itemId = bottomNav.menu.getItem(position).itemId
                    bottomNav.selectedItemId = itemId
                    changeSelected(when(itemId){
                        R.id.ItemHomeBot -> 0
                        R.id.ItemMyCreationBot -> 1
                        R.id.ItemLeaderboardBot -> 2
                        R.id.ItemSavedBot -> 3
                        R.id.ItemSettingsBot -> kesetting(bottomNav,navView)
                        else -> 0
                    }, viewPagerMain, navView, bottomNav)
                }
            }
        )

        bottomNav.setOnItemSelectedListener{
            changeSelected(when(it.itemId){
                R.id.ItemHomeBot -> 0
                R.id.ItemMyCreationBot -> 1
                R.id.ItemLeaderboardBot -> 2
                R.id.ItemSavedBot -> 3
                R.id.ItemSettingsBot -> kesetting(bottomNav,navView)
                else -> 0
            }, viewPagerMain, navView, bottomNav)
            true
        }

        navView.setNavigationItemSelectedListener {
            changeSelected(when(it.itemId){
                R.id.ItemHomeDrawer -> 0
                R.id.ItemMyCreationDrawer -> 1
                R.id.ItemLeaderboardDrawer -> 2
                R.id.ItemSavedDrawer -> 3
                R.id.ItemSettingsDrawer -> kesetting(bottomNav, navView)
                else -> 0
            }, viewPagerMain, navView, bottomNav)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val navView: NavigationView = findViewById(R.id.navView)
        val headerView: View = navView.getHeaderView(0)
        val fab = headerView.findViewById<View>(R.id.fabLogOut) as? FloatingActionButton
        fab?.setOnClickListener {
            var editor = shared?.edit()
            editor?.putString(LoginActivity.SHARED_USERNAME,null)
            editor?.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val unameheader = headerView.findViewById<View>(R.id.txtUsernameHeader) as? TextView
        unameheader?.setText(uname)

        val fullnameheader = headerView.findViewById<View>(R.id.txtFullNameHeader) as? TextView
        val imgheader = headerView.findViewById<View>(R.id.imgProfile) as? ImageView

        if (firstName=="null"&&lastName=="null"){
            fullnameheader?.setText("User")
        }else if(firstName!="null"&&lastName=="null"){
            fullnameheader?.setText(firstName.toString())
        }else {
            fullnameheader?.setText(firstName.toString() + " " + lastName.toString())
        }
        Picasso.get().load(URLavatar).into(imgheader)
    }

    fun changeSelected(id:Int, viewPager2: ViewPager2, navView:NavigationView, bottomNav: BottomNavigationView){
        viewPager2.currentItem=id
        navView.menu.getItem(id).isChecked=true
        navView.menu.getItem(id).isCheckable=true
        bottomNav.menu.getItem(id).isChecked = true
        bottomNav.menu.getItem(id).isCheckable = true
    }

    fun kesetting(bottomNav: BottomNavigationView,navView: NavigationView): Int {
        for (i in 0 until 4){
            navView.menu.getItem(i).isChecked = false
            navView.menu.getItem(i).isCheckable = false
        }
        bottomNav.menu.getItem(4).isChecked = false
        bottomNav.menu.getItem(4).isCheckable = false
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        return 0
    }
}