package ru.sergeykamyshov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Класс отвечает за обработку на экране "О приложении"
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setActionBarTitle(getString(R.string.action_bar_title_about));

        TextView version = (TextView) findViewById(R.id.text_version_name);
        version.append(BuildConfig.VERSION_NAME);
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
