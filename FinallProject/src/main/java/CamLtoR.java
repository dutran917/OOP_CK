
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MyPC
 */
public class CamLtoR extends JPanel{

    ArrayList<Area> areavat;
    Area areacam;
    public CamLtoR(ArrayList<Area> area1,Area area2)
    {
        this.areavat = area1;
        this.areacam = area2;
    }
    public void paintComponent(Graphics g)
    {
//        r.setHeight(r.getHeight()*20);
//        r.setLength(r.getLength()*20);
//        r.setWidth(r.getWidth()*20);
//        
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics2D g2d = (Graphics2D) g;
        Area cam_tmp=(Area)areacam.clone();
        System.out.println("nghia dep zai");
        for(Area t: areavat)
        {
            areacam.subtract(t);            
        }
//        g2d.fill(areacam);

//        for(Area t: areavat)
//        {
//            Area vat_tmp=(Area)t.clone();
//            vat_tmp.subtract(cam_tmp);
//            t.subtract(vat_tmp); 
//            g2d.fill(t);
//        }
        Area vat_tmp =(Area) areavat.get(0).clone();
        vat_tmp.subtract(areacam);
        areavat.get(0).subtract(vat_tmp);
        g2d.setColor(Color.white);
        g2d.fill(areacam);
        g2d.setColor(Color.GRAY);
        g2d.fill(areavat.get(0));
    }

    
}
