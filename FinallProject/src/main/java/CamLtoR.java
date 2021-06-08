
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

   ArrayList<Area> areavat = new ArrayList<Area>();
    Area areacam;
    public CamLtoR(ArrayList<Area> area1,Area area2)
    {
        this.areavat = area1;
        this.areacam = area2;
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics2D g2d = (Graphics2D) g;
        Area cam = areacam;

//        Area a = areas.get(0);
//        a.intersect(cam);
//        double lech =300 - areacam.getBounds2D().getCenterX();
        g2d.setColor(Color.WHITE);
//        cam.getBounds2D().setRect(cam.getBounds2D().getX()+lech, cam.getBounds2D().getY(),cam.getBounds2D().getWidth() , cam.getBounds2D().getHeight());
        g2d.fill(cam);
        g2d.setColor(Color.RED);
        for(Area area : areavat)
        {
            area.intersect(cam);          
            g2d.fill(area);
        }
    }        
}
