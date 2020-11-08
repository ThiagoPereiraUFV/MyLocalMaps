package com.thiagopereira.mylocalmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String menu[] = new String[]{"Minha casa na cidade natal", "Minha casa em Viçosa", "Meu departamento", "Fechar aplicação"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
		setListAdapter(arrayAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final String aux = l.getItemAtPosition(position).toString();
		final Intent it = new Intent(getBaseContext(), maps.class);

		switch(aux) {
			case "Minha casa na cidade natal":
				it.putExtra("local", "natal");
				break;
			case "Minha casa em Viçosa":
				it.putExtra("local", "vicosa");
				break;
			case "Meu departamento":
				it.putExtra("local", "depto");
				break;
			case "Fechar aplicação":
				finish();
		}

		startActivity(it);
	}
}