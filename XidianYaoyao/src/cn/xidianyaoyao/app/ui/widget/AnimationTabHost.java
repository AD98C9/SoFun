package cn.xidianyaoyao.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import cn.xidianyaoyao.app.R;

/**
 * FragmentTab�л�����
 * 
 * @author WangTanyun
 * 
 */

public class AnimationTabHost extends TabHost {

	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private int mTabCount;// ��¼��ǰ��ǩҳ������

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_left);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_left);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_right);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_right);
	}

	public int getTabCount() {
		return mTabCount;
	}

	public void addTab(TabSpec tabSpec) {
		mTabCount++;
		super.addTab(tabSpec);
	}

	public void setCurrentTab(int index) {
		int mCurrentTabID = getCurrentTab();

		// �����˳���Ļ����
		if (getCurrentTabView() != null) {// ��һ�ν���ú���ʱ��Ϊ��
			if (mCurrentTabID == (mTabCount - 1) && index == 0) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
				getCurrentView().startAnimation(slideRightOut);
			} else if (index > mCurrentTabID) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < mCurrentTabID) {
				getCurrentView().startAnimation(slideRightOut);
			}

		}

		// ���������Ļ����
		if (getCurrentTabView() != null) {
			if (mCurrentTabID == (mTabCount - 1) && index == 0) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
				getCurrentView().startAnimation(slideRightIn);
			} else if (index > mCurrentTabID) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (index < mCurrentTabID) {
				getCurrentView().startAnimation(slideRightIn);
			}
		}
	}
}
