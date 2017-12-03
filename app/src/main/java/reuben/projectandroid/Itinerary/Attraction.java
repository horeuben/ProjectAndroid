package reuben.projectandroid.Itinerary;

public class Attraction {

    private int x;

    private static int[][] publicMat = new int[][]{
            {0,  17, 26, 35, 19, 84},
            {17,  0, 31, 38, 24, 85},
            {24, 29,  0, 10, 18, 85},
            {33, 38, 10,  0, 27, 92},
            {18, 23, 19, 28,  0, 83},
            {86, 87, 86, 96, 84,  0}
    };

    private static double[][] publicPriceMat = new double[][]{
            {0.00, 0.83, 1.18, 4.03, 0.88, 1.96},
            {0.83, 0.00, 1.26, 4.03, 0.98, 1.89},
            {1.18, 1.26, 0.00, 2.00, 0.98, 1.99},
            {1.18, 1.26, 0.00, 0.00, 0.98, 1.99},
            {0.88, 0.98, 0.98, 3.98, 0.00, 1.91},
            {1.88, 1.96, 2.11, 4.99, 1.91, 0.00}
    };

    private static int[][] taxiMat = new int[][]{
            {0,   3, 14, 19,  8, 30},
            {6,   0, 13, 18,  8, 29},
            {12, 14,  0,  9, 11, 31},
            {13, 14,  4,  0, 12, 32},
            {7,   8,  9, 14,  0, 30},
            {32, 29, 32, 36, 30,  0}
    };

    private static double[][] taxiPriceMat = new double[][]{
            {0.00,   3.22,  6.96,  8.50,  4.98, 18.40},
            {4.32,   0.00,  7.84,  9.38,  4.76, 18.18},
            {8.30,   7.96,  0.00,  4.54,  6.42, 22.58},
            {8.74,   8.40,  3.22,  0.00,  6.64, 22.80},
            {5.32,   4.76,  4.98,  6.52,  0.00, 18.40},
            {22.48, 19.40, 21.48, 23.68, 21.60,  0.00}
    };

    private static int[][] walkMat = new int[][]{
            {  0,  14,  69,  76,  28, 269, 215,  28,  19,  55},
            { 14,   0,  81,  88,  39, 264, 211,  27,  29,  51},
            { 69,  81,   0,  12,  47, 270, 166,  65,  91,  88},
            { 76,  88,  12,   0,  55, 285, 185,  78, 104, 102},
            { 28,  39,  47,  55,   0, 264, 188,  18,  44,  49},
            {269, 264, 270, 285, 264,   0, 174, 255, 287, 235},
            {215, 210, 165, 198, 188, 176,   0, 180, 224, 182},
            { 27,  24,  64,  90,  17, 257, 180,   0,  37,  23},
            { 16,  21,  86, 111,  44, 290, 225,  38,   0,  65},
            { 53,  47,  87, 113,  46, 240, 180,  22,  62,   0},
    };

    public Attraction(int x) {
        this.x = x;
    }

    public int timeToAttraction(Attraction attraction, int transport) {
        if (transport == 0)
            return taxiMat[this.x][attraction.x];
        else if (transport == 1)
            return publicMat[this.x][attraction.x];
        else
            return walkMat[this.x][attraction.x];
    }

    public double priceToAttraction(Attraction attraction, int transport) {
        if (transport == 0)
            return taxiPriceMat[this.x][attraction.x];
        else if (transport == 1)
            return publicPriceMat[this.x][attraction.x];
        else
            return 0.00;
    }

    public int getX() {
        return x;
    }

    public static void main(String[] args) {
        int total = 0;
        total += publicPriceMat[0][1];
        total += publicPriceMat[1][5];
        total += publicPriceMat[5][2];
        total += publicPriceMat[2][3];
        total += publicPriceMat[3][4];
        total += publicPriceMat[4][0];
        System.out.println(total);
    }

}