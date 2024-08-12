package com.alex.materialdiary.sys.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.net.PskoveduApi.Companion.getInstance
import com.alex.materialdiary.sys.net.models.ShareUser
import com.alex.materialdiary.ui.fragments.NewChangeUserFragment
import com.alex.materialdiary.ui.fragments.ShareQRFragmentDirections


class RecycleAdapterSharedUsers(context: NewChangeUserFragment, users: MutableList<ShareUser>) :
    RecyclerView.Adapter<RecycleAdapterSharedUsers.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: NewChangeUserFragment
    private val users: MutableList<ShareUser>
    private var lastPosition = -1

    init {
        inflater = LayoutInflater.from(context.requireContext())
        this.users = users
        fragment = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.users_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.Name.text = user.name
//        holder.Grade.text = user.classname + " класс"
        val navController = findNavController(
            fragment.requireActivity(),
            R.id.nav_host_fragment_content_main
        )
        holder.SchoolName.text = "${user.classname} класс, ${user.school}"
        holder.share.setOnClickListener {
            val action: NavDirections = ShareQRFragmentDirections.toShare(user)
            navController.navigate(action)
        }
        holder.itemView.setOnClickListener { getInstance().changeGuid(user.guid, user.name, navController) }
        setAnimation(holder.itemView, position);
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
//        var Grade: TextView
        var Name: TextView
        var SchoolName: TextView
//        var scan: ImageView
        var share: ImageView

        init {
//            Grade = v.findViewById(R.id.user_Grade)
            Name = v.findViewById(R.id.user_Name)
            SchoolName = v.findViewById(R.id.user_SchoolName)
//            scan = v.findViewById(R.id.scanQr)
            share = v.findViewById(R.id.shareQr)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(inflater.context, android.R.anim.fade_in)
            animation.startOffset = (position * 45).toLong()
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}