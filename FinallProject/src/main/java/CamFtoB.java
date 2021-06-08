
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
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
public class CamFtoB extends JPanel{
    ArrayList<Area> areavat;
    Area areacam;
    public CamFtoB(ArrayList<Area> area1,Area area2)
    {
        this.areavat = area1;
        this.areacam = area2;
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics2D g2d = (Graphics2D) g;
        Area cam_tmp=(Area)areacam.clone();
        for(Area t: areavat)
        {
            areacam.subtract(t);
            g2d.setColor(Color.red);
            g2d.fill(t);
        }
        System.out.println(areavat.size()+"+++++++++++");
        g2d.setColor(Color.white);
        g2d.fill(areacam);
    }     
}
