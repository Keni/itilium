package keni.itilium.Optima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import keni.itilium.Config;
import keni.itilium.R;
import keni.itilium.RequestHandler;

public class AppOptima extends AppCompatActivity
{
    private TextView textView;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_optima);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.APP_ID);

        textView = (TextView) findViewById(R.id.textView);


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
                loading = ProgressDialog.show(AppOptima.this, "Загрузка...", "Подождите...", false, false);
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
            String initiator_name = c.getString(Config.TAG_APP_INITIATOR_NAME);

            textView.setText(initiator_name);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
