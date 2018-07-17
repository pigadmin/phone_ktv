package phone.ktv.tootls;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SPUtil {
	private Context context;
	private SharedPreferences sp;
	private Editor editor;

	public SPUtil(Context context) {
		this.context = context;
		sp = this.context.getSharedPreferences("common", Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public void getInstance(Context context, String filename) {
		this.context = context;
		sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public void putBoolean(String key, Boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, Boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void putLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key, Long defValue) {
		return sp.getLong(key, defValue);
	}

	public void putString(String key, String value) {
		if (key == null) {
			return;
		}
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public void putInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void putSet(String key, Set<String> values) {
		editor.putStringSet(key, values);
		editor.commit();
	}

	public  Set<String> getSet(String key) {
		return sp.getStringSet(key, new HashSet<String>());
	}

	public Map<String, ?> getAll() {
		return sp.getAll();
	}

	public void clearSpData(){
		sp.edit().clear().commit();
	}
}
