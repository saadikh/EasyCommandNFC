package cnt.nfc.mbds.fr.easycommandnfc.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cnt.nfc.mbds.fr.easycommandnfc.R;


public class CategoryRecycleAdapter extends RecyclerView.Adapter<CategoryRecycleAdapter.MyViewHodler>{
    private List<String> mDataset;
    private Context context;

    public CategoryRecycleAdapter(Context context,List<String> mDataset) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        MyViewHodler myViewHodler = new MyViewHodler(v);
        return myViewHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHodler myViewHodler, int position) {
        myViewHodler.category.setText(mDataset.get(position));
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    class MyViewHodler extends RecyclerView.ViewHolder {
        TextView category;
        public MyViewHodler(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
        }
    }
}
