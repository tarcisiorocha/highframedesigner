/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.model;

/**
 *
 * @author victorpasqualino
 */
public enum ComponentTypeEnum {
    
    IArchitecture("IArchitecture"), 
    ISubArchitecture("ISubArchitecture"), 
    IComponent("IComponent"), 
    IInterface("IInterface"),
    None("None");
    
    private final String label;
    
    ComponentTypeEnum(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    }
    
}
