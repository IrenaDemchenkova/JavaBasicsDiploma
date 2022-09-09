package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static java.lang.Math.max;

public class ConverterRealisation implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int widthImg = img.getWidth();
        int heightImg = img.getHeight();
        double ratioImg = Math.round((double) heightImg / (double) (widthImg) * 100) / 100.0;
        setTextColorSchema(new ColorSchemaRealisation());
        double ratioHeight = 1;
        double ratioWidth = 1;
        System.out.println(widthImg);
        System.out.println(heightImg);
        if (maxRatio != 0) {
            System.out.println((double) (heightImg / widthImg));
            if ((double) (widthImg / heightImg) > maxRatio || (double) (heightImg / widthImg) > maxRatio) {
                throw new BadImageSizeException(ratioImg, maxRatio);
            }
        }
        if (maxWidth != 0 && maxHeight != 0) {
            if (widthImg > maxWidth || heightImg > maxHeight) { //проверка на слишком большой размер
                //  throw new BadImageSizeException(ratioImg, maxRatio);
                ratioHeight = Math.round((double) heightImg / (double) (maxHeight) * 100) / 100.0;
                ratioWidth = Math.round((double) widthImg / (double) (maxWidth) * 100) / 100.0;
            } else {
                ratioHeight = 1;
                ratioWidth = 1;
            }
        }
        double ratioCorrectur = max(ratioHeight, ratioWidth); //к-т, на который будем уменьшать
        int newWidth = (int) (widthImg / ratioCorrectur); // вычисляем новую ширину
        int newHeight = (int) (heightImg / ratioCorrectur); // вычисляем новую высоту
        System.out.println("New picture dimensions: width " + newWidth + " and height " + newHeight);
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        var bwRaster = bwImg.getRaster();
        char[][] bwPixel = new char[newHeight][newWidth];
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                bwPixel[h][w] = c;
            }
        }
        StringBuilder resultString = new StringBuilder();
        for (int x = 0; x < newHeight; x++) {
            for (int y = 0; y < newWidth; y++) {
                resultString.append(bwPixel[x][y]);
                resultString.append(bwPixel[x][y]);
            }
            resultString.append("\n");
        }
        return resultString.toString();
    }
}
