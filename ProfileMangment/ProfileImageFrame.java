package connecthub;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;// تنشيء صورة يمكن التعديل عليها
import java.awt.AlphaComposite;// تستخدم لدمج الصور
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class ProfileImageFrame extends JComponent {
    private Icon image; // الصورة
    private int borderSize = 3; // حجم الإطار
    private Color borderColor = new Color(60, 60, 60); // لون الإطار

   
    public ProfileImageFrame() {    
    }

    // Getter and Setter for Image
    public Icon getImage() {
        return image;
    }

    public void setImage(String Path){
        this.image = new ImageIcon(Path);
        repaint();
    }

    // Getter and Setter for Border Size
    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        repaint(); // تحديث المكون عند تغيير حجم الإطار
    }

    // Getter and Setter for Border Color
    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); // تحديث المكون عند تغيير لون الإطار
    }

    @Override
    public void paint(Graphics g) {
        if (image != null) {
            int width = image.getIconWidth();
            int height = image.getIconHeight();
            int diameter = Math.min(width, height);

            // قناع دائري
            BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = mask.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.fillOval(0, 0, diameter - 1, diameter - 1);
            g2d.dispose();

            // تطبيق القناع على الصورة
            BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            g2d = masked.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int x = (diameter - width) / 2;
            int y = (diameter - height) / 2;
            g2d.drawImage(toImage(image), x, y, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
            g2d.drawImage(mask, 0, 0, null);
            g2d.dispose();

            // رسم الصورة
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            Rectangle size = getAutoSize(image);
            g2.drawImage(toImage(new ImageIcon(masked)), size.getLocation().x, size.getLocation().y,
                    size.getSize().width, size.getSize().height, null);

            // رسم الإطار
            if (borderSize > 0) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderSize));
                g2.drawOval(size.x + (borderSize / 2), size.y + (borderSize / 2),
                        size.width - borderSize, size.height - borderSize);
            }
        }
        super.paint(g);
    }

    private Image toImage(Icon icon) {
        return ((ImageIcon) icon).getImage();
    }

    private Rectangle getAutoSize(Icon image) {
        int w = getWidth();
        int h = getHeight();
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        int x = (w - width) / 2;
        int y = (h - height) / 2;
        return new Rectangle(new Point(x, y), new Dimension(width, height));
    }
}
