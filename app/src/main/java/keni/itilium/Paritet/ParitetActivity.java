package keni.itilium.Paritet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import keni.itilium.Config;
import keni.itilium.R;
import keni.itilium.RequestHandler;

public class ParitetActivity extends AppCompatActivity
{
    Toolbar toolbar;

    private ListView listView;

    private String JSON_STRING, object;

    private int obj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paritet);
        initToolBar();

        listView = (ListView) findViewById(R.id.listparitet);
        getJSON();

    }

    private void showApps()
    {
        JSONObject jsonObject;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try
        {
            jsonObject = new JSONObject((JSON_STRING));
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String obj = jo.getString(Config.TAG_APP_OBJ_ID);
                String initiator_name = jo.getString(Config.TAG_APP_INITIATOR_NAME);
                String reason = jo.getString(Config.TAG_APP_REASON);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_APP_OBJ_ID, obj);
                apps.put(Config.TAG_APP_INITIATOR_NAME, initiator_name);
                apps.put(Config.TAG_APP_REASON, reason);
                list.add(apps);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(ParitetActivity.this, list, R.layout.list_apps_paritet,
                new String[]{Config.TAG_APP_OBJ_ID, Config.TAG_APP_INITIATOR_NAME, Config.TAG_APP_REASON},
                new int[]{R.id.obj, R.id.initiator_name, R.id.reason});

        listView.setAdapter(adapter);
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
                loading = ProgressDialog.show(ParitetActivity.this, "Загрузка", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showApps();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL_PARITET);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    public void initToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sib_paritet);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }
}
