package com.thiagopereira.mylocalmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
	public LocationManager lm;
	public Criteria criteria;
	public String provider;
	public int TEMPO_REQUISICAO_LATLONG = 5000;
	public int DISTANCIA_MIN_METROS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

		//Location Manager
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();

		//Testa se o aparelho tem GPS
		PackageManager packageManager = getPackageManager();
		boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

		//Estabelece critério de precisão
		if(hasGPS) {
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
		} else {
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		//Obtem melhor provedor habilitado com o critério estabelecido
		provider = lm.getBestProvider(criteria, true);

		if(provider == null) {
			Log.e("PROVEDOR", "Nenhum provedor encontrado!");
		} else {
			Log.i("PROVEDOR", "Está sendo utilizado o provedor: " + provider);

			//Obtem atualizações de posição
			if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			lm.requestLocationUpdates(provider, TEMPO_REQUISICAO_LATLONG, DISTANCIA_MIN_METROS, (LocationListener) this);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		map.addMarker(new MarkerOptions().position(VICOSA).title("Meu apartamento Viçosa"));
		map.addMarker(new MarkerOptions().position(NATAL).title("Minha casa em São Paulo"));
		map.addMarker(new MarkerOptions().position(DPTO).title("Departamento de informática"));

		Intent it = getIntent();
		String localName = it.getStringExtra("local");

		LatLng local = null;

		switch(localName) {
			case "natal":
				local = NATAL;
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case "vicosa":
				local = VICOSA;
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case "depto":
				local = DPTO;
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			default:
				break;
		}

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(local, 16);
		map.animateCamera(update);
	}

	public void setLocal(View v) {
		final String tag = v.getTag().toString();

		LatLng local = null;

		switch(tag) {
			case "natal":
				local = NATAL;
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case "vicosa":
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				local = VICOSA;
				break;
			case "depto":
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				local = DPTO;
				break;
			case "local":
				map.addMarker(new MarkerOptions()
					.position(VICOSA).title("AP")
					.snippet("Aqui")
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				break;
			default:
				return;
		}

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(local, 16);
		map.animateCamera(update);
	}
}