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
import com.example.visitor.R
import com.example.visitor.adapters.VisitorAdapter
import com.example.visitor.interfaces.Callbacks
import com.example.visitor.interfaces.ProfileListCallback
import com.example.visitor.model.Profile
import com.example.visitor.utils.DatabaseUtils

class HomeFragment: Fragment(), ProfileListCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var profileList: ArrayList<Profile>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        DatabaseUtils.getAllEntries(this)
        profileList = ArrayList()
    }

    private fun findViews() {
        progressBar = view!!.findViewById(R.id.home_progress_bar)
        recyclerView = view!!.findViewById(R.id.recyclerview_recent_visitors)

        recyclerView.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView() {

        recyclerView.apply {
            adapter = VisitorAdapter(profileList)
            layoutManager = LinearLayoutManager(this@HomeFragment.context,RecyclerView.VERTICAL,false)
        }
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        Log.d("RV","Here")

    }

    override fun getProfileList(profileList: ArrayList<Profile>) {
        this.profileList = profileList
        setupRecyclerView()
    }

    companion object {

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }
}