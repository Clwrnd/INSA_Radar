package fr.insa.insaradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BuildingsAdapter extends RecyclerView.Adapter<BuildingsAdapter.BuildingViewHolder> {
    ArrayList<BuildingModel> buildings;
    private final RecyclerViewListener recyclerViewListener;
    Context context;

    public BuildingsAdapter(ArrayList<BuildingModel> buildings, Context context, RecyclerViewListener recyclerViewListener) {
        this.buildings = buildings;
        this.context = context;
        this.recyclerViewListener = recyclerViewListener;
    }

    @NonNull
    @Override
    public BuildingsAdapter.BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Cette méthode est appelée pour inflater le layout (donner une apparence à chaque élément de la liste)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_building, parent, false);
        return new BuildingsAdapter.BuildingViewHolder(view, recyclerViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingsAdapter.BuildingViewHolder holder, int position) {
        holder.buildingTextView.setText(buildings.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class BuildingViewHolder extends RecyclerView.ViewHolder {
        TextView buildingTextView;

        public BuildingViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);
            buildingTextView = itemView.findViewById(R.id.buildingTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }
}
