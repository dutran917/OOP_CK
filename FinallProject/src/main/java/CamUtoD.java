
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
public class CamUtoD extends JPanel{
    public ArrayList<Area> areavat;
    public ArrayList<Area> areacam;

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        for (Area poly : areacam)
        {
            g2d.fill(poly);
        }

        randomcolor a =new randomcolor();
        for (Area obj : areavat)
        {
            g2d.setColor(a.randomColor());
            g2d.fill(obj);
        }
    }       
}
