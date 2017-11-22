package ru.sergeykamyshov.imgurclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.sergeykamyshov.imgurclient.fragments.ImagesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, ImagesFragment.newInstance())
                .commit();
    }
}
