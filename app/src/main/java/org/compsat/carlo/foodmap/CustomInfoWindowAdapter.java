package org.compsat.carlo.foodmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    public View mView;
    public Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.marker_info, null);
    }

    public void renderWindow(Marker m, View view){
        String rName = m.getTitle();
        String rAddress = m.getSnippet();

        TextView tvName = view.findViewById(R.id.restaurant_name);
        TextView tvAddress = view.findViewById(R.id.restaurant_address);

        tvName.setText(rName);
        tvAddress.setText(rAddress);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker, mView);
        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker, mView);
        return mView;
    }
}
