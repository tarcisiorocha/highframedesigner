/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.model;

public final class ComponentInterface {
    
    private String name;
    private String signature;
    private ProvideRequireEnum provideRequire;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public ProvideRequireEnum getProvideRequire() {
        return provideRequire;
    }
    
    public void setProvideRequire(ProvideRequireEnum provideRequire) {
        this.provideRequire = provideRequire;
    }    
}
