package keni.itilium.Optima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import keni.itilium.Config.Config;
import keni.itilium.Config.ObjectsAndEngineers;
import keni.itilium.Config.RequestHandler;
import keni.itilium.R;

public class ViewAppOptimaActivity extends AppCompatActivity
{
    private TextView textViewInitiator;

    private EditText editTextReason;
    private EditText editTextComment;

    private String id, JSON_STRING, engineer;

    private Toolbar toolbar;

    private Spinner spinnerEngineer;

    ArrayList<ObjectsAndEngineers> engineersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_app_optima);
        initToolBar();

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.APP_ID);

        textViewInitiator = (TextView) findViewById(R.id.textViewInitiator);

        editTextReason = (EditText) findViewById(R.id.editTextReason);
        editTextComment = (EditText) findViewById(R.id.editTextComment);

        spinnerEngineer = (Spinner) findViewById(R.id.spinnerEngineer);

        getJSON();

        spinnerEngineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ObjectsAndEngineers engineerId = (ObjectsAndEngineers) parent.getSelectedItem();
                engineer = engineerId.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppOptimaActivity.this, "Загрузка", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                loadEngineers();
                getApp();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ENGINNERS_OPTIMA);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void loadEngineers()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            engineersList.add(0, new ObjectsAndEngineers("0", "Не назначен"));

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String engineer_id = jo.getString(Config.TAG_ENGINEER_ID);
                String engineer_name = jo.getString(Config.TAG_ENGINEER_NAME);

                engineersList.add(new ObjectsAndEngineers(engineer_id, engineer_name));
            }

            ArrayAdapter<ObjectsAndEngineers> adapter = new ArrayAdapter<ObjectsAndEngineers>(this, R.layout.spinner_objects_item, engineersList);
            adapter.setDropDownViewResource(R.layout.spinner_objects_dropdown_item);

            spinnerEngineer.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


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
                loading = ProgressDialog.show(ViewAppOptimaActivity.this, "Загрузка...", "Ждите...", false, false);
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
                String s = rh.sendGetRequestParam(Config.URL_GET_APP_OPTIMA, id);
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

            for (int i = 0; i < engineersList.size(); i++)
            {
                if (spinnerEngineer.getItemAtPosition(i).toString().equals(engineer))
                {
                    spinnerEngineer.setSelection(i);
                }
            }

            toolbar.setTitle(obj);
            textViewInitiator.setText(initiator);
            editTextReason.setText(reason);
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
        final String comment = editTextComment.getText().toString().trim();

        class UpdateApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppOptimaActivity.this, "Обновление...", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewAppOptimaActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_APP_ID, id);
                hashMap.put(Config.KEY_APP_REASON, reason);
                hashMap.put(Config.KEY_APP_ENGINEER, engineer);
                hashMap.put(Config.KEY_APP_COMMENT, comment);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_APP_OPTIMA, hashMap);

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
