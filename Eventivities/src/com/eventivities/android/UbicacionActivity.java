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

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class UbicacionActivity extends SherlockActivity {

	protected static final int VALENCIA = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubicacion);
		getSupportActionBar().setHomeButtonEnabled(true);
		
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

		supportInvalidateOptionsMenu(); 
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.general, menu);
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		boolean login = prefs.getBoolean("logIn", false);
		if(login)
			menu.findItem(R.id.menu_login).setTitle(prefs.getString("usuarioActual", getString(R.string.menu_login).toUpperCase()));
		else 
			menu.findItem(R.id.menu_login).setTitle(getString(R.string.menu_login));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(UbicacionActivity.this, LocalesActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.menu_login:
			startActivity(new Intent(UbicacionActivity.this, MiPerfilActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.menu_location:
			startActivity(new Intent(UbicacionActivity.this, UbicacionActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		}
		
		return super.onOptionsItemSelected(item);
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
