package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MakeForm {

	static final String APIKEY = "CdLAnC44phy82yESaLuUk0shkvSd8rL5QJLjVGDgykGJIdLUbXQaxBYIOCay1xZkmWEm1cUHRMKNIRsJxRRqwQ";
	static final String CLOUDCONVERT = "https://api.cloudconvert.org/process";
	static String DOWNLOADURL;
	static String PROCESSID;


	public static String getProcessID(String InputFormat, String OutputFormat) {
		System.out.println("[amr Convert]" + "Start getProcessID");
		JSONObject json = null;
		Object obj;
		String ProcessID = "";
		JSONParser jsonParser = new JSONParser();

		HttpPost post = null;

		DefaultHttpClient client = new DefaultHttpClient();

		post = new HttpPost(CLOUDCONVERT);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apikey", APIKEY));
		params.add(new BasicNameValuePair("inputformat", InputFormat));
		params.add(new BasicNameValuePair("outputformat", OutputFormat));


		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(
							responsePOST.getEntity().getContent(), "utf-8"));

			String line, page = "";
			while ((line = bufferedReader.readLine()) != null) {
				page += line;
				System.out.println("line : " + line);
			}
			System.out.println("page : " + page);

			obj = jsonParser.parse(page);
			json = (JSONObject)obj;
			ProcessID = (String) json.get("url");


			System.out.println("URL : " + ProcessID);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ProcessID;		
	}

	public String ccc(String ProcessID, String FilePath, String FileName, String OutputFormat) throws ClientProtocolException, IOException{
		String DownloadURL = "";
		String url = "http:" + ProcessID;
		HttpClient httpclient = new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		File fileToUpload = new File(FilePath);
		FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
		entity.addPart("input", new StringBody("download"));
		entity.addPart("data", fileBody);
		entity.addPart("file", new StringBody(FilePath));
		entity.addPart("filename", new StringBody(FileName));
		entity.addPart("outputformat", new StringBody(OutputFormat));

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		
		HttpResponse responsePOST = httpclient.execute(httpPost);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
						responsePOST.getEntity().getContent(), "utf-8"));

		String line, page = "";
		while ((line = bufferedReader.readLine()) != null) {
			page += line;
			System.out.println("line : " + line);
		}
		System.out.println("page : " + page);
		return DownloadURL;
	}

	public String aaa(String ProcessID, String FilePath, String FileName, String OutputFormat) throws ClientProtocolException, IOException{
		String DownloadURL = "";
		String url = "http:" + ProcessID;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		FileBody bin = new FileBody(new File(FilePath));
		StringBody comment = new StringBody("Filename: " + FilePath);

		MultipartEntity reqEntity = new MultipartEntity();

		reqEntity.addPart("file", bin);
		reqEntity.addPart("input", new StringBody("download"));
		reqEntity.addPart("filename", new StringBody(FileName));
		reqEntity.addPart("outputformat", new StringBody(OutputFormat));
		httppost.setEntity(reqEntity);
		/*
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("input", "download"));
		params.add(new BasicNameValuePair("file", FilePath));
		params.add(new BasicNameValuePair("filename", FileName));
		params.add(new BasicNameValuePair("outputformat", OutputFormat));

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
			httppost.setEntity(ent);
		 */
		HttpResponse responsePOST = httpclient.execute(httppost);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
						responsePOST.getEntity().getContent(), "utf-8"));

		String line, page = "";
		while ((line = bufferedReader.readLine()) != null) {
			page += line;
			System.out.println("line : " + line);
		}
		System.out.println("page : " + page);


		return DownloadURL;
	}

	public String bbb(String ProcessID, String FilePath, String FileName, String OutputFormat) throws ClientProtocolException, IOException{
		String DownloadURL = "";
		
		MultipartUtility mpu = new MultipartUtility("https:"+ProcessID, "UTF-8");
		mpu.addFormField("input", "upload");
		mpu.addFormField("filename", FileName);
		mpu.addFormField("outputformat", OutputFormat);
		mpu.addFilePart("file", new File(FilePath));
		mpu.addFormField("file", FilePath);
		mpu.finish();
		return DownloadURL;
	}

	public static String getDownloadURL(String ProcessID, String FilePath, String FileName, String OutputFormat) {
		HttpPost post = null;
		String DownloadURL = "";

		DefaultHttpClient client = new DefaultHttpClient();

		post = new HttpPost("http:" + ProcessID);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("input", "download"));
		params.add(new BasicNameValuePair("file", FilePath));
		params.add(new BasicNameValuePair("filename", FileName));
		params.add(new BasicNameValuePair("outputformat", OutputFormat));



		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);

			InputStreamReader isr = new InputStreamReader(responsePOST.getEntity().getContent(), "utf-8");
			BufferedReader bufferedReader = new BufferedReader(isr);

			String line, page = "";
			while ((line = bufferedReader.readLine()) != null) {
				page += line;
				System.out.println("line : " + line);
			}
			System.out.println("page : " + page);
			isr.close();


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} 

		return DownloadURL;
	}
}
