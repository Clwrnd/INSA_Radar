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
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view, recyclerViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomModel room = rooms.get(position);
        holder.roomTextView.setText(room.getName());
        holder.availabilityTextView.setText(room.getAvailability());
        holder.descriptionTextView.setText(room.getDescription());
        boolean visible = room.isVisible();
        holder.expandedLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomTextView, descriptionTextView, availabilityTextView;
        ConstraintLayout expandedLayout;

        public RoomViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);
            roomTextView = itemView.findViewById(R.id.roomTextView);
            descriptionTextView = itemView.findViewById(R.id.roomDescription);
            availabilityTextView = itemView.findViewById(R.id.roomAvailability);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Collapse all rooms
                        for (RoomModel room : rooms) {
                            room.collapse();
                        }

                        // Expand the clicked room
                        RoomModel clickedRoom = rooms.get(position);
                        clickedRoom.setVisible(true);

                        // Notify the adapter to update the views
                        notifyDataSetChanged();

                        // Notify the listener
                        listener.onItemClicked(position);
                    }
                }
            });
        }
    }
}