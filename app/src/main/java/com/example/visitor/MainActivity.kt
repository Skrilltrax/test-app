package com.example.visitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.visitor.fragments.EntryFragment
import com.example.visitor.fragments.HomeFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var floatingActionButton: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame, HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
            .commit()
        setContentView(R.layout.activity_main)
        findViews()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setClickListeners()
    }

    private fun setClickListeners() {
        floatingActionButton.setOnClickListener {


            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(R.id.frame,EntryFragment.newInstance(),EntryFragment::class.java.simpleName)
                .addToBackStack(EntryFragment::class.java.simpleName)
                .commit()
            floatingActionButton.hide(true)
        }
    }

    private fun findViews() {
        toolbar = findViewById(R.id.toolbar)
        floatingActionButton = findViewById(R.id.fab)
    }

    fun showFab() {
        floatingActionButton.show(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            showFab()
        }
    }

}
