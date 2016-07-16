package keni.itilium.Optima;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import keni.itilium.Config.Config;
import keni.itilium.Config.RequestHandler;
import keni.itilium.Config.ObjectsAndEngineers;
import keni.itilium.R;

public class AddAppOptimaActivity extends AppCompatActivity
{
    private EditText editTextInitiator;
    private EditText editTextReason;

    private String obj, JSON_STRING;

    private Spinner spinnerObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app);
        initToolBar();

        editTextInitiator = (EditText) findViewById(R.id.editTextInitiator);
        editTextReason = (EditText) findViewById(R.id.editTextReason);

        spinnerObjects = (Spinner) findViewById(R.id.spinnerObjects);

        getJSON();


        spinnerObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ObjectsAndEngineers objectId = (ObjectsAndEngineers) parent.getSelectedItem();
                obj = objectId.getId();
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
                loading = ProgressDialog.show(AddAppOptimaActivity.this, "Загрузка", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                loadObjects();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_OBJECTS_OPTIMA);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void loadObjects()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            ArrayList<ObjectsAndEngineers> objectsList = new ArrayList<>();

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String obj_id = jo.getString(Config.TAG_OBJ_ID);
                String name = jo.getString(Config.TAG_OBJ_NAME);

                objectsList.add(new ObjectsAndEngineers(obj_id, name));
            }

            ArrayAdapter<ObjectsAndEngineers> adapter = new ArrayAdapter<ObjectsAndEngineers>(this, R.layout.spinner_objects_item, objectsList);
            adapter.setDropDownViewResource(R.layout.spinner_objects_dropdown_item);

            spinnerObjects.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void addApp()
    {
        final String initiator_name = editTextInitiator.getText().toString().trim();
        final String description = editTextReason.getText().toString().trim();

        class AddApp extends AsyncTask<Void, Void, String>
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                Toast.makeText(AddAppOptimaActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v)
            {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_APP_OBJ, obj);
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
        final String initiator_name = editTextInitiator.getText().toString().trim();
        final String description = editTextReason.getText().toString().trim();

        switch (item.getItemId())
        {
            case R.id.done:
                if (initiator_name.isEmpty() || description.isEmpty())
                    Toast.makeText(AddAppOptimaActivity.this, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
                else
                {
                    addApp();
                    onBackPressed();
                    break;
                }
        }
        return true;
    }
}
