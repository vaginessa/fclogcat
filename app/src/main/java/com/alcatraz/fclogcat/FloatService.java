package com.alcatraz.fclogcat;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.view.View.*;
import android.view.WindowManager.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.util.*;
import android.widget.SeekBar.*;

public class FloatService extends Service
{
	public final static String ADD_TAG="ADDfhhff";
	WindowManager wm;
	WindowManager.LayoutParams pa;
	List<View> rls;
	Recgg hh;
	int index_overall=0;
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
	}
	public void regist(){
		hh=new Recgg();
		IntentFilter ifil=new IntentFilter();
		ifil.addAction(ADD_TAG);
		registerReceiver(hh,ifil);
	}
	@Override
	public void onStart(Intent intent, int startId)
	{
		wm=(WindowManager) getSystemService(WINDOW_SERVICE);
		rls=new ArrayList<View>();
		regist();
		// TODO: Implement this method
		super.onStart(intent, startId);
	}
	public Drawable getIcon(String pkg){
		PackageManager pm=getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}

	}

	@Override
	public void onDestroy()
	{
		unregisterReceiver(hh);
		// TODO: Implement this method
		super.onDestroy();
	}
	
	public List<String> initData(String path,String pkg){
		List<String> data=new ArrayList<String>();
		File f=new File(path);
		try{
			FileInputStream fis=new FileInputStream(f);
			if(fis!=null){
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader reader=new BufferedReader(isr);
				String exc="";
				String cls="";
				String line;
				int i=0;
				boolean hasGotExc=false;
				while((line=reader.readLine())!=null){
					data.add(line+"																																													");
					if(i>=2&&!hasGotExc){
						if(line.contains("Exception")){
							exc=LogCatAnalyser.getException(line);
							hasGotExc=true;
						}
					}
					if(i>=2){
						if(line.contains("at "+pkg)){
							String[] pro_1=line.split(" ");
							cls=pro_1[pro_1.length-1]+"\n";
						}
					}
					i++;
				}
				data.add(exc);
				data.add(cls);
				fis.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
	public void createFloat(final int index,Intent i){
		String label=i.getStringExtra("label");
		String path=i.getStringExtra("path");
		String pkg=i.getStringExtra("pkg");
		List<String> read=initData(path,pkg);
		String line_=read.get(read.size()-1);
		String excp=read.get(read.size()-2);
		read.remove(read.size()-1);
		read.remove(read.size()-2);
		SharedPreferences spf=getSharedPreferences(getPackageName()+"_preferences",MODE_PRIVATE);
		Set<String> hldb=spf.getStringSet("hldb",SpfConstants.getDefSet());
		String hlb=spf.getString("hb","#9fa8da");
		pa=new WindowManager.LayoutParams();
		pa.type=LayoutParams.TYPE_SYSTEM_ALERT; 
        pa.format=PixelFormat.RGBA_8888;
        pa.flags=LayoutParams.FLAG_NOT_FOCUSABLE;
        pa.gravity=Gravity.LEFT|Gravity.TOP;       
        pa.x=0;
        pa.y=0;
        pa.width=WindowManager.LayoutParams.WRAP_CONTENT;
        pa.height=WindowManager.LayoutParams.WRAP_CONTENT;
		LayoutInflater li=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View root=li.inflate(R.layout.floats,null);
		final LinearLayout ll1=(LinearLayout) root.findViewById(R.id.floatwindowLinearLayout1);
		final ImageButton imgb_tog=(ImageButton) root.findViewById(R.id.floatwindowImageButton1);
		ImageButton imgb_side=(ImageButton) root.findViewById(R.id.floatbarImageButton1);
		final DrawerLayout dl=(DrawerLayout) root.findViewById(R.id.floatwindowDrawerLayout1);
		ListView main=(ListView) root.findViewById(R.id.floatwindowListView1);
		ListViewAdapter lva=new ListViewAdapter(this,read,main,pkg,hldb,hlb,false);
		main.setAdapter(lva);
		SeekBar sb=(SeekBar) root.findViewById(R.id.floatsideSeekBar1);
		sb.setProgress((int)ll1.getAlpha()*100);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3)
				{

					if(p2!=0){
						ll1.setAlpha(p2/100f);
					}else{
						ll1.setAlpha(1);
						p1.setProgress(100);
					}

					// TODO: Implement this method
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}
			});
		TextView app=(TextView) root.findViewById(R.id.floatsideTextView1);
		TextView exc=(TextView) root.findViewById(R.id.floatsideTextView2);
		TextView line=(TextView) root.findViewById(R.id.floatsideTextView3);
		TextView main_t=(TextView) root.findViewById(R.id.floatbarTextView1);
		Button btn=(Button) root.findViewById(R.id.floatsideButton1);
		app.setText(path);
		exc.setText(excp);
		line.setText(line_);
		main_t.setText(label);
		imgb_tog.setImageDrawable(getIcon(pkg));
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					wm.removeView(rls.get(index));
					// TODO: Implement this method
				}
			});
		imgb_tog.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(ll1.getVisibility()==View.GONE){
						ll1.setVisibility(View.VISIBLE);
					}else{
						ll1.setVisibility(View.GONE);
					}
					// TODO: Implement this method
				}
			});
		imgb_side.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(!dl.isDrawerOpen(Gravity.LEFT)){
						dl.openDrawer(Gravity.LEFT);
					}
					// TODO: Implement this method
				}
			});
		dl.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.EXACTLY));
        root.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        imgb_tog.setOnTouchListener(new OnTouchListener() 
			{

				@Override
				public boolean onTouch(View v, MotionEvent event) 
				{
					if(ll1.getVisibility()==View.VISIBLE){
						pa.x=(int) event.getRawX()+imgb_tog.getMeasuredWidth()/2-100-dl.getWidth();
						pa.y=(int) event.getRawY()-imgb_tog.getMeasuredHeight()/2-60;
						wm.updateViewLayout(root,pa);
					}else{
						pa.x=(int) event.getRawX()+imgb_tog.getMeasuredWidth()/2-100;
						pa.y=(int) event.getRawY()-imgb_tog.getMeasuredHeight()/2-60;
						wm.updateViewLayout(root,pa);
					}
					return false;
				}
			});	
			rls.add(root);
		wm.addView(root,pa);
		index_overall++;
	}
	class Recgg extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context p1, Intent p2)
		{
			if(p2.getAction().equals(ADD_TAG)){
			createFloat(index_overall,p2);
			}
			// TODO: Implement this method
		}
		
		
	}
}
