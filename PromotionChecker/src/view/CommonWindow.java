/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Miro
 */
public abstract class CommonWindow extends JFrame{
    
    public CommonWindow(){
        setframeIcon();
    }
    
     private void setframeIcon(){
        try{
            InputStream imgStream = this.getClass().getResourceAsStream("/resources/main_icon.png");
            BufferedImage bi = ImageIO.read(imgStream);
            ImageIcon myImg = new ImageIcon(bi);
            this.setIconImage(myImg.getImage());
        }catch(Exception e){
            System.out.println(e);
        }   
    };
}
