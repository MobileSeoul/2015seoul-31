package com.ku.seoultrace;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.parse.Parse;

public class ParseApplication extends Application {
	private DisplayImageOptions options = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "D3j4RiKAJsXR6Jd0ZfuBJpbkmFhMIUzbEmiwnURl", "FZERsE5uwpoEXwxEeCII3gwEu9FsaYoNbkckPm2l");
        this.initImageLoader(getApplicationContext());
    }
    
    private void initImageLoader(Context context)
	{
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.MAX_PRIORITY)
			.denyCacheImageMultipleSizesInMemory()
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			.diskCacheSize(50 * 1024 * 1024) // 50 Mb
			.tasksProcessingOrder(QueueProcessingType.LIFO)
//			.writeDebugLogs() // Remove for release app
			.build();
		
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions getDisplayImageOptions()
	{
		if(this.options == null)
		{
			this.options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.icon_store)
			.showImageForEmptyUri(R.drawable.icon_store)
			.showImageOnFail(R.drawable.icon_store) 
			.resetViewBeforeLoading(false) 
			//.delayBeforeLoading(1000)
			//.preProcessor(...)
			//.postProcessor(...)
			//.extraForDownloader(...)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565)
			//.decodingOptions(...)
			.displayer(new SimpleBitmapDisplayer())
			.handler(new Handler())
			.cacheInMemory(true)
			.cacheOnDisk(true) 
			.considerExifParams(true)
			.build();
		}
		
		return this.options;
	}
	
	public void removeFromCache(String imageUri)
	{
		DiskCacheUtils.removeFromCache(imageUri, ImageLoader.getInstance().getDiskCache());
		
		MemoryCacheUtils.removeFromCache(imageUri, ImageLoader.getInstance().getMemoryCache());
	}
 
}