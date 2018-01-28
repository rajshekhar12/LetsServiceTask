package raj.com.letsservicetask;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by prakash on 1/27/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context gContext;
    ArrayList<LogObject> gArrayList;

    public MyAdapter(Context gContext, ArrayList<LogObject> gArrayList) {
        this.gContext = gContext;
        this.gArrayList = gArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View lView= LayoutInflater.from(gContext).inflate(R.layout.data_xml,parent,false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.TvLat.setText(gArrayList.get(position).getLat());
        holder.TvLng.setText(gArrayList.get(position).getLng());
        holder.TvTime.setText(gArrayList.get(position).getDateAndTime());

    }

    @Override
    public int getItemCount() {
        return gArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView TvLat,TvLng,TvTime;
        public ViewHolder(View itemView) {
            super(itemView);

            TvLat=(AppCompatTextView)itemView.findViewById(R.id.tv_lat);
            TvLng=(AppCompatTextView)itemView.findViewById(R.id.tv_lng);
            TvTime=(AppCompatTextView)itemView.findViewById(R.id.tv_time);

        }

    }
}
