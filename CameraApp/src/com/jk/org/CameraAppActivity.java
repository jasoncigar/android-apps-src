package com.jk.org;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraAppActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {

	SurfaceView cameraPreview;
	SurfaceHolder holder;
	Camera camera;

	ImageButton click;
	ImageButton save;
	ImageButton delete;

	PictureCallback jpeg;
	FileOutputStream fos;

	byte[] imageBytes;
	File root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		root = Environment.getExternalStorageDirectory();

		cameraPreview = (SurfaceView) findViewById(R.id.CameraPreviewContainer);
		holder = cameraPreview.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);

		click = (ImageButton) findViewById(R.id.click);
		click.setOnClickListener(this);

		save = (ImageButton) findViewById(R.id.save);
		save.setOnClickListener(this);
		save.setEnabled(false);

		delete = (ImageButton) findViewById(R.id.delete);
		delete.setOnClickListener(this);
		delete.setEnabled(false);

		jpeg = new PictureCallback() {

			public void onPictureTaken(byte[] data, Camera camera) {
				click.setEnabled(false);
				save.setEnabled(true);
				delete.setEnabled(true);
				imageBytes = data;
			}
		};

		cameraPreview.setFocusable(true);
		cameraPreview.setFocusableInTouchMode(true);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();

		Log.e("Camera", "SurfaceCreated");
		try {
			camera.setPreviewDisplay(holder);

		} catch (IOException e) {
			e.printStackTrace();
		}

		camera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.click) {

			camera.takePicture(null, null, jpeg);

		} else if (v.getId() == R.id.save) {
			String currentDateTimeString = DateFormat.getDateInstance().format(
					new Date());
			currentDateTimeString = root.getPath() + "/"
					+ currentDateTimeString + "," + System.currentTimeMillis()
					+ ".jpeg";

			try {
				fos = new FileOutputStream(new File(currentDateTimeString));
				fos.write(imageBytes);
				fos.close();
				camera.startPreview();
				click.setEnabled(true);
				save.setEnabled(false);
				delete.setEnabled(false);
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(), "Image save Failed",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Image save Failed",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else if (v.getId() == R.id.delete) {
			if (imageBytes != null) {
				imageBytes = null;
				camera.startPreview();
				click.setEnabled(true);
				save.setEnabled(false);
				delete.setEnabled(false);
				Toast.makeText(getApplicationContext(), "Image Deleted",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}