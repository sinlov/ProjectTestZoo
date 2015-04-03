package cn.com.incito.launcher;

import cn.com.incito.launcher.app.anim.AnimationCommon;
import cn.com.incito.launcher.app.system.ApplicationConnector;
import cn.com.incito.launcher.app.system.HomeListenByBroadcast;
import cn.com.incito.launcher.app.system.HomeListenByBroadcast.OnHomeBtnPressLitener;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollAdapter;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollAdapter.OnScrollAdapterClickListener;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollLayout;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollLayout.OnAddOrDeletePage;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollLayout.OnEditModeListener;
import cn.com.incito.launcher.widget.scrolllayout.InteractiveScrollLayout.OnPageChangedListener;
import cn.com.incito.launcher.widget.scrolllayout.MoveItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnAddOrDeletePage, OnPageChangedListener, OnEditModeListener, OnScrollAdapterClickListener{
	
	public final static int CONTAINER_ROW = 5;
	public final static int CONTAINER_COLUNM = 4;
	public final static String TAG = "Incito Launcher";
	public final static String APP_PACKAGE_NAME = "cn.com.incito.launcher";
	public final static int MSG_APP_AUTO_OPEN = 10000;
	
	private static String autoOpenPackageName = "cn.com.incito.tidestationhelper";
	private static String autoOpenPackageMain = "cn.com.incito.tidestationhelper.LoadingActivity";
	
	
	// 滑动控件的容器Container
	private InteractiveScrollLayout container;

	// Container的Adapter
	private InteractiveScrollAdapter itemsAdapter;
	
	// Container中滑动控件列表
	private List<MoveItem> mList;

	
	private HomeListenByBroadcast mHomeListen;
	
	private Myhanlder myHandler;
	
	private static class Myhanlder extends Handler{

		private static WeakReference<MainActivity> wrActivity;
		/**
		 * set target Activity to Weak reference and the customize handler be recycle timely.
		 *@param bewrMainActivity
		 */
		public Myhanlder(MainActivity bewrMainActivity) {
			wrActivity = new WeakReference<MainActivity>(bewrMainActivity);
		}
		@SuppressWarnings("unused")
		public WeakReference<MainActivity> get(){
			return wrActivity;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity mainAct = wrActivity.get();
			if (null != mainAct) {
				int what = msg.what;
				switch (what) {
				case MSG_APP_AUTO_OPEN:
					mainAct.skipToPullActivity(mainAct);
					break;

				default:
					break;
				}
			}
		}
		
	}
	
	private void skipToPullActivity(Context contex){
		if (ApplicationConnector.isLocalInstalledAPPByPM(contex, autoOpenPackageName)) {
			ApplicationConnector.openAppByAppInfoAtAnimation(contex, autoOpenPackageName, autoOpenPackageMain,R.anim.anim_enter, R.anim.anim_exit, Intent.FLAG_ACTIVITY_TASK_ON_HOME| Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w(TAG, "MainActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myHandler = new Myhanlder(this);
		initHomeListen();
		initUI();
		loadBackground();
	}
	
	@Override
	protected void onResume() {
		Log.w(TAG, "MainActivity onResume");
		this.skipToPullActivity(this);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
		super.onResume();
		//TODO
//		myHandler.obtainMessage(MSG_APP_AUTO_OPEN).sendToTarget();
		mHomeListen.start();
		container.snapToScreen(0,true);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.w(TAG, "MainActivity onRestart");
		super.onRestart();
//		getDataFromMidCache();
//		loadBackground();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.w(TAG, "MainActivity onPause");
		if(AnimationCommon.anim_in!=0 && AnimationCommon.anim_out!=0){
			super.overridePendingTransition(AnimationCommon.anim_in, AnimationCommon.anim_out);
			AnimationCommon.clear();
			}
		super.onPause();
//		getDataFromCache();
//		loadBackground();
		mHomeListen.stop();
	}
	
	@Override
	protected void onDestroy() {
		//TODO
		Log.w(TAG, "MainActivity onDestroy");
		super.onDestroy();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_HOME) {
//			//TODO
//			container.snapToScreen(0,true);
//			return true;
//		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			container.snapToScreen(0,true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initHomeListen(){
		mHomeListen = new HomeListenByBroadcast(this);
        mHomeListen.setOnHomeBtnPressListener( new OnHomeBtnPressLitener( ) {
            @Override
            public void onHomeBtnPress() {
            	Log.w(TAG, "MainActivity onHomeBtnPress");
                showToast("按下Home按键！");
            }
             
            @Override
            public void onHomeBtnLongPress() {
            	Log.w(TAG, "MainActivity onHomeBtnLongPress");
                showToast("----长按Home按键！----");
            }
        });
	}
	
	@Override
	protected void onUserLeaveHint() {
		Log.w(TAG, "MainActivity onUserLeaveHint");
		super.onUserLeaveHint();
	}
	
    private void showToast( String toastInfoStr ){
        Toast.makeText( this, toastInfoStr, Toast.LENGTH_SHORT).show();
    }
	

	/**
	 * 初始化界面
	 * @description 
	 * @author   tianran
	 * @createDate Mar 31, 2015
	 */
	private void initUI() {
		container = (InteractiveScrollLayout) findViewById(R.id.container);
		checkData();
		//初始化Container的Adapter
		itemsAdapter = new InteractiveScrollAdapter(this, mList);
		
		itemsAdapter.setOnScrollAdapterClickListener(this);
		//设置Container添加删除Item的回调
		container.setOnAddPage(this);
		//设置Container页面换转的回调，比如自第一页滑动第二页
		container.setOnPageChangedListener(this);
		//设置Container编辑模式的回调，长按进入修改模式
		container.setOnEditModeListener(this);
		//设置Adapter
		container.setSaAdapter(itemsAdapter);
		//可动态设置Container每页的列数
		container.setColCount(CONTAINER_COLUNM);
		//可动态设置Container每页的行数
		container.setRowCount(CONTAINER_ROW);
		//调用refreView绘制所有的Item
		container.refreView();
	}

	/**
	 * 如果没有缓存数据，则手动添加10条
	 * <p>根据drawable name获取对于的ID
	 * @description 
	 * @author   tianran
	 * @createDate Mar 31, 2015
	 */
	private void checkData(){
		if (mList == null || mList.size() == 0) {
			mList = new ArrayList<MoveItem>();
			for (int i = 1; i < 101; i++) {
				MoveItem item = new MoveItem();
				item.setImgdown(getDrawableId("item" + i + "_down", APP_PACKAGE_NAME));
				item.setImgurl(getDrawableId("item" + i + "_normal", APP_PACKAGE_NAME));
				item.setOrderId(i);
				item.setMid(i);
				mList.add(item);
			}
		}else {
			
		}
	}
	
	/**
	 * 通过包名获取绘制对象的资源ID
	 * @description 
	 * @author   tianran
	 * @createDate Mar 31, 2015
	 * @param name
	 * @param packageName
	 * @return
	 */
	private int getDrawableId(String name, String packageName) {
		return getResources().getIdentifier(name, "drawable", packageName);
	}
	
	/**
	 * 设置Container滑动背景图片
	 * @description 
	 * @author   tianran
	 * @createDate Mar 31, 2015
	 */
	private void loadBackground() {
		Options options = new Options();
		options.inSampleSize = 2;
		container.setBackGroud(BitmapFactory.decodeResource(getResources(),
				R.drawable.main_bg, options));
	}
	
	/**
	 * 保存当前的Items，记得所有item的位置
	 * @description 
	 * @author   tianran
	 * @createDate Apr 1, 2015
	 */
	private void saveImage2DB() {
		try {
//			List<MoveItem> list = container.getAllMoveItems();
//			xDbUtils.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}
	
	@Override
	public void onBackPressed() {
		//back键监听，如果在编辑模式，则取消编辑模式
		if (container.isEditting()) {
			container.showEdit(false);
			return;
		} else {
			saveImage2DB();
			super.onBackPressed();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	@Override
	public void onClick(View v) {
		Log.d(TAG, "onScrollAdapterClickListener");
		// TODO Auto-generated method stub
		myHandler.obtainMessage(MSG_APP_AUTO_OPEN).sendToTarget();
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onEdit");
		saveImage2DB();
	}

	@Override
	public void onPage2Other(int former, int current) {
		// TODO Auto-generated method stub
		Log.d(TAG, "former-->" + former +"  current-->" + current);
	}

	@Override
	public void onAddOrDeletePage(int page, boolean isAdd) {
		// TODO Auto-generated method stub
		Log.d(TAG, "page-->" + page +"  isAdd-->" + isAdd);
	}
	
}
