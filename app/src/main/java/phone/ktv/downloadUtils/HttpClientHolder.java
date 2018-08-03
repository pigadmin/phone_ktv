//package phone.ktv.downloadUtils;
//
//import org.apache.http.HttpVersion;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.params.ConnManagerParams;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//
///**
// * get the httpclient
// *
// * @author laohuai
// *
// */
//public class HttpClientHolder {
//	private static DefaultHttpClient client;
//
//	private static void buildInstance() {
//		if (client == null) {
//			client = new DefaultHttpClient();
//			HttpParams params = new BasicHttpParams();
//			ConnManagerParams.setMaxTotalConnections(params, 100);
//
//			HttpConnectionParams.setConnectionTimeout(params, 50000);
//			HttpConnectionParams.setSoTimeout(params, 50000);
//
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//
//			SchemeRegistry schemeRegistry = new SchemeRegistry();
//			schemeRegistry.register(new Scheme("http", PlainSocketFactory
//					.getSocketFactory(), 80));
//
//			ClientConnectionManager cm = new ThreadSafeClientConnManager(
//					params, schemeRegistry);
//			client = new DefaultHttpClient(cm, params);
//		}
//	}
//
//	public static DefaultHttpClient getInstance() {
//		buildInstance();
//		return client;
//	}
//
//}
