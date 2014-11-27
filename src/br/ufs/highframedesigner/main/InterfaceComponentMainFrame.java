/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.main;

import br.ufs.highframedesigner.components.IPanelProperties;
import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.ComponentTypeEnum;
import javax.swing.SwingUtilities;

/**
 *
 * @author victorpasqualino
 */
public class InterfaceComponentMainFrame extends javax.swing.JFrame {
    
	private static final long serialVersionUID = 7863824147014066396L;
	public InterfaceComponentMainFrame() {        
        initComponents();
        IPanelProperties.instance().initializeComboComponents();
        IPanelProperties.instance().initializeComboSubArch();
    }

    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        iPanelProperties1 = IPanelProperties.instance();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuNew = new javax.swing.JMenu();
        menuItemNewArchitecture = new javax.swing.JMenuItem();
        menuItemNewSubArchitecture = new javax.swing.JMenuItem();
        menuOpen = new javax.swing.JMenu();
        menuItemOpenArch = new javax.swing.JMenuItem();
        menuItemSubArch = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        menuItemClose = new javax.swing.JMenuItem();
        menuItemSave = new javax.swing.JMenuItem();
        menuItemExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuItemsubArchFolder = new javax.swing.JMenuItem();
        menuItemDeploy = new javax.swing.JMenuItem();
        menuItemExport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("High Frame Designer");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("mainFrame"); // NOI18N

        jScrollPane.setAutoscrolls(true);
        jScrollPane.setName(""); // NOI18N
        jScrollPane.setPreferredSize(new java.awt.Dimension(500, 500));

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        menuFile.setMnemonic('F');
        menuFile.setText("File");
        menuFile.setToolTipText("");

        menuNew.setMnemonic('N');
        menuNew.setText("New");

        menuItemNewArchitecture.setMnemonic('A');
        menuItemNewArchitecture.setText("Architecture");
        menuItemNewArchitecture.setToolTipText("");
        menuItemNewArchitecture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNewArchitectureActionPerformed(evt);
            }
        });
        menuNew.add(menuItemNewArchitecture);

        menuItemNewSubArchitecture.setMnemonic('S');
        menuItemNewSubArchitecture.setText("SubArchitecture");
        menuItemNewSubArchitecture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNewSubArchitectureActionPerformed(evt);
            }
        });
        menuNew.add(menuItemNewSubArchitecture);

        menuFile.add(menuNew);

        menuOpen.setMnemonic('O');
        menuOpen.setText("Open");
        menuOpen.setToolTipText("");

        menuItemOpenArch.setText("Architecture");
        menuItemOpenArch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpenArchActionPerformed(evt);
            }
        });
        menuOpen.add(menuItemOpenArch);

        menuItemSubArch.setText("SubArchitecture");
        menuItemSubArch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSubArchActionPerformed(evt);
            }
        });
        menuOpen.add(menuItemSubArch);

        menuFile.add(menuOpen);

        jMenuItem1.setText("Execute Deploy");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuFile.add(jMenuItem1);

        menuItemClose.setText("Close Project");
        menuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCloseActionPerformed(evt);
            }
        });
        menuFile.add(menuItemClose);

        menuItemSave.setMnemonic('S');
        menuItemSave.setText("Save Project");
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuItemSave);

        menuItemExit.setMnemonic('E');
        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemExit);

        jMenuBar1.add(menuFile);

        menuEdit.setText("Edit");

        menuItemsubArchFolder.setText("Choose SubArch Folder");
        menuItemsubArchFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemsubArchFolderActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemsubArchFolder);

        menuItemDeploy.setText("Choose Deploy Folder");
        menuItemDeploy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeployActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemDeploy);

        menuItemExport.setText("Export as image");
        menuEdit.add(menuItemExport);

        jMenuBar1.add(menuEdit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(iPanelProperties1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iPanelProperties1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        MainFrameController.instance().exit();
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveActionPerformed
        MainFrameController.instance().save();
    }//GEN-LAST:event_menuItemSaveActionPerformed

    private void menuItemNewArchitectureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewArchitectureActionPerformed
        MainFrameController.instance().newArchitecture(jScrollPane, componentType);
        componentType = ComponentTypeEnum.IArchitecture;
        IPanelProperties.instance().repaintComponent();
        repaint();        
    }//GEN-LAST:event_menuItemNewArchitectureActionPerformed

    private void menuItemNewSubArchitectureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewSubArchitectureActionPerformed
        MainFrameController.instance().newSubArchitecture(jScrollPane, componentType);
        componentType = ComponentTypeEnum.ISubArchitecture;
        IPanelProperties.instance().repaintComponent();        
        repaint();        
    }//GEN-LAST:event_menuItemNewSubArchitectureActionPerformed

    private void menuItemOpenArchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenArchActionPerformed
        MainFrameController.instance().openArch(jScrollPane, componentType);
        componentType = ComponentTypeEnum.IArchitecture;
        SwingUtilities.updateComponentTreeUI(jScrollPane);
    }//GEN-LAST:event_menuItemOpenArchActionPerformed

    private void menuItemSubArchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSubArchActionPerformed
        MainFrameController.instance().openSubArch(jScrollPane, componentType);
        componentType = ComponentTypeEnum.ISubArchitecture;
        SwingUtilities.updateComponentTreeUI(jScrollPane);
    }//GEN-LAST:event_menuItemSubArchActionPerformed

    private void menuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCloseActionPerformed
        MainFrameController.instance().close(jScrollPane, componentType);
        componentType = ComponentTypeEnum.None;
        MainFrameController.instance().setComponentType(componentType);
        IPanelProperties.instance().repaintComponent();
        SwingUtilities.updateComponentTreeUI(jScrollPane);
    }//GEN-LAST:event_menuItemCloseActionPerformed

    private void menuItemDeployActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeployActionPerformed
        MainFrameController.instance().chooseDeployFolder();
    }//GEN-LAST:event_menuItemDeployActionPerformed

    private void menuItemsubArchFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemsubArchFolderActionPerformed
        MainFrameController.instance().chooseSubArchFolder();
    }//GEN-LAST:event_menuItemsubArchFolderActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        MainFrameController.instance().deploy();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
   

    private br.ufs.highframedesigner.components.IPanelProperties iPanelProperties1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuItemClose;
    private javax.swing.JMenuItem menuItemDeploy;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemExport;
    private javax.swing.JMenuItem menuItemNewArchitecture;
    private javax.swing.JMenuItem menuItemNewSubArchitecture;
    private javax.swing.JMenuItem menuItemOpenArch;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JMenuItem menuItemSubArch;
    private javax.swing.JMenuItem menuItemsubArchFolder;
    private javax.swing.JMenu menuNew;
    private javax.swing.JMenu menuOpen;
    private ComponentTypeEnum componentType;
}
