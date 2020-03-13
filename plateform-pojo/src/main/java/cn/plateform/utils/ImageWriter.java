package cn.plateform.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 配置图像写入器 ,二维码生成工具
 */
@Component
public class ImageWriter {

    private static final int black = 0xFF000000;
    private static final int white = 0xFFFFFFFF; //白色

    private ImageWriter(){}

    public static BufferedImage toBufferedImage(BitMatrix matrix){
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int x=0;x<width;x++){
            for (int y=0;y<height;y++){
                image.setRGB(x,y,matrix.get(x,y)?black:white);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    //二维码测试代码
    public void test(){
        try {
            String content = "https://www.baidu.com";
            String path = "D:/CloudMusic";// 二维码保存的路径
            String codeName = UUID.randomUUID().toString();// 二维码的图片名
            String imageType = "jpg";// 图片类型
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file1 = new File(path, codeName + "." + imageType);
            ImageWriter.writeToFile(bitMatrix, imageType, file1);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
