/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

/**
 *
 * @author hebai
 */
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import javax.swing.Icon;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
public class CoverImageFrame extends JComponent {
    
    Icon image;
    int borderSize = 5;
    Color borderColor  = new Color(60,60,60);

    public Icon getImage() {
        return image;
    }
    
    public void setImage(String Path){
        this.image = new ImageIcon(Path);
        repaint();
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    
    @Override
    public void paint(Graphics g){
        if(image != null){
            int width = image.getIconWidth();
            int height = image.getIconHeight();
            
            BufferedImage masked = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = masked.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // No oval, just draw a rectangle now
            g2d.drawImage(toImage(image), 0, 0, width, height, null);
            g2d.dispose();
            
            Icon icon = new ImageIcon(masked);
            Rectangle size = getAutoSize(icon);
            Graphics2D g2 = (Graphics2D )g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(toImage(icon), size.getLocation().x, size.getLocation().y, size.getSize().width, size.getSize().height, null);
            
            if(borderSize > 0){
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderSize));
                // Draw rectangle border instead of oval
                g2.drawRect(size.x + borderSize / 2, size.y + borderSize / 2, size.width - borderSize, size.height - borderSize);
            }
        }
        super.paint(g);
    }

    private Image toImage(Icon icon){
        return ((ImageIcon) icon).getImage();
    }

    private Rectangle getAutoSize(Icon image){
        int w = getWidth();
        int h = getHeight();
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        double xScale = (double) w/iw;
        double yScale = (double) h/ih;
        double scale = Math.max(xScale, yScale);
        int width = (int) (scale*iw);
        int height = (int) (scale*ih);
        int x = (w - width) / 2;
        int y = (h - height) / 2;
        return new Rectangle(new Point(x, y), new Dimension(width, height));
    }
}