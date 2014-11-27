package br.ufs.highframedesigner.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import br.ufs.highframedesigner.components.IPanelProperties;
import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.ComponentGeneric;
import br.ufs.highframedesigner.model.ComponentInterface;
import br.ufs.highframedesigner.model.ProvideRequireEnum;
import br.ufs.highframedesigner.util.Properties;

public final class ComponentClassLoader {
	
	private final FileFilter componentsFilter = new FileFilter() {		
        @Override
        public boolean accept(File pathname) {
                return pathname.getName().endsWith(".xml");
        }
    };	
	
	public void loadComponents(){
		List<ComponentGeneric> lista = new ArrayList<>();
        SAXBuilder builder = new SAXBuilder();
        File directory = new File(Properties.instance().getComponentsPath());
        File[] componentFiles = directory.listFiles(componentsFilter);
        for (File file : componentFiles){
            try {
                Document doc = builder.build(file);
                Element root = doc.getRootElement();
                ComponentGeneric componentGeneric = createComponentGeneric(root);
                lista.add(componentGeneric);
            } catch (JDOMException | IOException ex) {
                Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MainFrameController.instance().setComponentsGenericList(lista);
        IPanelProperties.instance().repaint();
	}
	
	private ComponentGeneric createComponentGeneric(Element root){
		ComponentGeneric componentGeneric = new ComponentGeneric();
		
		String compName = root.getAttributeValue("name");
		componentGeneric.setName(compName);
		
		Element interfaces = root.getChild("interfaces");
		
		for (Element child : interfaces.getChildren()){
			ComponentInterface componentInterface = new ComponentInterface();
			
			String name = child.getAttributeValue("name");
			String signature = child.getAttributeValue("signature");
			String type = child.getAttributeValue("type");
			
			componentInterface.setName(name);
			componentInterface.setSignature(signature);
			componentInterface.setProvideRequire(ProvideRequireEnum.valueOf(type));
			
			componentGeneric.addInterface(componentInterface);			
		}
        
		return componentGeneric;
	}

}
