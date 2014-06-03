package core;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.simple.*;
import org.snu.ids.ha.index.Keyword;
import org.snu.ids.ha.index.KeywordExtractor;
import org.snu.ids.ha.index.KeywordList;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.util.Timer;

public class ParseText extends Database{
	String string;
	MorphemeAnalyzer ma;

	public int returnIndex(String argu){
		//System.out.println(argu);
		int value = -1;
		switch(argu){
		case "일번":case "일":case "일 번":
			value = 1;
			break;
		case "이번":case "이":case "이 번":
			value = 2;
			break;
		case "삼번":case "삼":case "삼 번":
			value = 3;
			break;
		case "사번":case "사":case "사 번":
			value = 4;
			break;
		case "오번":case "오":case "오 번":
			value = 5;
			break;
		case "육번":case "육":case "육 번":
			value = 6;
			break;
		case "칠번":case "칠":case "칠 번":
			value = 7;
			break;
		case "팔번":case "팔":case "팔 번":
			value = 8;
			break;
		case "구번":case "구":case "구 번":
			value = 9;
			break;
		case "영번":case "영":case "영 번":
			value = 0;
			break;
		default:
			value = -1;
			break;	
		}

		return value;
	}

	public String getAllText(String arsnum) throws SQLException{
		String checkQuery = "SELECT * FROM arsee_ars_infos WHERE number = ? order by depth, parent, indexs";
		JSONObject result = new JSONObject();
		initializeDB();
		ResultSet rss = makePstmtExecute(checkQuery, arsnum);		
		rss.last();
		if(rss.getRow() > 0){
			rss.beforeFirst();			
			int depth = 0;
			JSONObject results = new JSONObject();
			result.put("name", rss.getString("company"));
			while(rss.next()){
				if(depth < Integer.parseInt(rss.getString("depth"))){
					depth = Integer.parseInt(rss.getString("depth"));
					results.put(depth, rss.getString("text"));
				}else{

				}
			}

		}
		return result.toJSONString();
	}

	public String getDepthText(String arsnum, String depth, String parent) throws SQLException{
		String checkQuery = "SELECT * FROM arsee_ars_infos WHERE number = ? AND depth = ? AND parent = ? order by depth, parent, indexs";
		JSONObject result = new JSONObject();
		initializeDB();
		ResultSet rss = makePstmtExecute(checkQuery, arsnum, depth, parent);
		while(rss.next()){
			result.put(rss.getString("indexs"), rss.getString("text"));
		}
		return result.toJSONString();
	}


	public void deleteNumber(String ars) throws SQLException{
		String deleteQueryInfo = "delete FROM arsee_ars_infos WHERE number = ? LIMIT 1";
		String deleteQueryKeyword = "delete FROM arsee_kwrds WHERE number = ? LIMIT 1";
		initializeDB();
		makePstmtUpdate(deleteQueryInfo, ars);
		makePstmtUpdate(deleteQueryKeyword, ars);
	}

	public String indexingNumber(HashMap<String, String> map, String text){		
		while (true){
			boolean ch = false;
			if(text.indexOf("일번")!= -1){
				map.put("1", indexingNumber(map, text.split("일번")[0].trim()));
				text = text.split("일번")[1].trim();
				ch = true;
			}
			if(text.indexOf("일 번")!= -1){
				map.put("1", indexingNumber(map, text.split("일 번")[0].trim()));
				text = text.split("일 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("이번")!= -1){;
			map.put("2", indexingNumber(map, text.split("이번")[0].trim()));
			text = text.split("이번")[1].trim();
			ch = true;
			}
			if(text.indexOf("이 번")!= -1){
				map.put("2", indexingNumber(map, text.split("이 번")[0].trim()));
				text = text.split("이 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("삼번")!= -1){
				map.put("3", indexingNumber(map, text.split("삼번")[0].trim()));
				text = text.split("삼번")[1].trim();
				ch = true;

			}
			if(text.indexOf("삼 번")!= -1){
				map.put("3", indexingNumber(map, text.split("삼 번")[0].trim()));
				text = text.split("삼 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("사번")!= -1){
				map.put("4", indexingNumber(map, text.split("사번")[0].trim()));
				text = text.split("사번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("사 번")!= -1){
				map.put("4", indexingNumber(map, text.split("사 번")[0].trim()));
				text = text.split("사 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("오번")!= -1){
				map.put("5", indexingNumber(map, text.split("오번")[0].trim()));
				text = text.split("오번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("오 번")!= -1){
				map.put("5", indexingNumber(map, text.split("오 번")[0].trim()));
				text = text.split("오 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("육번")!= -1){
				map.put("6", indexingNumber(map, text.split("육번")[0].trim()));
				text = text.split("육번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("육 번")!= -1){
				map.put("6", indexingNumber(map, text.split("육 번")[0].trim()));
				text = text.split("육 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("칠번")!= -1){
				map.put("7", indexingNumber(map, text.split("칠번")[0].trim()));
				text = text.split("칠번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("칠 번")!= -1){
				map.put("7", indexingNumber(map, text.split("칠 번")[0].trim()));
				text = text.split("칠 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("팔번")!= -1){
				map.put("8", indexingNumber(map, text.split("팔번")[0].trim()));
				text = text.split("팔번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("팔 번")!= -1){
				map.put("8", indexingNumber(map, text.split("팔 번")[0].trim()));
				text = text.split("팔 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("구번")!= -1){
				map.put("9", indexingNumber(map, text.split("구번")[0].trim()));
				text = text.split("구번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("구 번")!= -1){
				map.put("9", indexingNumber(map, text.split("구 번")[0].trim()));
				text = text.split("구 번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("영번")!= -1){
				map.put("0", indexingNumber(map, text.split("영번")[0].trim()));
				text = text.split("영번")[1].trim();
				ch = true; 
			}
			if(text.indexOf("영 번")!= -1){
				map.put("0", indexingNumber(map, text.split("영 번")[0].trim()));
				text = text.split("영 번")[1].trim();
				ch = true; 
			}
			if(!ch){
				break;
			}
		}		
		return text;
	}

	public HashMap<String, String> parseNumbers(String ars,String string,String depth,String parent) throws Exception{
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


	public boolean insertNumber(String ars,String string,String depth,String parent, String company) throws Exception{
		HashMap<String, String> parsingResult = parseNumbers(ars, string, depth, parent);

		String insertQuery = "INSERT INTO arsee_ars_infos ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? );";		
		String checkUpdateQuery = "SELECT text FROM arsee_ars_infos_update WHERE parent = ? AND number = ? AND depth = ? AND indexs = ? AND text = ? AND company = ? AND AmPm = ?";
		String insertUpdateQueryAm = "INSERT INTO arsee_ars_infos_update ( `text`, `number`, `depth`, `indexs`, `parent`, `count`, `company`, `starttime`, `endtime`, `AmPm` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ? )";
		String checkStartEndTime = "SELECT starttime, endtime FROM arsee_ars_infos_update WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String updateCountStarttime = "UPDATE arsee_ars_infos_update SET count = count + 1, starttime = NOW() WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String updateCountEndtime = "UPDATE arsee_ars_infos_update SET count = count + 1, endtime = NOW() WHERE number = ? AND indexs = ? AND depth = ? AND parent = ? AND company = ? AND text = ? AND AmPm = ?";
		String checkTime = "SELECT lastupdate FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String checkUpdate = "SELECT updates FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String makeUpdate = "UPDATE arsee_table_update_check SET updates = ? WHERE number = ? AND company = ?";
		String updateTime = "UPDATE arsee_table_update_check SET lastupdate = NOW() WHERE number = ? AND company = ?";
		String findMaxCount = "SELECT *, MAX(count) FROM arsee_ars_infos_update WHERE depth = ? AND company = ? AND number = ?";
		String deleteArsOrigin = "DELETE FROM arsee_ars_infos WHERE number = ? AND company = ?";
		String deleteArsUpdate = "DELETE FROM arsee_ars_infos_update WHERE number = ? AND company = ?";
		String chechByUpdateTable = "SELECT updates FROM arsee_table_update_check WHERE number = ? AND company = ?";
		String insertUpdateTable = "INSERT INTO  arsee_table_update_check ( `date` ,`updates` ,`lastupdate` ,`number`, `company`) VALUES ( CURRENT_TIMESTAMP ,  ?,  NOW( ),  ?, ? ) ";

		initializeDB();
		ResultSet findDepthAndNumber = makePstmtExecute(chechByUpdateTable, ars, company);
		System.out.println("체크를 위해 업데이트 테이블을 불러보자");
		findDepthAndNumber.last();
		Calendar today = Calendar.getInstance();

		String ampm = "1";
		if(culTimeToSecond(today.get(Calendar.HOUR_OF_DAY),today.get(Calendar.MINUTE),today.get(Calendar.SECOND)) < 43200){
			ampm = "0";
		}

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
//				if(true){
				if(Integer.parseInt(re[0]) < today.get(Calendar.YEAR) || Integer.parseInt(re[1]) < today.get(Calendar.MONTH)+1 || Integer.parseInt(re[2]) < today.get(Calendar.DATE)){
					System.out.println("업데이트 시간이 되었다");
					int count = 0;
					int timechecking[] = new int[2];
					timechecking[0] = 36000;
					timechecking[1] = 75600;

					while(true){
						for(int dbdepth=0 ; dbdepth < 10; dbdepth++){
							// 깊이가 0일 경우 가장 count가 높은 녀석을 찾는다.
							ResultSet updateRes = makePstmtExecute("SELECT * FROM arsee_ars_infos_update WHERE depth = ? AND company = ? AND number = ? order by count desc limit 1", ""+dbdepth, company, ars);
							updateRes.last();
							// 있다면
							if(updateRes.getRow() > 0){
								updateRes.beforeFirst();
								updateRes.next();
								System.out.println(updateRes.getString("text"));
								ResultSet parentRes = makePstmtExecute("SELECT parent FROM arsee_ars_infos_update WHERE depth = ? AND company = ? AND number = ? group by parent", ""+dbdepth, company, ars);
								while(parentRes.next()){
									count++;
									System.out.println(count);
									// 그때의 인덱스를 찾고 시간계산a을 하기위한 시간기준 청하게 된다.

									// 그리고 그와 같은 depth를 가지고 index가 다른 시간들을 찾는다.
									for(int friend = -1 ; friend < 10 ; friend++){								
										System.out.println("start index depth, parent, index : "+dbdepth+", "+parentRes.getString("parent")+", "+friend);
										HashMap<String, String> hmap = new HashMap<>();
										int startchecking = timechecking[0];
										HashMap<Integer, int[]> checkingDates = new HashMap<>(); 

										// 그리고 시간을 계속해서 조금씩 늘려가면서 그 사이에 depth index가 같은
										DEBUG_MODE = false;
										while(startchecking <= timechecking[1]){
											String test2 = "SELECT * FROM arsee_ars_infos_update WHERE parent = ? AND depth = ? AND company = ? AND indexs = ? AND number = ? AND (starttime >= ? OR endtime <= ?) order by count desc";
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
										DEBUG_MODE = true;
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
													makePstmtUpdate("DELETE FROM arsee_ars_infos_update WHERE id = ?", ""+index);
												}
											}
										}
										// 그리고 keySet을 불러온 뒤
										for(String keys : hmap.keySet()){
											// 그 아이디를 찾고
											ResultSet findAndInsert = makePstmtExecute("SELECT * FROM arsee_ars_infos_update WHERE id = ?", hmap.get(keys));
											findAndInsert.next();
											makePstmtUpdate(insertQuery, findAndInsert.getString("text"), ars, findAndInsert.getString("depth"), keys, findAndInsert.getString("parent"), company, culSecondToTime(timechecking[0]), culSecondToTime(timechecking[1]));								
											makePstmtUpdate("DELETE FROM arsee_ars_infos_update WHERE id = ?", hmap.get(keys));
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


	public boolean checkFrequency(String arsnum, String text, String dpt, String parent, String index, String company, boolean insert) throws SQLException{
		System.out.println("frequency");
		KeywordList kl = keTest(text);
		for( int i = 0; i < kl.size(); i++ ) {
			Keyword kwrd = kl.get(i);
			//			System.out.println(kwrd.getTag()+" : "+kwrd.getString());
			if( kwrd.getTag() == "NNG" && returnIndex(kwrd.getString())==-1){
				//System.out.println(kwrd.getTag());
				if(insert){
					String checkTimeQuery = "SELECT id FROM arsee_ars_infos WHERE number = ? AND company = ? AND parent = ? AND depth = ? AND text = ? AND indexs = ?";
					String checkQuery = "SELECT id FROM arsee_kwrds WHERE key_word = ? and number = ? and depth = ? and parent = ? and indexs = ? and company = ?";
					String updateCount = "UPDATE arsee_kwrds SET count = count + 1 WHERE key_word = ? and depth =? and parent = ? and indexs = ? and company = ?";
					String insertQuery = "INSERT INTO arsee_kwrds (`key_word`, `number`, `depth`, `parent`, `indexs`, `count`, `company`, `ars_info_id`) VALUES (?, ?, ?, ?, ? ,?, ?, ?);";
					initializeDB();
					ResultSet rs = makePstmtExecute(checkQuery, kwrd.getString(),arsnum,dpt,parent, index, company);
					rs.last();
					if(rs.getRow()>0){
						makePstmtUpdate(updateCount, kwrd.getString(), dpt, parent, index, company);
					}else{
						ResultSet rst = makePstmtExecute(checkTimeQuery, arsnum, company, parent, dpt, text ,index);
						rst.last();
						if(rst.getRow()>0){
							rst.beforeFirst();
							makePstmtUpdate(insertQuery, kwrd.getString(), arsnum, dpt, parent, index, "1", company, rst.getString("id"));											
						}
					}
				}
			}
		}	
		return false;
	}

	public String checkUpdateByNumber(String arsnum) throws SQLException{
		String checkQuery = "SELECT * FROM  arsee_table_update_check WHERE number = ? order by depth , parent, indexs";
		JSONObject result = new JSONObject();
		initializeDB();
		ResultSet rss = makePstmtExecute(checkQuery, arsnum);
		while(rss.next()){
			result.put("result", rss.getString("updates"));
		}
		return result.toJSONString();
	}

	public String checkMaxKwrdByCount(String arsnum, String kwrd) throws SQLException{
		String checkQuery = "SELECT * FROM arsee_kwrds WHERE number = ? AND key_word = ? order by depth, parent, indexs";
		JSONObject result = new JSONObject();
		initializeDB();
		ResultSet rss = makePstmtExecute(checkQuery, arsnum, kwrd);
		while(rss.next()){
			result.put("depth-index",rss.getString("depth") +"-"+ rss.getString("indexs"));
		}
		return result.toJSONString();
	}

	public String checkMaxKwrdByCount(String arsnum, String kwrd, String depth) throws SQLException{
		String checkQuery = "SELECT * FROM arsee_kwrds WHERE number = ? AND depth = ? AND key_word = ? order by depth, parent, indexs";
		JSONObject result = new JSONObject();
		initializeDB();
		ResultSet rss = makePstmtExecute(checkQuery, arsnum, depth, kwrd);
		while(rss.next()){
			result.put("depth-index",rss.getString("depth") +"-"+ rss.getString("indexs"));
		}
		return result.toJSONString();
	}

	public String checkMaxKwrdBy(){
		return "";
	}

	public KeywordList keTest(String strToExtrtKwrd) {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(strToExtrtKwrd, true);
		return kl;
	}

	public JSONObject jsonToString(String jsonstring){
		JSONObject jsonObj = (JSONObject) JSONValue.parse(jsonstring);
		return jsonObj;		
	}

	public JSONObject stringToJson(String string){
		JSONObject a = new JSONObject();
		return a;
	}

	public void parseingText(String string){

	}


	@SuppressWarnings("unchecked")
	public boolean parsingForGoal(String string, String arsnum, String depth, String parent, String company) throws Exception{
		JSONObject a = new JSONObject();
		System.out.println("1");
		a.put("text", string);
		System.out.println("2");
		a.put("ars", arsnum);		
		System.out.println("3");
		a.put("dpt", depth);
		System.out.println("4");
		a.put("parent", parent);
		System.out.println("5");
		a.put("company", company);
		System.out.println("6");
		return insertNumber(a.get("ars").toString(),a.get("text").toString(),a.get("dpt").toString(),a.get("parent").toString(), a.get("company").toString());
	}

	public boolean parsingForGoal(String string) throws Exception{
		System.out.println(string);
		Object Obj = JSONValue.parse(string);
		JSONObject a = (JSONObject)Obj;
		return insertNumber(a.get("ars").toString(),a.get("text").toString(),a.get("dpt").toString(),a.get("parent").toString(), a.get("company").toString());
	}
}
