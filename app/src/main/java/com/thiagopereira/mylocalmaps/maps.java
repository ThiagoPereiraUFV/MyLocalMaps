package com.thiagopereira.mylocalmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class maps extends FragmentActivity implements OnMapReadyCallback {
	private final LatLng VICOSA = new LatLng(-20.752946, -42.879097);
	private final LatLng NATAL = new LatLng(-21.12881, -42.374247);
	private final LatLng DPTO = new LatLng(-21.109725, -42.381738);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		/*
		map.addMarker(new MarkerOptions()
			.position(VICOSA).title("AP")
			.snippet("Aqui")
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/

		map.addMarker(new MarkerOptions().position(VICOSA).title("Meu apartamento Viçosa"));
		map.addMarker(new MarkerOptions().position(NATAL).title("Minha casa em São Paulo"));
		map.addMarker(new MarkerOptions().position(DPTO).title("Departamento de informática"));
	}

	public void setLocal(View v) {
		final String tag = v.getTag().toString();

		LatLng local = null;

		switch(tag) {
			case "natal":
				local = NATAL;
				break;
			case "vicosa":
				local = VICOSA;
				break;
			case "depto":
				local = DPTO;
				break;
			case "local":
				break;
			default:
				return;
		}

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(local, 16);
		map.animateCamera(update);
	}
}