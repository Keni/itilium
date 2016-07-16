package keni.itilium;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import keni.itilium.Optima.OptimaFragment;
import keni.itilium.Paritet.ParitetFragment;

/**
 * Created by Keni on 04.07.2016.
 */
public class MainActivity extends AppCompatActivity implements OnItemSelectedListener
{
    private String[] data = {"Оптима", "Сибирский Паритет"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContent, new OptimaFragment()).commit();
        }
         */

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_toolbar_item, data);
        adapter.setDropDownViewResource(R.layout.spinner_toolbar_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        Class fragmentClass;
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragmentClass = OptimaFragment.class;
                break;
            case 1:
                fragmentClass = ParitetFragment.class;
                break;
            default:
                fragmentClass = OptimaFragment.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
    }
}
