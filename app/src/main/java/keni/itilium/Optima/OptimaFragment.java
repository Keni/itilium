package keni.itilium.Optima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import keni.itilium.Config.Config;
import keni.itilium.Config.RequestHandler;
import keni.itilium.R;

/**
 * Created by Keni on 04.07.2016.
 */
public class OptimaFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener
{
    public ListView list_optima_apps;

    private String JSON_STRING;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String obj_filter = "71,72,73,74,75,83,84,85,86";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.activity_listview, containter, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent add = new Intent(getActivity(), AddAppOptimaActivity.class);
                startActivity(add);
            }
        });

        list_optima_apps = (ListView) getActivity().findViewById(R.id.list_apps);
        list_optima_apps.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
                getJSON();
            }
        });
    }

    @Override
    public void onRefresh()
    {
        getJSON();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getJSON();
    }

    private void getJSON()
    {
        swipeRefreshLayout.setRefreshing(true);

        class GetJSON extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Загрузка", "Подождите...", false, false);
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
                String s = rh.sendGetRequestParam(Config.URL_GET_ALL_APPS_OPTIMA, obj_filter);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showApps()
    {
        JSONObject jsonObject;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String app_id = jo.getString(Config.TAG_APP_ID);
                String obj = jo.getString(Config.TAG_APP_OBJ);
                String initiator_name = jo.getString(Config.TAG_APP_INITIATOR_NAME);
                String reason = jo.getString(Config.TAG_APP_REASON);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_APP_ID, app_id);
                apps.put(Config.TAG_APP_OBJ, obj);
                apps.put(Config.TAG_APP_INITIATOR_NAME, initiator_name);
                apps.put(Config.TAG_APP_REASON, reason);
                list.add(apps);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.list_apps,
                new String[] {Config.TAG_APP_OBJ, Config.TAG_APP_INITIATOR_NAME, Config.TAG_APP_REASON},
                new int[] {R.id.obj, R.id.initiator_name, R.id.reason});

        list_optima_apps.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent app = new Intent(getActivity(), ViewAppOptimaActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String appId = map.get(Config.TAG_APP_ID);

        app.putExtra(Config.APP_ID, appId);
        startActivity(app);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflate)
    {
        inflate.inflate(R.menu.filteroptima, menu);
        super.onCreateOptionsMenu(menu, inflate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case (R.id.allObjects):
                obj_filter = "71,72,73,74,75,83,84,85,86";
                item.setChecked(true);
                break;

            case (R.id.arenda):
                obj_filter = "71";
                item.setChecked(true);
                break;

            case (R.id.daliGor):
                obj_filter = "72";
                item.setChecked(true);
                break;

            case (R.id.daliMol):
                obj_filter = "73";
                item.setChecked(true);
                break;

            case (R.id.optima):
                obj_filter = "74";
                item.setChecked(true);
                break;

            case (R.id.harley):
                obj_filter = "75";
                item.setChecked(true);
                break;

            case (R.id.dreamLand):
                obj_filter = "83";
                item.setChecked(true);
                break;

            case (R.id.mir):
                obj_filter = "84";
                item.setChecked(true);
                break;

            case (R.id.newYork):
                obj_filter = "85";
                item.setChecked(true);
                break;

            case (R.id.bigYorker):
                obj_filter = "86";
                item.setChecked(true);
                break;
        }

        getJSON();
        return true;
    }
}