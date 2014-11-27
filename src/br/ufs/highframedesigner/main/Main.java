/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.main;

import br.ufs.highframedesigner.loader.ComponentClassLoader;
import br.ufs.highframedesigner.loader.GenericComponentDownloader;
import br.ufs.highframedesigner.loader.SubArchClassLoader;
import br.ufs.highframedesigner.util.Properties;

public class Main {
    
     public static void main(String args[]) {        
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceComponentMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Properties.instance().loadProperties();
                new GenericComponentDownloader().download();
                new ComponentClassLoader().loadComponents();
                new SubArchClassLoader().loadSubArchs();
                new InterfaceComponentMainFrame().setVisible(true);
            }
        });
    }
    
}
