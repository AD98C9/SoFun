package cn.xidianyaoyao.app.ui.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * ����Ӧ�ò������õ���
 * 
 * @author WangTanyun
 * 
 */
public class PreferencesService {

	private Context context;

	public PreferencesService(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * �����ȡ������Ϣ
	 * 
	 * @param cusName
	 *            ;�˺�
	 * @param cusGender
	 *            ;�Ա�
	 * @param cusMailbox
	 *            ;����
	 * @param cusHeadUri
	 *            ;ͷ���ַ
	 */
	public void save_cusInfo(String cusName, String cusGender,
			String cusMailbox, String cusHeadUri) {
		SharedPreferences preferences = context.getSharedPreferences(
				"cus_info", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("cusName", cusName);
		editor.putString("cusGender", cusGender);
		editor.putString("cusMailbox", cusMailbox);
		editor.putString("cusHead", cusHeadUri);
		editor.commit();
	}

	public Map<String, String> cusInfo_getPreferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences(
				"cus_info", Context.MODE_PRIVATE);
		params.put("cusName", preferences.getString("cusName", ""));
		params.put("cusGender", preferences.getString("cusGender", ""));
		params.put("cusMailbox", preferences.getString("cusMailbox", ""));
		params.put("cusHead", preferences.getString("cusHead", ""));
		return params;
	}

	/**
	 * 
	 * �����ȡҡҡ��Ч����
	 * 
	 * @param voiceSet
	 *            ;��Ч����
	 * 
	 */
	public void save_voiceSet(Boolean voiceSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"voice_set", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("voiceSet", voiceSet).commit();

	}

	public Boolean voiceSet_getPreferences() {

		SharedPreferences preferences = context.getSharedPreferences(
				"voice_set", Context.MODE_PRIVATE);
		return preferences.getBoolean("voiceSet", false);
	}

	/**
	 * 
	 * �����ȡҡҡ������
	 * 
	 * @param vibrateSet
	 *            ;�񶯿���
	 * 
	 */
	public void save_vibrateSet(Boolean vibrateSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"vibrate_set", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("vibrateSet", vibrateSet).commit();

	}

	public Boolean vibrateSet_getPreferences() {

		SharedPreferences preferences = context.getSharedPreferences(
				"vibrate_set", Context.MODE_PRIVATE);
		return preferences.getBoolean("vibrateSet", false);
	}

	/**
	 * 
	 * �����ȡѡ��ҡҡ����
	 * 
	 * @param shakeItemSet
	 *            ;һ��ҡ��������
	 * @param shakePrice
	 *            ;����
	 * @param shakeScore
	 *            ;����
	 * @param shakeTaste
	 *            ;��ζ
	 * @param shakeNutrition
	 *            ;Ӫ��
	 */
	public void save_shakeItem(String shakeItemSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_item", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeItemSet", shakeItemSet).commit();
	}

	public String shakeItem_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_item", Context.MODE_PRIVATE);
		return preferences.getString("shakeItemSet", "");
	}

	/* =============================== */
	public void save_shakePrice(String shakePriceSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_price", Context.MODE_PRIVATE);
		preferences.edit().putString("shakePriceSet", shakePriceSet).commit();
	}

	public String shakePrice_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_price", Context.MODE_PRIVATE);
		return preferences.getString("shakePriceSet", "");
	}

	/* =============================== */
	public void save_shakePriceNum(int shakePriceNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_price_num", Context.MODE_PRIVATE);
		preferences.edit().putInt("shakePriceNumSet", shakePriceNumSet)
				.commit();
	}

	public int shakePriceNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_price_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakePriceNumSet", 0);
	}

	/* ================================ */
	public void save_shakeScore(String shakeScoreSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_score", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeScoreSet", shakeScoreSet).commit();
	}

	public String shakeScore_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_score", Context.MODE_PRIVATE);
		return preferences.getString("shakeScoreSet", "");
	}

	/* =============================== */
	public void save_shakeScoreNum(int shakeScoreNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_score_num", Context.MODE_PRIVATE);
		preferences.edit().putInt("shakeScoreNumSet", shakeScoreNumSet)
				.commit();
	}

	public int shakeScoreNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_score_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakeScoreNumSet", 0);
	}

	/* ===================================== */
	public void save_shakeTaste(String shakeTasteSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_taste", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeTasteSet", shakeTasteSet).commit();
	}

	public String shakeTaste_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_taste", Context.MODE_PRIVATE);
		return preferences.getString("shakeTasteSet", "");
	}

	/* =============================== */
	public void save_shakeTasteNum(int shakeTasteNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_taste_num", Context.MODE_PRIVATE);
		preferences.edit().putInt("shakeTasteNumSet", shakeTasteNumSet)
				.commit();
	}

	public int shakeTasteNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_taste_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakeTasteNumSet", 0);
	}

	/* =============================== */
	public void save_shakeNutrition(String shakeNutritionSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_nutrition", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeNutritionSet", shakeNutritionSet)
				.commit();
	}

	public String shakeNutrition_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_nutrition", Context.MODE_PRIVATE);
		return preferences.getString("shakeNutritionSet", "");
	}

	/* =============================== */
	public void save_shakeNutritionNum(int shakeNutritionNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_nutrition_num", Context.MODE_PRIVATE);
		preferences.edit().putInt("shakeNutritionNumSet", shakeNutritionNumSet)
				.commit();
	}

	public int shakeNutritionNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_nutrition_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakeNutritionNumSet", 0);
	}

	/**
	 * 
	 * �����ȡ����ҡҡ����
	 * 
	 * @param shakeRestau
	 *            ;����
	 * @param shakePrice
	 *            ;�ܼ�
	 * @param shakeNumber
	 *            ;����
	 */
	
	public void save_shakeRestau(String shakeRestauSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_restau", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeRestauSet", shakeRestauSet)
				.commit();
	}

	public String shakeRestau_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_restau", Context.MODE_PRIVATE);
		return preferences.getString("shakeRestauSet", "");
	}
	/* =============================== */
	public void save_shakeGroupPrice(String shakeGroupPriceSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_groupPrice", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeGroupPriceSet", shakeGroupPriceSet)
				.commit();
	}

	public String shakeGroupPrice_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_groupPrice", Context.MODE_PRIVATE);
		return preferences.getString("shakeGroupPriceSet", "");
	}

	/* =============================== */
	public void save_shakeGroupPriceNum(int shakeGroupPriceNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_groupprice_num", Context.MODE_PRIVATE);
		preferences.edit()
				.putInt("shakeGroupPriceNumSet", shakeGroupPriceNumSet)
				.commit();
	}

	public int shakeGroupPriceNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_groupprice_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakeGroupPriceNumSet", 0);
	}

	/* =============================== */
	public void save_shakeNumber(String shakeNumberSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_number_num", Context.MODE_PRIVATE);
		preferences.edit().putString("shakeNumberSet", shakeNumberSet).commit();
	}

	public String shakeNumber_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_number_num", Context.MODE_PRIVATE);
		return preferences.getString("shakeNumberSet", "");
	}

	/* =============================== */
	public void save_shakeNumberNum(int shakeNumberNumSet) {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_number_num", Context.MODE_PRIVATE);
		preferences.edit().putInt("shakeNumberNumSet", shakeNumberNumSet)
				.commit();
	}

	public int shakeNumberNum_getPreferences() {
		SharedPreferences preferences = context.getSharedPreferences(
				"shake_number_num", Context.MODE_PRIVATE);
		return preferences.getInt("shakeNumberNumSet", 0);
	}

}