/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int width;
    private int height;
    private Picture originalImage;
    private double[][] energyImage;
    //
    // public SeamCarver() {
    // }

    public SeamCarver(Picture picture) {

        if (picture == null) {
            throw new IllegalArgumentException("");
        }

        originalImage = new Picture(picture);
        width = picture.width();
        height = picture.height();


        energyImage = new double[height][width];
        int xBound = width - 1;
        int yBound = height - 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == xBound || y == 0 || y == yBound) {
                    energyImage[y][x] = 1000;
                }
                else {

                    double xValue = pixelEnergy(originalImage.getRGB(x - 1, y),
                                                originalImage.getRGB(x + 1, y));
                    double yValue = pixelEnergy(originalImage.getRGB(x, y - 1),
                                                originalImage.getRGB(x, y + 1));


                    energyImage[y][x] = Math.sqrt(xValue + yValue);

                }
            }
        }
    }


    public Picture picture() {
        return new Picture(originalImage);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("");
        }


        return energyImage[y][x];
    }


    private double pixelEnergy(int leftPixelRGB, int rightPixelRGB) {

        int r = ((rightPixelRGB >> 16) & 0xFF) - ((leftPixelRGB >> 16) & 0xFF);
        int g = ((rightPixelRGB >> 8) & 0xFF) - ((leftPixelRGB >> 8) & 0xFF);
        int b = ((rightPixelRGB) & 0xFF) - ((leftPixelRGB) & 0xFF);

        return r * r + g * g + b * b;
    }


    public int[] findVerticalSeam() {
        // Might need to + 1 ?
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int y = 0; y < distTo.length; y++) {
            for (int x = 0; x < distTo[0].length; x++) {
                distTo[y][x] = Integer.MAX_VALUE;
                edgeTo[y][x] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < distTo[0].length; i++) {
            distTo[0][i] = 0;
            edgeTo[0][i] = 0;
            distTo[distTo.length - 1][i] = 0;
            edgeTo[distTo.length - 1][i] = 0;
        }


        // The grid is already in toplocial order if we go row by row


        // this loop should go through in topilogical order;
        for (int y = 0; y < energyImage.length - 1; y++) {
            for (int x = 0; x < energyImage[0].length; x++) {

                // relax method should take in a vertex V

                // For each of the 2 or 3 verticies which this vertex points to (2 for edge of pictures 3 for non edge V)

                // look at the distTo this child vetex, is it > than this current vertex energy + the energy of the
                double currentDistance = distTo[y][x];
                // Relax one directly down from us
                double vertexDist = energy(x, y + 1);
                if (distTo[y + 1][x] > currentDistance + vertexDist) {
                    distTo[y + 1][x] = currentDistance + vertexDist;
                    // we know it all differs by 1 row of pixels so can be just 1 value?
                    edgeTo[y + 1][x] = x;

                }

                // if we are not on the LeftHand Edge Node Only have two childer (Down and right)
                if (x > 0) {
                    // relax one to bottom left
                    vertexDist = energy(x - 1, y + 1);
                    if (distTo[y + 1][x - 1] > currentDistance + vertexDist) {
                        distTo[y + 1][x - 1] = currentDistance + vertexDist;
                        // we know it all differs by 1 row of pixels so can be just 1 value?
                        edgeTo[y + 1][x - 1] = x;

                    }
                }
                // if we are a LeftHand Edge Node Only have two childen (Down and left)
                if (x < width - 1) {
                    // relax one to bottom right
                    vertexDist = energy(x + 1, y + 1);
                    if (distTo[y + 1][x + 1] > currentDistance + vertexDist) {
                        distTo[y + 1][x + 1] = currentDistance + vertexDist;
                        // we know it all differs by 1 row of pixels so can be just 1 value?
                        edgeTo[y + 1][x + 1] = x;

                    }
                }
            }
        }

        // find lowest value in the end, height -2 for skipping border and edge
        double lowest = Integer.MAX_VALUE;
        int lowestIndex = 0;

        if (width > 2 && height > 2) {
            for (int j = 0; j < width; j++) {
                if (distTo[height - 2][j] < lowest) {
                    lowest = distTo[height - 2][j];
                    lowestIndex = j;
                }
            }
        }



        int[] seam = new int[height];
        int path = lowestIndex;
        // make the start of the path (bottom) the downwards edge pixel
        seam[height - 1] = path;

        // Set the current seam array value to the path we are pointing at(ignore path set for last pixel as set above)
        for (int walker = height - 2; walker >= 0; walker--) {
            seam[walker] = path;
            // set new path to the current path is point to
            path = edgeTo[walker][path];
        }

        return seam;
    }

    public int[] findHorizontalSeam() {
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        for (int y = 0; y < distTo.length; y++) {
            for (int x = 0; x < distTo[0].length; x++) {
                distTo[y][x] = Integer.MAX_VALUE;
                edgeTo[y][x] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < distTo.length; i++) {
            distTo[i][0] = 0;
            edgeTo[i][0] = 0;
            distTo[i][distTo[0].length - 1] = 0;
            edgeTo[i][distTo[0].length - 1] = 0;
        }


       

        for (int x = 0; x < energyImage[0].length - 1; x++) {
            for (int y = 0; y < energyImage.length; y++) {
               
                double currentDistance = distTo[y][x];
                // Relax one directly down from us
                double vertexDist = energy(x + 1, y);
                if (distTo[y][x + 1] > currentDistance + vertexDist) {
                    distTo[y][x + 1] = currentDistance + vertexDist;
                    // we know it all differs by 1 row of pixels so can be just 1 value?
                    edgeTo[y][x + 1] = y;

                }

                if (y < height - 1) {

                    vertexDist = energy(x + 1, y + 1);
                    if (distTo[y + 1][x + 1] > currentDistance + vertexDist) {
                        distTo[y + 1][x + 1] = currentDistance + vertexDist;
                        edgeTo[y + 1][x + 1] = y;

                    }
                }
                if (y > 0) {

                    vertexDist = energy(x + 1, y - 1);
                    if (distTo[y - 1][x + 1] > currentDistance + vertexDist) {
                        distTo[y - 1][x + 1] = currentDistance + vertexDist;
                        // we know it all differs by 1 row of pixels so can be just 1 value?
                        edgeTo[y - 1][x + 1] = y;

                    }
                }
            }
        }

        // find lowest value in the end
        double lowest = Integer.MAX_VALUE;
        int lowestIndex = 0;
        if (width > 2 && height > 2) {
            for (int j = 0; j < height; j++) {
                if (distTo[j][width - 2] < lowest) {
                    lowest = distTo[j][width - 2];
                    lowestIndex = j;
                }
            }
        }




        // Stack<int> path =
        int[] seam = new int[width];
        int path = lowestIndex;
        // make the start of the path (bottom) the downwards edge pixel
        seam[width - 1] = path;

        // Set the current seam array value to the path we are pointing at(ignore path set for last pixel as set above)
        for (int walker = width - 2; walker >= 0; walker--) {
            seam[walker] = path;
            // set new path to the current path is point to
            path = edgeTo[path][walker];
        }

        return seam;
    }


    public void removeVerticalSeam(int[] seam) {
        if (width <= 1) {
            throw new IllegalArgumentException("");
        }
        if (seam == null || seam.length != height) {
            throw new IllegalArgumentException("");
        }
        if (seam.length == 1) {
            if (seam[0] >= width || seam[0] < 0) {
                throw new IllegalArgumentException("");
            }
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] >= width || seam[i] < 0) {
                throw new IllegalArgumentException("");
            }
            if (seam[i + 1] >= width || seam[i + 1] < 0) {
                throw new IllegalArgumentException("");
            }
            int dist = Math.abs(seam[i + 1] - seam[i]);
            if (dist < 0 || dist > 1) {
                throw new IllegalArgumentException("");
            }
        }


        Picture edited = new Picture(width - 1, height);
        double[][] editedEnergyMap = new double[height][width - 1];




        for (int y = 0; y < height; y++) {
            if (seam[y] == width) {
                continue;
            }
            int removed = 0;
            for (int x = 0; x < width; x++) {
                // if x is equal to index value in the seam array(equals y because we are removing by row)
                if (x == seam[y]) {
                    // ignore this pixel
                    removed += 1;
                }
                else {
                    edited.setRGB(x - removed, y, originalImage.getRGB(x, y));
                    editedEnergyMap[y][x - removed] = energyImage[y][x];
                }
            }
        }
        width = edited.width();
        height = edited.height();
      
        int xBound = width - 1;
        int yBound = height - 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == xBound || y == 0 || y == yBound) {
                    editedEnergyMap[y][x] = 1000;
                }
                else {

                    double xValue = pixelEnergy(edited.getRGB(x - 1, y),
                                                edited.getRGB(x + 1, y));
                    double yValue = pixelEnergy(edited.getRGB(x, y - 1),
                                                edited.getRGB(x, y + 1));


                    editedEnergyMap[y][x] = Math.sqrt(xValue + yValue);

                }
            }
        }


        originalImage = edited;
        energyImage = editedEnergyMap;
        width = edited.width();
        height = edited.height();


    }


    public void removeHorizontalSeam(int[] seam) {
        if (height <= 1) {
            throw new IllegalArgumentException("");
        }
        if (seam == null || seam.length != width) {
            throw new IllegalArgumentException("");
        }

        if (seam.length == 1) {
            if (seam[0] >= height || seam[0] < 0) {
                throw new IllegalArgumentException("");
            }
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] >= height || seam[i] < 0) {
                throw new IllegalArgumentException("");
            }
            if (seam[i + 1] >= height || seam[i + 1] < 0) {
                throw new IllegalArgumentException("");
            }
            int dist = Math.abs(seam[i + 1] - seam[i]);
            if (dist < 0 || dist > 1) {
                throw new IllegalArgumentException("");
            }
        }

        // System.out.println(originalImage.toString());

        Picture edited = new Picture(width, height - 1);
        double[][] editedEnergyMap = new double[height - 1][width];


        // seam length should be equal to y?


        for (int x = 0; x < width; x++) {
            if (seam[x] == height) {
                continue;
            }

            int removed = 0;
            for (int y = 0; y < height; y++) {
                // if x is equal to index value in the seam array(equals y because we are removing by row)
                if (y == seam[x]) {
                    // ignore this pixel
                    removed += 1;
                }
                else {
                    edited.setRGB(x, y - removed, originalImage.getRGB(x, y));
                    editedEnergyMap[y - removed][x] = energyImage[y][x];
                }
            }

        }
        width = edited.width();
        height = edited.height();
       
        int xBound = width - 1;
        int yBound = height - 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == xBound || y == 0 || y == yBound) {
                    editedEnergyMap[y][x] = 1000;
                }
                else {

                    double xValue = pixelEnergy(edited.getRGB(x - 1, y),
                                                edited.getRGB(x + 1, y));
                    double yValue = +pixelEnergy(edited.getRGB(x, y - 1),
                                                 edited.getRGB(x, y + 1));


                    editedEnergyMap[y][x] = Math.sqrt(xValue + yValue);

                }
            }
        }


        originalImage = edited;
        energyImage = editedEnergyMap;
        width = edited.width();
        height = edited.height();


    }


    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture(6, 4));
        // sc.energyImage = new double[][] {
        //         { 1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.00 }
        //         , { 1000.00, 6.86, 10.49, 9.85, 8.54, 1000.00 }
        //         , { 1000.00, 3.46, 8.49, 8.37, 10.20, 1000.00 }
        //         , { 1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.00 },
        //         };
        //
        //
        // sc.energyImage = new double[][] {
        //         { 1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.00 }
        //         , { 1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.00 }
        //         , { -, -, 1000.00, 1000.00, 1000.00, 1000.00 }
        //         , { 1000.00, 1000.00, -, -, 1000.00, 1000.00 }
        //         , { 1000.00, 1000.00, 1000.00, 1000.00, -, 1000.00 }
        //         , { 1000.00, 1000.00, 1000.00, 1000.00, 1000.00, - }
        //
        // };

        // int[] seam1 = new int[] { 2, 2, 3, 3, 4, 5 };
        // int[] seam2 = new int[] { 4, 3, 2, 2, 3, 2 };
        // StdOut.printf("Horizontal seam: { ");
        // int[] horizontalSeam = sc.findHorizontalSeam();
        // for (int y : horizontalSeam)
        //     StdOut.print(y + " ");
        // StdOut.println("}");
        // PrintSeams.printSeam(sc, horizontalSeam, true);
    }
}
