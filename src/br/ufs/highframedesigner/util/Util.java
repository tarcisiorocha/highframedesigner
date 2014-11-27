package br.ufs.highframedesigner.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import br.ufs.highframedesigner.model.ComponentInterface;
import br.ufs.highframedesigner.model.ProvideRequireEnum;

public final class Util {	
    
    public static final ImageIcon imgProvidedRight = new ImageIcon("img/requiredRightSide.png");
    public static final ImageIcon imgProvidedLeft = new ImageIcon("img/requiredLeftSide.png");
    public static final ImageIcon imageRequiredRight = new ImageIcon("img/providedRightSide.png");
    public static final ImageIcon imageRequiredLeft = new ImageIcon("img/providedLeftSide.png");
    
    public static final Color BACKGROUND_YELLOW = new Color(245,254,136);
            
    public static boolean isStringEmpty(String string) {
        return (string == null || string.trim().equals(""));
    }
    
    public static Point getMiddle(Rectangle r){
        return new Point(r.x+r.width/2,r.y+r.height/2);
    }
    
    public static String getStringFormated(String atrib, String str){
        return String.format(Messages.FORMAT_DATA, atrib, str);
    }
    
    public static String getStringWithoutTitle(String string){
        try {
            return string.replaceFirst(".*: ", "");
        } catch (Exception e){
            return "";
        }        
    }
    
    public static ImageIcon getImageIcon(ComponentInterface ci){
         return ci.getProvideRequire()==ProvideRequireEnum.P ? imgProvidedRight : imageRequiredRight;
    }
    
    public static ImageIcon getImageIconLeft(ComponentInterface ci){
        return ci.getProvideRequire()==ProvideRequireEnum.P ? imgProvidedLeft : imageRequiredLeft;
    }
    
}
