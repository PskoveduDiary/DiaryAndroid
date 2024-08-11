package com.alex.materialdiary.sys.adapters

import androidx.navigation.Navigation.findNavController
import com.alex.materialdiary.sys.net.PskoveduApi.Companion.getInstance
import com.alex.materialdiary.ui.fragments.NewChangeUserFragment
import com.alex.materialdiary.sys.net.models.ShareUser
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alex.materialdiary.R
import android.widget.TextView
import androidx.navigation.NavDirections
import com.alex.materialdiary.ui.fragments.ShareQRFragmentDirections

class RecycleAdapterSharedUsers(context: NewChangeUserFragment, users: MutableList<ShareUser>) :
    RecyclerView.Adapter<RecycleAdapterSharedUsers.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: NewChangeUserFragment
    private val users: MutableList<ShareUser>

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
        holder.Grade.text = user.classname + " класс"
        holder.SchoolName.text = user.school
        holder.share.setOnClickListener {
            val action: NavDirections = ShareQRFragmentDirections.toShare(user)
            findNavController(
                fragment.requireActivity(),
                R.id.nav_host_fragment_content_main
            ).navigate(action)
        }
        holder.itemView.setOnClickListener { getInstance().changeGuid(user.guid, user.name) }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        var Grade: TextView
        var Name: TextView
        var SchoolName: TextView
        var scan: ImageView
        var share: ImageView

        init {
            Grade = v.findViewById(R.id.user_Grade)
            Name = v.findViewById(R.id.user_Name)
            SchoolName = v.findViewById(R.id.user_SchoolName)
            scan = v.findViewById(R.id.scanQr)
            share = v.findViewById(R.id.shareQr)
        }
    }
}