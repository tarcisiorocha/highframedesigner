package br.ufs.highframedesigner.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import br.ufs.highframedesigner.util.Properties;
import br.ufs.highframedesigner.util.Zipper;

public final class GenericComponentDownloader {
	
	private HttpClient httpClient;
	private HttpGet request;
	private File file =  new File(Properties.instance().getComponentsPath(), "components.zip");
	
	private void initialize(){
		httpClient = HttpClientBuilder.create().build();
		request = new HttpGet(Properties.instance().getComponentServer().concat("/components.zip"));		
	}
	
	public void download(){
		initialize();		
		request.addHeader("User-Agent", HTTP.USER_AGENT);
		try {
			HttpResponse response = httpClient.execute(request);
			InputStream ir = response.getEntity().getContent();
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length = 0;
			while ((length = ir.read(b)) != -1) {				
				outputStream.write(b, 0, length);
			}			
			ir.close();
			outputStream.flush();
			outputStream.close();
			
			 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			unzipAfterDownload();
		}
	}
	
	public void unzipAfterDownload(){
		Zipper zipper = new Zipper();
		File directory = new File(Properties.instance().getComponentsPath());
		try {
			zipper.unzip(file, directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
