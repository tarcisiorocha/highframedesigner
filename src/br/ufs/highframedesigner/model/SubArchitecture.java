/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victorpasqualino
 */
public class SubArchitecture {
    
    private Dimension size;
    private String id;
    private String name;
    private String host;
    private String model;
    private List<Component> components = new ArrayList<>();
    private List<Bind> binds = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    
    public void addComponent(Component c){
        components.add(c);
    }
    
    public void addBind(Bind b){
        binds.add(b);
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Bind> getBinds() {
        return binds;
    }

    public void setBinds(List<Bind> binds) {
        this.binds = binds;
    }

    @Override
    public String toString() {
        return name;
    }    
    
}
