package phone.ktv.tootls;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对Gson操作的工具类
 */
public class GsonJsonUtils {
    /**
     * @param json
     * @return map
     * @throws Exception
     * @des 将一个Json字符串转变为Map集合
     */
    public static Map<String, Object> parseJson2Map(String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = new JsonReader(new StringReader(json));
            map = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception ex) {

        }
        return map;
    }

    /**
     * @param object
     * @return json
     * @throws Exception
     * @des 将一个对象转变为一个字符串
     */
    public static String parseObj2Json(Object object) {
        if (object == null) {
            return "";
        }
        try {
            Gson g = new GsonBuilder().create();
            String json = g.toJson(object, object.getClass());
            return json;
        } catch (Exception ex) {
            Logger.e(ex);
            return null;
        }
    }

    /**
     * @param json
     * @param typeToken
     * @return 一个指定的对象
     * @throws Exception
     * @des json字符串转变为指定的对象
     */
    public static <T> T parseJson2Obj(String json, TypeToken<T> typeToken) {
        if (TextUtils.isEmpty(json) || json.equals("null"))
            return null;
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(json));
            return gson.fromJson(reader, typeToken.getType());
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    public static <T> T parseJson2Obj(String json, Class<T> _C) {
        if (TextUtils.isEmpty(json) || json.equals("null"))
            return null;
        try {
            return new Gson().fromJson(json, _C);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    public static <T> List<T> parseJson2ListObj(String strJson, Class<T> cls) {
        try {
            List<T> mList = new ArrayList<T>();

            JsonArray ja = new JsonParser().parse(strJson).getAsJsonArray();
            for (final JsonElement elem : ja) {
                mList.add(new Gson().fromJson(elem, cls));
            }
            return mList;
        } catch (Exception ex) {
            Logger.e(ex);
            return null;
        }
    }
}
