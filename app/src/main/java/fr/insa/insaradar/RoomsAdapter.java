package fr.insa.insaradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    ArrayList<RoomModel> rooms;
    private final RecyclerViewListener recyclerViewListener;
    Context context;

    public RoomsAdapter(ArrayList<RoomModel> rooms, Context context, RecyclerViewListener recyclerViewListener) {
        this.rooms = rooms;
        this.context = context;
        this.recyclerViewListener = recyclerViewListener;
    }

    @NonNull
    @Override
    public RoomsAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room, parent, false);
        return new RoomsAdapter.RoomViewHolder(view, recyclerViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.RoomViewHolder holder, int position) {
        RoomModel room = rooms.get(position);
        holder.roomTextView.setText(rooms.get(position).getName());
        holder.availabilityTextView.setText(rooms.get(position).getAvailability());
        holder.descriptionTextView.setText(rooms.get(position).getDescription());
        boolean visible = room.isVisible();
        holder.expandedLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomTextView, availabilityTextView, descriptionTextView;
        ConstraintLayout expandedLayout;

        public RoomViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);
            roomTextView = itemView.findViewById(R.id.roomTextView);
            availabilityTextView = itemView.findViewById(R.id.roomAvailability);
            descriptionTextView = itemView.findViewById(R.id.roomDescription);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandedLayout.getVisibility() == View.VISIBLE) {
                        expandedLayout.setVisibility(View.GONE);
                    } else {
                        expandedLayout.setVisibility(View.VISIBLE);
                    }

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