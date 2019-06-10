package bootstrap;

public class MergeSortExample {

    static void sort(int[] array, int l, int r) {
        if ( l < r ) {
            int m = (r + l) / 2;

            sort(array, l, m);
            sort(array, m + 1, r);

            merge(array, l, m, r);
        }
    }

    static void merge(int[] array, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = array[l + i];
        }

        for (int j = 0; j < n2; j++) {
            R[j] = array[m + j + 1];
        }

        int k = l;
        int i = 0;
        int j = 0;

        while ((i < n1) && (j < n2)) {
            if ( L[i] <= R[j] ) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World.");

        int[] temp = { 23, 3, 2, 5, 8, 12 };
        sort(temp, 0, temp.length - 1);
        printArray(temp);

    }

    static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "|");
        }
        System.out.println("\n======================");
    }
}
