/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufs.highframedesigner.components;

import br.ufs.highframedesigner.controller.BindController;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author victorpasqualino
 */
public class ImgInterface extends JLabel{
    

	private static final long serialVersionUID = 1L;
	private IInterface iInterface;
    private final MouseListener mouseListener = new MouseListener();
    
    public ImgInterface(){
        setSize(40, 20);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public IInterface getiInterface() {
        return iInterface;
    }

    public void setiInterface(IInterface iInterface) {
        this.iInterface = iInterface;
    }
    
    private class MouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            BindController.instance().setStart(e.getLocationOnScreen());            
        }
        
        @Override
        public void mouseReleased(MouseEvent e){
            BindController.instance().setEnd(e.getLocationOnScreen());
            BindController.instance().execute();
        }
        
        @Override
        public void mouseDragged(MouseEvent e){
            if (!(e.getComponent().getParent() instanceof ISubArchitecture)) return;
            ISubArchitecture isa = (ISubArchitecture) e.getComponent().getParent();
            Point location = isa.getLocationOnScreen();            
            Graphics2D g2D = (Graphics2D) isa.getGraphics();
            g2D.setColor(Color.BLACK);
            Point start = BindController.instance().getStart();
            g2D.drawLine(start.x-location.x, start.y-location.y, e.getXOnScreen()-location.x, e.getYOnScreen()-location.y);
            isa.repaint(1000);
        }

    }
    
    
}
