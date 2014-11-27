/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victorpasqualino
 */
public final class Component {
    
    private Point location;
    private Dimension size;
    private String id;
    private ComponentGeneric componentGeneric;
    private List<Interface> interfaces = new ArrayList<>();

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComponentGeneric getComponentGeneric() {
        return componentGeneric;
    }

    public void setComponentGeneric(ComponentGeneric componentGeneric) {
        this.componentGeneric = componentGeneric;
    }
    
    public void addInterface(Interface interface1){
        interfaces.add(interface1);
    }
    
    public List<Interface> getInterfaces(){
        return interfaces;
    }
   
}
