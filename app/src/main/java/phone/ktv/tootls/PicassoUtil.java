package phone.ktv.tootls;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PicassoUtil {
    //基础使用 - 网络加载
    public static void picassoPrimary(Context context, String loadUrl, ImageView imageView) {
        Picasso.with(context).load(loadUrl).into(imageView);
    }
    //显错使用 - 网络加载
    public static void picassoIntermediate(Context context, String loadUrl, int errorImageView, ImageView imageView) {
        Picasso.with(context).load(loadUrl).error(errorImageView).into(imageView);
    }
    //站位使用 - 网络加载 - 推荐
    public static void picassoAdvanced(Context context, String loadUrl, int placeImageView, int errorImageView, ImageView imageView) {
        Picasso.with(context).load(loadUrl).placeholder(placeImageView).error(errorImageView).into(imageView);
    }
    //限制使用（可设置图片大小，圆角） - 网络加载
    public static void picassoClip(Context context, String loadUrl, int width, int height, ImageView imageView) {
        Picasso.with(context).load(loadUrl).resize(width, height).centerCrop().into(imageView);
    }
    //资源加载
    public static void picassoResource(Context context, int resource, ImageView imageView) {
        Picasso.with(context).load(resource).into(imageView);
    }
    //本地加载
    public static void picassoFile(Context context, String file, ImageView imageView) {
        Picasso.with(context).load(new File(file)).into(imageView);
    }
    //Adapter取消已经在视野之外的ImageView- 此封装可能有误，建议不要使用此方法
    public static void picassoCancel(Context context, String url,ImageView imageView){
        /**
         * 在Adapter的getView中执行String url = getItem(position)
         * 之后获取url输入到第二个参数就可以
         * */
        Picasso.with(context).load(url).into(imageView);
    }

    /**
     * 加载中文网址
     * @param str
     * @return
     */
    public  static String utf8Togb2312(String str){
        String data="";
        try {
            for(int i=0;i<str.length();i++){
                char c=str.charAt(i);
                if(c+"".getBytes().length>1&&c!=':'&&c!='/'){
                    data = data+java.net.URLEncoder.encode(c+"","utf-8");
                }else {
                    data=data+c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println(data);
        }
        return  data;
    }
}
