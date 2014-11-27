/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import br.ufs.highframedesigner.model.BindTypeEnum;
import java.awt.Point;

/**
 *
 * @author victorpasqualino
 */
public class IBind {
    
    private IInterface source;
    private IInterface destination;
    private Point start;
    private Point end;
    private String typeBinding;
    private BindTypeEnum bindType;
    
    public void repaint(){        
        setStart(getStartPointBind());
        setEnd(getEndPointBind());
    }
    
    private Point getStartPointBind() {
        if (bindType == BindTypeEnum.I){ 
            Point location = source.getLabelImage().getLocation();
            int x = (int) (location.x + source.getLabelImage().getWidth()*0.75);
            int y = (int) (location.y + source.getLabelImage().getWidth()*0.25);
            Point newPoint = new Point(x,y);
            return newPoint;  
        } else if (bindType == BindTypeEnum.E) {
            Point locationScreen = source.getLabelImage().getLocationOnScreen();
            Point location = new Point (locationScreen.x - IArchitecture.instance().getLocationOnScreen().x,
                                locationScreen.y - IArchitecture.instance().getLocationOnScreen().y);
            int x = (int) (location.x + source.getLabelImage().getWidth()*0.75);
            int y = (int) (location.y + source.getLabelImage().getWidth()*0.25);
            Point newPoint = new Point(x,y);
            return newPoint;
        }
        return null;
    }
    
    private Point getEndPointBind() {
        if (bindType == BindTypeEnum.I){
            Point location = destination.getLabelImage().getLocation();
            int x = (int) (location.x + destination.getLabelImage().getWidth()*0.75);
            int y = (int) (location.y + destination.getLabelImage().getWidth()*0.25);
            Point newPoint = new Point(x,y);
            return newPoint;            
        } else if (bindType == BindTypeEnum.E) {
            Point locationScreen = destination.getLabelImage().getLocationOnScreen();
            Point location = new Point (locationScreen.x - IArchitecture.instance().getLocationOnScreen().x,
                                locationScreen.y - IArchitecture.instance().getLocationOnScreen().y);
            int x = (int) (location.x + destination.getLabelImage().getWidth()*0.75);
            int y = (int) (location.y + destination.getLabelImage().getWidth()*0.25);
            Point newPoint = new Point(x,y);
            return newPoint;
        }
        return null;
    }

    public IInterface getSource() {
        return source;
    }

    public void setSource(IInterface source) {
        this.source = source;
    }

    public IInterface getDestination() {
        return destination;
    }

    public void setDestination(IInterface destination) {
        this.destination = destination;
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

    public String getTypeBinding() {
        return typeBinding;
    }

    public void setTypeBinding(String typeBinding) {
        this.typeBinding = typeBinding;
    }

    public BindTypeEnum getBindType() {
        return bindType;
    }

    public void setBindType(BindTypeEnum bindType) {
        this.bindType = bindType;
    }
        
}
