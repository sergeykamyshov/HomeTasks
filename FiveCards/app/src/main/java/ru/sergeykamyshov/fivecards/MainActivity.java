package ru.sergeykamyshov.fivecards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.sergeykamyshov.fivecards.fragments.AboutFragment;
import ru.sergeykamyshov.fivecards.fragments.CardsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_cards_tab:
                        fragment = CardsFragment.newInstance();
                        break;
                    case R.id.action_about_tab:
                        fragment = AboutFragment.newInstance();
                        break;
                    default:
                        return false;
                }
                replaceOnFragment(fragment);
                return true;
            }
        });
        // Устанавливаем фрагмент с карточками при первом открытии приложения
        replaceOnFragment(CardsFragment.newInstance());
    }

    /**
     * Обновляет FrameLayout на указанный переданный фрагмент
     *
     * @param fragment - фрагмент, который необходимо установить
     */
    private void replaceOnFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}
