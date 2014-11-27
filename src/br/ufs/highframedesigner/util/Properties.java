package br.ufs.highframedesigner.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import br.ufs.highframedesigner.loader.ComponentClassLoader;
import br.ufs.highframedesigner.loader.SubArchClassLoader;

public final class Properties {
    
    private static Properties instance;
    
    private String deployPath;
    private String subArchsPath;
    private String componentsPath;
    private String componentServerAddress;
    private File fileProperties;
    
    private Properties(){
    	URL url = getClass().getClassLoader().getResource("properties.xml");
    	fileProperties = new File(url.getFile()); 
    }
    
    public static Properties instance(){
        if(instance == null){
            instance = new Properties();
        }
        return instance;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
        changeDeployPath();
    }

    public String getSubArchsPath() {
        return subArchsPath;
    }

    public void setSubArchsPath(String subArchsPath) {        
        this.subArchsPath = subArchsPath;
        changeSubArchPath();
        new SubArchClassLoader().loadSubArchs();
    }   
    
    public String getComponentsPath() {
		return componentsPath;
	}

	public void setComponentsPath(String componentsPath) {
		this.componentsPath = componentsPath;
		changeComponentPath();
		new ComponentClassLoader().loadComponents();
	}
    
    public String getComponentServer() {
		return componentServerAddress;
	}

	public void setComponentServer(String componentServerAddress) {
		this.componentServerAddress = componentServerAddress;
	}
	
	public void loadProperties(){
        loadPropertiesFromFile();       
    }

	private void loadPropertiesFromFile(){		
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(fileProperties);
            Element root = doc.getRootElement();
            
            Element subArch = root.getChild("subArhcPath");
            this.subArchsPath = subArch.getAttributeValue("value");
            
            Element deploy = root.getChild("deployPath");
            this.deployPath = deploy.getAttributeValue("value");
            
            Element component = root.getChild("componentsPath");
            this.componentsPath = component.getAttributeValue("value");
            
            Element componentServer = root.getChild("componentServerAddress");
            this.componentServerAddress = componentServer.getAttributeValue("value");
            
        } catch (JDOMException | IOException | NullPointerException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void changeDeployPath(){
        SAXBuilder builder = new SAXBuilder();
        XMLOutputter xMLOutputter = new XMLOutputter();
        Document doc;
        try {
            doc = builder.build(fileProperties);
            Element root = doc.getRootElement();
            
            Element deploy = root.getChild("deployPath");
            deploy.setAttribute("value", getDeployPath());
            xMLOutputter.output(doc, new FileWriter(fileProperties));
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    private void changeSubArchPath(){
        SAXBuilder builder = new SAXBuilder();
        XMLOutputter xMLOutputter = new XMLOutputter();
        Document doc;
        try {
            doc = builder.build(fileProperties);
            Element root = doc.getRootElement();
            
            Element subArch = root.getChild("subArhcPath");
            subArch.setAttribute("value", getSubArchsPath());
            xMLOutputter.output(doc, new FileWriter(fileProperties));
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    private void changeComponentPath(){
        SAXBuilder builder = new SAXBuilder();
        XMLOutputter xMLOutputter = new XMLOutputter();
        Document doc;
        try {
            doc = builder.build(fileProperties);
            Element root = doc.getRootElement();
            
            Element deploy = root.getChild("componentsPath");
            deploy.setAttribute("value", getComponentsPath());
            xMLOutputter.output(doc, new FileWriter(fileProperties));
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }   
    
}
