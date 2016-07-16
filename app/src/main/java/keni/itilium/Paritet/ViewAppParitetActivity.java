package keni.itilium.Paritet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import keni.itilium.Config.Config;
import keni.itilium.Config.RequestHandler;
import keni.itilium.R;

public class ViewAppParitetActivity extends AppCompatActivity
{
    private TextView textViewInitiator;

    private EditText editTextReason;
    private EditText editTextEngineer;
    private EditText editTextComment;

    private String id;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_app_paritet);
        initToolBar();

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.APP_ID);

        textViewInitiator = (TextView) findViewById(R.id.textViewInitiator);

        editTextReason = (EditText) findViewById(R.id.editTextReason);
        editTextEngineer = (EditText) findViewById(R.id.editTextEngineer);
        editTextComment = (EditText) findViewById(R.id.editTextComment);

        getApp();
    }

    private void getApp()
    {
        class GetApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppParitetActivity.this, "Загрузка...", "Ждите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                showApp(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_APP_PARITET, id);
                return s;
            }
        }
        GetApp ga = new GetApp();
        ga.execute();
    }

    private void showApp(String json)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            String obj = c.getString(Config.TAG_APP_OBJ);
            String initiator = c.getString(Config.TAG_APP_INITIATOR_NAME);
            String reason = c.getString(Config.TAG_APP_REASON);
            String engineer = c.getString(Config.TAG_APP_ENGINEER);
            String comment = c.getString(Config.TAG_APP_COMMENT);

            toolbar.setTitle(obj);
            textViewInitiator.setText(initiator);
            editTextReason.setText(reason);
            editTextEngineer.setText(engineer);
            editTextComment.setText(comment);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void updateApp()
    {
        final String reason = editTextReason.getText().toString().trim();
        final String enginner = editTextEngineer.getText().toString().trim();
        final String comment = editTextComment.getText().toString().trim();

        class UpdateApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppParitetActivity.this, "Обновление...", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewAppParitetActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_APP_ID, id);
                hashMap.put(Config.KEY_APP_REASON, reason);
                hashMap.put(Config.KEY_APP_ENGINEER, enginner);
                hashMap.put(Config.KEY_APP_COMMENT, comment);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_APP_PARITET, hashMap);

                return s;
            }
        }

        UpdateApp ua = new UpdateApp();
        ua.execute();

    }

    public void initToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.obj);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
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
                updateApp();
                break;
        }
        return true;
    }
}
