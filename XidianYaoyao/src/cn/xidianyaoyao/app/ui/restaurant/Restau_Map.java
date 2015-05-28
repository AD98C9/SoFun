package cn.xidianyaoyao.app.ui.restaurant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * �ٶȵ�ͼ��ʾ����λ��
 * 
 * @author WangTanyun
 * 
 */

public class Restau_Map extends Activity {

	private ImageView mMapBack;
	private TextView mMapTitleName;

	private double mLat;// λ��γ��
	private double mLon;// λ�þ���

	private BMapManager mBMapManager = null;
	// private static final String BAIDU_MAP_KEY = "ckGucNo7a2AKMdCFfEaFDYOC";
	// // �ٶȵ�ͼ�����������Կ
	private static final String BAIDU_MAP_KEY = "pGvTt9pmC9Yn7Rm6kO6GygGD"; // �ٶȵ�ͼ�����������Կ

	/**
	 * MapView �ǵ�ͼ���ؼ�
	 */
	private MapView mMapView = null;
	/**
	 * ��MapController��ɵ�ͼ����
	 */
	private MapController mMapController = null;
	/**
	 * MKMapViewListener ���ڴ����ͼ�¼��ص�
	 */
	private MKMapViewListener mMapListener = null;

	public void onCreate(Bundle savedInstanceState) {
		/**
		 * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager. BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
		 * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
		 */

		super.onCreate(savedInstanceState);
		initEngineManager();
		setContentView(R.layout.restau_map);

		initView();
		setLister();
		initViewMap();
		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	/**
	 * ������֤key
	 * 
	 * @param pContext
	 */
	private void initEngineManager() {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(getApplicationContext());
			mBMapManager.init(BAIDU_MAP_KEY, new MKGeneralListener() {

				@Override
				public void onGetPermissionState(int iError) {
					if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
						// ��ȨKey����
						Toast.makeText(Restau_Map.this,
								"��������ȷ����ȨKey,������������������Ƿ�������error: " + iError,
								Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onGetNetworkState(int iError) {
					if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
						Toast.makeText(Restau_Map.this, "���������������",
								Toast.LENGTH_LONG).show();
					} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
						Toast.makeText(Restau_Map.this, "������ȷ�ļ���������",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		public OverlayTest(Drawable marker, MapView mapView) {
			super(marker, mapView);
		}
	}

	private void initView() {
		mMapBack = (ImageView) findViewById(R.id.restauMap_back);
		mMapTitleName = (TextView) findViewById(R.id.restauMap_titleName);
		mMapTitleName.setText(getIntent().getStringExtra("RestauName"));
	}

	private void setLister() {
		mMapBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
	}

	private void initViewMap() {
		/**
		 * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
		 */
		mMapView = (MapView) findViewById(R.id.bmapView);
		/**
		 * ������ʾ�Ŵ���С�Ŀ��ư�ť
		 */
		mMapView.setBuiltInZoomControls(true);
		/**
		 * ���õ�ͼ��ʾģʽsetTraffic��ͨsetSatellite����
		 */
		// mMapView.setTraffic(true);
		// mMapView.setSatellite(true);// ����Ϊ����ģʽ
		/**
		 * ��ȡ��ͼ������
		 */
		mMapController = mMapView.getController();
		/**
		 * ���õ�ͼ�Ƿ���Ӧ����¼� .
		 */
		mMapController.enableClick(true);
		/**
		 * ���õ�ͼ���ż���
		 */
		mMapController.setZoom(18);

		/**
		 * ����ָʾλ�õ�ͼ��
		 */
		Drawable marker = getResources().getDrawable(
				R.drawable.shape_location_marker);// ָʾλ�õ�ͼƬ
		OverlayTest ov = new OverlayTest(marker, mMapView);

		mLat = Double.parseDouble(getIntent().getStringExtra("RestauLat"));// λ��γ�ȸ�ֵ
		mLon = Double.parseDouble(getIntent().getStringExtra("RestauLon"));// λ�þ��ȸ�ֵ

		GeoPoint geoPoint = new GeoPoint((int) (mLat * 1e6), (int) (mLon * 1e6));
		mMapController.setCenter(geoPoint);
		OverlayItem item = new OverlayItem(geoPoint, null, null);
		item.setMarker(marker);
		ov.addItem(item);
		mMapView.getOverlays().add(ov);

		/**
		 * MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * �ڴ˴����ͼ�ƶ���ɻص� ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * �ڴ˴����ͼpoi����¼� ��ʾ��ͼpoi���Ʋ��ƶ����õ� ���ù���
				 * mMapController.enableClick(true); ʱ���˻ص����ܱ�����
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(Restau_Map.this, title, Toast.LENGTH_SHORT)
							.show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ���� ���ڴ˱����ͼ���洢�豸
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
				 */
			}

			/**
			 * �ڴ˴����ͼ������¼�
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(Restau_Map.this, "��ͼ�������", Toast.LENGTH_SHORT)
						.show();

			}
		};
		mMapView.regMapViewListener(mBMapManager, mMapListener);
	}

	@Override
	protected void onPause() {
		/**
		 * MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
		 */
		mMapView.onPause();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
		 */
		mMapView.onResume();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView������������Activityͬ������activity����ʱ�����MapView.destroy()
		 */
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
