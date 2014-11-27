package br.ufs.highframedesigner.loader;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.Bind;
import br.ufs.highframedesigner.model.Component;
import br.ufs.highframedesigner.model.ComponentGeneric;
import br.ufs.highframedesigner.model.Interface;
import br.ufs.highframedesigner.model.SubArchitecture;
import br.ufs.highframedesigner.util.Properties;

public final class SubArchClassLoader {	
    
    private final FileFilter subArchFileFilter = new FileFilter() {		
        @Override
        public boolean accept(File pathname) {
                return pathname.getName().endsWith(".sba");
        }
    };	
	
	public void loadSubArchs(){
        List<SubArchitecture> lista = new ArrayList<>();
        lista.add(getSubArchDefault());
        SAXBuilder builder = new SAXBuilder();
        File directory = new File(Properties.instance().getSubArchsPath());
        File[] subFiles = directory.listFiles(subArchFileFilter);
        for (File file : subFiles){
            try {
                Document doc = builder.build(file);
                Element root = doc.getRootElement();
                SubArchitecture sub = createSubArchitecture(root);
                lista.add(sub);
            } catch (JDOMException | IOException | NullPointerException ex) {
                Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException e){
            	JOptionPane.showMessageDialog(null, "SubArchitectetura com Component Genérico inválido", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        MainFrameController.instance().setSubArchitectureList(lista);
    }
    
    private SubArchitecture getSubArchDefault(){
        SubArchitecture sub = new SubArchitecture();
        sub.setName("Default");
        return sub;
    }
    
     private SubArchitecture createSubArchitecture(Element root) {
        SubArchitecture subArch = new SubArchitecture();
        String id = root.getAttributeValue("id");
        String name = root.getAttributeValue("name");
        String host = root.getAttributeValue("host");
        String model = root.getAttributeValue("model");
        
        subArch.setId(id);
        subArch.setName(name);
        subArch.setModel(model);
        subArch.setHost(host);
        
        createComponents(subArch, root);
        
        createBinds(subArch, root);
        return subArch;
    }
     
     private void createComponents(SubArchitecture subArch, Element subArchitecture) {
        List<Element> components = subArchitecture.getChildren("component");
        for (Element component : components) {
            String id = component.getAttributeValue("id");
            String name = component.getAttributeValue("name");
            int x = Integer.parseInt(component.getAttributeValue("x"));
            int y = Integer.parseInt(component.getAttributeValue("y"));
            int width = Integer.parseInt(component.getAttributeValue("width"));
            int height = Integer.parseInt(component.getAttributeValue("height"));
            Point point = new Point(x, y);
            Dimension dimension = new Dimension(width, height);
            
            ComponentGeneric c = MainFrameController.instance().getComponentGenericWithName(name);
            if (c == null) throw new IllegalStateException();
            
            Component comp = new Component();
            comp.setId(id);
            comp.setLocation(point);
            comp.setSize(dimension); 
            comp.setComponentGeneric(c);
            
            createInterfaces(comp, component);
          
            subArch.addComponent(comp);           
        }
    }
     
     private void createInterfaces(Component component, Element compEl) {
        List<Element> interfaces = compEl.getChildren("interface");
        for (Element interfa : interfaces){
            String name = interfa.getAttributeValue("name");
            String id = interfa.getAttributeValue("id");
            
            Interface inter = new Interface();           
            inter.setName(name);
            inter.setId(id);
            
            component.addInterface(inter);
        }
    }
    
    private void createBinds(SubArchitecture subArch, Element element){
        List<Element> binds = element.getChildren("bind");
        for (Element bind : binds){           
            String idCompSource = bind.getAttributeValue("idCompSource");
            String idCompDest = bind.getAttributeValue("idCompDestination");
            String nmIntSource = bind.getAttributeValue("nmInterSource");
            String nmIntDest = bind.getAttributeValue("nmInterDestination");            
                             
            Bind b = new Bind();
            
            b.setIdCompDest(idCompDest);
            b.setIdCompSource(idCompSource);
            b.setNmIntDest(nmIntDest);
            b.setNmIntSource(nmIntSource);
            
            subArch.addBind(b);
        }
    }

}
