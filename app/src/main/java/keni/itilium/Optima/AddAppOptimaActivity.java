package keni.itilium.Optima;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import keni.itilium.Config.Config;
import keni.itilium.Config.RequestHandler;
import keni.itilium.R;

public class AddAppOptimaActivity extends AppCompatActivity
{
    private EditText editTextInitiator;
    private EditText editTextReason;

    private Spinner spinnerObjects;

    private ArrayList<String> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app);
        initToolBar();

        objects = new ArrayList<String>();

        editTextInitiator = (EditText) findViewById(R.id.editTextInitiator);
        editTextReason = (EditText) findViewById(R.id.editTextReason);

        spinnerObjects = (Spinner) findViewById(R.id.spinnerObjects);
    }

    private void addApp()
    {
        //final String obj
        final String initiator_name = editTextInitiator.getText().toString().trim();
        final String description = editTextReason.getText().toString().trim();

        class AddApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(AddAppOptimaActivity.this, "Добавление...", "Ждите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddAppOptimaActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v)
            {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_APP_INITIATOR_NAME, initiator_name);
                params.put(Config.KEY_APP_REASON, description);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_APP_OPTIMA, params);
                return res;
            }
        }

        AddApp aa = new AddApp();
        aa.execute();
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_app);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.done:
                addApp();
                onBackPressed();
                break;
        }
        return true;
    }
}
