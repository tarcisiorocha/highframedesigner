package br.ufs.highframedesigner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ComponentGeneric {
    
    private String name;
    private List<ComponentInterface> interfaces = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<ComponentInterface> getInterfaces(){
        return this.interfaces;
    }
    
    public void setInterfaces(List<ComponentInterface> interfaces){
        this.interfaces = interfaces;
    }
    
    public void addInterface(ComponentInterface cI){
        interfaces.add(cI);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComponentGeneric other = (ComponentGeneric) obj;
        return Objects.equals(this.name, other.name);
    }    
    
}
