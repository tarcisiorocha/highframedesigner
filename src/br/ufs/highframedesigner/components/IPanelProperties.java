/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import br.ufs.highframedesigner.controller.MainFrameController;
import br.ufs.highframedesigner.model.BindTypeEnum;
import br.ufs.highframedesigner.model.ComponentGeneric;
import br.ufs.highframedesigner.model.ComponentTypeEnum;
import br.ufs.highframedesigner.model.SubArchitecture;
import br.ufs.highframedesigner.util.Messages;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author victorpasqualino
 */
public class IPanelProperties extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelChooseComp;
    private JLabel labelChooseSubArch;
    private JLabel labelHost;
    private JLabel labelID;
    private JLabel labelModel;
    private JLabel labelName;
    private JLabel labelProperties;
    private JLabel labelBind;
    private JTextField textFieldHost;
    private JTextField textFieldID;
    private JTextField textFieldModel;
    private JTextField textFieldName;
    private JTextField textFieldBind;
    private JSeparator jSeparator1, jSeparator2;
    private JComboBox<ComponentGeneric> comboComponents;
    private JComboBox<SubArchitecture> comboSubArchitecture;
    private JButton unmakeBind;
    
    private static IPanelProperties instance;
    private KeyListener keyListener = new KeyListener();
    
    public static IPanelProperties instance(){
        if (instance==null){
            instance = new IPanelProperties();
        }
        return instance;
    }
    
    public IPanelProperties() {
        initialize();
    }
    
    private void initialize(){
        setAlignmentX(LEFT_ALIGNMENT);
        setCursor(Cursor.getDefaultCursor());
        
        comboComponents = new JComboBox<>();
        comboComponents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onComboComponentsActionPerformed();
            }
        });        
        
        comboSubArchitecture = new JComboBox<>();
        comboSubArchitecture.setEnabled(false);
        comboSubArchitecture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onComboSubArchsActionPerformed();
            }
        });        
        
        labelChooseComp = new JLabel("Select Component");
        labelHost = new JLabel(Messages.HOST);
        labelID = new JLabel(Messages.ID);
        labelModel = new JLabel(Messages.MODEL);
        labelName = new JLabel(Messages.NAME);
        labelProperties = new JLabel("Component Properties");
        labelChooseSubArch = new JLabel("Select SubArchitecture");
        labelBind = new JLabel("External Bind Type");
        
        textFieldName = new JTextField(50);        
        textFieldModel = new JTextField(50);
        textFieldHost = new JTextField(50);        
        textFieldID = new JTextField(50);
        textFieldBind = new JTextField(50);
        
        textFieldID.addKeyListener(keyListener);
        textFieldHost.addKeyListener(keyListener);
        textFieldModel.addKeyListener(keyListener);
        textFieldName.addKeyListener(keyListener);
        textFieldBind.addKeyListener(keyListener);
        
        jSeparator1 = new JSeparator(SwingConstants.HORIZONTAL);
        jSeparator2 = new JSeparator(SwingConstants.HORIZONTAL);
        
        unmakeBind = new JButton("UnmakeBind");
        unmakeBind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IInterface ii = (IInterface) MainFrameController.instance().getComponentSelected();
                ii.removeBind();
                IPanelProperties.instance().repaintComponent();
            }
        });
        
        repaintComponent();
        
        initializeLayout();
    }
    
    private void onComboComponentsActionPerformed() {
        MainFrameController.instance().setItemSelected((ComponentGeneric) comboComponents.getSelectedItem());
    }
    
    private void onComboSubArchsActionPerformed() {
        MainFrameController.instance().setSubArchSelected((SubArchitecture) comboSubArchitecture.getSelectedItem());
    }
    
    public void initializeComboComponents(){
        if (MainFrameController.instance().getComponentsGenericList() == null) return;
        for (ComponentGeneric c : MainFrameController.instance().getComponentsGenericList()){
            comboComponents.addItem(c);
        }
    }
    
    public void initializeComboSubArch(){
        if (MainFrameController.instance().getSubArchitectureList() == null) return;
        for (SubArchitecture s : MainFrameController.instance().getSubArchitectureList()){
            comboSubArchitecture.addItem(s);
        }
    }
    
    public void repaintComponent(){
        Component component = MainFrameController.instance().getComponentSelected();
        ComponentTypeEnum cType = MainFrameController.instance().getComponentType();
        comboSubArchitecture.setEnabled(IArchitecture.isOpen());
        comboComponents.setEnabled(IArchitecture.isOpen() || ISubArchitecture.isOpen());
        switch(cType){
            case ISubArchitecture:
                ISubArchitecture iS = (ISubArchitecture) component;
                loadISubArchitectureProperties(iS);
                break;
            case IComponent:
                IComponent iC = (IComponent) component;
                loadIComponentProperties(iC);
                break;
            case IInterface:
                IInterface iI = (IInterface) component;
                loadIInterfaceProperties(iI);
                break;
            default:
                labelName.setVisible(false);
                textFieldName.setVisible(false);                
                labelID.setVisible(false);
                textFieldID.setVisible(false);
                labelModel.setVisible(false);
                textFieldModel.setVisible(false);
                labelHost.setVisible(false);
                textFieldHost.setVisible(false);
                labelBind.setVisible(false);
                textFieldBind.setVisible(false);
                textFieldBind.setText(null);
                unmakeBind.setVisible(false);
                break;            
        }
        repaint();
    }
    
    public void loadISubArchitectureProperties(ISubArchitecture iSubArchitecture){
        labelName.setVisible(true);
        textFieldName.setVisible(true);                
        labelID.setVisible(true);
        textFieldID.setVisible(true);
        labelModel.setVisible(true);
        textFieldModel.setVisible(true);
        labelHost.setVisible(true);
        textFieldHost.setVisible(true);
        labelBind.setVisible(false);
        textFieldBind.setVisible(false);
        unmakeBind.setVisible(false);
        textFieldName.setText(iSubArchitecture.getComponentName());
        textFieldID.setText(iSubArchitecture.getId());
        textFieldHost.setText(iSubArchitecture.getHost());
        textFieldModel.setText(iSubArchitecture.getModel());  
    }
    
    public void loadIComponentProperties(IComponent iComponent){
        labelName.setVisible(false);
        textFieldName.setVisible(false);                
        labelID.setVisible(true);
        textFieldID.setVisible(true);
        labelModel.setVisible(false);
        textFieldModel.setVisible(false);
        labelBind.setVisible(false);
        textFieldBind.setVisible(false);
        labelHost.setVisible(false);
        textFieldHost.setVisible(false);
        textFieldID.setText(iComponent.getId());
        unmakeBind.setVisible(false);
        textFieldName.setText(null);
        textFieldHost.setText(null);
        textFieldModel.setText(null);
    }
    
    public void loadIInterfaceProperties(IInterface iInterface){
        labelName.setVisible(false);
        textFieldName.setVisible(false);                
        labelID.setVisible(true);
        textFieldID.setVisible(true);
        labelModel.setVisible(false);        
        textFieldModel.setVisible(false);
        labelHost.setVisible(false);
        textFieldHost.setVisible(false);
        if (iInterface.hasBind() && iInterface.getIBind().getBindType() == BindTypeEnum.E){
            labelBind.setVisible(true);
            textFieldBind.setVisible(true);
            textFieldBind.setText(iInterface.getBindType());
        } else {
            labelBind.setVisible(false);
            textFieldBind.setVisible(false);
            textFieldBind.setText(null);
        }
        unmakeBind.setVisible(true);
        textFieldID.setText(iInterface.getID());
        textFieldName.setText(null);
        textFieldHost.setText(null);
        textFieldModel.setText(null);    
        unmakeBind.setEnabled(iInterface.hasBind());
    }
    
    public void setISubArchitectureProperties(){
        ISubArchitecture is = (ISubArchitecture) MainFrameController.instance().getComponentSelected();
        is.setId(textFieldID.getText());
        is.setComponentName(textFieldName.getText());
        is.setHost(textFieldHost.getText());
        is.setModel(textFieldModel.getText());
    }
    
    public void setIComponentProperties(){
        IComponent ic = (IComponent) MainFrameController.instance().getComponentSelected();
        ic.setId(textFieldID.getText());       
    }
    
    public void setIInterfaceProperties(){
        IInterface ii = (IInterface) MainFrameController.instance().getComponentSelected();
        ii.setID(textFieldID.getText());
        if (ii.hasBind() && ii.getIBind().getBindType() == BindTypeEnum.E){
            ii.setBindType(textFieldBind.getText());
            IInterface other = ii.getIBind().getSource() == ii ? ii.getIBind().getDestination() :
                    ii.getIBind().getSource();
            other.setBindType(textFieldBind.getText());
        } else {
            ii.setBindType(null);
        }        
    }

    private void initializeLayout() {
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(comboComponents, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textFieldID)
                    .addComponent(textFieldName)
                    .addComponent(textFieldHost)
                    .addComponent(textFieldModel)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelChooseComp)
                                .addComponent(labelName)
                                .addComponent(labelHost)
                                .addComponent(labelModel)
                                .addComponent(labelProperties)
                                .addComponent(labelChooseSubArch, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                .addComponent(comboSubArchitecture, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(labelID)
                            .addComponent(unmakeBind)
                            .addComponent(labelBind))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(textFieldBind))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelChooseComp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboComponents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelChooseSubArch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboSubArchitecture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProperties)
                .addGap(10, 10, 10)
                .addComponent(labelID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelHost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelModel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelBind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldBind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGap(18, 18, 18)
                .addComponent(unmakeBind)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        
    }
    
    private class KeyListener extends KeyAdapter{
        @Override 
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER){               
                ComponentTypeEnum cType = MainFrameController.instance().getComponentType();
                switch(cType){
                    case ISubArchitecture:
                        setISubArchitectureProperties();
                        break;
                    case IComponent:
                        setIComponentProperties();
                        break;
                    case IInterface:
                        setIInterfaceProperties();
                        break;
                    default: break;
                }
                
            }
        }        
    } 
    
}
