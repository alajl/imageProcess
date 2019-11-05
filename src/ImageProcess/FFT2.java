package ImageProcess;

public class FFT2 {

    public final static float __PI = 3.1416f;

    public static void matrixReverse(Complex[][] array) {
        //int count = 0;//用于统计总共循环次数
        for (int i = 0; i < array.length - 1; i++) {
            //列循环从：i+1开始，提高循环效率
            for (int j = i + 1; j < array[i].length; j++) {
                Complex temp = array[i][j];
                array[i][j] = array[j][i];
                array[j][i] = temp;
                //count++;
            }
        }
        // System.out.println(count);
    }

    public static Complex[] twoDimensionalArraysToOneDimensionalArray(Complex[][] array) {
        int index = 0;
        Complex[] res = new Complex[array.length * array[0].length];
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array.length; x++) {
                res[index] = array[y][x];
                index++;
            }
        }
        return res;
    }

    public static int[] twoDimensionalArraysToOneDimensionalArray(int[][] array) {
        int index = 0;
        int[] res = new int[array.length * array[0].length];
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array.length; x++) {
                res[index] = array[y][x];
                index++;
            }
        }
        return res;
    }

    public static Complex[][] oneDimensionalArrayToTwoDimensionalArrays(Complex[] array) {
        int length = (int) (Math.sqrt(array.length));
        Complex[][] res = new Complex[length][length];
        int index = 0;
        for (int i = 0; i < array.length; i += length) {
            for (int ii = 0; ii < length; ii++) {
                res[index][ii] = array[i + ii];
            }
            index++;
        }
        return res;
    }

    public static int filterNoise(int value) {
        if (value > 255) {
            return 255;
        }
        if (value < 0) {
            return 0;
        }
        return value;
    }

    public static void printArray(Complex array[][]) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print("[" + array[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.println("***************");
    }

    public static void printArray(Complex array[]) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            System.out.print("[" + array[i] + "] ");
            count++;
            if (count % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println("***************");
    }

    public static Complex[] getOneRow(Complex in[]) {
        Complex[] res = new Complex[in.length];
        for (int i = 0; i < in.length; i++) {
            res[i] = in[i];
        }
        return res;
    }

    public static void setOneRow(Complex in[], Complex value[]) {
        for (int i = 0; i < in.length; i++) {
            in[i] = value[i];
        }
    }

    /*
     1.设二维数组包含待变换的数据
     2.对二维数组的每一行做傅里叶变换，并将变换结果替换原数组。
     3.将二维数组的行与列元素互换。
     4.再对该二维数组的每一行做傅里叶变换，并将变换结果放回原数组。
     5.再将二维数组的行与列元素互换，其结果即为二维傅里叶变换结果
     */

    public static void FFT_2(Complex[][] in, int N) {
        Complex[] temp;
        for (int y = 0; y < in.length; y++) {
            temp = FFT.fft(getOneRow(in[y]));
            setOneRow(in[y], temp);
        }

        matrixReverse(in);

        for (int y = 0; y < in.length; y++) {
            temp = FFT.fft(getOneRow(in[y]));
            setOneRow(in[y], temp);
        }

        matrixReverse(in);
    }

    public static void IFFT_2(Complex[][] in, int N) {
        Complex[] temp;
        matrixReverse(in);

        for (int y = 0; y < in.length; y++) {
            temp = FFT.ifft(getOneRow(in[y]));
            setOneRow(in[y], temp);
        }
        matrixReverse(in);

        for (int y = 0; y < in.length; y++) {
            temp = FFT.ifft(getOneRow(in[y]));
            setOneRow(in[y], temp);
        }
    }

    public static void FFT_2_Shift(Complex in[], int N) {
        Complex temp;
        for (int y = 0; y < N / 2; y++) {
            for (int x = 0; x < N / 2; x++) {
                temp = in[y * N + x];
                in[y * N + x] = in[y * N + N * N / 2 + x + N / 2];
                in[y * N + N * N / 2 + x + N / 2] = temp;
            }
        }
        for (int y = 0; y < N / 2; y++) {
            for (int x = N / 2; x < N; x++) {
                temp = in[y * N + x];
                in[y * N + x] = in[y * N + N * N / 2 + x - N / 2];
                in[y * N + N * N / 2 + x - N / 2] = temp;
            }
        }
    }

    // sample client for testing
    public static void main(String[] args) {
        int count = 1;
        Complex[][] test = new Complex[4][4];
        Complex[] change = new Complex[4];
        for (int y = 0; y < test.length; y++) {
            for (int x = 0; x < test[0].length; x++) {
                test[y][x] = new Complex(count, count);
                count++;
            }
        }
        count = 101;
        for (int x = 0; x < change.length; x++) {
            change[x] = new Complex(count, count);
            count++;
        }
        Complex[] res = FFT2.twoDimensionalArraysToOneDimensionalArray(test);
        FFT2.printArray(res);
        FFT2.printArray(FFT2.oneDimensionalArrayToTwoDimensionalArrays(res));
//        FFT2.printArray(test);
//        FFT2.FFT_2(test, 4);
//        FFT2.printArray(test);
//        FFT2.IFFT_2(test, 4);
//        FFT2.printArray(test);
//        Complex[] test1 = FFT2.twoDimensionalArraysToOneDimensionalArray(test);
//        FFT2.FFT_2_Shift(test1, 4);
//        FFT2.printArray(test1);
//        FFT2.FFT_2_Shift(test1, 4);
//        FFT2.printArray(test1);
//        FFT2.matrixReverse(test);
//        FFT2.printArray(test);
//        FFT2.matrixReverse(test);
//        FFT2.printArray(test);
//        FFT2.printArray(FFT2.twoDimensionalArraysToOneDimensionalArray(test));
//        FFT2.setOneRow(test[0], change);
//        FFT2.printArray(test);
    }
}
