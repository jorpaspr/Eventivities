package com.eventivities.android.servicioweb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageApi extends EventivitiesApi {

	private String imageNameAsJPG;

	public ImageApi(String imageNameAsJPG) {
		super("/img/"+imageNameAsJPG);

		this.imageNameAsJPG = imageNameAsJPG;
	}

	public Bitmap getBitMap() throws IOException{
		InputStream is = doConnection();

		Bitmap imgBitmap = BitmapFactory.decodeStream(is);
		
		saveImage(imgBitmap);
		
		return imgBitmap;
	}

	private void saveImage(Bitmap img) throws IOException{
		File directoryToSave = new File(Environment.getExternalStorageDirectory(), "eventivities/.cache");
		directoryToSave.mkdirs();
		
		File imageFile = new File(directoryToSave, imageNameAsJPG);
		
		FileOutputStream outStream = new FileOutputStream(imageFile);
		img.compress(Bitmap.CompressFormat.JPEG, 90,outStream);

		outStream.flush();
		outStream.close();
	}
}
