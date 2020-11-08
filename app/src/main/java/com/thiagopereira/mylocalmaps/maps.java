package com.thiagopereira.mylocalmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class maps extends FragmentActivity implements OnMapReadyCallback, LocationListener {
	private final LatLng VICOSA = new LatLng(-20.7589588, -42.8910717);
	private final LatLng NATAL = new LatLng(-23.7715005, -46.6898259);
	private final LatLng DPTO = new LatLng(-20.764977, -42.8706477);
	private GoogleMap map;
	private Location location;
	public LocationManager lm;
	public Criteria criteria;
	public String provider;
	public int TEMPO_REQUISICAO_LATLONG = 5000;
	public int DISTANCIA_MIN_METROS = 0;
	public static final int LOCATION_PERMISSION = 1;

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

			if ((ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
					(ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

				// verifica se precisa explicar para o usuário a necessidade da permissão
				if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) ||
						(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION))) {

					//explica para o usuário a necessidade da permissão caso ele já tenha negado pelo menos uma vez
					Toast.makeText(getBaseContext(),"Permita o uso do serviço de localização para rastrear este aparelho!",Toast.LENGTH_LONG).show();

					//pede permissão
					ActivityCompat.requestPermissions(this,
							new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
							LOCATION_PERMISSION);

					Log.i("PERMISSION","Devo dar explicação");

				} else {

					// Pede a permissão direto a primeira vez que o usuário tentar usar o recurso.
					ActivityCompat.requestPermissions(this,
							new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
							LOCATION_PERMISSION);

					Log.i("PERMISSION","Pede a permissão");
					// LOCATION_PERMISSION é uma constante declarada para ser usada no callback da resposta da permissão
				}
			} else {
				Log.i("PERMISSION","Já tenho essa permissão");
			}
			lm.requestLocationUpdates(provider, TEMPO_REQUISICAO_LATLONG, DISTANCIA_MIN_METROS, (LocationListener) this);
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
	}

	@Override
	protected void onDestroy() {
		//interrompe o Location Manager
		lm.removeUpdates((LocationListener) this);
		Log.w("PROVEDOR","Provedor " + provider + " parado!");
		super.onDestroy();
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

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
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
				local = new LatLng(location.getLatitude(), location.getLongitude());
				map.addMarker(new MarkerOptions()
					.position(local).title("Minha localização atual")
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