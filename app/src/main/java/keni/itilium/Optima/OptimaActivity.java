package keni.itilium.Optima;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import keni.itilium.Config;
import keni.itilium.R;
import keni.itilium.RequestHandler;

public class OptimaActivity extends AppCompatActivity
{
    Toolbar toolbar;

    private List<AppsOptima> optimaList = new ArrayList<>();

    private RecyclerView optima_list;

    private String JSON_STRING;

    private OptimaAppsAdapter aAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optima);
        initToolBar();

        optima_list = (RecyclerView) findViewById(R.id.optima_list);

        aAdapter = new OptimaAppsAdapter(optimaList);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        optima_list.setLayoutManager(aLayoutManager);
        optima_list.setItemAnimator(new DefaultItemAnimator());
        optima_list.setAdapter(aAdapter);
        optima_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), optima_list, new ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Intent app = new Intent(OptimaActivity.this, AppOptima.class);
                AppsOptima appsOptima = optimaList.get(position);
                String appId = appsOptima.getId();
                app.putExtra(Config.APP_ID, appId);
                startActivity(app);
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));

        getJSON();

    }

    private void showApps()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject((JSON_STRING));
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_APP_ID);
                String obj = jo.getString(Config.TAG_APP_OBJ_ID);
                String initiator_name = jo.getString(Config.TAG_APP_INITIATOR_NAME);
                String reason = jo.getString(Config.TAG_APP_REASON);

                AppsOptima appsOptima = new AppsOptima();
                appsOptima.setId(id);
                appsOptima.setObj(obj);
                appsOptima.setInitiator_name(initiator_name);
                appsOptima.setReason(reason);

                optimaList.add(appsOptima);

                aAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

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
                loading = ProgressDialog.show(OptimaActivity.this, "Загрузка", "Подождите...", false, false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL_OPTIMA);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public interface ClickListener
    {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {
        private GestureDetector gestureDetector;
        private OptimaActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OptimaActivity.ClickListener clickListener)
        {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
        {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {
        }
    }


    public void initToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.optima);
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
