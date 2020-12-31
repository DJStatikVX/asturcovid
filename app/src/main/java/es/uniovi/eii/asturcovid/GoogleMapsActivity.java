package es.uniovi.eii.asturcovid;

// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

import static es.uniovi.eii.asturcovid.MainActivity.AREA_SANITARIA_SELECCIONADA;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class GoogleMapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private AreaSanitaria area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        //Recepción datos como activity secundaria
        Intent intentAreaSanitaria = getIntent();
        area = intentAreaSanitaria .getParcelableExtra(AREA_SANITARIA_SELECCIONADA);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Funcionalidad del ExtendedFloatingActionButton
        ExtendedFloatingActionButton eFab = findViewById(R.id.extended_fab);
        eFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitarWeb(area.getHospital().getWeb_hospital());
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // en la ActionBar activar una flecha para volver hacia atrás
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng posHospital = new LatLng(area.getHospital().getLatitud(), area.getHospital().getLongitud());
        googleMap.addMarker(new MarkerOptions()
                .position(posHospital)
                .title(area.getHospital().getNombre_hospital())
                .snippet(area.getHospital().getDireccion_hospital()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posHospital,15.5f));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void visitarWeb(String urlWebHospital) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlWebHospital)));
    }
}