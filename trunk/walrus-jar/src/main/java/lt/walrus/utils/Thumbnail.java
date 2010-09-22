package lt.walrus.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Thumbnail {
    public static void createThumbnail(InputStream orig, String thumb, int maxDim) throws IOException {
            // Get the image from a file.
            //Image inImage = new ImageIcon(orig).getImage();
            Image inImage = ImageIO.read(orig);
            

            // Determine the scale.
            double scale = (double) maxDim / (double) inImage.getHeight(null);
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double) maxDim / (double) inImage.getWidth(null);
            }

            // Determine size of new image.
            // One of them
            // should equal maxDim.
            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            // Create an image buffer in
            // which to paint on.
            BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();

            // If the image is smaller than
            // the desired image size,
            // don't bother scaling.
            if (scale < 1.0d) {
                tx.scale(scale, scale);
            }

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();

            // JPEG-encode the image
            // and write to file.
            OutputStream os = new FileOutputStream(thumb);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            encoder.encode(outImage);
            os.close();
    }
}