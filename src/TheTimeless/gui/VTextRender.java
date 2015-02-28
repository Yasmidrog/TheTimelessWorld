package TheTimeless.gui;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author yew_mentzaki
 */
public final class VTextRender {

    private final Texture letter[] = new Texture[Short.MAX_VALUE];
    private Font f;

    public VTextRender(int size, String name) {
        f = new Font(name, 0, size);
        getWidth("ABCCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`'[](){}<>:,-.?\";/|\\!@#$%^&*_+-*=§АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя");
    }

    private Texture letter(char i) {
        if (letter[(int) i] != null) {
            return letter[(int) i];
        } else {
            /*
             Making empty buffer to get character's width.
             */
            BufferedImage im = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
            Graphics g = im.getGraphics();
            FontMetrics fm = g.getFontMetrics(f);
            int w = fm.charWidth(i);
            int h = f.getSize() + (int)(f.getSize()/4.6);
            /*
             Making the java.awt.BufferedImage with character.
             */
            if (w == 0) {
                w = 1;
                h = 1;
            }
            im = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g2 = (Graphics2D) im.getGraphics();
            g2.setFont(f);
            g2.setColor(Color.WHITE);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            g2.drawString(i + "", 0, f.getSize());

            {
                /*
                 Converting image to the Texture.
                 */
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(im, "png", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    Texture t = TextureLoader.getTexture("PNG", is);
                    letter[(int) i] = t;
                    return t;
                } catch (IOException ex) {
                    System.out.println("\"" + i + "\" isn't loaded.");
                }
                return null;

            }
        }
    }

    public int getWidth(String text) {
        int width = 0;
        String[] fText = text.split("\n");
        for (String str : fText) {
            int w = 0;
            for (char c : str.toCharArray()) {
                Texture t = letter(c);
                if (t == null) {
                    continue;
                }

                w += t.getImageWidth();
            }
            if (w > width) {
                width = w;
            }
        }
        return width;
    }

    public String splitString(String text, int maxWidth, boolean wordwrap) {
        if (getWidth(text) > maxWidth) {
            String result = "";
            if (wordwrap || (!text.contains("\n"))) {
                int w = 0,spaceW=0;
                String[] fText = text.split(" ");
                for (String str : fText) {
                    spaceW=getWidth(str + " ");
                    if (str.contains("\n"))
                        w = 0;
                    if (spaceW + w <= maxWidth) {
                        result += (str + " ");
                        w += spaceW;
                    } else if (spaceW + w >= maxWidth) {
                        w = 0;
                        result += "\n";
                    }
                }
            } else if (!wordwrap) {
                float w = 0,charW=0;
                for (char c : text.toCharArray()) {
                    if (c == '\n') {
                        w = 0;
                    }
                    charW=getWidth(Character.toString(c)) * 1.25f;
                    if (w +charW  >= maxWidth) {
                        w = 0;
                        result += "\n";
                    } else if (w + charW <= maxWidth) {
                        w += charW;
                        result += c;
                    }
                }
            }
            return result;
        }
        else  return text;
    }

    public void drawString(String text, int fx, int fy, org.newdawn.slick.Color clr) {
        int x = fx, y = fy;
        glColor4f(clr.r, clr.g, clr.b, clr.a);
        for (char c : text.toCharArray()) {
            Texture t = letter(c);
            if (t == null) {
                continue;
            }
            t.bind();
            if (c == '\n') {
                y += f.getSize() + 5;
                x = fx;
            }
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2i(x, y);
            glTexCoord2f(t.getWidth(), 0);
            glVertex2i(x + t.getImageWidth(), y);
            glTexCoord2f(t.getWidth(), t.getHeight());
            glVertex2i(x + t.getImageWidth(), y + t.getImageHeight());
            glTexCoord2f(0, t.getHeight());
            glVertex2i(x, y + t.getImageHeight());
            glEnd();
            x += t.getImageWidth();
        }
    }

    public int getHeight() {
        return f.getSize() + 5;
    }
}
