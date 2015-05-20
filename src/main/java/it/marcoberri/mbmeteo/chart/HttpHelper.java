package it.marcoberri.mbmeteo.chart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	final static String USER_AGENT = "Mozilla/5.0";

	public static String getData(String url) throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createMinimal();

		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {

			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

		} finally {
			response.close();
		}

	}

	public static boolean sendPostData(String url, String data, boolean backup) {

		CloseableHttpClient httpclient = null;
		HttpResponse response = null;
		if (backup) {
			final File backupFile = new File(ConfigurationHelper.getProperties().getProperty("app.file.save.backup"));
			try {
				if (data.lastIndexOf("\n") == -1) {
					data += "\n";
				}

				FileUtils.writeStringToFile(backupFile, data, true);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			httpclient = HttpClients.createMinimal();
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			data = data.trim();
			formparams.add(new BasicNameValuePair("data", data));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("send " + data);

			response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			System.out.println("result --> " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200)
				return true;
			return false;

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null)
				try {
					httpclient.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}

		}
		return false;

	}
}