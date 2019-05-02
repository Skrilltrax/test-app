package com.example.visitor.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.visitor.R
import com.example.visitor.model.Profile
import de.hdodenhof.circleimageview.CircleImageView

class VisitorAdapter(val visitorList: ArrayList<Profile>): RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_visitor,parent,false)
        return VisitorViewHolder(v)
    }

    override fun onBindViewHolder(holder: VisitorViewHolder, position: Int) {
        holder.setValues(visitorList[position])
    }

    override fun getItemCount(): Int {
        Log.d("Size","Here")
//        return 10
        return visitorList.size
    }


    class VisitorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var profileImage: CircleImageView = itemView.findViewById(R.id.profile_image)
        private var profileName: TextView = itemView.findViewById(R.id.profile_name)

        fun setValues(profile: Profile) {

            profileName.text = profile.name

            Glide.with(itemView)
                .load(profile.photoUrl)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(profileImage)

        }

    }
}