/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import static br.ufs.highframedesigner.util.Util.getStringFormated;
import static br.ufs.highframedesigner.util.Util.getStringWithoutTitle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.controller.MoveController;
import br.ufs.highframedesigner.controller.ResizeController;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.ComponentGeneric;
import br.ufs.highframedesigner.model.ComponentInterface;
import br.ufs.highframedesigner.util.Messages;
import br.ufs.highframedesigner.util.Util;


public final class IComponent extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private static final String NAME = "<< Component >>";
    private JLabel labelName = new JLabel();
    private JLabel labelId = new JLabel();
    
    private JPanel panelMover = new JPanel();
    private JPanel panelResizer = new JPanel();
    private JPanel panelRemover = new JPanel();
    private JPanel panelInterfaces = new JPanel();
    
    private final MoveController componentMover = new MoveController(this, panelMover);
    private final ResizeController componentResizer = new ResizeController(this, panelResizer);
    
    private List<IInterface> iInterfaces = new ArrayList<>();
    private final LineBorder lineBorder = (LineBorder) BorderFactory.createLineBorder(Color.BLACK);
    private final ComponentGeneric componentGeneric;
    
    public IComponent(Point point, String id, ComponentGeneric componentGeneric){                
        this.componentGeneric = componentGeneric;
        setLocation(point);
        initialize();
        setId(id);
        initializeInterfaces();
    }
    
    private void initialize(){
        setBackground(Color.WHITE);
        setLayout(null);
        setMinimumSize(new Dimension(100, 100));
        setSize(new Dimension(180, 125));
        setBorder(BorderFactory.createTitledBorder(lineBorder, NAME, TitledBorder.CENTER, TitledBorder.BELOW_TOP, Font.getFont("arial"), Color.BLACK ));       
        componentMover.setEdgeInsets(new Insets(75, 3, 3, 3));
        
        initializePanelMover();
        add(panelMover);
        
        initializePanelResizer();
        add(panelResizer);
        
        initializePanelRemover();
        add(panelRemover);
        
        initializePanelInterfaces();
        add(panelInterfaces);        
    }
    
    private void initializeInterfaces() {
        for (ComponentInterface cInterface : componentGeneric.getInterfaces()){
            IInterface iInterface = new IInterface(cInterface);
            panelInterfaces.add(new JSeparator(JSeparator.HORIZONTAL));
            panelInterfaces.add(iInterface);
            panelInterfaces.repaint();
            setLabelImageLocation(iInterface);
            iInterfaces.add(iInterface);
        }
    }
    
    public void setLabelImageLocation(IInterface iInterface){
        ImgInterface imgLabel = iInterface.getLabelImage();
        int x, y;
        if (iInterface.hasBind()) {
            IBind iBind = iInterface.getIBind();
            IComponent other;
            if (iBind.getSource() == iInterface){
                other = (IComponent) iBind.getDestination().getParent().getParent();
            } else {
                other = (IComponent) iBind.getSource().getParent().getParent();
            }
            if (getLocationOnScreen().getX() > (other.getLocationOnScreen().getX() + other.getWidth())){
                x = getX() - imgLabel.getWidth() + 10;
                y = getY() + (panelInterfaces.getY()+iInterface.getY());
                imgLabel.setIcon(Util.getImageIconLeft(iInterface.getComponentInterface()));
            } else {
                x = getX() + getWidth() - 5;
                y = getY() + (panelInterfaces.getY()+iInterface.getY());
                imgLabel.setIcon(Util.getImageIcon(iInterface.getComponentInterface()));
            }                
        } else {
            x = getLocation().x + getWidth() - 5;
            y = getLocation().y + (panelInterfaces.getY()+iInterface.getY());
            imgLabel.setIcon(Util.getImageIcon(iInterface.getComponentInterface()));
        }
        imgLabel.setLocation(new Point(x, y));        
    }
    
    private void initializePanelInterfaces() {
        panelInterfaces.setBackground(Color.WHITE);
        panelInterfaces.setLayout(new BoxLayout(panelInterfaces, BoxLayout.PAGE_AXIS));
        panelInterfaces.setLocation(3, 50);
        panelInterfaces.setSize(getWidth()-7, getHeight()-60);
    }    
    
    private void initializePanelMover(){
        panelMover.setBackground(Color.WHITE);
        panelMover.setLocation(3, 16);
        panelMover.setSize(getWidth()- 7, 32);
        panelMover.setLayout(new BoxLayout(panelMover, BoxLayout.PAGE_AXIS));
        labelId.setAlignmentY(RIGHT_ALIGNMENT);
        labelName.setAlignmentY(RIGHT_ALIGNMENT);
        labelName.setText(getStringFormated(Messages.NAME, componentGeneric.getName()));
        panelMover.add(labelId);
        panelMover.add(labelName);
    }
    
    private void initializePanelResizer(){
        //panelResizer.setBackground(new Color(180,254,169));  
        panelResizer.setBackground(Color.WHITE);       
        panelResizer.setLocation(getWidth()-13, getHeight()-13);
        panelResizer.setSize(10, 10);
        panelResizer.setToolTipText("Resize");
    }
    
    private void initializePanelRemover(){
        panelRemover.setBackground(Color.RED);        
        panelRemover.setLocation(getWidth()-13, 3);
        panelRemover.setSize(10, 10);
        panelRemover.addMouseListener(mouseListenerRemover);
        panelRemover.addMouseMotionListener(mouseListenerRemover);
        panelRemover.setToolTipText("Remove Component");
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        panelRemover.setLocation(getWidth()-13, 3);
        panelMover.setSize(getWidth()- 7, 32);
        panelInterfaces.setSize(getWidth()-7, getHeight()-60);
        panelResizer.setLocation(getWidth()-13, getHeight()-13);
        for (IInterface ii : iInterfaces){
            setLabelImageLocation(ii);
        }
    }
    
    @Override
    public void removeAll(){
        boolean achou = false;
        for (IInterface ii : getIInsterfaces()) {
            if (ii.hasBind()){
                IBind iBind = ii.getIBind();
                IInterface other = ii.getIBind().getSource() == ii ? ii.getIBind().getDestination() : ii.getIBind().getSource();
                other.setIBind(null);                   
                if (ii.getIBind().getBindType() == BindTypeEnum.E){
                    ((ISubArchitecture) other.getParent().getParent().getParent()).removeBind(iBind);
                    IArchitecture.instance().removeBind(iBind);
                    achou = true;
                }
                ((ISubArchitecture) getParent()).removeBind(iBind);
                iBind.setSource(null);
                iBind.setDestination(null);                               
            }
            ((ISubArchitecture) getParent()).remove(ii.getLabelImage());
        }
        labelName = null;
        labelId = null;
        panelInterfaces = null;
        panelMover = null;
        panelRemover = null;
        panelResizer = null;
        iInterfaces = null;
        super.removeAll();
        if(achou) IArchitecture.instance().repaint();
    }
    
    public boolean hasExternalBind(){
        for (IInterface ii : iInterfaces){
            if (ii.getIBind() == null) continue;
            if (ii.getIBind().getBindType() == BindTypeEnum.E){
                return true;
            }
        }
        return false;
    }
    
    public String getId(){
        return getStringWithoutTitle(labelId.getText());
    }
     
    public void setId(String id){        
        labelId.setText(getStringFormated(Messages.ID, id));            
    }
    
    public String getComponentName(){
        return getStringWithoutTitle(labelName.getText());
    }
    
    public void setComponentName(String name){        
        labelName.setText(getStringFormated(Messages.NAME, name));        
    }
    
    public List<IInterface> getIInsterfaces(){
        return iInterfaces;
    }
    
    public IInterface getIInterfaceWithName(String name){
        for (IInterface ii : iInterfaces){
            if (ii.getComponentInterface().getName().equals(name)){
                return ii;
            }
        }
        return null;
    }
    
    public IInterface getIInterfaceAtPointOnScreen(Point pointOnScreen){
        for (IInterface ii : iInterfaces){
            Point location = ii.getLocationOnScreen();
            Point point = new Point(pointOnScreen.x - location.x, pointOnScreen.y - location.y);
            if (ii.contains(point)){
                return ii;
            }
        }
        return null;
    }
    
    public MouseAdapter mouseListenerRemover = new MouseAdapter(){
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            MainFrameController.instance().setComponentSelected(null);
            ISubArchitecture isa = (ISubArchitecture) IComponent.this.getParent();
            isa.removeIComponent(IComponent.this);
            isa.repaint();
        }
    };

   
}
