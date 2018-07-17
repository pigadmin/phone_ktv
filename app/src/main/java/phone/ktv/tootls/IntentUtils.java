package phone.ktv.tootls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * @author quan zi
 * @time 2016/9/22 9:30
 * @des 1.意图对象的工具类
 */
public class IntentUtils {

    /**
     * @des 从一个类跳到另一个类
     */
    public static void thisToOther(Context context, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        context.startActivity(intent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有一个String类型的值
     */
    public static void strIntent(Context context, Class<?> cla, String name, String data) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(name, data);
        context.startActivity(intent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有两个String类型的值
     */
    public static void strIntentString(Context context, Class<?> cla, String Key1, String Key2, String Value1, String Value2) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(Key1, Value1);
        intent.putExtra(Key2, Value2);
        context.startActivity(intent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有一个String类型的值
     */
    public static void strIntentInt(Context context, Class<?> cla, String Key1, String Key2, int Value1, String Value2) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(Key1, Value1);
        intent.putExtra(Key2, Value2);
        context.startActivity(intent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有一个int类型的值
     */
    public static void intIntent(Context context, Class<?> cla, String name, int data) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(name, data);
        context.startActivity(intent);
    }

    public static void intBunderIntent(Context context, Class<?> cla, String bunderName, String name, int data) {
        Intent mPwIntent = new Intent(context, cla);
        Bundle bundle = new Bundle();
        bundle.putInt(name, data);
        mPwIntent.putExtra(bunderName, bundle);
        context.startActivity(mPwIntent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有一个Serializable类型的对象
     */
    public static void serializableIntent(Context context, Class<?> cla, String name, Serializable serializable) {
        Intent intent = new Intent(context, cla);
        Bundle bundle = new Bundle();
        bundle.putSerializable(name, serializable);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * @des 从一个类跳到另一个类, 并且带有一个Serializable类型的对象
     */
    public static void serializableIntIntent(Context context, Class<?> cla, String name, Serializable serializable, String intName, int constant) {
        Intent intent = new Intent(context, cla);
        Bundle bundle = new Bundle();
        bundle.putSerializable(name, serializable);
        bundle.putInt(intName, constant);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
