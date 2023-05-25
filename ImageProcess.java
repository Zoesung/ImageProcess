package com.company;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class Main {


    //rgb重新組回圖片
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

    public static int returnMedian(int arr[]){
        int size = arr.length;
        for (int i = 0 ; i<size; i++){
            for (int j = i ; j< size ; j++){
                if(arr[i] > arr[j]){
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return arr[4];
    }

    public static int returnMax(int arr[]){
        int size = arr.length;
        for (int i = 0 ; i<size; i++){
            for (int j = i ; j< size ; j++){
                if(arr[i] > arr[j]){
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return arr[8];
    }

    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];
        for(int i=1; i<histogram.length; i++) histogram[i] = 0;

        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int color = input.getRGB(i,j);
                int red = (color >> 16) & 0xff;
                histogram[red]++;
            }
        }

        return histogram;

    }

    public static void main(String[] args) throws IOException {
        // write your code here
        BufferedImage img = null; //創一個img物件
        File a = null; //創一個file物件保存路徑


        a = new File("D:\\123\\img_work\\sand.jpg");
        img = ImageIO.read(a);

        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage gray_img = new BufferedImage(width, height, img.getType());
        BufferedImage negative_img = new BufferedImage(width, height, img.getType());
        BufferedImage gammabig_img = new BufferedImage(width, height, img.getType());
        BufferedImage gammaavg_img = new BufferedImage(width, height, img.getType());
        BufferedImage gammasmall_img = new BufferedImage(width, height, img.getType());
        BufferedImage salt_img = new BufferedImage(width, height, img.getType());
        BufferedImage medianfilter_img = new BufferedImage(width, height, img.getType());
        BufferedImage laplaciain_img = new BufferedImage(width, height, img.getType());
        BufferedImage maxfilter_img = new BufferedImage(width, height, img.getType());
        //BufferedImage otsu_img = new BufferedImage(width, height, img.getType());

        //灰階
        //使用RGB三個分量的平均值作為灰度圖的灰度值
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y); //保存pixel

                int al = (p >> 24) & 0xff; //保存alpha
                int r = (p >> 16) & 0xff; //保存紅色值
                int g = (p >> 8) & 0xff; //保存綠色值
                int b = p & 0xff; //保存藍色值

                //計算RGB的平均值
                int avg = (r + g + b) / 3;

                //用計算的平均值替換像素的RGB值。
                p = (al << 24) | (avg << 16) | (avg << 8) | avg;

                gray_img.setRGB(x, y, p);
            }
        }
        a = new File("D:\\123\\img_work\\gray.jpg");
        ImageIO.write(gray_img, "jpg", a);


        //負片
        BufferedImage img1 = null;
        File b = null;

        b = new File("D:\\123\\img_work\\gray.jpg");
        img1 = ImageIO.read(b);

        int width_g = img1.getWidth();
        int height_g = img1.getHeight();

        //轉負片
        for (int y = 0; y < height_g; y++) {
            for (int x = 0; x < width_g; x++) {
                int p = img1.getRGB(x, y);
                int all = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int bl = p & 0xff;

                //255減掉rgb
                r = 255 - r;
                g = 255 - g;
                bl = 255 - bl;

                //新的rgb pixel
                p = (all << 24) | (r << 16) | (g << 8) | bl;
                //更改圖片pixel值
                negative_img.setRGB(x, y, p);
            }
        }
        b = new File("D:\\123\\img_work\\negative.jpg");
        ImageIO.write(negative_img, "jpg", b);

        //gamma
        File c = null;
        File d = null;
        File e = null;

        //找pixel最大最小值
        double[] minmax = new double[2];
        minmax[0] = 0;
        minmax[1] = 255;
        double gammasmall = 0.5;
        double gammaavg = 1;
        double gammabig = 1.5;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int ii = img1.getRGB(i, j) & 0xff;
                //System.out.println(ii);
                if (ii > minmax[0]) {
                    minmax[0] = ii;//最大
                }
                if (ii < minmax[1]) {
                    minmax[1] = ii;//最小
                }
            }
        }
        //(pixel值-最小值)/(最大值-最小值)的gamma次方*255
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double ii = img1.getRGB(i, j)& 0xff;
                int gsmall = (int) (Math.pow(((ii-minmax[1])/(minmax[0]-minmax[1])),gammasmall)*255); //gamma=0.5
                int gavg = (int) (Math.pow(((ii-minmax[1])/(minmax[0]-minmax[1])),gammaavg)*255); //gamma=1
                int gbig = (int) (Math.pow(((ii-minmax[1])/(minmax[0]-minmax[1])),gammabig)*255); //gamma=1.5

                int new1 = colorToRGB(255, gsmall, gsmall, gsmall);
                int new2 = colorToRGB(255, gavg, gavg, gavg);
                int new3 = colorToRGB(255, gbig, gbig, gbig);
                gammasmall_img.setRGB(i, j, new1);
                gammaavg_img.setRGB(i, j, new2);
                gammabig_img.setRGB(i, j, new3);
            }
        }

        c = new File("D:\\123\\img_work\\gammabig.jpg");
        ImageIO.write(gammabig_img, "jpg", c);
        d = new File("D:\\123\\img_work\\gammaavg.jpg");
        ImageIO.write(gammaavg_img, "jpg", d);
        e = new File("D:\\123\\img_work\\gammasmall.jpg");
        ImageIO.write(gammasmall_img, "jpg", e);


        //胡椒鹽雜訊
        Random rand = new Random();
        float rate = 0.95f;
        BufferedImage img2 = null; //創一個img物件
        File f = null; //創一個file物件保存路徑

        //讀取圖片
        f = new File("D:\\123\\img_work\\gammasmall.jpg");
        img2 = ImageIO.read(f);

        int width_gg = img2.getWidth();
        int height_gg = img2.getHeight();

        for (int i = 0; i < width_gg; i++) {
            for (int j = 0; j < height_gg; j++) {
                int color = img2.getRGB(i, j);
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int bs = color & 0xff;

                //隨機選擇pixel位置將灰階值設為0或255
                if(rand.nextFloat() > rate){
                    if(rand.nextFloat() > 0.5f){
                        int newPixel = colorToRGB(255, 255, 255, 255);
                        salt_img.setRGB(i, j, newPixel);
                    }
                    else{
                        int newPixel = colorToRGB(255, 0, 0, 0);
                        salt_img.setRGB(i, j, newPixel);
                    }
                }
                else{
                    int newPixel = colorToRGB(255, r, g, bs);
                    salt_img.setRGB(i, j, newPixel);
                }
            }
        }
        f = new File("D:\\123\\img_work\\salt.jpg");
        ImageIO.write(salt_img, "jpg", f);

        //中值濾波器
        BufferedImage img3 = null; //創一個img物件
        File g = null; //創一個file物件保存路徑

        //讀取圖片
        g = new File("D:\\123\\img_work\\salt.jpg");
        img3 = ImageIO.read(g);

        int window_ = 3; //window尺寸3*3
        int med = (window_ - 1)/2;

        int end_width = img3.getWidth()-med;
        int end_height = img3.getHeight()-med;

        //找filter
        for (int i = med ; i < end_width; i++) {
            for (int j = med ; j < end_height; j++) {
                int r_save[] = new int[9];
                int g_save[] = new int[9];
                int b_save[] = new int[9];

                int index = 0;
                for (int k = -1 ; k <= med; k++){
                    for (int l = -1 ; l <= med ; l++){
                        int color = img.getRGB(i+l, j+k);
                        //System.out.println(color+"");
                        r_save[index] = (color >> 16) & 0xff;
                        g_save[index]  = (color >> 8) & 0xff;
                        b_save[index]  = color & 0xff;
                        index++;
                    }
                }
                int r = 0;
                int gm = 0;
                int bm = 0;
                r = returnMedian(r_save);
                gm = returnMedian(g_save);
                bm = returnMedian(b_save);

                int newPixel = colorToRGB(255, r, r, r);
                medianfilter_img.setRGB(i, j, newPixel);
            }
        }
        g = new File("D:\\123\\img_work\\median.jpg");
        ImageIO.write(medianfilter_img, "jpg", g);

        //laplaciain
        int start_l = 1;

        BufferedImage img4 = null;
        File h = null;

        h = new File("D:\\123\\img_work\\gammaavg.jpg");
        img4 = ImageIO.read(h);

        //矩陣是0~2；人是1~3；所以需要-1，由人的角度轉移至電腦角度
        int end_width_l = img4.getWidth()-start_l;
        int end_height_l = img4.getHeight()-start_l;

        for (int i = start_l ; i < end_width_l; i++) {
            for (int j = start_l ; j < end_height_l; j++) {
                //原pixel
                int color = img4.getRGB(i, j);
                int r = (color >> 16) & 0xff;
                int gl = (color >> 8) & 0xff;
                int bl = color & 0xff;

                //上面pixel
                color = img4.getRGB(i-1, j);
                int r_top = (color >> 16) & 0xff;
                int g_top = (color >> 8) & 0xff;
                int b_top = color & 0xff;

                //左邊pixel
                color = img4.getRGB(i, j-1);
                int r_left = (color >> 16) & 0xff;
                int g_left = (color >> 8) & 0xff;
                int b_left = color & 0xff;

                //右邊pixel
                color = img4.getRGB(i, j+1);
                int r_right = (color >> 16) & 0xff;
                int g_right = (color >> 8) & 0xff;
                int b_right = color & 0xff;

                //下面pixel
                color = img4.getRGB(i+1, j);
                int r_bot = (color >> 16) & 0xff;
                int g_bot = (color >> 8) & 0xff;
                int b_bot = color & 0xff;

                //透過二階差分找到邊緣處，越邊緣處差分值越大（取絕對值後）
                int r_new = Math.abs( 4*r - r_top - r_left - r_right - r_bot);
                int g_new = Math.abs( 4*gl - g_top - g_left - g_right - g_bot);
                int b_new = Math.abs( 4*bl - b_top - b_left - b_right - b_bot);

                int newPixel = colorToRGB(255, r_new, g_new, b_new);
                laplaciain_img.setRGB(i, j, newPixel);
            }
        }
        h = new File("D:\\123\\img_work\\laplacian.jpg");
        ImageIO.write(laplaciain_img, "jpg", h);

        //最大值濾波器
        BufferedImage img5 = null; //創一個img物件
        File i = null; //創一個file物件保存路徑

        i = new File("D:\\123\\img_work\\laplacian.jpg");
        img5 = ImageIO.read(i);

        //找filter
        int filter_size_max = 3;
        int start_m = (filter_size_max-1)/2;

        int end_width_m = img5.getWidth()-start_m;
        int end_height_m = img5.getHeight()-start_m;

        for (int im = start_m ; im < end_width_m; im++) {
            for (int j = start_m ; j < end_height_m; j++) {
                int r_arr[] = new int[9];
                int g_arr[] = new int[9];
                int b_arr[] = new int[9];

                int index = 0;
                for (int k = -1 ; k <= start_m; k++){
                    for (int l = -1 ; l <= start_m ; l++){
                        int color = img5.getRGB(im+l, j+k);
                        //System.out.println(color+"");
                        r_arr[index] = (color >> 16) & 0xff;
                        g_arr[index]  = (color >> 8) & 0xff;
                        b_arr[index]  = color & 0xff;
                        index++;
                    }
                }
                int r = 0;
                int gmax = 0;
                int bmax = 0;

                //找各window最大值
                r = returnMax(r_arr);
                gmax = returnMax(g_arr);
                bmax = returnMax(b_arr);

                int newPixel = colorToRGB(255, r, gmax, bmax);
                maxfilter_img.setRGB(im, j, newPixel);
            }
        }
        File imax = null;
        imax = new File("D:\\123\\img_work\\maxfilter.jpg");
        ImageIO.write(maxfilter_img, "jpg", imax);

        //otsu
        BufferedImage original = null; //創一個img物件
        File j = null; //創一個file物件保存路徑

        j = new File("D:\\123\\img_work\\gammabig.jpg");
        original = ImageIO.read(j);

        BufferedImage otsu_img = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for(int aa=0; aa<256; aa++) sum += aa * histogram[aa];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for(int aa=0 ; aa<256 ; aa++) {
            wB += histogram[aa];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (aa * histogram[aa]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween >= varMax) {
                threshold = aa;
                varMax = varBetween;
            }
        }
        int newPixel;

        for(int aa=0; aa<original.getWidth(); aa++) {
            for(int bb=0; bb<original.getHeight(); bb++) {

                // Get pixels
                int color = original.getRGB(aa, bb);
                int red = (color >> 16) & 0xff;
                int alpha = (color >> 24) & 0xff;

                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = (0xff000000 | newPixel << 16 | newPixel << 8 | newPixel);
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                otsu_img.setRGB(aa, bb, newPixel);

            }
        }
        File ww = null;
        ww = new File("D:\\123\\img_work\\otsu.jpg");
        ImageIO.write(otsu_img, "jpg", ww);

    }
}

