package keni.itilium.Optima;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import keni.itilium.R;

/**
 * Created by Keni on 01.06.2016.
 */
public class OptimaAppsAdapter extends RecyclerView.Adapter<OptimaAppsAdapter.MyViewHolder> {

private List<AppsOptima> appsOptimaList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView obj, initiator_name, reason;

    public MyViewHolder(View view)
    {
        super(view);
        obj = (TextView) view.findViewById(R.id.obj);
        initiator_name = (TextView) view.findViewById(R.id.initiator_name);
        reason = (TextView) view.findViewById(R.id.reason);
    }
}


    public OptimaAppsAdapter(List<AppsOptima> appsOptimaList)
    {
        this.appsOptimaList = appsOptimaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_apps_optima, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        AppsOptima apps = appsOptimaList.get(position);
        holder.obj.setText(apps.getObj());
        holder.initiator_name.setText(apps.getInitiator_name());
        holder.reason.setText(apps.getReason());
    }

    @Override
    public int getItemCount()
    {
        return appsOptimaList.size();
    }


}
