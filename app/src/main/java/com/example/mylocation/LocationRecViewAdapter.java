package com.example.mylocation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.mylocation.model.UserSelectedLocation;

/**
 * Used for displaying user locations
 */
public class LocationRecViewAdapter extends RecyclerView.Adapter<LocationRecViewAdapter.ViewHolder>{

    private ArrayList<UserSelectedLocation> userSelectedLocations = new ArrayList<>();
    private Context context;

    public LocationRecViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * this method is used to create view holder
     * @param parent - parent view group
     * @param viewType - view type
     * @return - ViewHolder with all the list of location
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * this method is used to perform action on the user selected locations like viewing details or deleting the locations
     * @param holder - viewHolder
     * @param position - position of the user selected location
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //show location name in the user's location list
        holder.locationName.setText(userSelectedLocations.get(position).getLocationName());

        //show weather button in the user's location list, which will lead the user to the weather page
        holder.locationWeather.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowWeatherActivity.class);
            intent.putExtra("city", userSelectedLocations.get(position).getLocationName());
            context.startActivity(intent);
        });

        //show map button in the user's location list, which will lead the user to the map page
        holder.locationMap.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowMapActivity.class);
            UserSelectedLocation loc = userSelectedLocations.get(position);
            intent.putExtra("city", loc.getLocationName());
            intent.putExtra("longitude", loc.getLongitude());
            intent.putExtra("latitude", loc.getLatitude());
            context.startActivity(intent);
        });

        //remove location button in the user's location list, which will remove the location if clicked
        holder.locationImageRemove.setOnClickListener(view -> {
            userSelectedLocations.remove(position);
            notifyItemRemoved(position);
        });

    }

    /**
     * to get count of user selected locations
     * @return -  count of user selected locations
     */
    @Override
    public int getItemCount() {
        return userSelectedLocations.size();
    }

    /**
     * this method is used to set the user selected locations
     * @param userSelectedLocations - list of user selected locations
     */
    public void setUserSelectedLocations(ArrayList<UserSelectedLocation> userSelectedLocations) {
        this.userSelectedLocations = userSelectedLocations;
        notifyDataSetChanged();
    }

    /**
     * Used to display buttons for location details and location deletion for each location on main activity
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationName;
        private ImageButton locationImageRemove;
        private Button locationWeather;
        private Button locationMap;

        /**
         * Parameterized constructor for initializing view holder
         * @param itemView = item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            locationImageRemove = itemView.findViewById(R.id.locationImageRemove);
            locationWeather = itemView.findViewById(R.id.locationWeather);
            locationMap = itemView.findViewById(R.id.locationMap);

        }
    }
}