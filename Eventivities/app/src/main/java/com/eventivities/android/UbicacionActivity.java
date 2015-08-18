package com.eventivities.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class UbicacionActivity extends BaseActivity {

	protected static final int VALENCIA = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubicacion);
		
		Spinner cuidades = (Spinner) findViewById(R.id.spinnerCambiar);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.ciudades,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cuidades.setAdapter(adapter);
		
		final Button buttonMaps = (Button) findViewById(R.id.buttonMaps);		
		buttonMaps.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UbicacionActivity.this, MapsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));				
			}
		});
	}
	
	@Override
	protected void onResume() {
		SharedPreferences prefs = getSharedPreferences("UbicacionPreferences", Context.MODE_PRIVATE);
		int ciudad = prefs.getInt("ubicacionActual", VALENCIA);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCambiar);
		spinner.setSelection(ciudad);

		super.onResume();
	}

	@Override
	protected void onPause() {
		SharedPreferences prefs = getSharedPreferences("UbicacionPreferences", Context.MODE_PRIVATE);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCambiar);
		int ciudad = spinner.getSelectedItemPosition();
		
		Editor editor = prefs.edit();
		editor.putInt("ubicacionActual", ciudad);
		editor.commit();
		super.onPause();
	}
	
	
}
