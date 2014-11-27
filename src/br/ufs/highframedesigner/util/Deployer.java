package br.ufs.highframedesigner.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.ufs.highframedesigner.components.IArchitecture;
import br.ufs.highframedesigner.components.IBind;
import br.ufs.highframedesigner.components.IComponent;
import br.ufs.highframedesigner.components.IInterface;
import br.ufs.highframedesigner.components.ISubArchitecture;
import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.ComponentInterface;
import br.ufs.highframedesigner.model.ProvideRequireEnum;

public final class Deployer {
    
    private final XMLOutputter xmlOutput = new XMLOutputter();
    
    public boolean deploy(){
        File dir = new File(Properties.instance().getDeployPath()); 
        File fileData = new File(dir, "data.xml");
        File filePlan = new File(dir, "plan.xml");
        Document dataDoc = deployData();
        Document planDoc = deplayPlan();
        
        try {
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(dataDoc, new FileWriter(fileData));
            xmlOutput.output(planDoc, new FileWriter(filePlan));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ex);
            fileData.delete();
            filePlan.delete();
            return false;
        }        
    }
    
    private Document deployData(){
        Element arch = new Element("architecture");
        Document document = new Document(arch);
        
        createSubArchitectureInDeploy(document);
        
        return document;        
    }
    
    private Document deplayPlan(){
        Element plan = new Element("plan");
        Document document = new Document(plan);
        
        createDeploymentInDeploy(document);
        
        createRemoteBindInDeploy(document);
        
        return document;        
    }
    
    private void createSubArchitectureInDeploy(Document document){
        for (ISubArchitecture iSubArchitecture : IArchitecture.instance().getISubArchitectures()){
            Element subArchElem = new Element("subarchitecture");
            subArchElem.setAttribute("idsubarchitecture", iSubArchitecture.getId());
            
            createComponentsInDeploy(iSubArchitecture, subArchElem);
            
            createBindsInDeploy(iSubArchitecture, subArchElem);
            
            createExportableBindsInDeploy(iSubArchitecture, subArchElem);
            
            document.getRootElement().addContent(subArchElem);
        }
    }
    
    private void createComponentsInDeploy(ISubArchitecture iSubArchitecture, Element element){
        for (IComponent iComponent : iSubArchitecture.getIComponents()){
            Element component = new Element("component");
            component.setAttribute("id", iComponent.getId());
            component.setAttribute("name", iComponent.getComponentName());
            
            createInterfacesInDeploy(iComponent, component);
            
            element.addContent(component);
        }
    }
    
    private void createInterfacesInDeploy(IComponent iComponent, Element compElem) {
        for (IInterface iInterface : iComponent.getIInsterfaces()){
            ComponentInterface ci = iInterface.getComponentInterface();
            Element interElement;
            if (ci.getProvideRequire() == ProvideRequireEnum.P) {
                interElement = new Element("providedinterface");
            } else {
                interElement = new Element("requiredinterface");
            }
            
            interElement.setAttribute("idinterface", iInterface.getID());
            interElement.setAttribute("name", ci.getName());
            interElement.setAttribute("signature", ci.getSignature());
            
            compElem.addContent(interElement);
        }
    }
    
    private void createBindsInDeploy(ISubArchitecture iSubArchitecture, Element element) {
        for (IBind iBind : iSubArchitecture.getIInternalBinds()){
            if(iBind.getBindType() == BindTypeEnum.E) continue;
            IComponent source = (IComponent) iBind.getSource().getParent().getParent();
            IComponent destination = (IComponent) iBind.getDestination().getParent().getParent();
            IInterface iSO = iBind.getSource().getComponentInterface().getProvideRequire() == ProvideRequireEnum.P ? 
                    iBind.getSource() : iBind.getDestination();
            IInterface iDE = iBind.getSource() == iSO ? iBind.getDestination() : iBind.getSource();
            if (iBind.getSource().getComponentInterface().getProvideRequire() == ProvideRequireEnum.R){
                IComponent temp = source;
                source = destination;
                destination = temp;
            }
            Element component = new Element("binding");
            
            component.setAttribute("clientcomponent", destination.getId());
            component.setAttribute("clientinterface", iDE.getID());
            component.setAttribute("servercomponent", source.getId());
            component.setAttribute("serverinterface", iSO.getID());
            
            element.addContent(component);
        }
    }
    
    private void createExportableBindsInDeploy(ISubArchitecture iSubArchitecture, Element element) {
        for (IBind iBind : iSubArchitecture.getIInternalBinds()){
            if (iBind.getBindType() == BindTypeEnum.I) continue;
            
            IInterface s =  iSubArchitecture == iBind.getSource().getParent().getParent().getParent() ?
                    iBind.getSource() : iBind.getDestination();
            
            Element component = new Element("exportedinterface");
            
            component.setAttribute("idinterface", s.getID());
            
            element.addContent(component);
        }
    }

    private void createDeploymentInDeploy(Document document) {
        for (ISubArchitecture iSubArchitecture : IArchitecture.instance().getISubArchitectures()){
            Element subArchElem = new Element("deployment");
            
            subArchElem.setAttribute("idsubarchitecture", iSubArchitecture.getId());
            
            subArchElem.setAttribute("componentmodel", iSubArchitecture.getModel());
            
            subArchElem.setAttribute("host", iSubArchitecture.getHost());         
            
            document.getRootElement().addContent(subArchElem);
        }
    }
    
    private void createRemoteBindInDeploy(Document document) {
        for (IBind iBind : IArchitecture.instance().getIExternalBinds()) {
            
            IInterface source = iBind.getSource().getComponentInterface().getProvideRequire() == ProvideRequireEnum.P
                    ? iBind.getSource() : iBind.getDestination();
            IInterface dest = source == iBind.getSource() ? iBind.getDestination() : iBind.getSource();
            
            Element bindElem = new Element("remotebindind");
            
            bindElem.setAttribute("idexportedclientinterface", dest.getID());
            
            bindElem.setAttribute("idexportedserverinterface", source.getID());
            
            bindElem.setAttribute("bindingtype", iBind.getTypeBinding());         
            
            document.getRootElement().addContent(bindElem);
        }
    }
    
}
