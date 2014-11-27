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
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.controller.MoveController;
import br.ufs.highframedesigner.controller.ResizeController;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.util.Messages;
import br.ufs.highframedesigner.util.Util;

public final class ISubArchitecture extends JPanel {
    
	private static final long serialVersionUID = 1L;
	public static final String NAME = "<< SubArchitecture >>";
    public static final Dimension DEFAULT_SIZE = new Dimension(600, 400);
    
    private JLabel labelId = new JLabel("ID: ");
    private JLabel labelName = new JLabel("Name: ");
    private JLabel labelModel = new JLabel("Model: ");
    private JLabel labelHost = new JLabel("Host: ");
    
    private JPanel panelMover = new JPanel();
    private JPanel panelResize;
    private JPanel panelRemover;
    
    private final LineBorder lineBorder = (LineBorder) BorderFactory.createLineBorder(Color.BLACK);
    
    private ResizeController componentResizer;
    private MoveController componentMover = new MoveController(this, panelMover);
    private final MouseListener mouseListener = new MouseListener();
    
    private List<IComponent> iComponents = new ArrayList<>();
    private List<IBind> iInternalBinds = new ArrayList<>();
    
    private static ISubArchitecture instance;
    
    private int minWidth = 200;
    private int minHeight = 200;
    
    private ISubArchitecture(){
        initializeSigleton();
    }
    
    public static ISubArchitecture instance(){
        if (instance == null){
            instance = new ISubArchitecture();
        }
        return instance;
    }
    
    public ISubArchitecture(Point point, String id){
        setLocation(point);        
        initialize();
        setId(id);
    }
    
    private void initialize() {
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(minWidth, minHeight));
        setSize(DEFAULT_SIZE);
        setBorder(BorderFactory.createTitledBorder(lineBorder, NAME, TitledBorder.CENTER, TitledBorder.BELOW_TOP, Font.getFont("arial"), Color.BLACK ));   
        setLayout(null);
        addMouseListener(mouseListener);
        
        panelResize = new JPanel();
        panelRemover = new JPanel();
        
        componentResizer = new ResizeController(this, panelResize);        
        
        initializePanelMover();        
        add(panelMover);
       
        initializePanelResize();
        add(panelResize);
        
        initializePanelRemover();
        add(panelRemover);
    }
    
    private void initializeSigleton(){
        setLayout(null);
        setBackground(Util.BACKGROUND_YELLOW);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 0), NAME, TitledBorder.CENTER, TitledBorder.BELOW_TOP, Font.getFont(Font.SERIF), Color.BLACK ));
        setCursor(Cursor.getDefaultCursor());
        
        addMouseListener(mouseListener);  
        
        componentMover.setMoveable(false);
        initializePanelMover();
        panelMover.setBackground(Util.BACKGROUND_YELLOW);
        add(panelMover);
    }
    
    private void initializePanelMover() {
        panelMover.setBackground(Color.WHITE);
        panelMover.setLocation(3, 16);
        panelMover.setSize(150, 70);
        panelMover.setLayout(new BoxLayout(panelMover, BoxLayout.PAGE_AXIS));       
        labelId.setAlignmentY(RIGHT_ALIGNMENT);
        labelName.setAlignmentY(RIGHT_ALIGNMENT);
        labelModel.setAlignmentY(RIGHT_ALIGNMENT);
        labelHost.setAlignmentY(RIGHT_ALIGNMENT);
        panelMover.add(labelId);
        panelMover.add(labelName);
        panelMover.add(labelModel);       
        panelMover.add(labelHost);        
    }
    
    private void initializePanelResize(){
        //panelResize.setBackground(new Color(180,254,169));
        panelResize.setBackground(Color.WHITE);
        panelResize.setLocation(getWidth()-13, getHeight()-13);
        panelResize.setSize(10, 10);        
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
        if (panelRemover != null) panelRemover.setLocation(getWidth()-13, 3);
        //if (panelMover != null) panelMover.setSize(getWidth() - 7, 60);
        if (panelResize != null) panelResize.setLocation(getWidth()-13, getHeight()-13);
        g.setColor(Color.BLACK);
        for (IBind iBind : iInternalBinds){
            iBind.repaint();
            if (iBind.getBindType() == BindTypeEnum.I){                
                g.drawLine(iBind.getStart().x, iBind.getStart().y, iBind.getEnd().x, iBind.getEnd().y);
            } else {
                int xS = iBind.getStart().x - getX();
                int yS = iBind.getStart().y - getY();
                int xE = iBind.getEnd().x - getX();
                int yE = iBind.getEnd().y - getY();
                g.drawLine(xS, yS, xE, yE);
            }            
        }
    }
    
    @Override
    public void removeAll(){
        for (IComponent ic : iComponents){
            ic.removeAll();
        }
        iComponents = null;
        labelId = null;
        labelHost = null;
        labelModel = null;
        labelName = null;
        panelMover = null;
        panelRemover = null;
        panelResize = null;
        super.removeAll();
    }
    
    public void addBind(IBind iBind){
        iInternalBinds.add(iBind);
    }
    
    public void removeBind(IBind iBind){
        iInternalBinds.remove(iBind);
    }
    
    private void createIComponent(Point point){
        String name = String.format("comp%d", iComponents.size()+1);
        IComponent iComponent = new IComponent(point, name, MainFrameController.instance().getItemSelected());
        addIComponent(iComponent);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void addIComponent(IComponent iComponent){
        iComponents.add(iComponent);
        add(iComponent);
        for (IInterface ii : iComponent.getIInsterfaces()) {
            add(ii.getLabelImage());
        }
    }
    
    public void removeIComponent(IComponent ic){
        ic.removeAll();
        remove(ic);
        iComponents.remove(ic);  
    }
    
    public String getId(){
        return getStringWithoutTitle(labelId.getText());
    }
     
    public void setId(String id){       
        labelId.setText(getStringFormated(Messages.ID, id));               
    }
    
    public String getModel(){
        return getStringWithoutTitle(labelModel.getText());
    }
    
    public void setModel(String model){       
        labelModel.setText(getStringFormated(Messages.MODEL, model));       
    }
    
    public String getHost(){
        return getStringWithoutTitle(labelHost.getText());
    }
    
    public void setHost(String host){        
        labelHost.setText(getStringFormated(Messages.HOST, host));       
    }
    
    public String getComponentName(){
        return getStringWithoutTitle(labelName.getText());
    }
    
    public void setComponentName(String name){         
        labelName.setText(getStringFormated(Messages.NAME, name));        
    }
    
    public List<IComponent> getIComponents(){
        return iComponents;
    }
    
    public List<IBind> getIInternalBinds(){
        return iInternalBinds;
    }
    
    private boolean canCreateIComponent(MouseEvent e){
        return SwingUtilities.isLeftMouseButton(e)
                && MainFrameController.instance().getItemSelected() != null 
                && !clickedInsideAnotheOne(e.getPoint());                
    }
    
    private boolean clickedInsideAnotheOne(Point p){
        for (IComponent iComponent : iComponents){
            if (iComponent.getBounds().contains(p))
                return true;
        }
        return false;
    }
    
    public IComponent getIComponentAtPointOnScreen(Point pointOnScreen){
        for (IComponent ic : iComponents){
            Point location = ic.getLocationOnScreen();
            Point point = new Point(pointOnScreen.x - location.x, pointOnScreen.y - location.y);
            if (ic.contains(point)){
                return ic;
            }
        }
        return null;
    }
    
    public IComponent getIComponentWithId(String id){
        for (IComponent ic : iComponents) {
            if (ic.getId().equals(id)){
                return ic;
            }
        }
        return null;
    }
    
    public IInterface getIInterfaceAtPointOnScreen(Point pointOnScreen){
        for (IComponent ic : iComponents){
            for (IInterface ii : ic.getIInsterfaces()){
                Point location = ii.getLabelImage().getLocationOnScreen();
                Point point = new Point(pointOnScreen.x - location.x, pointOnScreen.y - location.y);
                if (ii.getLabelImage().contains(point)){
                    return ii;
                }
            }           
        }
        return null;
    }
    
    public static boolean isOpen(){
        return ISubArchitecture.instance != null;
    }
    
    public static void close(){
        ISubArchitecture.instance = null;
    }
    
    private class MouseListener extends MouseAdapter {               
        @Override
        public void mouseClicked(MouseEvent e){
            if (canCreateIComponent(e)){                 
                createIComponent(e.getPoint());
            }
        }
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
           IArchitecture.instance().removeISubArchitecture(ISubArchitecture.this);
           IArchitecture.instance().repaint();
        }
    };

    
}
