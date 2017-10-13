package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.action_bar_title_about));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.menu_item_schedule));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            startActivity(new Intent(this, ScheduleActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
