package com.example.visitor.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.visitor.MainActivity
import com.example.visitor.R
import com.example.visitor.adapters.VisitorAdapter
import com.example.visitor.interfaces.Callbacks
import com.example.visitor.interfaces.ProfileListCallback
import com.example.visitor.model.Profile
import com.example.visitor.utils.DatabaseUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class HomeFragment: Fragment(), ProfileListCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var floatingActionButton: ExtendedFloatingActionButton
    private lateinit var profileList: ArrayList<Profile>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        (this.activity as MainActivity).showFab()
        DatabaseUtils.getAllEntries(this)
//        profileList = ArrayList()
        setupRecyclerView(ArrayList())
    }

    private fun findViews() {
        progressBar = view!!.findViewById(R.id.home_progress_bar)
        recyclerView = view!!.findViewById(R.id.recyclerview_recent_visitors)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView(profileList: ArrayList<Profile>) {

        if (profileList.isNullOrEmpty()) {
            recyclerView.apply {
                adapter = VisitorAdapter(profileList)
                layoutManager = LinearLayoutManager(this@HomeFragment.context, RecyclerView.VERTICAL, false)
            }
        } else {
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            recyclerView.adapter = VisitorAdapter(profileList)
        }
    }

    override fun getProfileList(profileList: ArrayList<Profile>) {
        setupRecyclerView(profileList)
    }

    companion object {

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }
}