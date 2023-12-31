package com.alex.materialdiary.sys.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.OptIn;
import androidx.navigation.NavDirections;

import com.alex.materialdiary.DiaryFragment;
import com.alex.materialdiary.LessonFragmentDirections;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.net.models.diary_day.DatumDay;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;

import java.util.Arrays;
import java.util.List;

public class ProgramAdapterDiary extends ArrayAdapter<String> {
    DiaryFragment context;
    List<DatumDay> data;

    // This is the constructor of the class. It's called when you create an object of the class.
    public ProgramAdapterDiary(DiaryFragment context, List<DatumDay> data) {
        super(context.getContext(), R.layout.lesson_item, R.id.lesson_Name, Arrays.asList(new String[data.size()]));

        this.context = context;

        Log.d("qwreeyt", String.valueOf(data));
        this.data = data;
    }


    /**
     * When you're creating a new item, you'll inflate the layout and initialize the ViewHolder.
     * When you're recycling, you'll get the ViewHolder from the singleItem
     *
     * @param position The position of the item in the list.
     * @param convertView The view that you want to reuse.
     * @param parent The ViewGroup object that contains the list view.
     * @return The View object that is being returned is a View object that is created by the getView()
     * method.
     */
    @SuppressLint("ResourceAsColor")
    @Override
    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    public View getView(final int position, View convertView, ViewGroup parent) {
        // The parameter convertView is null when your app is creating a new item for the first time. It's not null when
        // recycling.
        // Assign the convertView in a View object
        View singleItem = convertView;
        // Find a View from the entire View hierarchy by calling findViewById() is a fairly expensive task.
        // So, you'll create a separate class to reduce the number of calls to it.
        // First, create a reference of ProgramViewHolder and assign it to null.
        ProgramViewHolderDiary holder = null;
        // Since layout inflation is a very expensive task, you'll inflate only when creating a new item in the ListView. The first
        // time you're creating a new item, convertView will be null. So, the idea is when creating an item for the first time,
        // we should perform the inflation and initialize the ViewHolder.
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.lesson_item, parent, false);
            // Pass the singleItem to the constructor of ProgramViewHolder. This singleItem object contains a LinearLayout
            // as the root element for single_item.xml file that contains other Views as well for the ListView.
            holder = new ProgramViewHolderDiary(singleItem);
            // When you create an object of ProgramViewHolder, you're actually calling findViewById() method inside the constructor.
            // By creating ProgramViewHolder only when making new items, you call findViewById() only when making new rows.
            // At this point all the three Views have been initialized. Now you need to store the holder so that you don't need to
            // create it again while recycling and you can do this by calling setTag() method on singleItem and passing the holder as a parameter.
            singleItem.setTag(holder);
        }
        // If singleItem is not null then we'll be recycling
        else{
            // Get the stored holder object
            holder = (ProgramViewHolderDiary) singleItem.getTag();
        }
        holder.Num.setText(data.get(position).getLessonNumber().toString() + " урок");
        holder.Time.setText(data.get(position).getLessonTimeBegin() + " - " + data.get(position).getLessonTimeEnd());
        holder.Name.setText(data.get(position).getSubjectName());
        String marks = "";
        String abscence = "";
        for (int i = 0; i < data.get(position).getAbsence().size(); i++)
        {
            if (!abscence.equals("")) abscence += ", ";
            abscence += data.get(position).getAbsence().get(i).getFullName();
        }
        for (int i = 0; i < data.get(position).getMarks().size(); i++ ) {
            if (!marks.equals("")) marks += ", ";
            marks += data.get(position).getMarks().get(i).getShortName();
        }
        holder.Mark.setText(marks);
        holder.Absence.setText(abscence);
        holder.Teacher.setText(data.get(position).getTeacherName());
        String hm = data.get(position).getHomeworkPrevious() != null ? data.get(position).getHomeworkPrevious().getHomework() : "";
        holder.HomeWork.setText(hm);
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NavDirections act = LessonFragmentDirections.toLesson(data.get(position));
                //context.get_nav().navigate(act);
                context.openBottomSheet(data.get(position));
                //Toast.makeText(getContext(), "You clicked:"+ Logins.get(position), Toast.LENGTH_SHORT).show();
                //Intent openLinksIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
                //context.startActivity(openLinksIntent);
            }
        });
        return singleItem;
    }
}
