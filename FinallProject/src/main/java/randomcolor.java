
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MyPC
 */
public class randomcolor {
    public Color randomColor()
    {
        int a = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);
        int c = (int)(Math.random()*256);
        return (new Color(a,b,c));
    }
}
