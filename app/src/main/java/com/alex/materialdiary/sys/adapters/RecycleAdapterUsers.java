package com.alex.materialdiary.sys.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.materialdiary.ChangeUserFragment;
import com.alex.materialdiary.R;
import com.alex.materialdiary.ShareQRFragmentDirections;
import com.alex.materialdiary.WebLoginFragmentDirections;
import com.alex.materialdiary.sys.ImageLoader;
import com.alex.materialdiary.sys.common.CommonAPI;
import com.alex.materialdiary.sys.common.models.ShareUser;
import com.alex.materialdiary.sys.common.models.get_user.Participant;
import com.alex.materialdiary.sys.common.models.get_user.SchoolInfo;
import com.alex.materialdiary.sys.common.models.get_user.Schools;
import com.alex.materialdiary.sys.common.models.period_marks.Mark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecycleAdapterUsers extends RecyclerView.Adapter<RecycleAdapterUsers.ViewHolder>{

    private final LayoutInflater inflater;
    private final ChangeUserFragment fragment;
    private final List<Participant> participants;
    private final List<SchoolInfo> schoolsForUsers = new ArrayList<>();

    public RecycleAdapterUsers(ChangeUserFragment context, List<Schools> schools) {
        this.inflater = LayoutInflater.from(context.requireContext());
        participants = transform(schools);
        fragment = context;
    }

    @NonNull
    @Override
    public RecycleAdapterUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.users_item, parent, false);
        return new ViewHolder(view);
    }
    List<Participant> transform(List<Schools> schools){
        List<Participant> list = new ArrayList<Participant>();
        if (schools == null) return null;
        for (Schools sch: schools) {
            if (sch.getRoles().contains("participant")) {
                list.add(sch.getParticipant());
                schoolsForUsers.add(sch.getSchool());
            }
            else {
                list.addAll(sch.getUserParticipants());
                for (int i = 0; i < sch.getUserParticipants().size(); i++) {
                    schoolsForUsers.add(sch.getSchool());
                }
            }
        }
        return list;
    }
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterUsers.ViewHolder holder, int position) {
        Participant participant = participants.get(position);
        SchoolInfo school = schoolsForUsers.get(position);
        holder.Name.setText(participant.getName() + " " + participant.getSurname());
        holder.Grade.setText(participant.getGrade().getName() + " класс");
        holder.SchoolName.setText(school.getName());
        holder.scan.setOnClickListener(v -> {
            NavDirections action =
                    WebLoginFragmentDirections.toWebLogin(participant.getSysGuid(),
                            participant.getName() + " " + participant.getSurname());
            Navigation.findNavController(fragment.requireActivity(), R.id.nav_host_fragment_content_main).navigate(action);
        });
        holder.share.setOnClickListener(v -> {
            NavDirections action =
                    ShareQRFragmentDirections.toShare(new ShareUser(participant.getName() + " " + participant.getSurname(),
                            participant.getSysGuid(), school.getShortName(), participant.getGrade().getName()));
            Navigation.findNavController(fragment.requireActivity(), R.id.nav_host_fragment_content_main).navigate(action);
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonAPI.getInstance().ChangeUuid(participant.getSysGuid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Grade;
        TextView Name;
        TextView SchoolName;
        ImageView scan;
        ImageView share;
        // Get the handles by calling findViewById() on View object inside the constructor
        ViewHolder(View v)
        {
            super(v);
            Grade = v.findViewById(R.id.user_Grade);
            Name = v.findViewById(R.id.user_Name);
            SchoolName = v.findViewById(R.id.user_SchoolName);
            scan = v.findViewById(R.id.scanQr);
            share = v.findViewById(R.id.shareQr);
        }
    }
}
