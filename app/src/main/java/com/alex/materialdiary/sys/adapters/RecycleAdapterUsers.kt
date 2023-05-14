package com.alex.materialdiary.sys.adapters

import androidx.navigation.Navigation.findNavController
import com.alex.materialdiary.sys.net.PskoveduApi.Companion.getInstance
import com.alex.materialdiary.ChangeUserFragment
import com.alex.materialdiary.sys.net.models.get_user.Schools
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.alex.materialdiary.sys.net.models.get_user.SchoolInfo
import android.view.ViewGroup
import android.widget.ImageView
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.net.models.ShareUser
import android.widget.TextView
import androidx.navigation.NavDirections
import com.alex.materialdiary.ShareQRFragmentDirections
import com.alex.materialdiary.sys.net.models.get_user.Participant
import java.util.ArrayList

class RecycleAdapterUsers(context: ChangeUserFragment, schools: List<Schools>?) :
    RecyclerView.Adapter<RecycleAdapterUsers.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: ChangeUserFragment
    private val participants: List<Participant?>?
    private val schoolsForUsers: MutableList<SchoolInfo?> = ArrayList()

    init {
        inflater = LayoutInflater.from(context.requireContext())
        participants = transform(schools)
        fragment = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.users_item, parent, false)
        return ViewHolder(view)
    }

    fun transform(schools: List<Schools>?): List<Participant?>? {
        val list: MutableList<Participant?> = ArrayList()
        if (schools == null) return null
        for ((roles, school, _, _, _, participant, _, userParticipants) in schools) {
            if (roles.contains("participant")) {
                list.add(participant)
                schoolsForUsers.add(school)
            } else {
                list.addAll(userParticipants!!)
                for (i in userParticipants.indices) {
                    schoolsForUsers.add(school)
                }
            }
        }
        return list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val participant = participants!![position]
        val school = schoolsForUsers[position]
        holder.Name.text = participant!!.name + " " + participant.surname
        holder.Grade.text = participant.grade!!.name + " класс"
        holder.SchoolName.text = school!!.name
        holder.share.setOnClickListener {
            val action: NavDirections = ShareQRFragmentDirections.toShare(
                ShareUser(
                    participant.name + " " + participant.surname,
                    participant.sysGuid!!, school.shortName!!, participant.grade!!.name!!
                )
            )
            findNavController(
                fragment.requireActivity(),
                R.id.nav_host_fragment_content_main
            ).navigate(action)
        }
        holder.itemView.setOnClickListener {
            val name = participant.surname + " " + participant.name + " " + participant.secondname
            getInstance(inflater.context).changeGuid(participant.sysGuid!!, name)
        }
    }

    override fun getItemCount(): Int {
        return participants!!.size
    }

    class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        var Grade: TextView
        var Name: TextView
        var SchoolName: TextView
        var scan: ImageView
        var share: ImageView

        // Get the handles by calling findViewById() on View object inside the constructor
        init {
            Grade = v.findViewById(R.id.user_Grade)
            Name = v.findViewById(R.id.user_Name)
            SchoolName = v.findViewById(R.id.user_SchoolName)
            scan = v.findViewById(R.id.scanQr)
            share = v.findViewById(R.id.shareQr)
        }
    }
}