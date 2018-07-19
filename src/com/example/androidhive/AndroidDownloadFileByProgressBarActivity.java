package com.example.androidhive;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;







import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class AndroidDownloadFileByProgressBarActivity extends Activity {
	
	String get_result;

	// button to show progress dialog
//	Button btnShowProgress;
	
	// Progress Dialog
	private ProgressDialog pDialog;
	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 
	
    String Icerik="";
    String Icerik2="";
    
    File SDCardRoot;
    
    File SDCardRootCheck;
    
    File SDCardRoot_frontpage;
    
    File SDCardRootCheck_frontpage;
    
    String device_id;
    
    SharedPreferences prefs;
    
	String get_url = ""; //Icerik
	String get_name = ""; //Icerik2
    String get_resim;
	
	String get_type_of_download;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
/*		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		try {
			// REQUIRES ROOT

			Build.VERSION_CODES vc = new Build.VERSION_CODES();
			Build.VERSION vr = new Build.VERSION();
			String ProcID = "79"; // HONEYCOMB AND OLDER

			// v.RELEASE //4.0.3
			if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
				ProcID = "42"; // ICS AND NEWER
			}

			// REQUIRES ROOT
			Process proc = Runtime.getRuntime().exec(
					new String[] {
							"su",
					    	"-c",
							"service call activity " + ProcID
								+ " s16 com.android.systemui" }); // WAS																		// 79
			proc.waitFor();

		} catch (Exception ex) {
			// Toast.LENGTH_LONG).show();
		}*/

		
		setContentView(R.layout.main);
		
		
		
		

//		// show progress bar button
//		btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
		// Image view to show image after downloading
		my_image = (ImageView) findViewById(R.id.my_image);


		//Get Device IMEI number
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		device_id = telephonyManager.getDeviceId();
		Log.i("IMEI NUMBER", device_id);
		
		SDCardRoot_frontpage = new File("/mnt/sdcard/ipharmafrontpage/");
		if (!SDCardRoot_frontpage.exists()) {
			SDCardRoot_frontpage.mkdir();
		}
	
		
		Context con;
		try {
			con = getApplicationContext().createPackageContext("com.androidhive.pushnotifications", Context.MODE_WORLD_WRITEABLE);
			
			SharedPreferences pref = con.getSharedPreferences("app_pdownload", Context.MODE_WORLD_READABLE);
			get_name = pref.getString("get_message_ad", "alınamdı");
			Log.i("get_Name_shared", get_name);
			get_url = pref.getString("get_message_link", "alınamdı");
			Log.i("get_Link_shared", get_url);
			
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		try {
			con = getApplicationContext().createPackageContext("com.androidhive.pushnotifications", Context.MODE_WORLD_WRITEABLE);
			
			SharedPreferences pref_download_type = con.getSharedPreferences("download_typem", Context.MODE_WORLD_READABLE);
			get_type_of_download = pref_download_type.getString("get_message_download", "alınamdı");
			Log.i("get_Name_shared", get_name);

			
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			con = getApplicationContext().createPackageContext("com.androidhive.pushnotifications", Context.MODE_WORLD_WRITEABLE);
			
			SharedPreferences pref_get_resim = con.getSharedPreferences("resim_yuklu", Context.MODE_WORLD_READABLE);
			get_resim = pref_get_resim.getString("resim", "alınamdı");
			Log.i("get_Name_shared", get_resim);
	
			
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
//		AsyncCallBanner banner = new AsyncCallBanner();
//		try {
//			banner.execute().get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
//		Log.i("Icerik feefe", Icerik);
//		Log.i("Icerik2 cscs", Icerik2);
		
		
//		if(get_resim.equals("Resim_Yuklu")){
//			
//			AsyncCallBanner_sendata_work work = new AsyncCallBanner_sendata_work();
//			work.execute();
//		}
		
		
		/**
		 * Check String has contains video or picture
		 */
		if(get_type_of_download.equals("backendpage")){	     
		
		if(get_name.contains("mp4")){
			
			
			SDCardRoot = new File("/mnt/sdcard/" + "/videos");
			
			if (!SDCardRoot.exists()) {
				SDCardRoot.mkdir();
			}
			
			final File[] videos = SDCardRoot.listFiles();
			for (int i = 0; i < videos.length; i++) {
				videos[i].setExecutable(true);
				Log.d("blaaa", "............." + videos[i].canExecute());
				Log.d("blaaa", "............." + videos[i].getAbsolutePath());
			}
			
			
			SDCardRootCheck = new File("/mnt/sdcard/" + "/videos/" + get_name );
			
			
			if (!SDCardRootCheck.exists()) {
				
				// starting video downloading Async Task
		
				new DownloadTask(this).execute();

			
			} else if (SDCardRootCheck.exists()) {

                   Toast.makeText(getApplicationContext(), "Video indirilmiş", Toast.LENGTH_SHORT).show();
                 
			}
			
			
		}
		
		 else{
				// starting photo downloading Async Task
			
					new DownloadFileFromURL().execute(get_url);
			

//				
//				
//				AsyncCallBanner_sendata dene = new AsyncCallBanner_sendata();
//				dene.execute();
		  }
	}
	
	
	else if(get_type_of_download.equals("frontpage")){
		
		
		if(get_name.contains("mp4")){
			
			
			SDCardRoot_frontpage = new File("/mnt/sdcard/" + "ipharmafrontpage");
			
			if (!SDCardRoot_frontpage.exists()) {
				SDCardRoot_frontpage.mkdir();
			}
			
			final File[] videos = SDCardRoot_frontpage.listFiles();
			for (int i = 0; i < videos.length; i++) {
				videos[i].setExecutable(true);
				Log.d("blaaa", "............." + videos[i].canExecute());
				Log.d("blaaa", "............." + videos[i].getAbsolutePath());
			}
			
			
			SDCardRootCheck_frontpage = new File("/mnt/sdcard/" + "ipharmafrontpage/" + get_name );
			
			
			if (!SDCardRootCheck_frontpage.exists()) {
				
				// starting video downloading Async Task
		
				new DownloadTask_front_page(this).execute();

			
			} else if (SDCardRootCheck.exists()) {

                   Toast.makeText(getApplicationContext(), "Video indirilmiş", Toast.LENGTH_SHORT).show();
                 
			}
			
			
		}
		
		 else{
				// starting photo downloading Async Task
			
					new DownloadFileFromURL_front_page().execute(get_url);
			

//				
//				
//				AsyncCallBanner_sendata dene = new AsyncCallBanner_sendata();
//				dene.execute();
		  }
		
		
	}
	
	
	 }

	/**
	 * Showing Dialog
	 * */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}
	
	
	
	/**
	 * Background download Video for backendPage
	 */
	public class DownloadTask extends AsyncTask<Void, Void, String> {

		private final ProgressDialog progressDialog_m;

		private final Context context;

		/**
		 * 
		 * @param context
		 * @param pdfDoc
		 *            the document of the PDF
		 */
		public DownloadTask(Context context) {
			this.context = context;

			progressDialog_m = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			progressDialog_m.setCanceledOnTouchOutside(false);
			progressDialog_m.setMessage("Video Güncelleniyor...");
			progressDialog_m.setIndeterminate(true);
			progressDialog_m.show();
			

		}

		@Override
		protected String doInBackground(Void... arg0) {
			downloadFiles(get_url, get_name);
			return null;
			// download here
		}

		@Override
		protected void onPostExecute(final String result) {
			progressDialog_m.dismiss();
			
		   //Send Download_Message to the SendMessage web servis	
           Connect_send_data();
           Connect_send_data_work();
           
            //After go the MainAvtivity of PushNotification package
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.androidhive.pushnotifications");
			launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			launchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(launchIntent);
			android.os.Process.killProcess(android.os.Process.myPid());

		}
	}
	
	

	
	
	/**
	 * Download video method for BackendPage
	 */
	synchronized boolean downloadFiles(String link, String videoName) {
		try {
			URL url = new URL(link); // you can write here any link

			long startTime = System.currentTimeMillis();
			Log.d("VideoManager", "download begining");
			Log.d("VideoManager", "download url:" + url);
			Log.d("VideoManager", "downloaded file name:" + videoName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(new File(SDCardRoot,
					videoName));
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("VideoManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Background Async Task to download Photo for BackendPage
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();

	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            
	            // Output stream to write file
	            OutputStream output = new FileOutputStream("/sdcard/" + get_name);

	            byte data[] = new byte[1024];

	            long total = 0;

	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	                
	                // writing data to file
	                output.write(data, 0, count);
	            }

	            // flushing output
	            output.flush();
	            
	            // closing streams
	            output.close();
	            input.close();
	            
	            
	            
	        } catch (Exception e) {
	        	Log.e("Error: ", e.getMessage());
	        }
	        
	        
	        
	        
	        return null;
		}
		
		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(progress_bar_type);
			
			Connect_send_data();
			Connect_send_data_work();
			
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.androidhive.pushnotifications");
			launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	
			startActivity(launchIntent);
			android.os.Process.killProcess(android.os.Process.myPid());
			
			// Displaying downloaded image into image view
			// Reading image path from sdcard
//			String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
//			// setting downloaded into image view
//			my_image.setImageDrawable(Drawable.createFromPath(imagePath));
		}

	}
	
	
	
	
	/**
	 * Background download Video for FrontPage
	 */
	public class DownloadTask_front_page extends AsyncTask<Void, Void, String> {

		private final ProgressDialog progressDialog_m;

		private final Context context;

		/**
		 * 
		 * @param context
		 * @param pdfDoc
		 *            the document of the PDF
		 */
		public DownloadTask_front_page(Context context) {
			this.context = context;

			progressDialog_m = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			progressDialog_m.setCanceledOnTouchOutside(false);
			progressDialog_m.setMessage("Video Güncelleniyor...");
			progressDialog_m.setIndeterminate(true);
			progressDialog_m.show();
			
			deleteDirectory(SDCardRoot_frontpage);

			SDCardRoot_frontpage.mkdir();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			downloadFiles_front_page(get_url, get_name);
			return null;
			// download here
		}

		@Override
		protected void onPostExecute(final String result) {
			progressDialog_m.dismiss();
			
		   //Send Download_Message to the SendMessage web servis	
           Connect_send_data();
           Connect_send_data_work();
           
            //After go the MainAvtivity of PushNotification package
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.androidhive.pushnotifications");
//			launchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(launchIntent);
			android.os.Process.killProcess(android.os.Process.myPid());

		}
	}
	
	
	/**
	 * Download video method for FrontdPage
	 */
	synchronized boolean downloadFiles_front_page(String link, String videoName) {
		try {
			URL url = new URL(link); // you can write here any link

			long startTime = System.currentTimeMillis();
			Log.d("VideoManager", "download begining");
			Log.d("VideoManager", "download url:" + url);
			Log.d("VideoManager", "downloaded file name:" + videoName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(new File(SDCardRoot_frontpage,
					videoName));
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("VideoManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	/**
	 * Background Async Task to download Photo for FrontPage
	 * */
	class DownloadFileFromURL_front_page extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
			
			deleteDirectory(SDCardRoot_frontpage);

			SDCardRoot_frontpage.mkdir();
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();

	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            
	            // Output stream to write file
	            OutputStream output = new FileOutputStream("/sdcard/ipharmafrontpage/" + get_name);

	            byte data[] = new byte[1024];

	            long total = 0;

	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	                
	                // writing data to file
	                output.write(data, 0, count);
	            }

	            // flushing output
	            output.flush();
	            
	            // closing streams
	            output.close();
	            input.close();
	            
	            
	            
	        } catch (Exception e) {
	        	Log.e("Error: ", e.getMessage());
	        }
	        
	        
	        
	        
	        return null;
		}
		
		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(progress_bar_type);
			
			Connect_send_data();
			Connect_send_data_work();
			
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.androidhive.pushnotifications");
//			launchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	
			startActivity(launchIntent);
			android.os.Process.killProcess(android.os.Process.myPid());
			
			// Displaying downloaded image into image view
			// Reading image path from sdcard
//			String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
//			// setting downloaded into image view
//			my_image.setImageDrawable(Drawable.createFromPath(imagePath));
		}

	}
	

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	
	/**
	 * Send Message services
	 */
	private void Connect_send_data() {
		// TODO Auto-generated method stub

		JSONObject returndata = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://78.186.62.169:9666/svc.asmx/SendMessage");
		httppost.setHeader("Content-type", "application/json");
		JSONObject jsonparameter = new JSONObject();
		


		
		try {
				
			jsonparameter.put("Imei", device_id);
			jsonparameter.put("MSG", "CLIENT_DOWNLOAD_OK");
			jsonparameter.put("Par1", get_name);
			jsonparameter.put("Par2", "Download");
			
			
			
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {

			// jsonparameter.put("AnketID", "3");

			httppost.setEntity(new StringEntity(jsonparameter.toString(),
					"UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity);

			Log.i("@banner_responseString", "" + responseString);

			try {
//				returndata = new JSONObject(responseString);
//				String d = returndata.get("d").toString();
//				
//				Icerik = d;
//				

				returndata = new JSONObject(responseString);
				

				
     			 get_result = returndata.optString("d");
     			
     			Log.i("IMEI NUMBER", ""+get_result);
//				
			
//				
			

//                String al = get_result;
				

			} catch (JSONException e) {
 
			}

		} catch (Exception e) {

		}

	}
	
	/*
	 * Send Service Device is active
	 */
	
	private void Connect_send_data_work() {
		// TODO Auto-generated method stub

		JSONObject returndata = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://78.186.62.169:9666/svc.asmx/SendMessage");
		httppost.setHeader("Content-type", "application/json");
		JSONObject jsonparameter = new JSONObject();
		


		
		try {
				
			jsonparameter.put("Imei", device_id);
			jsonparameter.put("MSG", "CLIENT_UP_DEVICE");
			jsonparameter.put("Par1", "Live");
			jsonparameter.put("Par2", "UpDevice");
			
			
			
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {

			// jsonparameter.put("AnketID", "3");

			httppost.setEntity(new StringEntity(jsonparameter.toString(),
					"UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity);

			Log.i("@banner_responseString", "" + responseString);

			try {
//				returndata = new JSONObject(responseString);
//				String d = returndata.get("d").toString();
//				
//				Icerik = d;
//				

				returndata = new JSONObject(responseString);
				

				
     			 get_result = returndata.optString("d");
     			
     			Log.i("IMEI NUMBER", ""+get_result);


			} catch (JSONException e) {
 
			}

		} catch (Exception e) {

		}

	}
	
	
	private class AsyncCallBanner_sendata_work extends AsyncTask<Void, Void, String> {

//		ProgressDialog dialog;

		@Override
		protected String doInBackground(Void... params) {
			Log.i("TAG", "doInBackground");
			//Connect_send_data();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("TAG", "onPostExecute");
			// txt1.setText(Icerik);
			
			
			
			
			   //Send Download_Message to the SendMessage web servis	
	           Connect_send_data();
	           
	           
	            //After go the MainAvtivity of PushNotification package
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.androidhive.pushnotifications");
//				launchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(launchIntent);
				android.os.Process.killProcess(android.os.Process.myPid());
			    
			

			
	/*			else{
				
				Toast.makeText(getApplicationContext(), "Gitmedi", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}*/
			

		}

		@Override
		protected void onPreExecute() {
			Log.i("TAG", "onPreExecute");

		}

	}

	
	
}