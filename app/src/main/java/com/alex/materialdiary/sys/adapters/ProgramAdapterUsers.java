package com.alex.materialdiary.sys.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.alex.materialdiary.ChangeUserFragment;
import com.alex.materialdiary.DiaryFragment;
import com.alex.materialdiary.R;
import com.alex.materialdiary.WebLoginFragment;
import com.alex.materialdiary.WebLoginFragmentDirections;
import com.alex.materialdiary.sys.common.CommonAPI;
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay;
import com.alex.materialdiary.sys.common.models.get_user.Schools;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProgramAdapterUsers extends ArrayAdapter<String> {
    ChangeUserFragment context;
    List<Schools> data;

    // This is the constructor of the class. It's called when you create an object of the class.
    public ProgramAdapterUsers(ChangeUserFragment context, List<Schools> data) {
        super(context.getContext(), R.layout.lesson_item, R.id.lesson_Name, Arrays.asList(new String[data.size()]));

        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // The parameter convertView is null when your app is creating a new item for the first time. It's not null when
        // recycling.
        // Assign the convertView in a View object
        View singleItem = convertView;
        // Find a View from the entire View hierarchy by calling findViewById() is a fairly expensive task.
        // So, you'll create a separate class to reduce the number of calls to it.
        // First, create a reference of ProgramViewHolder and assign it to null.
        ProgramViewHolderUsers holder = null;
        // Since layout inflation is a very expensive task, you'll inflate only when creating a new item in the ListView. The first
        // time you're creating a new item, convertView will be null. So, the idea is when creating an item for the first time,
        // we should perform the inflation and initialize the ViewHolder.
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.users_item, parent, false);
            // Pass the singleItem to the constructor of ProgramViewHolder. This singleItem object contains a LinearLayout
            // as the root element for single_item.xml file that contains other Views as well for the ListView.
            holder = new ProgramViewHolderUsers(singleItem);
            // When you create an object of ProgramViewHolder, you're actually calling findViewById() method inside the constructor.
            // By creating ProgramViewHolder only when making new items, you call findViewById() only when making new rows.
            // At this point all the three Views have been initialized. Now you need to store the holder so that you don't need to
            // create it again while recycling and you can do this by calling setTag() method on singleItem and passing the holder as a parameter.
            singleItem.setTag(holder);
        }
        // If singleItem is not null then we'll be recycling
        else{
            // Get the stored holder object
            holder = (ProgramViewHolderUsers) singleItem.getTag();
        }
        if (data.get(position).getParticipant() == null){
            if (data.get(position).getUserParticipants().size() > 0){
                holder.Name.setText(data.get(position).getUserParticipants().get(0).getName() + " " + data.get(position).getUserParticipants().get(0).getSurname());
                holder.Grade.setText(data.get(position).getUserParticipants().get(0).getGrade().getName() + " класс");
                holder.SchoolName.setText(data.get(position).getUserParticipants().get(0).getGrade().getSchool().getName());
                holder.scan.setOnClickListener(v -> {
                    NavDirections action =
                            WebLoginFragmentDirections.toWebLogin(data.get(position).getUserParticipants().get(0).getSysGuid(),
                            data.get(position).getUserParticipants().get(0).getName() + " " + data.get(position).getUserParticipants().get(0).getSurname());
                    Navigation.findNavController(context.requireActivity(), R.id.nav_host_fragment_content_main).navigate(action);
                });
            }
            else {
                holder.Name.setText("Произошла ошибка при получении данных");
                holder.Grade.setText("");
                holder.SchoolName.setText("");
            }
        }
        else {
            holder.Name.setText(data.get(position).getParticipant().getName() + " " + data.get(position).getParticipant().getSurname());
            holder.Grade.setText(data.get(position).getParticipant().getGrade().getName() + " класс");
            holder.SchoolName.setText(data.get(position).getSchool().getName());
            holder.scan.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    NavDirections action =
                            WebLoginFragmentDirections.toWebLogin(data.get(position).getParticipant().getSysGuid(),
                                    data.get(position).getParticipant().getName() + " " + data.get(position).getParticipant().getSurname());
                    Navigation.findNavController(context.requireActivity(), R.id.nav_host_fragment_content_main).navigate(action);
                }
            });
        }
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).getParticipant() == null) {
                    if (data.get(position).getUserParticipants().size() > 0) {
                        CommonAPI.getInstance().ChangeUuid(data.get(position).getUserParticipants().get(0).getSysGuid());
                    }
                }
                else{
                    CommonAPI.getInstance().ChangeUuid(data.get(position).getParticipant().getSysGuid());
                }
                //Intent openLinksIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
                //context.startActivity(openLinksIntent);
            }
        });
        return singleItem;
    }
}
/*
* {
    "success": true,
    "system": false,
    "message": "ok",
    "data": {
        "LOGIN": "185-953-213 08",
        "SURNAME": "Николаева",
        "NAME": "Татьяна",
        "SECONDNAME": "Анатольевна",
        "EMAIL": "nta80@yandex.ru",
        "CONFIRMATION": "MANUAL",
        "CONFIRM_EXPIRATION": null,
        "SESSION_ID": null,
        "SCHOOLS": [
            {
                "ROLES": [
                    "participant"
                ],
                "SCHOOL": {
                    "SYS_GUID": "20226019",
                    "ID": 19,
                    "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                    "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                },
                "GOVERNMENT": null,
                "TEACHER": null,
                "PARENT": null,
                "PARTICIPANT": {
                    "SYS_GUID": "F068571671C66FEB0D92037590008D5F",
                    "SURNAME": "Николаев",
                    "NAME": "Антон",
                    "SECONDNAME": "Антонович",
                    "SEX": "М",
                    "GRADE": {
                        "SYS_GUID": "984828FE23D636AD66C4A04F91802DA5",
                        "NAME": "8А",
                        "SCHOOL": {
                            "SYS_GUID": "20226019",
                            "ID": 19,
                            "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                            "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                        },
                        "GRADE_HEAD": {
                            "SYS_GUID": "BB22A89BD1C599357021B3B886C48316",
                            "SURNAME": "Курбатова",
                            "NAME": "Ольга",
                            "SECONDNAME": "Николаевна",
                            "SEX": "Ж",
                            "SCHOOL": {
                                "SYS_GUID": "20226019",
                                "ID": 19,
                                "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                                "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                            }
                        }
                    }
                },
                "USER_GRADES": [],
                "USER_PARTICIPANTS": [],
                "USER_PARENTS": [
                    {
                        "SYS_GUID": "BD7AC0CCAECA03261C7343AC0DFEDE34",
                        "SURNAME": "Николаева",
                        "NAME": "Татьяна",
                        "SECONDNAME": "Анатольевна",
                        "SEX": null,
                        "SCHOOL": {
                            "SYS_GUID": "20226019",
                            "ID": 19,
                            "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                            "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                        }
                    }
                ]
            }
        ]
    }
}
* {
    "success": true,
    "system": false,
    "message": "ok",
    "data": {
        "LOGIN": "085-349-005 77",
        "SURNAME": "Сергеева",
        "NAME": "Светлана",
        "SECONDNAME": "Анатольевна",
        "EMAIL": "ssveta.kmv@gmail.com",
        "CONFIRMATION": "ESIA",
        "CONFIRM_EXPIRATION": null,
        "SESSION_ID": null,
        "SCHOOLS": [
            {
                "ROLES": [
                    "parents"
                ],
                "SCHOOL": {
                    "SYS_GUID": "2022602",
                    "ID": 2,
                    "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Псковский технический лицей\"",
                    "SHORT_NAME": "МБОУ \"ПТЛ\""
                },
                "GOVERNMENT": null,
                "TEACHER": null,
                "PARENT": {
                    "SYS_GUID": "3B2C4BDB14B939E62A51C22E62A9CDC3",
                    "SURNAME": "Сергеева",
                    "NAME": "Светлана",
                    "SECONDNAME": "Анатольевна",
                    "SEX": "Ж",
                    "SCHOOL": {
                        "SYS_GUID": "2022602",
                        "ID": 2,
                        "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Псковский технический лицей\"",
                        "SHORT_NAME": "МБОУ \"ПТЛ\""
                    }
                },
                "PARTICIPANT": null,
                "USER_GRADES": [],
                "USER_PARTICIPANTS": [],
                "USER_PARENTS": []
            },
            {
                "ROLES": [
                    "parents"
                ],
                "SCHOOL": {
                    "SYS_GUID": "20226019",
                    "ID": 19,
                    "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                    "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                },
                "GOVERNMENT": null,
                "TEACHER": null,
                "PARENT": {
                    "SYS_GUID": "49CBBFA7B96A2547E51034839FC5E12C",
                    "SURNAME": "Сергеева",
                    "NAME": "Светлана",
                    "SECONDNAME": "Анатольевна",
                    "SEX": null,
                    "SCHOOL": {
                        "SYS_GUID": "20226019",
                        "ID": 19,
                        "NAME": "Муниципальное бюджетное общеобразовательное учреждение \"Средняя общеобразовательная школа №23 с углубленным изучением английского языка\"",
                        "SHORT_NAME": "МБОУ \"Средняя общеобразовательная школа №23\""
                    }
                },
                "PARTICIPANT": null,
                "USER_GRADES": [],
                "USER_PARTICIPANTS": [],
                "USER_PARENTS": []
            }
        ]
    }
}*/