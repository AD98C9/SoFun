package cn.xidianyaoyao.app.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.net.Uri;
import cn.xidianyaoyao.app.ui.widget.MD5;

/**
 * 
 * ������������������� HttpClientͨ��
 * 
 * @author WangTanyun
 * 
 */
public class HttpUtils {
	private static HttpUtils mHttpUtils = null;
	public final static String IP = "http://nutshellsofun.duapp.com/";
	private final static int CONNECTION_TIME_OUT = 5000;// ���ӳ�ʱ
	private final static int SO_TIME_OUT = 10000;// ����ʱ

	private HttpUtils() {
	}

	public synchronized static HttpUtils getInstance() {
		if (mHttpUtils == null) {
			mHttpUtils = new HttpUtils();
		}
		return mHttpUtils;
	}

	/***
	 * ��¼
	 * 
	 * @param username
	 *            --��¼���˺�
	 * @param password
	 *            --��¼������
	 * @return result--����˷��ص��ַ���
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String login(String username, String password)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "identify_processLogin";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("loginName", username));
		params.add(new BasicNameValuePair("password", password));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ע��
	 * 
	 * @param username
	 *            -�˺�
	 * @param password
	 *            -����
	 * @param email
	 *            -����
	 * @param gender
	 *            -�Ա�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String regist(String username, String password, String email,
			String gender) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "identify_register";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("customer.cusId", username));
		params.add(new BasicNameValuePair("customer.passwd", password));
		params.add(new BasicNameValuePair("customer.email", email));
		params.add(new BasicNameValuePair("customer.gender", gender));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �һ�����
	 * 
	 * @param username
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String find_pwd(String username) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "identify_fetchPassword";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("loginName", username));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �޸�����
	 * 
	 * @param username
	 *            -�˺�
	 * @param yuanpwd
	 *            -ԭ����
	 * @param xinpwd
	 *            -������
	 * @param dingpwd
	 *            -������
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String change_pwd(String username, String yuanpwd, String xinpwd)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "identify_modifyPassword";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("loginName", username));
		params.add(new BasicNameValuePair("password", yuanpwd));
		params.add(new BasicNameValuePair("newpassword", xinpwd));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ����
	 * 
	 * @param dishOrRestau
	 *            -�����򷹹���
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String search(String dishOrRestau) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "search_searchContent";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", dishOrRestau));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ѡ��ҡҡ
	 * 
	 * @param count
	 *            -һ��ҡ�����ٸ���
	 * @param price
	 *            -����
	 * @param score
	 *            -����
	 * @param taste
	 *            -��ζ
	 * @param nutrition
	 *            -Ӫ��
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String shake_select(String count, String price, String score,
			String taste, String nutrition) throws Exception {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "shake_randomDish";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("count", count));
		params.add(new BasicNameValuePair("price", price));
		params.add(new BasicNameValuePair("score", score));
		params.add(new BasicNameValuePair("taste", taste));
		params.add(new BasicNameValuePair("nutrition", nutrition));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ����ҡҡ
	 * 
	 * @param restau
	 *            -���ѷ���
	 * @param price
	 *            -�ܼ�
	 * @param number
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String shake_group(String restau, String price, String number)
			throws Exception {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "group_groupEat";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("restName", restau));
		params.add(new BasicNameValuePair("price", price));
		params.add(new BasicNameValuePair("personNo", number));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/**
	 * ����鿴����ͼƬ
	 * 
	 * ��ȡ����ͼƬ,���ͼƬ�����ڻ����У��ͷ��ظ�ͼƬ������������м��ظ�ͼƬ����������
	 * 
	 * @param path
	 *            ͼƬ·��
	 * @param cacheDir
	 *            ͼƬ�����ַ
	 * @return
	 */
	public Uri getCacheImage(String path, File cacheDir) throws Exception {
		// path -> MD5 ->32�ַ���.jpg/png
		File localFile = new File(cacheDir, MD5.getMD5(path)
				+ path.substring(path.lastIndexOf(".")));
		if (localFile.exists()) {
			return Uri.fromFile(localFile);
		} else {
			HttpClient client = new DefaultHttpClient(setTimeout());
			HttpPost request = new HttpPost(path);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				FileOutputStream outStream = new FileOutputStream(localFile);
				InputStream inputStream = response.getEntity().getContent();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				inputStream.close();
				outStream.close();
				return Uri.fromFile(localFile);
			}
			return null;
		}
	}

	/**
	 * ������鿴����ͼƬ
	 * 
	 * @param path
	 *            ͼƬ·��
	 * @return
	 */
	public byte[] getNoCacheImage(String path) throws Exception {
		HttpClient client = new DefaultHttpClient(setTimeout());
		HttpPost request = new HttpPost(path);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			InputStream inputStream = response.getEntity().getContent();
			return convertStreamToByte(inputStream);
		}
		return null;
	}

	/***
	 * �������а�
	 * 
	 * @param requestTimes
	 *            -�������
	 * @param limit
	 *            -ÿҳ��ʾ����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String dishRank(String requestTimes, String limit)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "recommend_rankDish";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("reqTimes", requestTimes));
		params.add(new BasicNameValuePair("pageSize", limit));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �������а�
	 * 
	 * @param requestTimes
	 *            -�������
	 * @param limit
	 *            -ÿҳ��ʾ����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restauRank(String requestTimes, String limit)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "recommend_rankRestaurant";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("reqTimes", requestTimes));
		params.add(new BasicNameValuePair("pageSize", limit));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �����Ƽ�
	 * 
	 * @param userName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String personRecom(String userName) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "recommend_obtainRecommend";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ����ղ�
	 * 
	 * @param userName
	 *            -�˺�
	 * @param DishId
	 *            -��
	 * @param restauId
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String personCollect(String userName, String DishId, String restauId)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "collection_addCollection";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("disId", DishId));
		params.add(new BasicNameValuePair("resId", restauId));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ȡ���ղ�
	 * 
	 * @param userName
	 *            -�˺�
	 * @param DishId
	 *            -��
	 * @param restauId
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String personNotCollect(String userName, String DishId,
			String restauId) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "collection_removeCollection";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("disId", DishId));
		params.add(new BasicNameValuePair("resId", restauId));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �Ƿ��ղ�
	 * 
	 * @param userName
	 *            -�˺�
	 * @param DishId
	 *            -��
	 * @param restauId
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String isOrNoCollect(String userName, String DishId, String restauId)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "collection_isCollected";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("disId", DishId));
		params.add(new BasicNameValuePair("resId", restauId));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴�ղز˵�
	 * 
	 * @param userName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String dishCollectShow(String userName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "collection_listCollection";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴��ע
	 * 
	 * @param userName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String followsShow(String userName) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_listFriends";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴��˿
	 * 
	 * @param userName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String fansShow(String userName) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_listFollows";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ��ӹ�ע
	 * 
	 * @param userName
	 *            -�˺�
	 * @param friendName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String followsAdd(String userName, String friendName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_addFollow";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("followedId", friendName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ȡ����ע
	 * 
	 * @param userName
	 *            -�˺�
	 * @param friendName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String followsDelete(String userName, String friendName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_removeFollow";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("followedId", friendName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �Ƿ��ע
	 * 
	 * @param userName
	 *            -�˺�
	 * @param friendName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String followsIsOrNot(String userName, String friendName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_isFollowed";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cusId", userName));
		params.add(new BasicNameValuePair("followedId", friendName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ��������
	 * 
	 * @param friendName
	 *            -�˺�
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String friendSearch(String friendName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "friend_searchFriend";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("friendName", friendName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �������
	 * 
	 * @param userSuggestion
	 *            -���
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String suggestion(String userSuggestion)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "advice_submitAdvice";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("advice", userSuggestion));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ��ӷ���
	 * 
	 * @param restauName
	 *            -����
	 * @param restauCall
	 *            -�绰
	 * @param restauAddr
	 *            -��ַ
	 * @param restauLat
	 *            -γ��
	 * @param restauLon
	 *            -����
	 * @param restauDescr
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restauAdd(String restauName, String restauCall,
			String restauAddr, String restauLat, String restauLon,
			String restauDescr) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "restaurant_addRestaurant";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("restaurant.resname", restauName));
		params.add(new BasicNameValuePair("restaurant.telephone", restauCall));
		params.add(new BasicNameValuePair("restaurant.addr", restauAddr));
		params.add(new BasicNameValuePair("restaurant.lat", restauLat));
		params.add(new BasicNameValuePair("restaurant.lng", restauLon));
		params.add(new BasicNameValuePair("restaurant.descr", restauDescr));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �����ѹ���
	 * 
	 * @param restauName
	 *            -���ݵ�����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restauClose(String restauName)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "advice_submitAdvice";// �Ķ�
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("advice", restauName));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ���ݵ绰���ߵ�ַ����
	 * 
	 * @param restauName
	 *            -���ݵ�����
	 * @param restauSummary
	 *            -���ݵ�����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restauCorrect(String restauName, String restauSummary)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "advice_submitAdvice";// �Ķ�
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("advice", restauName));
		params.add(new BasicNameValuePair("advice", restauSummary));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * ��ӷ�������
	 * 
	 * @param userName
	 *            -�û���
	 * @param dishId
	 *            -����ID
	 * @param restauId
	 *            -����
	 * @param evaluate
	 *            -����
	 * @param score
	 *            -����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String addEvaluate(String userName, String dishId, String restauId,
			String score, String evaluate) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "comment_commitComment";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment.cusId", userName));
		params.add(new BasicNameValuePair("comment.disId", dishId));
		params.add(new BasicNameValuePair("comment.resId", restauId));// �Ķ�
		params.add(new BasicNameValuePair("comment.score", score));
		params.add(new BasicNameValuePair("comment.descr", evaluate));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴��������
	 * 
	 * @param dishId
	 *            -��ID
	 * @param restauId
	 *            -����ID
	 * @param requestTimes
	 *            -�������
	 * @param limit
	 *            -ÿҳ����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String evaluateShow(String dishId, String restauId,
			String requestTimes, String limit) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "comment_listComments";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("dishId", dishId));
		params.add(new BasicNameValuePair("resId", restauId));// �Ķ�
		params.add(new BasicNameValuePair("reqTimes", requestTimes));
		params.add(new BasicNameValuePair("limit", limit));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴�������
	 * 
	 * @param restauId
	 *            -����ID
	 * @param requestTimes
	 *            -�������
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String photoShow(String restauId, String requestTimes)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "restaurant_fetchImage";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("resId", restauId));
		params.add(new BasicNameValuePair("reqTimes", requestTimes));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/***
	 * �鿴���������Ƽ��Ĳ�
	 * 
	 * @param restauId
	 *            -����ID
	 * @param requestTimes
	 *            -�������
	 * @param limit
	 *            -ÿҳ����
	 * @return result
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String restauRecom(String restauId, String requestTimes, String limit)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient(setTimeout());
		String url = IP + "recommend_recommendDish";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("resId", restauId));
		params.add(new BasicNameValuePair("reqTimes", requestTimes));
		params.add(new BasicNameValuePair("pageSize", limit));
		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			String result = convertStreamToString(response.getEntity()
					.getContent());
			return result;
		}
		return null;
	}

	/**
	 * �������ӳ�ʱ������ʱ
	 * 
	 * @return HttpParams
	 */
	private HttpParams setTimeout() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT);
		return httpParams;
	}

	/***
	 * �����ص�������ת��Ϊ�ַ���
	 * 
	 * @param is
	 *            --��Ҫת����������
	 * @return resultStr --ת������ַ���
	 */
	private String convertStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tempStr = "";
		try {
			while ((tempStr = br.readLine()) != null) {
				sb.append(tempStr);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		String resultStr = sb.toString();
		return resultStr;
	}

	private byte[] convertStreamToByte(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
