/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.Bind;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.Component;
import br.ufs.highframedesigner.model.Interface;
import br.ufs.highframedesigner.model.SubArchitecture;
import br.ufs.highframedesigner.util.Util;

public class IArchitecture extends JPanel{    
    
	private static final long serialVersionUID = -8335011637674096829L;
	private static final String NAME = "<< Architecture >>";
    private final JLabel labelName = new JLabel();
    private int qtSubArchitectures;
    
    private final List<ISubArchitecture> iSubArchitectures = new ArrayList<>();
    private final List<IBind> iExternalBinds = new ArrayList<>();
    
    private static IArchitecture instance;
    
    public static IArchitecture instance(){
        if (instance == null){
            instance = new IArchitecture();
        }
        return instance;
    }
    
    private IArchitecture() {        
        initialize();
    }
    
    private void initialize(){
        setLayout(null);
        setVisible(true);
        setBackground(Util.BACKGROUND_YELLOW);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 0), NAME, TitledBorder.CENTER, TitledBorder.BELOW_TOP, Font.getFont(Font.SERIF), Color.BLACK ));
        setCursor(Cursor.getDefaultCursor());
        labelName.setText("Architecture1");
        add(labelName);
        addMouseListener(new MouseListener());
    }
    
    private void createISubArchitecture(Point point){
        ISubArchitecture iSubArchitecture = new ISubArchitecture(point, getDefaultId());
        iSubArchitectures.add(iSubArchitecture);
        add(iSubArchitecture);
        MainFrameController.instance().setComponentSelected(iSubArchitecture);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void addISubArchitecture(ISubArchitecture iSubArchitecture){
        iSubArchitectures.add(iSubArchitecture);
        add(iSubArchitecture);
        MainFrameController.instance().setComponentSelected(iSubArchitecture);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void addIBind(IBind iBind){
        iExternalBinds.add(iBind);
    }
    
    private String getDefaultId(){
       return "subArch" + qtSubArchitectures++;
    }

    private boolean canCreateSubArchitecture(MouseEvent e){
        return SwingUtilities.isLeftMouseButton(e) && !clickedInside(e.getPoint());                
    }
    
    private boolean clickedInside(Point point){
        for (ISubArchitecture iSubArchitecture : iSubArchitectures){
            if (iSubArchitecture.getBounds().contains(point)){
                return true;
            }
        }
        return false;
    }
    
    public void setArchitectureName(String name){
        labelName.setText(name);
    }
    
    public String getComponentName(){
        return labelName.getText();
    }
    
    public void setComponentName(String name){
        labelName.setText(name);
    }
    
    public List<ISubArchitecture> getISubArchitectures(){
        return iSubArchitectures;
    }
    
    public void removeISubArchitecture(ISubArchitecture isa){        
        isa.removeAll();
        remove(isa);
        iSubArchitectures.remove(isa);        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (IBind iBind : iExternalBinds){
            iBind.repaint();
            g.drawLine(iBind.getStart().x, iBind.getStart().y, iBind.getEnd().x, iBind.getEnd().y);
        }
    }
    
    public void removeBind(IBind iBind){
        iExternalBinds.remove(iBind);
    }
    
    public void addBind(IBind iBind){
        iExternalBinds.add(iBind);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public List<IBind> getIExternalBinds(){
        return iExternalBinds;
    }
    
    public ISubArchitecture getISubArchitectureAtPointOnScreen(Point pointOnScreen){         
        for (ISubArchitecture isa : iSubArchitectures){
            Point location = isa.getLocationOnScreen();
            Point point = new Point(pointOnScreen.x - location.x, pointOnScreen.y - location.y);           
            if (isa.contains(point)){
                return isa;
            }
        }
        return null;
    }
    
    public ISubArchitecture getISubArchitectureWithId(String id){
        for (ISubArchitecture isa : iSubArchitectures){
            if (isa.getId().equals(id))
                return isa;
        }
        return null;
    }
    
    public static boolean isOpen(){
        return IArchitecture.instance != null;
    }
    
    public static void close(){
        IArchitecture.instance = null;
    }
    
    private class MouseListener extends MouseAdapter{        
        @Override
        public void mouseClicked(MouseEvent e){
            if (canCreateSubArchitecture(e)){
                SubArchitecture subArch = MainFrameController.instance().getSubArchSelected();
                if (subArch.getName().equals("Default")){
                    createISubArchitecture(e.getPoint());
                } else {
                    createSubArchitecture(subArch, e.getPoint());
                }                
            }
        }
    }
    
    private void createSubArchitecture(SubArchitecture subArch, Point point){

        ISubArchitecture iSubArchitecture = new ISubArchitecture(point, subArch.getId());
        iSubArchitecture.setSize(ISubArchitecture.DEFAULT_SIZE);
        iSubArchitecture.setModel(subArch.getModel());
        iSubArchitecture.setHost(subArch.getHost());
        iSubArchitecture.setComponentName(subArch.getName());

        createComponents(iSubArchitecture, subArch);
        
        createBinds(iSubArchitecture, subArch);

        iSubArchitectures.add(iSubArchitecture);
        add(iSubArchitecture);
        MainFrameController.instance().setComponentSelected(iSubArchitecture);
        SwingUtilities.updateComponentTreeUI(this);
   }
    
   private void createComponents(ISubArchitecture iSubArchitecture, SubArchitecture subArch) {
        for (Component component : subArch.getComponents()) {      
           
            IComponent iComponent = new IComponent(component.getLocation(), component.getId(), component.getComponentGeneric());
            iComponent.setSize(component.getSize());
            iComponent.setComponentName(component.getComponentGeneric().getName());
            
            createIInterface(component, iComponent);

            iSubArchitecture.addIComponent(iComponent);
        }
    }
   
    private void createIInterface(Component component, IComponent iComponent) {
        for (Interface inter : component.getInterfaces()){
            IInterface ii = iComponent.getIInterfaceWithName(inter.getName());
            
            if (ii == null) continue;
            
            ii.setID(inter.getId());
            
        }
    }

    private void createBinds(ISubArchitecture isa, SubArchitecture subArch){
        for (Bind bind : subArch.getBinds()){
            String idCompSource = bind.getIdCompSource();
            String idCompDest = bind.getIdCompDest();
            String nmIntSource = bind.getNmIntSource();
            String nmIntDest = bind.getNmIntDest();

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
    
}
