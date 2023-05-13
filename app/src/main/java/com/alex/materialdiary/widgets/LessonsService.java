package com.alex.materialdiary.widgets;
 
import android.content.Intent;
import android.widget.RemoteViewsService;
 
public class LessonsService extends RemoteViewsService {
 
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new LessonsFactory(getApplicationContext(), intent);
  }
 
}