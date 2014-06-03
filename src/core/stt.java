package core;

import java.io.*;
import java.net.HttpURLConnection; 
import java.net.URL; 

import javaFlacEncoder.FLACEncoder; 
import javaFlacEncoder.FLACFileOutputStream;
import javaFlacEncoder.StreamConfiguration; 

import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 

import java.nio.ByteBuffer; 
import java.nio.ByteOrder; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class stt {
	static String request = "https://www.google.com/speech-api/v1/recognize?xjerr=1&client=speech2text&lang=ko&maxresults=30";
	public void initialize(){

	}
	public void convertWaveToFlac(File inputFile, File outputFile) {

		StreamConfiguration streamConfiguration = new StreamConfiguration();
		streamConfiguration.setSampleRate(44100);
		streamConfiguration.setBitsPerSample(16);
		streamConfiguration.setChannelCount(1);

		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputFile);
			AudioFormat format = audioInputStream.getFormat();

			int frameSize = format.getFrameSize();

			FLACEncoder flacEncoder = new FLACEncoder();
			FLACFileOutputStream flacOutputStream = new FLACFileOutputStream(outputFile);

			flacEncoder.setStreamConfiguration(streamConfiguration);
			flacEncoder.setOutputStream(flacOutputStream);

			flacEncoder.openFLACStream();

			int frameLength = (int) audioInputStream.getFrameLength();
			if(frameLength <= AudioSystem.NOT_SPECIFIED){
				frameLength = 16384;//Arbitrary file size
			}
			int[] sampleData = new int[frameLength];
			byte[] samplesIn = new byte[frameSize];

			int i = 0;

			while (audioInputStream.read(samplesIn, 0, frameSize) != -1) {
				if (frameSize != 1) {
					ByteBuffer bb = ByteBuffer.wrap(samplesIn);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					short shortVal = bb.getShort();
					sampleData[i] = shortVal;
				} else {
					sampleData[i] = samplesIn[0];
				}

				i++;
			}

			sampleData = truncateNullData(sampleData, i);

			flacEncoder.addSamples(sampleData, i);
			flacEncoder.encodeSamples(i, false);
			flacEncoder.encodeSamples(flacEncoder.samplesAvailableToEncode(), true);

			audioInputStream.close();
			flacOutputStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private int[] truncateNullData(int[] sampleData, int index){
		if(index == sampleData.length) return sampleData;
		int[] out = new int[index];
		for(int i = 0; i<index; i++){
			out[i] = sampleData[i];
		}
		return out;
	}
		
	public boolean sendToGoogleSpeech(String inputFile, String outputFile){
		try{
			convertWaveToFlac(new File(inputFile), new File(outputFile));
			Path path = Paths.get(outputFile);
			byte[] data = Files.readAllBytes(path);
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();          
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "audio/x-flac; rate=44100");
			connection.setRequestProperty("User-Agent", "speech2text");
			connection.setConnectTimeout(60000);
			connection.setUseCaches (false);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();
			connection.disconnect();
			System.out.println("Done");
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				System.out.println(decodedString);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}		
		return true;
	}
}

