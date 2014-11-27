package br.ufs.highframedesigner.loader;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import br.ufs.highframedesigner.util.Properties;

public class Uploader {
	
	private HttpClient httpClient;
	private HttpPut request;
	private File file;
	
	public Uploader(File file) {
		this.file = file;
	}
	
	private void initialize(){
		httpClient = HttpClientBuilder.create().build();
		request = new HttpPut(Properties.instance().getComponentServer().concat("/hfserver"));		
	}
	
	public boolean upload(){
		boolean status = false;
		initialize();		
		request.addHeader("User-Agent", HTTP.USER_AGENT);
		request.addHeader("Content-Type", "application/octet-stream");
		try {
			FileEntity fileEntity = new FileEntity(file);
			request.setEntity(fileEntity);
			HttpResponse response = httpClient.execute(request);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200
					|| response.getStatusLine().getStatusCode() == 204){
				status = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;		
	}
}
