package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Vector;


public class WAVFILE {
	
	FileInputStream fis;
	File mFile;
	
	int mOffset=0; // 어디까지 읽었는지
	int mTotalFilesize; // 파일크기

	int mChunkLen; // 청크 크기. 딱히 필요없는듯?
	int mAudioFormat; // 1이면 PCM 임 거의 고정값
	int mNumofChannels; // 채널수 1 : mono, 2 : stereo 가 보통
	int mSampleRate; // 1초동안 소리를 몇개로 쪼개는지에 대한 것
	int mByteRate; // 1초동안 필요한 byte 수
	int mBlockAlign; // 한 샘플의 크기 byte
	int mBitPerSample; // 한 샘플은 몇 bit로 구성되어 있는가
	int mBytePerSample;
	
	int mDataLen; // 실제 wav 데이터 길이
	
	public WAVFILE(File file) {
		mFile = file;
		mTotalFilesize = (int)mFile.length();
		
		loadWavFile();
		//printfSample();
		//makewave();
		System.out.println("Gap less Start");
		gapless(0.4F); // float로 몇초 이상 공백이면 자를껀지 결정
		System.out.println("Gap less End");
	}
	
	public void loadWavFile() {
		
		/* 
		 * For Header 
		 * 안에 둔건 Method 끝나면 메모리 해제를 위해서 */
		byte[] wavHeader = new byte[12];
		byte[] chunkHeader = new byte[24];
		byte[] dataHeader = new byte[8];

		try {
			fis = new FileInputStream(mFile); // wav file open
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

        try {
			fis.read(wavHeader, 0, 12);
			fis.read(chunkHeader, 0, 24);
			fis.read(dataHeader, 0, 8); // 각각의 header read
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // wav 파일 header인지 확인
        if (wavHeader[0] != 'R' || wavHeader[1] != 'I' || wavHeader[2] != 'F' || wavHeader[3] != 'F' ||
            wavHeader[8] != 'W' || wavHeader[9] != 'A' || wavHeader[10] != 'V' || wavHeader[11] != 'E') {
        	System.out.println("Not wave file.");
        	return;
        }
        
		int mLen = ((0xff & wavHeader[7]) << 24) | 
					((0xff & wavHeader[6]) << 16) | 
					((0xff & wavHeader[5]) << 8) | 
					((0xff & wavHeader[4]));
		System.out.println("" + mLen);
        
        // wav 파일 포맷이 맞는지 확인. 음성정보를 담고있다고 함
        if(chunkHeader[0] != 'f' || chunkHeader[1] != 'm' || chunkHeader[2] != 't' || chunkHeader[3] != ' ') {
        	System.out.println("Not wave format.");
        	return;
        }
        
        mChunkLen = ((0xff & chunkHeader[7]) << 24) |
        			((0xff & chunkHeader[6]) << 16) |
        			((0xff & chunkHeader[5]) << 8) |
        			((0xff & chunkHeader[4]));
        System.out.println("" + mChunkLen);
        
        mAudioFormat = (((0xff & chunkHeader[9]) << 8) | ((0xff & chunkHeader[8])));
        mNumofChannels = (((0xff & chunkHeader[11]) << 8) | ((0xff & chunkHeader[10])));
        mSampleRate = ((0xff & chunkHeader[15]) << 24) |
        			  ((0xff & chunkHeader[14]) << 16) |
        			  ((0xff & chunkHeader[13]) << 8) |
        			  ((0xff & chunkHeader[12]));
        mByteRate = ((0xff & chunkHeader[19]) << 24) |
        			((0xff & chunkHeader[18]) << 16) |
        			((0xff & chunkHeader[17]) << 8) |
        			((0xff & chunkHeader[16]));
        mBlockAlign = (((0xff & chunkHeader[21]) << 8) | ((0xff & chunkHeader[20])));
        mBitPerSample = (((0xff & chunkHeader[23]) << 8) | ((0xff & chunkHeader[22])));
        mBytePerSample = mBitPerSample / 8;
        
        System.out.println("audioformat : " + mAudioFormat);
        System.out.println("numchannels : " + mNumofChannels);
        System.out.println("samplerate : " + mSampleRate);
        System.out.println("byterate : " + mByteRate);
        System.out.println("blockalign : " + mBlockAlign);
        System.out.println("bitpersample : " + mBitPerSample);
        
        // wav data 포맷이 맞는지 확인
        if(dataHeader[0] != 'd' || dataHeader[1] != 'a' || dataHeader[2] != 't' || dataHeader[3] != 'a') {
        	System.out.println("Not wave data format.");
        	return;
        }

        mDataLen = ((0xff & dataHeader[7]) << 24) |
        		   ((0xff & dataHeader[6]) << 16) |
        		   ((0xff & dataHeader[5]) << 8) |
        		   ((0xff & dataHeader[4]));
        System.out.println("" + mDataLen);
        
	}
	
	public void writeWavFile(File outFile, int start, int end, byte[] rawData) {
		FileOutputStream fos = null;
		//int totalDataLen = 36 + rawData.length; // 36(wave이후의 헤더 크기) + data길이
		int totalDataLen = 36 + end - start; // 44(wave이후의 헤더 크기) + data길이
		
		try {
			fos = new FileOutputStream(outFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
        byte[] header = new byte[44];
        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) mNumofChannels;
        header[23] = 0;
        header[24] = (byte) (mSampleRate & 0xff);
        header[25] = (byte) ((mSampleRate >> 8) & 0xff);
        header[26] = (byte) ((mSampleRate >> 16) & 0xff);
        header[27] = (byte) ((mSampleRate >> 24) & 0xff);
        header[28] = (byte) (mSampleRate * mBytePerSample & 0xff);
        header[29] = (byte) ((mSampleRate * mBytePerSample >> 8) & 0xff);
        header[30] = (byte) ((mSampleRate * mBytePerSample >> 16) & 0xff);
        header[31] = (byte) ((mSampleRate * mBytePerSample >> 24) & 0xff);
        header[32] = (byte) (2 * mNumofChannels);  // block align
        header[33] = 0;
        header[34] = (byte) mBitPerSample;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) ((end - start) & 0xff);
        header[41] = (byte) (((end - start) >> 8) & 0xff);
        header[42] = (byte) (((end - start) >> 16) & 0xff);
        header[43] = (byte) (((end - start) >> 24) & 0xff);
        
        try {
			fos.write(header, 0, 44);
			fos.write(rawData, start, end-start);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printfSample() {
		
		//System.out.println("wav 재생길이 : " + (double)mDataLen / (double)(mNumofChannels * mSampleRate * mBitPerSample / 8) + "초");
		System.out.println("1초당 : " + (mNumofChannels * mSampleRate * mBitPerSample / 8) + "바이트");
		
		byte[] sample = new byte[mNumofChannels * mSampleRate * mBitPerSample / 8];
		byte[] temp = new byte[2];
		
		while(mOffset < 16000) {
			try {
				fis.read(temp, 0, 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			sample[mOffset] = temp[0];
			sample[mOffset+1] = temp[1];
			mOffset += 2;
		}
		
		for(int i=0; i<1600; i+=2) {			
			short data = (short) (((0xff & sample[i+1]) << 8) | ((0xff & sample[i])));
			System.out.println((i/2) + "번 : " + data);
		}
	}
	
	public void makewave() {
		File wav = new File("test.wav");
		try {
			wav.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		byte[] sample = new byte[mDataLen];
		byte[] temp = new byte[mBytePerSample];
		
		while(mOffset < mDataLen) {
			try {
				fis.read(temp, 0, mBytePerSample);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			for(int i=0; i < mBytePerSample; i++) {
				sample[mOffset + i] = temp[i];
			}
			
			mOffset += mBytePerSample;
		}
		
		//writeWavFile(wav, sample);
	}
	
	public void gapless(float gap) {		
		BufferedWriter txtout = null;
		try {
			txtout = new BufferedWriter(new FileWriter("log.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		byte[] data = new byte[mDataLen];
		byte[] temp = new byte[mBytePerSample];
		
		int interval = (int)((float)(mNumofChannels * mSampleRate) * gap); // 자를 간격 mbyte를 빼줘야하나? 
		//여기선 2바이트가 한 단위니까 간격이 8000이라고 하면 실제론 거기에 나누기 2를 해서 4000이 되어야 하나...
		//int interval = (int)((float)(mNumofChannels * mSampleRate * mBytePerSample) * gap); // 자를 간격
		System.out.println("interval : " + interval);
			
		try {
			txtout.write("interval : " + interval);
			txtout.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		if(interval == 0) {
			System.out.println("interval is Zero!"); 
			try {
				txtout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		int start = 0, end = 0;
		Vector<Integer> vstart, vend;
		boolean iscut = false;
		
		short value=0;
		int count=0;
		
		mOffset = 0;
		
		while(mOffset < mDataLen) {
			try {
				fis.read(temp, 0, mBytePerSample);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(int i=0; i < mBytePerSample; i++) {
				data[mOffset + i] = temp[i];
			}
			
			mOffset += mBytePerSample;
		}
		
		int i;
		boolean ischeck = false;
		vstart = new Vector<>();
		vend = new Vector<>();
		vstart.add(0);
		
		for(i=0; i<mDataLen-interval; i+=mBytePerSample) {
			value = (short) (((0xff & data[i+1]) << 8) | ((0xff & data[i])));
			
			if(value < 0) value = (short)Math.abs(value); // 음수보정
			
			double dval = value / Math.pow(2, mBitPerSample-1);
			//System.out.format("dval : %.6f\n", dval);
			
			if(dval < 0.02 && ischeck == false) {
				ischeck = true;
				start = i;
			}
			
			if(ischeck) {
				if(dval < 0.02) {
					count++;
				} else {
					if(count >= interval)
					{
						try {
							txtout.write(start+ "에서 시작"); txtout.newLine();
							txtout.write(i + "에서 끊김 / " + count + "번 무음에 가까움"); txtout.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
						vstart.add(end);
						vend.add(start);
						//iscut = true;
					}
					end = i;
					count = 0;
					ischeck = false;
				}
			}

			/*if(dval >= 0.1 && start < end) {
				 어느 순간 값이 커지면 게속 커지는지 검사 
				//System.out.format("start %f sec\n", (double)i / (mNumofChannels * mSampleRate * mBytePerSample));
				
				sum = 0;
				count = 0;
				j = i;
				while(count < interval) {
					value = (short) (((0xff & data[j+1]) << 8) | ((0xff & data[j])));
					if(value < 0) value = (short)Math.abs(value);
					sum += (int) value;
					j+=mBytePerSample;
					count++;
				}
				aver = ((double)sum / interval / Math.pow(2, mBitPerSample-1));
				//System.out.format("\tstart aver[%d-%d] : %.6f\n", i, j, aver);
				
				logtext = String.format("\tstart aver[%d-%d] : %.6f\n", i, i+interval, aver);
				//logtext = String.format("\tstart aver[%d-%d] : %.6f\n", i, j, aver);
				try {
					txtout.write(logtext);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(aver >= 0.05) {
					iscut = false;
					start = i;
					System.out.println("find start " + (double)i / (mNumofChannels * mSampleRate * mBytePerSample) + "sec");
					try {
						txtout.write("find start " + (double)i / (mNumofChannels * mSampleRate * mBytePerSample) + "sec");txtout.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					i = j; // interval만큼 건너뜀
				}

			} else if(dval <= 0.05 && start > end) {
				 값이 작아지면 interval 구간까지 감소하는지를 검사.. 
				//System.out.format("end %f sec\n", (double)i / (mNumofChannels * mSampleRate * mBytePerSample));
				sum = 0;
				count = 0;
				j = i;
				while(count < interval) {
					value = (short) (((0xff & data[j+1]) << 8) | ((0xff & data[j])));
					if(value < 0) value = (short)Math.abs(value);
					sum += (int) value;
					j+=mBytePerSample;
					count++;
				}
				aver = ((double)sum / interval / Math.pow(2, mBitPerSample-1));
				//System.out.format("\tend aver[%d-%d] : %.6f\n", i, j, aver);
				logtext = String.format("\tend aver[%d-%d] : %.6f\n", i, i+interval, aver);
				try {
					txtout.write(logtext);
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
				if(aver < 0.01) {
					end = i;
					iscut = true;
					System.out.println("find end " + (double)i / (mNumofChannels * mSampleRate * mBytePerSample) + "sec");
					try {
						txtout.write("find end " + (double)i / (mNumofChannels * mSampleRate * mBytePerSample) + "sec");txtout.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}*/
						
			/*if(iscut) {
				String name = "file" + cnt++ + ".wav";
				System.out.println("make " + name);
				try {
					txtout.write("make " + name);txtout.newLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				File outFile = new File(name);
				try {
					outFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writeWavFile(outFile, start, end, data);
				iscut = false;
			}*/
		}
		
		if(vstart.size() > vend.size()) {
			vend.add(mDataLen);
		}
		
		for(i=0; i<vstart.size(); i++) {
			String name = "file" + i + ".wav";
			System.out.println("make " + name);
			try {
				txtout.write("make " + name);txtout.newLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			File outFile = new File(name);
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writeWavFile(outFile, vstart.get(i), vend.get(i), data);
			iscut = false;
		}
		
		try {
			txtout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
