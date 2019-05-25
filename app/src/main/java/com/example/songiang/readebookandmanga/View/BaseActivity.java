package com.example.songiang.readebookandmanga.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.songiang.readebookandmanga.R;

public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    protected Toolbar activateToolbar()
    {
        if(mToolbar == null)
        {
            mToolbar = findViewById(R.id.toolbar);
            if(mToolbar !=null)
            {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        return  mToolbar;
    }

}
