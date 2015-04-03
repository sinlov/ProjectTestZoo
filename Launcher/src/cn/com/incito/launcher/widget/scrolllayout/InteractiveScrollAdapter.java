package cn.com.incito.launcher.widget.scrolllayout;


import cn.com.incito.launcher.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/**
 * 滚动页面的适配器
 * @description 
 * @author   tianran
 * @createDate Mar 31, 2015
 * @version  1.0
 */
@SuppressLint("UseSparseArrays")
public class InteractiveScrollAdapter implements InteractiveSLAdapterItem {
	public static String SCROLL_ADAPTER_TAG = "ScrollAdapter"; 
	private Context context;
	private LayoutInflater inflater;

	private List<MoveItem> mList;
	
	public List<MoveItem> getmList() {
		return mList;
	}

	public void setmList(List<MoveItem> mList) {
		this.mList = mList;
	}

	private HashMap<Integer,SoftReference<Drawable>> mCache;

	public InteractiveScrollAdapter(Context context, List<MoveItem> list) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);

		this.mList = list;
		this.mCache = new HashMap<Integer, SoftReference<Drawable>>();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position) {
		View view = null;
		if (position < mList.size()) {
			MoveItem moveItem = mList.get(position);
			view = inflater.inflate(R.layout.item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.content_iv);
			StateListDrawable states = new StateListDrawable();
			final int img_ID = moveItem.getId();
			int imgUrl = moveItem.getImgurl();
			int imgUrlDown = moveItem.getImgdown();

			Drawable pressed = null;
			Drawable normal = null;

			SoftReference<Drawable> p = mCache.get(imgUrlDown);
			if (p != null) {
				pressed = p.get();
			}

			SoftReference<Drawable> n = mCache.get(imgUrl);
			if (n != null) {
				normal = n.get();
			}

			if (pressed == null) {
				pressed = context.getResources().getDrawable(imgUrlDown);
				mCache.put(imgUrlDown, new SoftReference<Drawable>(pressed));
			}

			if (normal == null) {
				normal = context.getResources().getDrawable(imgUrl);
				mCache.put(imgUrl, new SoftReference<Drawable>(normal));
			}

			states.addState(new int[] {android.R.attr.state_pressed},pressed);
			states.addState(new int[] {android.R.attr.state_focused},pressed);
			states.addState(new int[] { }, normal);
			
			iv.setImageDrawable(states);
			view.setTag(moveItem);
			//TODO set some click doing
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast show = Toast.makeText(context, "img_ID: " + img_ID, Toast.LENGTH_SHORT);
					show.setGravity(Gravity.CENTER, 0, 0);
					show.show();
					if (null != onScrollAdapterClickListener) {
						onScrollAdapterClickListener.onClick(v);
					}
				}
			});
		}
		return view;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public void exchange(int oldPosition, int newPositon) {
		MoveItem item = mList.get(oldPosition);
		mList.remove(oldPosition);
		mList.add(newPositon, item);
	}
	
	private OnScrollAdapterClickListener onScrollAdapterClickListener = null;
	
	public OnScrollAdapterClickListener getOnScrollAdapterClickListener() {
		return onScrollAdapterClickListener;
	}

	public void setOnScrollAdapterClickListener(
			OnScrollAdapterClickListener onScrollAdapterClickListener) {
		this.onScrollAdapterClickListener = onScrollAdapterClickListener;
	}

	public interface OnScrollAdapterClickListener{
		void onClick(View v);
	}
	
	private InteractiveOnDataChangeListener dataChangeListener = null;


	public InteractiveOnDataChangeListener getOnDataChangeListener() {
		return dataChangeListener;
	}

	public void setOnDataChangeListener(InteractiveOnDataChangeListener dataChangeListener) {
		this.dataChangeListener = dataChangeListener;
	}


	public void delete(int position) {
		if (position < getCount()) {
			mList.remove(position);
		}
	}

	public void add(MoveItem item) {
		mList.add(item);
	}

	public MoveItem getMoveItem(int position) {
		return mList.get(position);
	}

	/**
	 * 回收缓存
	 * @description 
	 * @author   tianran
	 * @createDate Mar 31, 2015
	 */
	public void recycleCache() {
		if (mCache != null) {
			Set<Integer> keys = mCache.keySet();
			for (Iterator<Integer> it = keys.iterator(); it.hasNext();) {
				Integer key = it.next();
				SoftReference<Drawable> reference = mCache.get(key);
				if (reference != null) {
					reference.clear();
				}
			}
			mCache.clear();
			mCache = null;
		}
	}
}
