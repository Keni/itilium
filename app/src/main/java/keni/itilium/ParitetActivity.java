package keni.itilium;

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
                obj = jo.getInt(Config.TAG_APP_OBJ_ID);

                // Вызов метода преобразование id объекта в его название (быдло из за кривизны создания бд :( )
                intToStr();

                String initiator_name = jo.getString(Config.TAG_APP_INITIATOR_NAME);
                String reason = jo.getString(Config.TAG_APP_REASON);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_APP_OBJ_ID, object);
                apps.put(Config.TAG_APP_INITIATOR_NAME, initiator_name);
                apps.put(Config.TAG_APP_REASON, reason);
                list.add(apps);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(ParitetActivity.this, list, R.layout.list_apps,
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    // Метод по id определяет обьект
    public void intToStr()
    {
        switch (obj)
        {
            case 71:
                object = "Аренда, ООО";
                break;
            case 72:
                object = "Городова Л.П., ИП (Дали Горькова)";
                break;
            case 73:
                object = "Городова Л.П., ИП (Дали Молокова)";
                break;
            case 74:
                object = "Оптима, ООО";
                break;
            case 75:
                object = "Харлей, ООО";
                break;
            case 83:
                object = "DreamLand";
                break;
            case 84:
                object = "Мир сумок";
                break;
            case 85:
                object = "Нью Йорк Оптима";
                break;
            case 86:
                object = "Биг Йоркер";
                break;
        }
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
