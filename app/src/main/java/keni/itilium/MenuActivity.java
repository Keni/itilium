package keni.itilium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import keni.itilium.Optima.OptimaActivity;
import keni.itilium.Paritet.ParitetActivity;

public class MenuActivity extends AppCompatActivity
{
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initToolBar();
    }

    public void onClickParitet(View view)
    {
        startActivity(new Intent(this, ParitetActivity.class));
    }

    public void onClickOptima(View view)
    {
        startActivity(new Intent(this, OptimaActivity.class));
    }

    public void initToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }
}
