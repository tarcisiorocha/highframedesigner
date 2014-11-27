/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.controller;

import static br.ufs.highframedesigner.util.Util.isStringEmpty;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.ufs.highframedesigner.components.IArchitecture;
import br.ufs.highframedesigner.components.IBind;
import br.ufs.highframedesigner.components.IComponent;
import br.ufs.highframedesigner.components.IInterface;
import br.ufs.highframedesigner.components.IPanelProperties;
import br.ufs.highframedesigner.components.ISubArchitecture;
import br.ufs.highframedesigner.loader.Uploader;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.ComponentGeneric;
import br.ufs.highframedesigner.model.ComponentTypeEnum;
import br.ufs.highframedesigner.model.SubArchitecture;
import br.ufs.highframedesigner.util.Deployer;
import br.ufs.highframedesigner.util.Properties;
import br.ufs.highframedesigner.util.Zipper;

public class MainFrameController {
    
    private ComponentGeneric itemSelected;
    private Component componentSelected;
    private ComponentTypeEnum componentType;
    private List<ComponentGeneric> componentsGenericList;
    private List<SubArchitecture> subArchitectureList;
    private SubArchitecture subArchSelected;
    
    private static MainFrameController instance;
    
    public static MainFrameController instance(){
        if (instance==null){
            instance = new MainFrameController();
        }
        return instance;
    }
    
    private MainFrameController(){
        componentType = ComponentTypeEnum.None;
    }

    public List<SubArchitecture> getSubArchitectureList() {
        return subArchitectureList;
    }

    public void setSubArchitectureList(List<SubArchitecture> subArchitectureList) {
        this.subArchitectureList = subArchitectureList;
        IPanelProperties.instance().repaint();
    }

    public SubArchitecture getSubArchSelected() {
        return subArchSelected;
    }

    public void setSubArchSelected(SubArchitecture subArchSelected) {
        this.subArchSelected = subArchSelected;
    }
    
    public void exit(){
        int result = JOptionPane.showConfirmDialog(null, "Tem certeza?", "Sair", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            System.exit(0);
        }
    }
    
    public void newArchitecture(JScrollPane jScrollPane, ComponentTypeEnum componentType){
        if (componentType != null){
            if (IArchitecture.isOpen()) {
            	IArchitecture.close();
            } else if (ISubArchitecture.isOpen()){
            	ISubArchitecture.close();
            }
        }
        jScrollPane.setViewportView(IArchitecture.instance());
    }
    
    public void newSubArchitecture(JScrollPane jScrollPane, ComponentTypeEnum componentType){
        if (componentType != null){
            clearComponent(jScrollPane, componentType);                        
        }
        jScrollPane.setViewportView(ISubArchitecture.instance());
    }
    
    private void clearComponent(JScrollPane jScrollPane, ComponentTypeEnum componentType){
        if (componentType != null && componentType != ComponentTypeEnum.None) {
            int result = JOptionPane.showConfirmDialog(null, "Tem certeza ?", "Sair", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;
            if (componentType == ComponentTypeEnum.IArchitecture){
                jScrollPane.remove(IArchitecture.instance());
                IArchitecture.close();               
            } else {
                jScrollPane.remove(ISubArchitecture.instance());
                ISubArchitecture.close();
            }
        }        
    }
    
    public void setComponentSelected(Component cs) {
        this.componentSelected = cs;
        if (cs == null){
            setComponentType(ComponentTypeEnum.None);
        } else {
            setComponentType(ComponentTypeEnum.valueOf(cs.getClass().getSimpleName()));
        }        
        IPanelProperties.instance().repaintComponent();
    }

    public Component getComponentSelected() {
        return componentSelected;
    }
    
    public void setItemSelected(ComponentGeneric itemSelected){
        this.itemSelected = itemSelected;
    }
    
    public ComponentGeneric getItemSelected(){
        return this.itemSelected;
    }

    public ComponentTypeEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentTypeEnum componentType) {
        this.componentType = componentType;
    }

    public List<ComponentGeneric> getComponentsGenericList() {
        return componentsGenericList;
    }

    public void setComponentsGenericList(List<ComponentGeneric> componentsGenericList) {
        this.componentsGenericList = componentsGenericList;
    }
    
    public void save() {
        if (!IArchitecture.isOpen() && !ISubArchitecture.isOpen()) return;
        File file = chooseFileToSave();
        if (file == null) return;      
        XMLOutputter xmlOutput = new XMLOutputter();
        Document document;
        try {
            if (IArchitecture.isOpen()) {            
                IArchitecture iArchitecture = IArchitecture.instance();

                Element componentElement = new Element("architecture");
                componentElement.setAttribute(new Attribute("name", file.getName()));
                document = new Document(componentElement);

                writeSubArchitectureInFile(document, iArchitecture);

                writeExternalBindsInFile(iArchitecture, document.getRootElement());

            } else if (ISubArchitecture.isOpen()){
                document = saveISubArchitecture();            
            } else {
                return;
            }
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter(file));
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            file.delete();
        }
    }
    
    private Document saveISubArchitecture(){
        ISubArchitecture isa = ISubArchitecture.instance();
        Element elementSubArch = new Element("subarchitecture");
        elementSubArch.setAttribute("id", isStringEmpty(isa.getId()) ? null : isa.getId());
        elementSubArch.setAttribute("name", isStringEmpty(isa.getComponentName()) ? null : isa.getComponentName());
        elementSubArch.setAttribute("model", isStringEmpty(isa.getModel()) ? null : isa.getModel());
        elementSubArch.setAttribute("host", isStringEmpty(isa.getHost()) ? null : isa.getHost());
        elementSubArch.setAttribute("width", String.valueOf(isa.getWidth()));
        elementSubArch.setAttribute("height", String.valueOf(isa.getHeight()));
        
        Document document = new Document(elementSubArch);
        
        writeComponentsInFile(isa, elementSubArch);
        
        writeInternalBindsInFile(isa, elementSubArch);
        
        return document;
    }
    
    private void writeSubArchitectureInFile(Document document, IArchitecture iArchitecture){
        for (ISubArchitecture iSubArchitecture : iArchitecture.getISubArchitectures()){
            Element subArchitecture = new Element("subarchitecture");
            subArchitecture.setAttribute("id", isStringEmpty(iSubArchitecture.getId()) ? null : iSubArchitecture.getId());
            subArchitecture.setAttribute("name", isStringEmpty(iSubArchitecture.getComponentName()) ? null : iSubArchitecture.getComponentName());
            subArchitecture.setAttribute("model", isStringEmpty(iSubArchitecture.getModel()) ? null : iSubArchitecture.getModel());
            subArchitecture.setAttribute("host", isStringEmpty(iSubArchitecture.getHost()) ? null : iSubArchitecture.getHost());         
            subArchitecture.setAttribute("x", String.valueOf(iSubArchitecture.getX()));
            subArchitecture.setAttribute("y", String.valueOf(iSubArchitecture.getY()));
            subArchitecture.setAttribute("width", String.valueOf(iSubArchitecture.getWidth()));
            subArchitecture.setAttribute("height", String.valueOf(iSubArchitecture.getHeight()));
            
            writeComponentsInFile(iSubArchitecture, subArchitecture);
            
            writeInternalBindsInFile(iSubArchitecture, subArchitecture);
            
            document.getRootElement().addContent(subArchitecture);
        }
    }
    
    private void writeComponentsInFile(ISubArchitecture iSubArchitecture, Element element){
        for (IComponent iComponent : iSubArchitecture.getIComponents()){
            Element component = new Element("component");
            component.setAttribute("id", isStringEmpty(iComponent.getId()) ? null : iComponent.getId());
            component.setAttribute("name", isStringEmpty(iComponent.getComponentName()) ? null : iComponent.getComponentName());
            component.setAttribute("x", String.valueOf(iComponent.getX()));
            component.setAttribute("y", String.valueOf(iComponent.getY()));
            component.setAttribute("width", String.valueOf(iComponent.getWidth()));
            component.setAttribute("height", String.valueOf(iComponent.getHeight()));
            
            writeInterfaceInFile(iComponent, component);
            
            element.addContent(component);
        }
    }
    
    private void writeInterfaceInFile(IComponent iComponent, Element compElement){
        for (IInterface iInterface : iComponent.getIInsterfaces()){
            Element interfa = new Element("interface");
            interfa.setAttribute("name", iInterface.getComponentInterface().getName());
            interfa.setAttribute("id", isStringEmpty(iInterface.getID()) ? null : iInterface.getID());
            
            if (iInterface.getIBind() != null && 
                    iInterface.getIBind().getBindType() == BindTypeEnum.E)
                interfa.setAttribute("bindType", isStringEmpty(iInterface.getBindType()) ? null : iInterface.getBindType());
            
            compElement.addContent(interfa);
        }
    }
    
    private void writeInternalBindsInFile(ISubArchitecture iSubArchitecture, Element element) {
        for (IBind iBind : iSubArchitecture.getIInternalBinds()){
            
            if(iBind.getBindType() == BindTypeEnum.E) continue;
        
            IComponent source = (IComponent) iBind.getSource().getParent().getParent();
            IComponent destination = (IComponent) iBind.getDestination().getParent().getParent();
            Element component = new Element("bind");
            component.setAttribute("startX", String.valueOf(iBind.getStart().x));
            component.setAttribute("startY", String.valueOf(iBind.getStart().y));
            component.setAttribute("endX", String.valueOf(iBind.getEnd().x));
            component.setAttribute("endY", String.valueOf(iBind.getEnd().y));
            component.setAttribute("idSubArch", iSubArchitecture.getId());
            component.setAttribute("idCompSource", source.getId());
            component.setAttribute("idCompDestination", destination.getId());
            component.setAttribute("nmInterSource", iBind.getSource().getComponentInterface().getName());
            component.setAttribute("nmInterDestination", iBind.getDestination().getComponentInterface().getName());                  
            
            element.addContent(component);
        }
    }
    
    private void writeExternalBindsInFile(IArchitecture ia, Element root){
        for (IBind iBind : ia.getIExternalBinds()){
            IComponent source = (IComponent) iBind.getSource().getParent().getParent();
            IComponent destination = (IComponent) iBind.getDestination().getParent().getParent();
            ISubArchitecture iSource = (ISubArchitecture) source.getParent();
            ISubArchitecture iDestination = (ISubArchitecture) destination.getParent();
            Element component = new Element("externalBind");
            component.setAttribute("startX", String.valueOf(iBind.getStart().x));
            component.setAttribute("startY", String.valueOf(iBind.getStart().y));
            component.setAttribute("endX", String.valueOf(iBind.getEnd().x));
            component.setAttribute("endY", String.valueOf(iBind.getEnd().y));
            component.setAttribute("idSubArchSource", iSource.getId());
            component.setAttribute("idSubArchDestination", iDestination.getId());
            component.setAttribute("idCompSource", source.getId());
            component.setAttribute("idCompDestination", destination.getId());
            component.setAttribute("nmInterSource", iBind.getSource().getComponentInterface().getName());
            component.setAttribute("nmInterDestination", iBind.getDestination().getComponentInterface().getName());            
            component.setAttribute("typeBinding", isStringEmpty(iBind.getTypeBinding()) ? null : iBind.getTypeBinding());
            
            root.addContent(component);
        }
    }
    
    private File chooseFileToSave(){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText("Save");
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
            file = fileChooser.getSelectedFile();
        }
        return file;
    }
    
    public void openArch(JScrollPane jScrollPane, ComponentTypeEnum componentType){
        clearComponent(jScrollPane, componentType);
        File file = chooseFileToOpen();
        if (file == null || !file.getName().endsWith(".hfd")) return;
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {            
           doc = builder.build(file);
           createArchitectureDesign((Element) doc.getRootElement());
           jScrollPane.setViewportView(IArchitecture.instance());
        } catch (JDOMException | IOException ex) {			
        	Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException e){
        	JOptionPane.showMessageDialog(null, "SubArchitectetura com Component Genérico inválido", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void openSubArch(JScrollPane jScrollPane, ComponentTypeEnum componentType){
        clearComponent(jScrollPane, componentType);
        File file = chooseFileSubArchToOpen();
        if (file == null || !file.getName().endsWith(".sba")) return;
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {            
           doc = builder.build(file);
           createSubArchitectureDesign((Element) doc.getRootElement());
           jScrollPane.setViewportView(ISubArchitecture.instance());
        } catch (JDOMException | IOException ex) {			
        	Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException e){
        	JOptionPane.showMessageDialog(null, "SubArchitectetura com Component Genérico inválido", "ERROR", JOptionPane.ERROR_MESSAGE);
        }    
    }
    
    public void chooseSubArchFolder(){
        File directory = chooseFolder();
        if (directory == null) return;
        Properties.instance().setSubArchsPath(directory.getAbsolutePath());
    }
    
    public void chooseDeployFolder(){
        File directory = chooseFolder();
        if (directory == null) return;
        Properties.instance().setDeployPath(directory.getAbsolutePath());        
    }
    
    private File chooseFolder(){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);      
        fileChooser.setFileFilter(new FileNameExtensionFilter("Component Designer", "hfd"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
            file = fileChooser.getSelectedFile();
        }
        return file;
    }
    
    private File chooseFileToOpen(){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);      
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Component Designer", "hfd"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
            file = fileChooser.getSelectedFile();
        }
        return file;
    }
    
    private File chooseFileSubArchToOpen(){
        File file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);      
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "SubArchitecture Designer", "sba"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
            file = fileChooser.getSelectedFile();
        }
        return file;
    }
    
    private void createArchitectureDesign(Element root){
        IArchitecture iArchitecture = IArchitecture.instance();
        iArchitecture.setComponentName(root.getAttributeValue("name"));
        
        createSubArchitectures(iArchitecture, root);
        
        createExternalBinds(iArchitecture, root);
    }
    
    private void createSubArchitectures(IArchitecture iArchitecture, Element root){
        List<Element> subArchitectures = root.getChildren("subarchitecture");
        for (Element subArchitecture : subArchitectures) {
            int x = Integer.parseInt(subArchitecture.getAttributeValue("x"));
            int y = Integer.parseInt(subArchitecture.getAttributeValue("y"));
            int width = Integer.parseInt(subArchitecture.getAttributeValue("width"));
            int height = Integer.parseInt(subArchitecture.getAttributeValue("height"));
            String id = subArchitecture.getAttributeValue("id");
            String name = subArchitecture.getAttributeValue("name");
            String host = subArchitecture.getAttributeValue("host");
            String model = subArchitecture.getAttributeValue("model");
            Point point = new Point(x, y);
            Dimension dimension = new Dimension(width, height);
            
            ISubArchitecture iSubArchitecture = new ISubArchitecture(point, id);
            iSubArchitecture.setSize(dimension);
            iSubArchitecture.setModel(model);
            iSubArchitecture.setHost(host);
            iSubArchitecture.setComponentName(name);
            
            createComponents(iSubArchitecture, subArchitecture);
            
            createInternalBinds(iSubArchitecture, subArchitecture);
            
            iArchitecture.addISubArchitecture(iSubArchitecture);
        }
    }
    
    private void createSubArchitectureDesign(Element root) {
        ISubArchitecture isa = ISubArchitecture.instance();               
        String id = root.getAttributeValue("id");
        String name = root.getAttributeValue("name");
        String host = root.getAttributeValue("host");
        String model = root.getAttributeValue("model");
        
        isa.setId(id);
        isa.setComponentName(name);
        isa.setModel(model);
        isa.setHost(host);
        
        createComponents(isa, root);
        
        createInternalBinds(isa, root);
    }
    
    private void createComponents(ISubArchitecture iSubArchitecture, Element subArchitecture) {
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
            
            IComponent iComponent = new IComponent(point, id, c);
            iComponent.setSize(dimension);
            iComponent.setComponentName(name);
            
            createInterfaces(iComponent, component);
            
            iSubArchitecture.addIComponent(iComponent);
        }
    }
    
    private void createInterfaces(IComponent iComponent, Element compEl) {
        List<Element> interfaces = compEl.getChildren("interface");
        for (Element interfa : interfaces){
            String name = interfa.getAttributeValue("name");
            String id = interfa.getAttributeValue("id");
            String bindType = interfa.getAttributeValue("bindType");
            
            IInterface ii = iComponent.getIInterfaceWithName(name);
            ii.setID(id);
            if (bindType != null)
                ii.setBindType(bindType);
        }
    }
    
    private void createInternalBinds(ISubArchitecture isa, Element element){
        List<Element> binds = element.getChildren("bind");
        for (Element bind : binds){
            String idCompSource = bind.getAttributeValue("idCompSource");
            String idCompDest = bind.getAttributeValue("idCompDestination");
            String nmIntSource = bind.getAttributeValue("nmInterSource");
            String nmIntDest = bind.getAttributeValue("nmInterDestination");          
            
            IComponent compSource = isa.getIComponentWithId(idCompSource);
            IComponent compDest = isa.getIComponentWithId(idCompDest);
            
            IInterface intSource = compSource.getIInterfaceWithName(nmIntSource);
            IInterface intDest = compDest.getIInterfaceWithName(nmIntDest);
                    
            IBind iBind = new IBind();
            
            iBind.setSource(intSource);
            iBind.setDestination(intDest);
            iBind.setBindType(BindTypeEnum.I);
            
            isa.addBind(iBind);
            intSource.setIBind(iBind);
            intDest.setIBind(iBind);          
        }
    }
    
    private void createExternalBinds(IArchitecture iArchitecture, Element root) {
         List<Element> binds = root.getChildren("externalBind");
         for (Element bind : binds){
            String idSubArchSource = bind.getAttributeValue("idSubArchSource");
            String idSubArchDestination = bind.getAttributeValue("idSubArchDestination");
            String idCompSource = bind.getAttributeValue("idCompSource");
            String idCompDest = bind.getAttributeValue("idCompDestination");
            String nmIntSource = bind.getAttributeValue("nmInterSource");
            String nmIntDest = bind.getAttributeValue("nmInterDestination");
            String typeBinding = bind.getAttributeValue("typeBinding");
            
            ISubArchitecture iSubSource = iArchitecture.getISubArchitectureWithId(idSubArchSource);
            ISubArchitecture iSubDest = iArchitecture.getISubArchitectureWithId(idSubArchDestination);
            
            IComponent iCompSource = iSubSource.getIComponentWithId(idCompSource);
            IComponent iCompDest = iSubDest.getIComponentWithId(idCompDest);
            
            IInterface intSource = iCompSource.getIInterfaceWithName(nmIntSource);
            IInterface intDest = iCompDest.getIInterfaceWithName(nmIntDest);
            
            IBind iBind = new IBind();
            iBind.setSource(intSource);
            iBind.setDestination(intDest);
            iBind.setBindType(BindTypeEnum.E);
            
            intSource.setIBind(iBind);
            intSource.setBindType(typeBinding);
            intDest.setIBind(iBind);
            intDest.setBindType(typeBinding);
            iSubSource.addBind(iBind);
            iSubDest.addBind(iBind);
            iArchitecture.addBind(iBind);
         }
    }

    public void close(JScrollPane jScrollPane, ComponentTypeEnum componentType) {
        clearComponent(jScrollPane, componentType);        
        jScrollPane.setViewportView(null);
    }
    
    public ComponentGeneric getComponentGenericWithName(String name){
        for (ComponentGeneric c : componentsGenericList){
            if (c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }
    
    public void deploy(){
        if (!IArchitecture.isOpen() || IArchitecture.instance().getISubArchitectures().isEmpty()){
            return;
        }
        if ("".equals(Properties.instance().getDeployPath().trim())){
            File directory = chooseFolder();
            if (directory == null) return;
            Properties.instance().setDeployPath(directory.getAbsolutePath());
        }
        Deployer deployer = new Deployer();
        long ini = System.currentTimeMillis();
        boolean deployOk = deployer.deploy();
        long end = System.currentTimeMillis();
        System.out.println((end-ini));
        
        if(deployOk) {
        	File dir = new File(Properties.instance().getDeployPath());
        	File zipFile = new File(dir, "deployPlan.zip");
        	Zipper zipper = new Zipper();
        	File[] files = dir.listFiles(new FileFilter() {				
				@Override
				public boolean accept(File file) {					
					return file.getName().equals("plan.xml")
							|| file.getName().equals("data.xml");
				}
			});
        	try {
				zipper.zip(files, zipFile);
				Uploader uploader = new Uploader(zipFile);
	        	deployOk = uploader.upload();
			} catch (IOException e) {				
				e.printStackTrace();
				deployOk = false;
			}        	
        }
        
        if (deployOk) {
            JOptionPane.showMessageDialog(null, "Deploy Realizado com Sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao executar Deploy!", "Information", JOptionPane.ERROR_MESSAGE);
        }      
    }
    
}
