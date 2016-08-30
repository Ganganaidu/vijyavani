package com.switchsoft.vijayavani.imagedownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.switchsoft.vijayavani.util.Constants;

/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 */
public class ImageDownloader {
	
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 4; // 5MB
	
	private static final String LOG_TAG = "ImageDownloaderNew";
	ColorDrawable color = new ColorDrawable(android.R.color.transparent);
	Context mContext;
	
	public ImageDownloader(Context context){
		mContext = context;

	}
	/**
	 * Download the specified image from the Internet and binds it to the provided ImageView. The
	 * binding is immediate if the image is found in the cache and will be done asynchronously
	 * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
	 *
	 * @param url The URL of the image to download.
	 * @param imageView The ImageView to bind the downloaded image to.
	 */
	public void loadImage(String url, ImageView imageView) {
		//		progressBar.setVisibility(View.GONE);
		WeakReference<ImageView> imageReference= new WeakReference<ImageView>(imageView);
		if(mMemoryCache.get(url)!=null) {
			cancelPotentialDownload(url, imageView);
			imageReference.get().setImageBitmap(mMemoryCache.get(url));

		} else {
			forceDownload(url, imageReference.get());
		}
	}
	/*private Bitmap getBitmapFromCache(String url){
		final Bitmap bitmap = SingletonConstants.getInstance().mMemoryCache.get(url);
		if (bitmap != null) {
			// Bitmap found in cache
			return bitmap;
		} else {
			// Bitmap has been Garbage Collected
			SingletonConstants.getInstance().mMemoryCache.remove(url);
			return null;
		}
	}*/

	/**
	 * Same as download but the image is always downloaded and the cache is not used.
	 * Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(String url, ImageView imageView) {
		// State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
		if (url == null) {
			imageView.setImageDrawable(null);
			return;
		}
		try{
			if (cancelPotentialDownload(url, imageView)) {
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
				imageView.setImageDrawable(downloadedDrawable);
				task.execute(url);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no download in
	 * progress on this image view.
	 * Returns false if the download in progress deals with the same url. The download is not
	 * stopped in that case.
	 */
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView Any imageView
	 * @return Retrieve the currently active download task (if any) associated with this imageView.
	 * null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Bitmap downloadBitmap(String url) {
		final int IO_BUFFER_SIZE = 4 * 1024;

		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android");

		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode +
						" while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//					bmpFactoryOptions.inSampleSize=2;
					bmpFactoryOptions.inSampleSize=1;
					bmpFactoryOptions.inDither=false;                     //Disable Dithering mode
					bmpFactoryOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
					bmpFactoryOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
					bmpFactoryOptions.inTempStorage=new byte[16 * 1024];
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					return BitmapFactory.decodeStream(new FlushedInputStream(inputStream),null,bmpFactoryOptions);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}
	private Bitmap getBitmap(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(getFilename(), filename);
		Log.i(LOG_TAG, "url::  "+url);
		// Is the bitmap in our cache?
		Bitmap writeBitmap = BitmapFactory.decodeFile(f.getPath());
		if(writeBitmap != null){
			if(mMemoryCache.get(url)!=null){
				mMemoryCache.remove(url);
			}
			mMemoryCache.put(url, writeBitmap);
			return writeBitmap;
		}

		// Nope, have to download it
		try {
			writeBitmap = null;
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//			bmpFactoryOptions.inSampleSize=2;
			bmpFactoryOptions.inSampleSize=1;
			bmpFactoryOptions.inDither=false;                     //Disable Dithering mode
			bmpFactoryOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
			bmpFactoryOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
			bmpFactoryOptions.inTempStorage=new byte[16 * 1024];
			if(writeBitmap == null){
				writeBitmap = BitmapFactory.decodeStream(new FlushedInputStream(new URL(url).openConnection().getInputStream()),null,bmpFactoryOptions);
				writeFile(writeBitmap, f);
				if(mMemoryCache.get(url)!=null){
					mMemoryCache.remove(url);
				}
				mMemoryCache.put(url, writeBitmap);
			}

			return writeBitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.i(LOG_TAG, "Exception:: "+ex.getMessage());
			return null;
		}
	}
	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;

		Log.i("writeFile", "writeFile::::::::");

		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			if(bmp!=null){
				bmp=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally { 
			try { if (out != null ) out.close(); }
			catch(Exception ex) {} 
		}
	}
	/*
	 * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference;
		//		private final WeakReference<ProgressBar> progressBarReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			//			progressBarReference = new WeakReference<ProgressBar>(progressBar);
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {

			url = params[0];
			return getBitmap(url);
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if(bitmap!=null){
				if (imageViewReference != null) {
					ImageView imageView = imageViewReference.get();
					/*ProgressBar progressBar = null;
				if(progressBarReference!=null){
					progressBar = progressBarReference.get();
				}*/
					BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
					// Change bitmap only if this process is still associated with it
					// Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
					if ((this == bitmapDownloaderTask)) {
						BitmapDrawable drwable = new BitmapDrawable(bitmap);
						final TransitionDrawable td =
								new TransitionDrawable(new Drawable[] {
										color,
										drwable
								});

						imageView.setImageDrawable(td);
						td.startTransition(200);
						drwable = null;

						//					imageView.setImageBitmap(bitmap);
						/*if(progressBar!=null){
						progressBar.setVisibility(View.GONE);
					}*/
					}
				}
			}
		}
	}


	/**
	 * A fake Drawable that will be attached to the imageView while the download is in progress.
	 *
	 * <p>Contains a reference to the actual download task, so that a download task can be stopped
	 * if a new binding is required, and makes sure that only the last started download process can
	 * bind its result, independently of the download finish order.</p>
	 */
	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super(android.R.color.transparent);
			bitmapDownloaderTaskReference =
					new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}
	
	@SuppressLint("NewApi")
	public LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(DEFAULT_MEM_CACHE_SIZE) {
		/**
		 * Measure item size in bytes rather than units which is more practical for a bitmap
		 * cache
		 */
		@SuppressLint("NewApi")
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				return bitmap.getByteCount();
			}
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	};
	
	/** Returns the path of SD card and file name of the sound */
	private String getFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,Constants.Cache_Filename);

		if(!file.exists()){
			file.mkdirs();
		}

		return (file.getAbsolutePath());
	}

//	File menuDir = new File(android.os.Environment.getExternalStorageDirectory(),"Bulletin");

}