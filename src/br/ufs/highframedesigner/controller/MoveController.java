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
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 *
 * @author victorpasqualino
 */
public final class MoveController extends MouseAdapter {
    private Insets dragInsets = new Insets(0, 0, 0, 0);
    private Dimension snapSize = new Dimension(1, 1);
    private Insets edgeInsets = new Insets(0, 0, 0, 0);
    private boolean autoLayout = false;

    private Component destinationComponent;
    private Component source;

    private Point pressed;
    private Point location;

    private boolean autoscrolls;
    private boolean potentialDrag;
    private boolean isMoveable = true;
    
    public MoveController() { }

    public MoveController(Component destinationComponent, Component component) {
        this.destinationComponent = destinationComponent;
        registerComponent(component);
    }
    
    public void registerComponent(Component component){
         component.addMouseListener(this);
    }
    
    public void setMoveable(boolean isMoveable){
        this.isMoveable = isMoveable;
    }
    
    public boolean isMoveable(){
        return isMoveable();
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }

    public void setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
    }

    public Insets getDragInsets() {
        return dragInsets;
    }
    
    public void setDragInsets(Insets dragInsets) {
        this.dragInsets = dragInsets;
    }

    public Insets getEdgeInsets() {
        return edgeInsets;
    }
    
    public void setEdgeInsets(Insets edgeInsets) {
        this.edgeInsets = edgeInsets;
    }

    public Dimension getSnapSize() {
        return snapSize;
    }
    
    public void setSnapSize(Dimension snapSize) {
        if (snapSize.width < 1  ||  snapSize.height < 1)
            throw new IllegalArgumentException("Snap sizes must be greater than 0");

        this.snapSize = snapSize;
    }
    
    @Override
    public void mouseEntered(MouseEvent e){
        destinationComponent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        destinationComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        MainFrameController.instance().setComponentSelected(destinationComponent);
        if (!isMoveable) return;
        source = e.getComponent();
        int width  = source.getSize().width  - dragInsets.left - dragInsets.right;
        int height = source.getSize().height - dragInsets.top - dragInsets.bottom;
        Rectangle r = new Rectangle(dragInsets.left, dragInsets.top, width, height);

        if (r.contains(e.getPoint())) setupForDragging(e);
    }

    private void setupForDragging(MouseEvent e) {
        if (!isMoveable) return;
        source.addMouseMotionListener( this );
        potentialDrag = true;
        pressed = e.getLocationOnScreen();
        location = destinationComponent.getLocation();

        if (destinationComponent instanceof JComponent) {
            JComponent jc = (JComponent)destinationComponent;
            autoscrolls = jc.getAutoscrolls();
            jc.setAutoscrolls( false );
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isMoveable) return;
        Point dragged = e.getLocationOnScreen();
        int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
        int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);

        int locationX = location.x + dragX;
        int locationY = location.y + dragY;

        while (locationX < edgeInsets.left)
                locationX += snapSize.width;

        while (locationY < edgeInsets.top)
                locationY += snapSize.height;

        Dimension d = getBoundingSize( destinationComponent );

        while (locationX + destinationComponent.getSize().width + edgeInsets.right > d.width)
                locationX -= snapSize.width;

        while (locationY + destinationComponent.getSize().height + edgeInsets.bottom > d.height)
                locationY -= snapSize.height;

        destinationComponent.setLocation(locationX, locationY);        
        
        if (destinationComponent instanceof IComponent){
            if (((IComponent)destinationComponent).hasExternalBind()){
                IArchitecture.instance().repaint();
            }
        }
        destinationComponent.getParent().repaint();
       
    }

    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    private Dimension getBoundingSize(Component source) {
        if (source instanceof Window) {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();
            return new Dimension(bounds.width, bounds.height);
        } else {
            return source.getParent().getSize();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!potentialDrag) return;

        source.removeMouseMotionListener( this );
        potentialDrag = false;       

        if (destinationComponent instanceof JComponent) {
            ((JComponent)destinationComponent).setAutoscrolls( autoscrolls );
        }

        if (autoLayout) {
            if (destinationComponent instanceof JComponent) {
                ((JComponent)destinationComponent).revalidate();
            } else {
                destinationComponent.validate();
            }
        }
    }
}
