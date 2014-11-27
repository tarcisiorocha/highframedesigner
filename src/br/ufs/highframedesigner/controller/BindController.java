/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.controller;

import br.ufs.highframedesigner.components.IArchitecture;
import br.ufs.highframedesigner.components.IBind;
import br.ufs.highframedesigner.components.IComponent;
import br.ufs.highframedesigner.components.IInterface;
import br.ufs.highframedesigner.components.ISubArchitecture;
import br.ufs.highframedesigner.model.BindTypeEnum;
import java.awt.Point;

/**
 *
 * @author victorpasqualino
 */
public class BindController {
    
    private static BindController instance;  
    private Point start;
    private Point end;
    
    public static BindController instance(){
        if (instance == null){
            instance = new BindController();
        }
        return instance;
    }
    
    private BindController(){  }

    public void execute(){
        try {
            if (start == null || end == null) 
                throw new IllegalStateException("Start or End id null");
            
            ISubArchitecture iSubSource = null;
            ISubArchitecture iSubDestination = null;
            
            if (IArchitecture.isOpen()){
                iSubSource = IArchitecture.instance().getISubArchitectureAtPointOnScreen(start);
                iSubDestination = IArchitecture.instance().getISubArchitectureAtPointOnScreen(end);
            } else if (ISubArchitecture.isOpen()){
                iSubSource = ISubArchitecture.instance();
                iSubDestination = ISubArchitecture.instance();
            }
            
            if (iSubSource == null || iSubDestination == null)
                throw new IllegalStateException("SubArchitecture(s) is/are null");        
            
            IInterface iInterSource = iSubSource.getIInterfaceAtPointOnScreen(start);
            IInterface iInterDestination = iSubDestination.getIInterfaceAtPointOnScreen(end);
            
            IComponent iCompSource = (IComponent) iInterSource.getParent().getParent();
            IComponent iCompDestination = (IComponent) iInterDestination.getParent().getParent();

            if (iCompSource == iCompDestination)
                throw new IllegalStateException("Interfaces of same component");
            
            if (iInterSource.hasBind() || iInterDestination.hasBind())
                throw new IllegalStateException("Component already binded");
            
            if (iInterSource.getComponentInterface().getProvideRequire() == iInterDestination.getComponentInterface().getProvideRequire())
                throw new IllegalStateException("Components of same type ");
            
            if (iSubSource == iSubDestination) {
                createInternalBind(iSubSource, iInterSource, iInterDestination);
            } else {
                createExternalBind(iSubSource, iSubDestination, iInterSource, iInterDestination);
            }
        } catch (IllegalStateException | NullPointerException e){
            clear();
            System.out.println(e.getMessage());
        }
    }
    
    private void createExternalBind(ISubArchitecture isS, ISubArchitecture isD, 
            IInterface iiS, IInterface iiD) {
        
        IBind iBind = new IBind();
        iBind.setSource(iiS);
        iBind.setDestination(iiD);
        iBind.setBindType(BindTypeEnum.E);
        
        IArchitecture.instance().addIBind(iBind);
        isS.addBind(iBind);
        isD.addBind(iBind);
        iiS.setIBind(iBind);
        iiD.setIBind(iBind);
        IArchitecture.instance().repaint();
    }
    
    private void createInternalBind(ISubArchitecture isa, IInterface iiS, IInterface iiD){       
                
        IBind iBind = new IBind();       
        iBind.setSource(iiS);
        iBind.setDestination(iiD);
        iBind.setBindType(BindTypeEnum.I);
        
        isa.addBind(iBind);
        iiS.setIBind(iBind);
        iiD.setIBind(iBind);
        isa.repaint();
    }
    
    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
    
    public void clear(){
        setStart(null);
        setEnd(null);
    }

  
    
}
