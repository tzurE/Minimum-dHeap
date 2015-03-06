import java.util.Arrays;
import java.util.Random;


public class Tester {
        
        static final int NUMBER_OF_EXPERIMENT_ITERATIONS = 3;
        static final int BASE_NUMBER_OF_INPUT_NUMBERS = 10;
        
        public static void main(String[] args){
                int[] ds = {2,3,4,};
                
                for (int d : ds){
                        if (runTest(d, true))
                                System.out.println("Test ended successfully for d="+d+"\n----------------------------");
                        else
                                System.out.println("Error occured during test for d="+d+"\n--------------------------");
                }
                        
        }
        
        public static boolean runTest(int d, boolean strict){
                
                Random rnd = new Random();
                
                for (int i=1;i<NUMBER_OF_EXPERIMENT_ITERATIONS+1;i++){
                        DHeap heap = new DHeap(d, i*(BASE_NUMBER_OF_INPUT_NUMBERS));
                        
                        //insertions
                        int insertionComparisons =0;
                        for (int j=1;j<i*(BASE_NUMBER_OF_INPUT_NUMBERS)+1;j++){
                                Integer randomNonNegativeNumber=Math.abs(rnd.nextInt(Integer.MAX_VALUE));
                                String intToString=randomNonNegativeNumber.toString();
                                DHeap_Item item = new DHeap_Item(intToString, randomNonNegativeNumber);
                                insertionComparisons+=heap.Insert(item);
                                
                                if (strict){
                                        if (!heap.isHeap()) {
                                        	//System.out.println(Arrays.toString(heap.array));
                                                System.out.println("insertion error");
                                                return false;
                                        }
                                }
                        }

                        //decrease-key
                        int decreaseComparisons = 0;
                        for (int j=1;j<heap.getSize();j++){
                                int index = rnd.nextInt(heap.getSize());
                                DHeap_Item item = heap.array[index];
                                decreaseComparisons+=heap.Decrease_Key(item, rnd.nextInt(Integer.MAX_VALUE-Math.abs(item.getKey())));

                                if (strict){
                                        if (!heap.isHeap()) {
                                        	//System.out.println("a");
                                                System.out.println("deckey error");
                                                return false;
                                        }
                                }
                        }

                        //deletions
                        int deleteComparisons = 0;
                        for (int j=0;j<i*(BASE_NUMBER_OF_INPUT_NUMBERS);j++){
                                int index = rnd.nextInt(heap.getSize());
                                DHeap_Item item = heap.array[index];
                                if (item!=null){
                                        deleteComparisons+=heap.Delete(item);
                                }

                                if (strict){
                                        if (!heap.isHeap()) {
                                                System.out.println("del error");
                                                return false;
                                        }
                                }
                        }

                        //HeapSort
                        int heapSortComparisons = 0;
                        int[] arr = new int[i*(BASE_NUMBER_OF_INPUT_NUMBERS)];
                        for (int j=0;j<i*(BASE_NUMBER_OF_INPUT_NUMBERS);j++){
                                arr[j]=Math.abs(rnd.nextInt(Integer.MAX_VALUE));
                        }
                        heapSortComparisons+=DHeap.DHeapSort(arr, d);
                        if (!isSorted(arr))
                                System.out.println("heapsort error");
                        
                        
                        System.out.println(        "|Heap size: "+i*(BASE_NUMBER_OF_INPUT_NUMBERS)+", d="+d+"\n"+
                                        "|Insert Comparisons = "+insertionComparisons+" ,"
                                        +"Insert avrg: "+(float)insertionComparisons/(float)(i*(BASE_NUMBER_OF_INPUT_NUMBERS))
                                        +"\n|Decrease Comparisons = "+decreaseComparisons+" ,"
                                        +"Decrease avrg: "+(float)decreaseComparisons/(float)(i*(BASE_NUMBER_OF_INPUT_NUMBERS))
                                        +"\n|Delete Comparisons = "+deleteComparisons+" ,"
                                        +"Delete avrg: "+(float)deleteComparisons/(float)(i*(BASE_NUMBER_OF_INPUT_NUMBERS))
                                        +"\n|Heapsort Comparisons = "+heapSortComparisons+" ,"
                                        +"Heapsort avrg: "+(float)heapSortComparisons/(float)(arr.length)
                                        +"\n");
                }
                
                return true;
                                
        }
        
        private static boolean isSorted(int[] array){
                for (int i=0;i<array.length-1;i++){
                        if (array[i]>array[i+1])
                                return false;
                }
                return true;
        }
}