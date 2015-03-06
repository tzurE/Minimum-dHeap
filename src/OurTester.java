import java.util.Arrays;


public class OurTester {
	public static void main(String[] args) {
		DHeap heap = new DHeap(2,20);
		for (int i = 5; i > 0; i--) {
			DHeap_Item temp = new DHeap_Item("a",i);
			heap.Insert(temp);
		}
		System.out.println(Arrays.toString(heap.array));
		System.out.println(heap.isHeap());
		heap.Delete(heap.array[4]);
		System.out.println(Arrays.toString(heap.array));
	}

}
