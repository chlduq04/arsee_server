package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class ImageUpload extends ImagePage{

	public void imageFileUpload(HttpServletRequest request) throws IOException, SQLException{
		int sizeLimit = 5 * 1024 * 1024 ; // 5메가까지 제한 넘어서면 예외발생
		System.out.println("in image!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		MultipartRequest multi;
		multi = new MultipartRequest(request, absolutepath, sizeLimit, new DefaultFileRenamePolicy());
		Enumeration filename = multi.getFileNames();
		String name = filename.nextElement().toString();
		System.out.println("이미지 업로드 완료 파일명은? : " + name);
		initializeDB();
		ResultSet st = makePstmtExecute("SELECT * FROM  arsee_image_paths WHERE phone = ? AND path = ?", name.split(ARS_DATA_SPLIT_KEY)[0], name );
		st.last();
		if(st.getRow() > 0){
			int count = Integer.parseInt(st.getString("count")) + 1;
			makePstmtUpdate("UPDATE arsee_image_paths SET count = count + 1 WHERE phone = ? AND path = ?", name.split(ARS_DATA_SPLIT_KEY)[0], name );
			String realn = name.substring( 0, name.lastIndexOf(".")) + count+ name.substring(name.lastIndexOf("."));
			int r = makePstmtUpdate("INSERT INTO arsee_image_paths (`phone`, `path`, `date`, `count`) VALUES (?, ?, CURRENT_TIMESTAMP, ?)", name.split(ARS_DATA_SPLIT_KEY)[0], realn, "0");
		}
		else{
			int r = makePstmtUpdate("INSERT INTO arsee_image_paths (`phone`, `path`, `date`, `count`) VALUES (?, ?, CURRENT_TIMESTAMP, ?)", name.split(ARS_DATA_SPLIT_KEY)[0], name, "0");
		}
		
	}

	public String fileToString(String strFileName){
		BufferedReader bufferReader;
		try {
			bufferReader = new BufferedReader( new FileReader( new File ( strFileName ) ) );
			StringBuilder sb = new StringBuilder();
			String line;
			while ( (line = bufferReader.readLine() ) != null ) {
				sb.append( line );
			}
			bufferReader.close();


			return sb.toString();
		} catch (FileNotFoundException e) {
			System.out.printf("");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.printf("");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public void fileToByte() throws SQLException{
		File file = new File(super.absolutepath+"01040750607.PNG");

		try {           
			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64 String
			//String imageDataString = encodeImage(imageData);
			// Converting a Base64 String into Image byte array
			//byte[] imageByteArray = decodeImage(imageDataString);

			// Write a image byte array into file system

			String name = "01040750607";           
			ResultSet rss = null;
			initializeDB();
			makePstmtExecute(getPathQuery, name);

			FileOutputStream imageOutFile = new FileOutputStream(super.absolutepath+"01040750607_0.PNG");
			imageOutFile.write(imageData);

			imageInFile.close();
			imageOutFile.close();

			System.out.println("Image Successfully Manipulated!");
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
	}

	public void byteToFile(){

	}

	public void StringToJson(){

	}

	public void jsonToString(){

	}

}
