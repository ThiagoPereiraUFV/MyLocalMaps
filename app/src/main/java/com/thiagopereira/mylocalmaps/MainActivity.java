package com.thiagopereira.mylocalmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

		switch(aux) {
			case "Minha casa na cidade natal":
				Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
				break;
			case "Minha casa em Viçosa":
				Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
				break;
			case "Meu departamento":
				Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
				break;
			case "Fechar aplicação":
				finish();
		}
	}
}