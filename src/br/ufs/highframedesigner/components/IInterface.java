/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.ComponentInterface;
import br.ufs.highframedesigner.util.Messages;
import br.ufs.highframedesigner.util.Util;

public final class IInterface extends JPanel {
    
	private static final long serialVersionUID = 1L;
	public final JLabel labelID = new JLabel();
    public final JLabel labelName = new JLabel();
    public final JLabel labelBind = new JLabel();
    private ImgInterface labelImage = new ImgInterface();
    private IBind iBind;
    
    private ComponentInterface componentInterface;
    
    private MouseListener mouseListener = new MouseAdapter(){
        @Override
        public void mousePressed(MouseEvent evt) {
            if (SwingUtilities.isLeftMouseButton(evt)){
                MainFrameController.instance().setComponentSelected(IInterface.this);
            }
        }
    };
    
    public IInterface(ComponentInterface componentInterface){
        this.componentInterface = componentInterface;
        initialize();
    }
    
    private void initialize(){
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addMouseListener(mouseListener);
        labelName.setText("- ".concat(Util.getStringFormated(componentInterface.getName(), componentInterface.getSignature())));      
        
        labelBind.setText(null);
        initializeLabelImage();
        
        add(labelID);
        add(labelBind);
        add(labelName);
    }
    
    private void initializeLabelImage(){    
        if (ISubArchitecture.isOpen()){
            labelImage.setBackground(Util.BACKGROUND_YELLOW);
        }
        labelImage.setIcon(Util.getImageIcon(componentInterface));
        labelImage.setiInterface(this);
        labelImage.repaint();
    }
    
    public void removeBind(){
        if (iBind.getBindType() == BindTypeEnum.E) {
            ISubArchitecture s = (ISubArchitecture) this.getParent().getParent().getParent();
            ISubArchitecture d =  iBind.getSource() == this ? (ISubArchitecture) iBind.getDestination().getParent().getParent().getParent()
                    : (ISubArchitecture) iBind.getSource().getParent().getParent().getParent();
            IInterface other = iBind.getSource() == this ? iBind.getDestination() : iBind.getSource();
            s.removeBind(iBind);
            d.removeBind(iBind);
            IArchitecture.instance().removeBind(iBind);
            other.setIBind(null);
            setIBind(null);
            setBindType(null);
            other.setBindType(null);
            s.repaint(); d.repaint(); IArchitecture.instance().repaint();
        } else {
            ISubArchitecture is = (ISubArchitecture) this.getParent().getParent().getParent();
            is.removeBind(iBind);
            IInterface other = iBind.getSource() == this ? iBind.getDestination() : iBind.getSource();
            other.setIBind(null);
            setIBind(null);
            setBindType(null);
            other.setBindType(null);
            is.repaint();
        }        
    }
    
    public ComponentInterface getComponentInterface() {
        return componentInterface;
    }
    
    public void setID(String id){
        if(!Util.isStringEmpty(id)){
            labelID.setText(Util.getStringFormated(Messages.ID,id));
        } else {
            labelID.setText(null);
        }
    }
    
    public String getID(){
        return Util.getStringWithoutTitle(labelID.getText());
    }

    public void setBindType(String bindType){
        if(!Util.isStringEmpty(bindType)){
            labelBind.setText(Util.getStringFormated(Messages.BIND_TYPE,bindType));
        } else {
            labelBind.setText(null);
        }
        if (iBind != null) iBind.setTypeBinding(bindType);
    }
    
    public String getBindType(){
        return Util.getStringWithoutTitle(labelBind.getText());
    }
    
    public String getComponentName(){
        return Util.getStringWithoutTitle(labelName.getText());
    }

    public void setComponentInterface(ComponentInterface componentInterface) {
        this.componentInterface = componentInterface;
    }

    public boolean hasBind() {
        return iBind != null;
    }
    
    public IBind getIBind(){
        return iBind;
    }

    public void setIBind(IBind iBind) {
        this.iBind = iBind;
    }

    public ImgInterface getLabelImage() {
        return labelImage;
    }

    public void setLabelImage(ImgInterface labelImage) {
        this.labelImage = labelImage;
    }
    
}
