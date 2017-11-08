package cs.sci.ku.androidlab9;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng kasetsart = new LatLng(13.844632, 100.571841);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(kasetsart)
                                        .zoom(17)
                                        .bearing(90)
                                        .tilt(30)
                                        .build();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(new LatLng(13.844632, 100.571841)).title("Faculty of Science"));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Log.d("Long Click", "(" + latLng.latitude + ", " + latLng.longitude + ")");
                final View addDialog = getLayoutInflater().inflate(R.layout.dialog_add_marker, null);
                new AlertDialog.Builder(MapsActivity.this)
                                .setView(addDialog)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        TextView titleTextView = addDialog.findViewById(R.id.et_title);
                                        String title = titleTextView.getText().toString();
                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
                                        mMap.addMarker(markerOptions);

                                    }
                                })
                                .create()
                                .show();
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Log.d("onClickInfo", marker.getTitle());
                final View editDialog = getLayoutInflater().inflate(R.layout.dialog_add_marker, null);
                final TextView titleTextView = editDialog.findViewById(R.id.et_title);
                titleTextView.setText(marker.getTitle());
                new AlertDialog.Builder(MapsActivity.this)
                        .setView(editDialog)
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                marker.remove();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String title = titleTextView.getText().toString();
                                marker.setTitle(title);
                                marker.showInfoWindow();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
