package core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft_17;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.snu.ids.ha.index.Keyword;
import org.snu.ids.ha.index.KeywordExtractor;
import org.snu.ids.ha.index.KeywordList;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.util.Timer;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
public class AudioManager extends AudioPage{
	
	static int threadCount = 0;
	
	class MultiThread extends Thread { // Thread 클래스를 상속
		int checkcount = 0;
		String name;
		String fullname;
		MultiThread(String fullname, String name){
			this.name = name;
			this.fullname = fullname;
			System.out.println(this.fullname);
		}
		
		@SuppressWarnings("deprecation")
		public void run() {
			String filename = name.split("\\.")[0];
			File file = new File("C:\\APM_Setup\\htdocs\\ARSee_Server\\WebContent\\AudioDatas\\"+filename+".wav");
			
			while(true){
				cout("파일 : '"+filename+"' 를 체크 중... "+checkcount);
				updateStatusSender("File -> "+filename + " : " + checkcount + " Times");
				if(checkcount > 20){
					cout("파일 : '"+filename+"' 에 대한 정보 부족... ");
					updateStatusSender("File -> "+filename+" : fail ");
					this.stop();
					threadCount--;
					break;
				}
				
				if(file.exists()) { 
					String parent = "0";
					try{
						parent = name.split("--")[1].split("\\.")[0];
						if(parent.equals(" ")){
							parent = "p";
						}
					}
					catch(Exception e){
						System.out.println("del Thread");
						threadCount--;
					}
					
					System.out.println("parent : " + parent);
					if(parent.equals("p")){
						parent = "0";
					}
					cout("number : "+name.split("--")[0]);
					cout("C:/APM_Setup/htdocs/ARSee_Server/WebContent/AudioDatas/"+filename+".wav");
					
					try {
						sendToSpeech("olleh", name.split("--")[0], parent, "C:\\APM_Setup\\htdocs\\ARSee_Server\\WebContent\\AudioDatas\\"+filename+".wav");
						threadCount--;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						this.stop();
						threadCount--;
						break;
					}
					this.stop();
				}else{
					try {
						sleep(5000);
						System.out.println("sleep");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						threadCount--;
						this.stop();
						break;
					}
					checkcount++;
				}
			}
		}
	}
	
	
	
	public void audioFileUpload(HttpServletRequest request) throws IOException, SQLException{
		int sizeLimit = 5 * 1024 * 1024 ; // 5메가까지 제한 넘어서면 예외발생
		System.out.println("in audio!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		MultipartRequest multi;
		multi = new MultipartRequest(request, absolutepath, sizeLimit, new DefaultFileRenamePolicy());
		Enumeration filename = multi.getFileNames();
		String name = filename.nextElement().toString();
		System.out.println("?? : " + name);
		if(threadCount++ < 20){
			MultiThread mm = new MultiThread(absolutepath+name, name);
			mm.start();
		}
	}

	public void download(String url){
		URL u;
		InputStream is = null;
		DataInputStream dis;
		String s;

		try {

			//------------------------------------------------------------//
			// Step 2:  Create the URL.                                   //
			//------------------------------------------------------------//
			// Note: Put your real URL here, or better yet, read it as a  //
			// command-line arg, or read it from a file.                  //
			//------------------------------------------------------------//

			u = new URL(url);

			//----------------------------------------------//
			// Step 3:  Open an input stream from the url.  //
			//----------------------------------------------//

			is = u.openStream();         // throws an IOException

			//-------------------------------------------------------------//
			// Step 4:                                                     //
			//-------------------------------------------------------------//
			// Convert the InputStream to a buffered DataInputStream.      //
			// Buffering the stream makes the reading faster; the          //
			// readLine() method of the DataInputStream makes the reading  //
			// easier.                                                     //
			//-------------------------------------------------------------//

			dis = new DataInputStream(new BufferedInputStream(is));

			//------------------------------------------------------------//
			// Step 5:                                                    //
			//------------------------------------------------------------//
			// Now just read each record of the input stream, and print   //
			// it out.  Note that it's assumed that this problem is run   //
			// from a command-line, not from an application or applet.    //
			//------------------------------------------------------------//

			while ((s = dis.readLine()) != null) {
				System.out.println(s);
			}

		} catch (MalformedURLException mue) {

			System.out.println("Ouch - a MalformedURLException happened.");
			mue.printStackTrace();
			System.exit(1);

		} catch (IOException ioe) {

			System.out.println("Oops- an IOException happened.");
			ioe.printStackTrace();
			System.exit(1);

		} finally {

			//---------------------------------//
			// Step 6:  Close the InputStream  //
			//---------------------------------//

			try {
				is.close();
			} catch (IOException ioe) {
				// just going to ignore this one
			}

		} // end of 'finally' clause

	}

	public static void download(String sourceUrl, String targetFilename) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(absolutepath + targetFilename);

			URL url = new URL(sourceUrl);
			URLConnection urlConnection = url.openConnection();
			is = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int readBytes;
			while ((readBytes = is.read(buffer)) != -1) {
				fos.write(buffer, 0, readBytes);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void testSErve() throws ClientProtocolException, IOException{
		MakeForm a = new MakeForm();
		System.out.println("wefwef1");
		String PROCESSID = a.getProcessID("amr", "wav");
		System.out.println("wefwef2");
		String DOWNLOADURL = a.bbb(PROCESSID,"C:/APM_Setup/htdocs/ARSee_Server/WebContent/amr/001.amr", "001.amr", "wav" );
		System.out.println("wefwef3");


		System.out.println("==========================================================================================");
		System.out.println(PROCESSID);
		System.out.println(DOWNLOADURL);
		//C:\\Users\\HanSangyun\\Documents\\workspace\\STT_PC\\src\\input.amr
	}

	public boolean amrToWav(String url) throws IOException{
		URL u = new URL(url);
		File filePath = new File("C:/APM_Setup/htdocs/ARSee_Server/WebContent/wav");
		File fileDir = filePath.getParentFile();
		FileOutputStream fos = new FileOutputStream("C:/APM_Setup/htdocs/ARSee_Server/WebContent/wav/ar1.wav");

		InputStream is = u.openStream();

		byte[] buf = new byte[1024];

		double len = 0;

		double tmp = 0;

		double percent = 0;

		while ((len = is.read(buf)) > 0) {

			tmp += len / 1024 / 1024;

			percent = tmp / 1229 * 100;

			System.out.printf("%.2f", tmp);

			System.out.print("MB / 1229MB (진행률: ");

			System.out.printf("%.1f", percent);

			System.out.println("%)");

			fos.write(buf, 0, (int) len);

		}

		fos.close();

		is.close();

		Runtime rt = Runtime.getRuntime();

		System.out.println("다운로드 완료\r\n폴더를 띄워드립니다.");

		rt.exec("explorer.exe C:\\Download\\");
		return true;
	}
	
	public String sendToSpeech(String company, String number, String parent, String filepath) throws Exception{
		try {
			synchronized(Object.class){
			System.out.println("Send Google STT...");
			String USER_AGENT = "speech2text",	url = "https://www.google.com/speech-api/v2/recognize?output=json&lang=ko-kr&key=AIzaSyBHM1eZURbBRXHWvt7QvjZkxXXWFnKOws0&client=chromium";
			
				
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "audio/l16; rate=8000");
			con.setConnectTimeout(10000);
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(Files.readAllBytes(Paths.get(filepath)));
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			cout(response.toString());
			updateStatusSender(response.toString());
			String decodedString = "";
			String AllString = "";
			boolean checkResult = false;
			while ((decodedString = in.readLine()) != null) {
				if (checkResult) {
					decodedString = decodedString.substring(11, decodedString.length() - 33);
					decodedString += "}";
					cout(decodedString);
					JSONParser parser=new JSONParser();
					Object objj = parser.parse(decodedString);
					JSONObject jobj = (JSONObject)objj;
					objj = parser.parse(jobj.get("alternative").toString());
					JSONArray array = (JSONArray)objj;
					for(int i=0 ; i<array.size() ; i++ ){
						objj = parser.parse(array.get(i).toString());
						jobj = (JSONObject)objj;
						String depth = ""+parent.length();
						if(parent.equals("0")){
							depth = "0";
						}
						insertNumber(number, jobj.get("transcript").toString(), depth, parent, company);
						updateStatusSender(i + " th word : " + jobj.get("transcript").toString());
					}
				}
				checkResult = true;

			}
			in.close();
			con.disconnect();
			return response.toString();
			}
		} catch (MalformedURLException e) {
			return "{}";
		}
	}

	public String indexingNumber(HashMap<String, String> map, String text){		
		while (true){
			boolean ch = false;
			for(int i=0;i<Indexing.length;i++){
				for(int j=0;j<Indexing[0].length;j++){
					if(text.indexOf(Indexing[i][j]) != -1){
						try{
							map.put(""+i, indexingNumber(map, text.split(Indexing[i][j])[0].trim()));
						}catch(Exception e){
							map.put(""+i, indexingNumber(map, text));
						}						
						try{
							text = text.split(Indexing[i][j])[1].trim();
						}catch(Exception e){
							text = "";
						}
						ch = true;
					}
				}
			}
			if(!ch){
				if(map.get("0") == null && map.get("1") == null && map.get("2") == null && map.get("3") == null && map.get("4") == null && map.get("5") == null && map.get("6") == null && map.get("7") == null && map.get("8") == null && map.get("9") == null){
					for(int i=0;i<SubIndexing.length;i++){
						for(int j=0;j<SubIndexing[0].length;j++){
							if(text.indexOf(SubIndexing[i][j]) != -1){
								try{
									map.put(""+i, indexingNumber(map, text.split(SubIndexing[i][j])[0].trim()));
								}catch(Exception e){
									map.put(""+i, indexingNumber(map, text));
								}

								try{
									text = text.split(SubIndexing[i][j])[1].trim();
								}catch(Exception e){
									text = "";
								}
								ch = true;
							}
						}
					}
				}else{
					for(int i=1;i<10;i++){
						if(map.get(""+i) == null){
							if(map.get(""+(i+1)) != null){
								String value = map.get(""+(i+1));
								for(int j=0 ; j<SubIndexing[i].length ; j++){
									if(value.indexOf(SubIndexing[i][j]) != -1){
										map.put(""+i, value.split(SubIndexing[i][j])[0].trim());									
										map.put(""+(i+1), value.split(SubIndexing[i][j])[1].trim());									
									}
								}
							}
						}
					}
				}
				break;
			}
		}		
		return text;
	}

	public HashMap<String, String> parseNumbers(String ars, String string, String depth, String parent) throws Exception{
		HashMap<String, String> arsDivide = new HashMap<String, String>();
		String elses = "";
		String tempstring = string;
		MorphemeAnalyzer ma = new MorphemeAnalyzer();
		Timer timer = new Timer();
		timer.start();
		List<MExpression> ret = ma.analyze(string);
		timer.stop();
		timer.printMsg("Time");

		ret = ma.postProcess(ret);
		ret = ma.leaveJustBest(ret);
		List<Sentence> stl = ma.divideToSentences(ret);

		String newst = "";
		for( int i = 0; i < stl.size(); i++ ){
			Sentence st = stl.get(i);
			newst += st.getSentence();
			newst += " ";
		}

		List<MExpression> rett = ma.analyze(newst);
		ret = ma.postProcess(rett);
		ret = ma.leaveJustBest(rett);
		stl = ma.divideToSentences(rett);

		for( int i = 0; i < stl.size(); i++ ) {
			Sentence st = stl.get(i);		
			System.out.println("=============================================:" + st.getSentence());
			if(indexingNumber(arsDivide, st.getSentence()).equals(st.getSentence())){
				elses += st.getSentence()+" ";
			}
		}
		arsDivide.put("100", elses);
		return arsDivide;
	}

	public KeywordList keTest(String strToExtrtKwrd) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(strToExtrtKwrd, true);
		return kl;
	}

	public int returnIndex(String argu){
		for(int i=0;i<Indexing.length;i++){
			for(int j=0;j<Indexing[0].length;j++){
				if(Indexing[i][j].equals(argu)){
					return i;
				}
			}
		}
		return -1;
	}

	public boolean checkFrequency(String arsnum, String text, String dpt, String parent, String index, String company) throws SQLException{
		System.out.println("frequency");
		KeywordList kl = keTest(text);
		for( int i = 0; i < kl.size(); i++ ) {
			Keyword kwrd = kl.get(i);
			if( kwrd.getTag() == "NNG" && returnIndex(kwrd.getString())==-1){
				String checkTimeQuery = "SELECT id FROM "+ARS_DBNAME_NOW_INFO+" WHERE number = ? AND company = ? AND parent = ? AND depth = ? AND text = ? AND indexs = ?";
				String checkQuery = "SELECT id FROM "+ARS_DBNAME_NOW_INFO_KEYWORD+" WHERE key_word = ? and number = ? and depth = ? and parent = ? and indexs = ? and company = ?";
				String updateCount = "UPDATE "+ARS_DBNAME_NOW_INFO_KEYWORD+" SET count = count + 1 WHERE key_word = ? and depth =? and parent = ? and indexs = ? and company = ?";
				String insertQuery = "INSERT INTO "+ARS_DBNAME_NOW_INFO_KEYWORD+" (`key_word`, `number`, `depth`, `parent`, `indexs`, `count`, `company`, `ars_info_id`) VALUES (?, ?, ?, ?, ? ,?, ?, ?);";
				initializeDB();
				ResultSet rs = makePstmtExecute(checkQuery, kwrd.getString(), arsnum, dpt, parent, index, company);
				rs.last();
				if(rs.getRow()>0){
					makePstmtUpdate(updateCount, kwrd.getString(), dpt, parent, index, company);
				}else{
					ResultSet rst = makePstmtExecute(checkTimeQuery, arsnum, company, parent, dpt, text ,index);
					rst.last();
					if(rst.getRow()>0){
						rst.beforeFirst();
						rst.next();
						makePstmtUpdate(insertQuery, kwrd.getString(), arsnum, dpt, parent, index, "1", company, rst.getString("id"));											
					}
				}
			}
		}	
		return false;
	}

	public boolean checkFrequency(String arsnum, String text, String dpt, String parent, String index, String company, boolean insert) throws SQLException{
		System.out.println("frequency");
		KeywordList kl = keTest(text);
		for( int i = 0; i < kl.size(); i++ ) {
			Keyword kwrd = kl.get(i);
			//			System.out.println(kwrd.getTag()+" : "+kwrd.getString());
			if( kwrd.getTag() == "NNG" && returnIndex(kwrd.getString())==-1){
				//System.out.println(kwrd.getTag());
				if(insert){
					String checkTimeQuery = "SELECT id FROM "+ARS_DBNAME_NOW_INFO+" WHERE number = ? AND company = ? AND parent = ? AND depth = ? AND text = ? AND indexs = ?";
					String checkQuery = "SELECT id FROM "+ARS_DBNAME_NOW_INFO_KEYWORD+" WHERE key_word = ? and number = ? and depth = ? and parent = ? and indexs = ? and company = ?";
					String updateCount = "UPDATE "+ARS_DBNAME_NOW_INFO_KEYWORD+" SET count = count + 1 WHERE key_word = ? and depth =? and parent = ? and indexs = ? and company = ?";
					String insertQuery = "INSERT INTO "+ARS_DBNAME_NOW_INFO_KEYWORD+" (`key_word`, `number`, `depth`, `parent`, `indexs`, `count`, `company`, `ars_info_id`) VALUES (?, ?, ?, ?, ? ,?, ?, ?);";
					initializeDB();
					ResultSet rs = makePstmtExecute(checkQuery, kwrd.getString(), arsnum, dpt, parent, index, company);
					rs.last();
					if(rs.getRow()>0){
						makePstmtUpdate(updateCount, kwrd.getString(), dpt, parent, index, company);
					}else{
						ResultSet rst = makePstmtExecute(checkTimeQuery, arsnum, company, parent, dpt, text ,index);
						rst.last();
						if(rst.getRow()>0){
							rst.beforeFirst();
							rst.next();
							makePstmtUpdate(insertQuery, kwrd.getString(), arsnum, dpt, parent, index, "1", company, rst.getString("id"));											
						}
					}
				}
			}
		}	
		return false;
	}

	public boolean insertNumber(String ars,String string,String depth,String parent, String company) throws Exception{
		HashMap<String, String> parsingResult = parseNumbers(ars, string, depth, parent);

		String insertQuery = "INSERT INTO "+ARS_DBNAME_NOW_INFO+" ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, '1' );";		
		String checkUpdateQuery = "SELECT text FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE parent = ? AND number = ? AND depth = ? AND indexs = ? AND text = ? AND company = ? AND AmPm = ?";
		String insertUpdateQueryAm = "INSERT INTO "+ARS_DBNAME_NOW_INFO_UPDATE+" ( `text`, `number`, `depth`, `indexs`, `parent`, `count`, `company`, `starttime`, `endtime`, `AmPm` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ? )";
		String checkStartEndTime = "SELECT starttime, endtime FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String updateCountStarttime = "UPDATE "+ARS_DBNAME_NOW_INFO_UPDATE+" SET count = count + 1, starttime = NOW() WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String updateCountEndtime = "UPDATE "+ARS_DBNAME_NOW_INFO_UPDATE+" SET count = count + 1, endtime = NOW() WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String checkTime = "SELECT lastupdate FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String checkUpdate = "SELECT updates FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String makeUpdate = "UPDATE arsee_table_update_check SET updates = ? WHERE number = ? AND company = ?";
		String updateTime = "UPDATE arsee_table_update_check SET lastupdate = NOW() WHERE number = ? AND company = ?";
		String findMaxCount = "SELECT *, MAX(count) FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE depth = ? AND company = ? AND number = ?";

		String deleteArsOrigin = "DELETE FROM "+ARS_DBNAME_NOW_INFO+" WHERE number = ? AND company = ?";
		String deleteArsUpdate = "DELETE FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE number = ? AND company = ?";
		String chechByUpdateTable = "SELECT updates FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String insertUpdateTable = "INSERT INTO  arsee_table_update_check ( `date` ,`updates` ,`lastupdate` ,`number`, `company`) VALUES ( CURRENT_TIMESTAMP ,  ?,  NOW( ),  ?, ? ) ";

		initializeDB();
		ResultSet findDepthAndNumber = makePstmtExecute(chechByUpdateTable, ars, company);
		System.out.println("체크를 위해 업데이트 테이블을 불러보자");
		findDepthAndNumber.last();
		Calendar today = Calendar.getInstance();

		String ampm = "1";
		//		if(culTimeToSecond(today.get(Calendar.HOUR_OF_DAY),today.get(Calendar.MINUTE),today.get(Calendar.SECOND)) < 43200){
		//			ampm = "0";
		//		}

		if(findDepthAndNumber.getRow() == 0){
			findDepthAndNumber.beforeFirst();
			System.out.println("확인본이 비었다");
			makePstmtUpdate(insertUpdateTable, "1", ars, company);
			for(String index : parsingResult.keySet()){
				System.out.println("인덱스 확인 : "+index);
				ResultSet rsc = makePstmtExecute(checkUpdateQuery, parent, ars, depth, index, parsingResult.get(index), company, ampm);
				rsc.last();
				if(rsc.getRow()>0){
					System.out.println("있으면 업데이트");
					ResultSet cset = makePstmtExecute(checkStartEndTime, ars, index, depth, parent, company, parsingResult.get(index),ampm);
					cset.next();
					int startT = culTimeToSecond(cset.getString("starttime"));
					int endT = culTimeToSecond(cset.getString("endtime"));
					int nowT = culTimeToSecond(today.get(Calendar.HOUR_OF_DAY),today.get(Calendar.MINUTE),today.get(Calendar.SECOND));
					if( startT > nowT ){
						makePstmtUpdate(updateCountStarttime, ars, index, depth, parent, company, parsingResult.get(index), ampm);
					}else if( endT < nowT ){
						makePstmtUpdate(updateCountEndtime, ars, index, depth, parent, company, parsingResult.get(index), ampm);						
					}
				}else{
					System.out.println("없으면 새로넣기");
					makePstmtUpdate(insertUpdateQueryAm, parsingResult.get(index),ars,depth,index,parent,"1",company,ampm);												
				}
			}
			return true;
		}else{
			ResultSet chup = makePstmtExecute(checkUpdate, ars, company);
			chup.next();
			if(chup.getString("updates").equals("0")){
				System.out.println("그런데 업데이트 해야하는가? 아니요");
				boolean checkupdatenow = false;
				while(findDepthAndNumber.next()){
					String dbs = findDepthAndNumber.getString("indexs");
					if(findLCS(parsingResult.get(dbs),findDepthAndNumber.getString("text")) < 0.5){
						System.out.println("체크해봤는데 업데이트 해야하겠다 : "+ parsingResult.get(dbs).trim() +"와 "+findDepthAndNumber.getString("text") + "비교");
						checkupdatenow = true;
						break;					
					}
				}

				if(checkupdatenow){
					System.out.println("업데이트 해야한다");
					makePstmtUpdate(makeUpdate, "1", ars);
					makePstmtUpdate(deleteArsUpdate);
					makePstmtUpdate(deleteArsOrigin);
					return true;
				}else{
					System.out.println("업데이트 안해도 된다");
					for(String index : parsingResult.keySet()){
						if(!index.equals("0")){
							checkFrequency(ars, parsingResult.get(index), depth, parent, index, company, true);
						}
					}
					return false;
				}				
			}else{
				System.out.println("업데이트 해야하는가 ? 그렇다");
				ResultSet findUpdateTime = makePstmtExecute(checkTime, ars, company);
				findUpdateTime.next();
				String re[] = findUpdateTime.getString("lastupdate").split(" ");
				re = re[0].split("-");
				if(false){
				//if(Integer.parseInt(re[0]) < today.get(Calendar.YEAR) || Integer.parseInt(re[1]) < today.get(Calendar.MONTH)+1 || Integer.parseInt(re[2]) < today.get(Calendar.DATE)){
					System.out.println("업데이트 시간이 되었다");
					int count = 0;
					int timechecking[] = new int[2];
					timechecking[0] = 36000;
					timechecking[1] = 75600;

					while(true){
						for(int dbdepth=0 ; dbdepth < 10; dbdepth++){
							// 깊이가 0일 경우 가장 count가 높은 녀석을 찾는다.
							ResultSet updateRes = makePstmtExecute("SELECT * FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE depth = ? AND company = ? AND number = ? order by count desc limit 1", ""+dbdepth, company, ars);
							updateRes.last();
							// 있다면
							if(updateRes.getRow() > 0){
								updateRes.beforeFirst();
								updateRes.next();
								System.out.println(updateRes.getString("text"));
								ResultSet parentRes = makePstmtExecute("SELECT parent FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE depth = ? AND company = ? AND number = ? group by parent", ""+dbdepth, company, ars);
								while(parentRes.next()){
									count++;
									System.out.println(count);
									// 그때의 인덱스를 찾고 시간계산a을 하기위한 시간기준 청하게 된다.

									// 그리고 그와 같은 depth를 가지고 index가 다른 시간들을 찾는다.
									for(int friend = -1 ; friend < 100 ; friend++){								
										if(friend > 10){
											friend = 100;
										}
										System.out.println("start index depth, parent, index : "+dbdepth+", "+parentRes.getString("parent")+", "+friend);
										HashMap<String, String> hmap = new HashMap<>();
										int startchecking = timechecking[0];
										HashMap<Integer, int[]> checkingDates = new HashMap<>(); 

										// 그리고 시간을 계속해서 조금씩 늘려가면서 그 사이에 depth index가 같은
										while(startchecking <= timechecking[1]){
											String test2 = "SELECT * FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE parent = ? AND depth = ? AND company = ? AND indexs = ? AND number = ? AND (starttime >= ? OR endtime <= ?) order by count desc";
											ResultSet checkQuery = makePstmtExecute(test2, parentRes.getString("parent"), ""+dbdepth, company, ""+friend, ars, culSecondToTime(startchecking - 3600), culSecondToTime(startchecking));
											checkQuery.last();
											boolean find = false;
											if(checkQuery.getRow() > 0){
												checkQuery.beforeFirst();
												find = true;
												// 그리고 찾은 결과를 next()를 해서 map에 일단 집어넣는다.
												while(checkQuery.next()){
													System.out.println("find value!!! : "+checkQuery.getString("text"));
													int ids[] = new int[50];
													ids[0] = Integer.parseInt(checkQuery.getString("count"));
													ids[1] = culTimeToSecond(checkQuery.getString("starttime"));
													ids[2] = culTimeToSecond(checkQuery.getString("endtime"));
													checkingDates.put(Integer.parseInt(checkQuery.getString("id")), ids);
													System.out.println(friend+"("+checkQuery.getString("id")+")"+" : "+checkQuery.getString("text"));
												}
											}
											if(startchecking == timechecking[1]){
												break;
											}
											startchecking += 3600;
											if(startchecking > timechecking[1]){
												startchecking = timechecking[1];
											}
										}
										// 그런데 만약 map이 비지 않았다면 업데이트 할 정보가 모인 것이다.
										if(!checkingDates.isEmpty()){
											int maxcount = 0;
											int maxid = 0;
											// 일단 최고 max가 무엇인지 확인한다.
											for(int index : checkingDates.keySet()){
												if(checkingDates.get(index)[0] >= maxcount){
													maxid = index;
													maxcount = checkingDates.get(index)[0];
												}
											}
											System.out.println("maxid : "+maxid);

											// 최고 count의 시간도 업데이트 해준다.
											if(timechecking[0] > checkingDates.get(maxid)[1]){
												timechecking[0] = checkingDates.get(maxid)[1];
												System.out.println("Start Time Update!");
											}
											if(timechecking[1] < checkingDates.get(maxid)[2]){
												timechecking[1] = checkingDates.get(maxid)[2];
												System.out.println("End Time Update!");
											}

											// 그리고 몇번 index가 가장 큰지 찾아서 넣는다.
											hmap.put(""+friend, ""+maxid);

											// 그리고 max가 아닌 찌거기들의 id를 지워준다.
											for(int index : checkingDates.keySet()){
												if(index != maxid){
													makePstmtUpdate("DELETE FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE id = ?", ""+index);
												}
											}
										}
										// 그리고 keySet을 불러온 뒤
										for(String keys : hmap.keySet()){
											// 그 아이디를 찾고
											ResultSet findAndInsert = makePstmtExecute("SELECT * FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE id = ?", hmap.get(keys));
											findAndInsert.next();
											makePstmtUpdate(insertQuery, findAndInsert.getString("text"), ars, findAndInsert.getString("depth"), keys, findAndInsert.getString("parent"), company, culSecondToTime(timechecking[0]), culSecondToTime(timechecking[1]));								
											checkFrequency(ars, findAndInsert.getString("text"), findAndInsert.getString("depth"), findAndInsert.getString("parent"), keys, company);
											makePstmtUpdate("DELETE FROM "+ARS_DBNAME_NOW_INFO_UPDATE+" WHERE id = ?", hmap.get(keys));
											// 최고값을 업데이트 한뒤, 임시 테이블에서 지운다.
										}
										System.out.println("일단 업데이트가 끝났습니다.");
									}
								}
							}else{
								System.out.println("업데이트에 실패했습니다. 더 정보가 필요합니다.");							
								break;
							}
						}

						// 수정이 필요합니다!!!!!
						/*
						if(checkOrs.getRow() == 0){
							break;
						}
						 */
						break;
					}
					System.out.println("모든 업데이트가 끝난 것 같습니다.");
					makePstmtUpdate(makeUpdate, "0", ars, company);
					makePstmtUpdate(updateTime, ars, company);
					return true;
				}else{
					System.out.println("업데이트 시간이 안되었다");
					for(String index : parsingResult.keySet()){
						System.out.println("인덱스 확인 : "+index);
						ResultSet rsc = makePstmtExecute(checkUpdateQuery, parent, ars,depth,index,parsingResult.get(index),company, ampm);
						rsc.last();
						if(rsc.getRow()>0){
							System.out.println("있으면 업데이트");
							ResultSet cset = makePstmtExecute(checkStartEndTime, ars, index, depth, parent, company, parsingResult.get(index),ampm);
							cset.next();
							int startT = culTimeToSecond(cset.getString("starttime"));
							int endT = culTimeToSecond(cset.getString("endtime"));
							int nowT = culTimeToSecond(today.get(Calendar.HOUR_OF_DAY),today.get(Calendar.MINUTE),today.get(Calendar.SECOND));
							if( startT > nowT ){
								makePstmtUpdate(updateCountStarttime, ars, index, depth, parent, company, parsingResult.get(index),ampm);
							}else if( endT < nowT ){
								makePstmtUpdate(updateCountEndtime, ars, index, depth, parent, company, parsingResult.get(index),ampm);						
							}
						}else{
							System.out.println("없으면 새로넣기");
							makePstmtUpdate(insertUpdateQueryAm, parsingResult.get(index),ars,depth,index,parent,"1",company,ampm);												
						}
					}
				}
				return true;
			}
		}
	}
}
