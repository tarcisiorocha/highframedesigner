/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.controller;

import br.ufs.highframedesigner.components.IArchitecture;
import br.ufs.highframedesigner.components.IComponent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author victorpasqualino
 */
public class ResizeController extends MouseAdapter {
    
    private final Component destinationComponent;
    private final Component source;
    private Point location;
     
    public ResizeController(Component destinationComponent, Component source) {
        this.destinationComponent = destinationComponent;
        this.source = source;
        registerComponent(source);
    }
    
    private void registerComponent(Component source) {
        source.addMouseListener(this);
        source.addMouseMotionListener(this);
    }   
   
    @Override
    public void mouseEntered(MouseEvent e){
        source.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        source.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
 
    @Override
    public void mousePressed(MouseEvent e) {        
        location = destinationComponent.getLocationOnScreen();        
        destinationComponent.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        int width = e.getXOnScreen()- location.x + 8;
        int height = e.getYOnScreen() - location.y + 8;
        
        Dimension d = new Dimension(width, height);
        Dimension minor = destinationComponent.getMinimumSize();
        if (d.width > minor.width && d.height > minor.height){
            destinationComponent.setSize(width, height);           
        } else if (d.width <= minor.width && d.height > minor.height) {
            destinationComponent.setSize(minor.width, height);           
        } else if (d.width > minor.width && d.height <= minor.height) {
            destinationComponent.setSize(width, minor.height);           
        }
        
        if (destinationComponent instanceof IComponent){
            if (((IComponent)destinationComponent).hasExternalBind()){
                IArchitecture.instance().repaint();
            }
        }        
        destinationComponent.getParent().repaint();
        SwingUtilities.updateComponentTreeUI(destinationComponent);
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        destinationComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
