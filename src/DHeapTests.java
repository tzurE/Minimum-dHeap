import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class DHeapTests {

	private static Random r;
	
	@BeforeClass
	public static void setup() {
		r = new Random();
		long seed = System.currentTimeMillis();
		r.setSeed(seed);
		System.out.println("Seed: " + seed);
	}

	@Test
	public void testIsHeapOnEmptyHeap() {
		DHeap heap = new DHeap(7, 100);
		assertTrue(heap.isHeap());
	}
	
	@Test
	public void testDecreaseKeyOfRoot() {
		DHeap heap = new DHeap(3, 100);
		DHeap_Item item = new DHeap_Item("7", 7);
		heap.Insert(item);
		for (int i = 0; i < 99; i++) {
			int v = r.nextInt(99999999) + 7;
			heap.Insert(new DHeap_Item(""+ v, v));
		}
		assertEquals(0, item.getPos());
		heap.Decrease_Key(item, 5);
		
		assertEquals(7 - 5, item.getKey());
		assertEquals(0, item.getPos());
		assertEquals(item, heap.Get_Min());
	}
	
	@Test
	public void testDecreaseKeyStayInPlaceRoot() {
		DHeap heap = new DHeap(3, 100);
		DHeap_Item root = new DHeap_Item("7", 7);
		DHeap_Item decreasingItem = new DHeap_Item("9", 9);
		heap.Insert(root);
		heap.Insert(decreasingItem);
		for (int i = 0; i < 98; i++) {
			int v = r.nextInt(99999999) + 10;
			heap.Insert(new DHeap_Item(""+ v, v));
		}
		assertEquals(1, decreasingItem.getPos());
		heap.Decrease_Key(decreasingItem, 1);
		
		assertEquals(9-1, decreasingItem.getKey());
		assertEquals(1, decreasingItem.getPos());
		assertEquals(root, heap.Get_Min());
	}
	
	@Test
	public void testDecreaseKeyReplacesWithRootRoot() {
		DHeap heap = new DHeap(3, 100);
		DHeap_Item root = new DHeap_Item("7", 7);
		DHeap_Item decreasingItem = new DHeap_Item("9", 9);
		heap.Insert(root);
		heap.Insert(decreasingItem);
		for (int i = 0; i < 98; i++) {
			int v = r.nextInt(99999999) + 10;
			heap.Insert(new DHeap_Item(""+ v, v));
		}
		assertEquals(1, decreasingItem.getPos());
		heap.Decrease_Key(decreasingItem, 8);
		
		assertEquals(9-8, decreasingItem.getKey());
		assertEquals(0, decreasingItem.getPos());
		assertEquals(decreasingItem, heap.Get_Min());
		assertEquals(1, root.getPos());
	}
	
	@Test
	public void testArrayToHeapEmptyArray() {
		DHeap heap = new DHeap(7, 100);
		heap.arrayToHeap(new DHeap_Item[0]);
		assertEquals(0, heap.getSize());
	}
	
	@Test
	public void testArrayToHeapSingletonArray() {
		DHeap heap = new DHeap(7, 100);
		DHeap_Item single = new DHeap_Item("", 1);
		heap.arrayToHeap(new DHeap_Item[]{single});
		assertEquals(1, heap.getSize());
		assertEquals(single, heap.Get_Min());
	}
	

	@Test
	public void testArrayToHeapShouldPlaceMinimumAtRoot() {
		DHeap heap = new DHeap(7, 100);
		DHeap_Item one = new DHeap_Item("", 1);
		DHeap_Item two = new DHeap_Item("", 2);
		DHeap_Item three = new DHeap_Item("", 3);
		heap.arrayToHeap(new DHeap_Item[]{three, two, one});
		assertEquals(3, heap.getSize());
		assertEquals(one, heap.Get_Min());
	}
	
	@Test
	public void testArrayToHeapShouldReplaceExistingContent() {
		DHeap heap = new DHeap(7, 100);
		for (int i = 0; i < 99; i++) {
			int v = r.nextInt();
			heap.Insert(new DHeap_Item(""+ v, v));
		}
		heap.Insert(new DHeap_Item("Minimum", Integer.MIN_VALUE));
		
		DHeap_Item one = new DHeap_Item("", 1);
		DHeap_Item two = new DHeap_Item("", 2);
		DHeap_Item three = new DHeap_Item("", 3);
		heap.arrayToHeap(new DHeap_Item[]{three, two, one});
		assertEquals(3, heap.getSize());
		assertEquals(one, heap.Get_Min());
	}
	
	@Test
	public void testChildrenOfRootInBinaryHeap() {
		int root_index = 0;
		// All D children of root (if exist) will be in locations [1, d]
		// Binary heap
		int firstChildInBinaryHeap = DHeap.child(root_index, 1, 2);
		int secondChildInBinaryHeap = DHeap.child(root_index, 2, 2);
		assertEquals(1, firstChildInBinaryHeap);
		assertEquals(2, secondChildInBinaryHeap);
	}
	
	@Test
	public void testChildrenOfRootInRandomSizedHeap() {
		int root_index = 0;
		int d = r.nextInt(100)+2;
		for (int i = 0; i < d; i++) {
			int ithSonOfRoot = DHeap.child(root_index, i+1, d);
			assertEquals("DHeap: " + d + " ChildNo: " + i+1 ,1+i, ithSonOfRoot);	
		}
	}
	
	@Test
	public void testChildrenOfSecondRowIn5Heap() {
		int d = 5;
		for (int i = 0; i < d; i++) {
			for (int j = 0; i < d; i++) {
				int jthSonOfithSon = DHeap.child(i+1, j+1, d);
				assertEquals(1 + d + d*i+j, jthSonOfithSon);
			}
		}
	}
	
	@Test
	public void testParentOfThirdRowIn5Heap() {
		int d = 5;
		for (int i = 0; i < d; i++) {
			int parentIndex = 1 + i;
			for (int j = 0; j < d; j++) {
				// i = 1, j = 0 -> 1 + 5 + 1*5 + 0 = 11 
				// ParentIndex = 2
				int thirdRowIndex = 1 + d + i*d + (j);
				int actualParent = DHeap.parent(thirdRowIndex, d);
				assertEquals("SecondRow: " + i + "\nSon (0 based): " + (j) + "\nSearchedIndex: " + thirdRowIndex,
						parentIndex, actualParent);
			}
		}
	}
	
	@Test
	public void testParentOfThirdRowInRandomHeap() {
		int d = r.nextInt(100)+2;
		for (int i = 0; i < d; i++) {
			int parentIndex = 1 + i;
			for (int j = 0; j < d; j++) {
				int thirdRowIndex = 1 + d + i*d + (j);
				int actualParent = DHeap.parent(thirdRowIndex, d);
				assertEquals("D: " + d + "\nSecondRow: " + i + "\nSon (0 based): " + (j) + "\nSearchedIndex: " + thirdRowIndex,
						parentIndex, actualParent);
			}
		}
	}

	@Test
	public void testInsertOneItem() {
		DHeap heap = new DHeap(7, 100);
		int value = r.nextInt();
		DHeap_Item item = new DHeap_Item("" + value, value); 
		heap.Insert(item);
		assertTrue(heap.isHeap());
		assertEquals(item, heap.Get_Min());
		assertEquals(1, heap.getSize());
	}
	

	@Test
	public void testInsertMultipleIncreasingOrder() {
		DHeap heap = new DHeap(7, 100);
		int minimum = r.nextInt(1000);
		DHeap_Item minItem = new DHeap_Item("" + minimum, minimum); 
		heap.Insert(minItem);			
		for (int i = 1; i < 11; i++) {
			int value = r.nextInt(1000) + 1000*i;
			DHeap_Item item = new DHeap_Item("" + value, value); 
			heap.Insert(item);			
			assertTrue(heap.isHeap());
			assertEquals(minItem, heap.Get_Min());
			assertEquals(1+i, heap.getSize());
		}
	}
	
	@Test
	public void testInsertWithHeapifyUpNeededSwap() {
		DHeap heap = new DHeap(7, 100);
		DHeap_Item item = new DHeap_Item("7", 7);
		heap.Insert(item);
		DHeap_Item smaller = new DHeap_Item("5", 5);
		heap.Insert(smaller);
		assertEquals(smaller, heap.Get_Min());
		assertEquals(0, smaller.getPos());
		assertEquals(1, item.getPos());
	}
	
	@Test
	public void testInsertWithoutHeapifyUpNeededSwap() {
		DHeap heap = new DHeap(7, 100);
		DHeap_Item item = new DHeap_Item("7", 7);
		heap.Insert(item);
		DHeap_Item larger = new DHeap_Item("15", 15);
		heap.Insert(larger);
		assertEquals(1, larger.getPos());
		assertEquals(0, item.getPos());
		assertEquals(item, heap.Get_Min());
	}
	
	@Test
	public void testInsertRandomOrder() {
		DHeap heap = new DHeap(7, 100);
		DHeap_Item minItem = new DHeap_Item("", Integer.MAX_VALUE); 
		for (int i = 0; i < 100; i++) {
			int value = r.nextInt();
			DHeap_Item item = new DHeap_Item("" + value, value);
			
			if (item.getKey() < minItem.getKey()) {
				minItem = item;
			}
			heap.Insert(item);
			assertTrue(heap.isHeap());
			DHeap_Item actualMin = heap.Get_Min();
			assertEquals("Inserted:" + item.getKey() + "\nExpected Min: " + minItem.getKey() + 
					"\nActual Min: " + actualMin.getKey() + "\n" + heap, 
					minItem, actualMin);
			assertEquals(i+1, heap.getSize());
		}
	}

	
	@Test
	public void testDeleteSingleElement() {
		DHeap heap = new DHeap(7, 100);
		int value = r.nextInt();
		DHeap_Item item = new DHeap_Item("" + value, value); 
		heap.Insert(item);
		heap.Delete(item);
		assertEquals(0, heap.getSize());
	}
	

	@Test
	public void testDeleteMinSingleElement() {
		DHeap heap = new DHeap(7, 100);
		int value = r.nextInt();
		DHeap_Item item = new DHeap_Item("" + value, value); 
		heap.Insert(item);
		heap.Delete_Min();
		assertEquals(0, heap.getSize());
	}
	
	@Test
	public void testDeleteFromRootWhenThereAreTwoLevels() {
		DHeap heap = new DHeap(3, 100);
		int v = r.nextInt(10000);
		DHeap_Item root = new DHeap_Item("" + v, v);
		// i1 will be the smallest son, and the next minimum
		int v1 = r.nextInt(10000) + v;
		DHeap_Item i1 = new DHeap_Item("" + v1, v1);
		int v2 = r.nextInt(10000) + v + v1;
		DHeap_Item i2 = new DHeap_Item("" + v2, v2);
		int v3 = r.nextInt(10000) + v + v1;
		DHeap_Item i3 = new DHeap_Item("" + v3, v3);
		heap.Insert(root);
		heap.Insert(i1);
		heap.Insert(i2);
		heap.Insert(i3);
		heap.Delete(root);
		assertEquals(3, heap.getSize());
		assertEquals(i1, heap.Get_Min());
	}
	
	@Test
	public void testDeleteFromRootWithNonCompleteLevel() {
		DHeap heap = new DHeap(3, 100);
		int v = r.nextInt(10000);
		DHeap_Item root = new DHeap_Item("" + v, v);
		// i1 will be the smallest son, and the next minimum
		int v1 = r.nextInt(10000) + v;
		DHeap_Item i1 = new DHeap_Item("" + v1, v1);
		int v2 = r.nextInt(10000) + v + v1;
		DHeap_Item i2 = new DHeap_Item("" + v2, v2);
		heap.Insert(root);
		heap.Insert(i1);
		heap.Insert(i2);
		heap.Delete(root);
		assertEquals(2, heap.getSize());
		assertEquals(i1, heap.Get_Min());
	}
	
	@Test
	public void testHeapSortEmptyArray() {
		int[] array = new int[]{};
		DHeap.DHeapSort(array, 3);
		assertTrue( Arrays.equals(array, new int[]{}) );
	}
	
	@Test
	public void testHeapSortSingletonArray() {
		int[] array = new int[]{4};
		DHeap.DHeapSort(array, 3);
		assertTrue( Arrays.equals(array, new int[]{4}) );
	}
	
	@Test
	public void testHeapSortReversedArray() {
		int[] array = new int[]{4,3,2,1};
		DHeap.DHeapSort(array, 3);
		assertTrue( Arrays.equals(array, new int[]{1,2,3,4}) );
	}
	
	@Test
	public void testHeapSortRandomArray() {
		int length = r.nextInt(6000);
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = r.nextInt();
		}
		int[] sorted = new int[length];
		System.arraycopy(array, 0, sorted, 0, length);
		Arrays.sort(sorted);
		
		for (int i = 2; i < 21; i++) {
			DHeap.DHeapSort(array, i);
			assertTrue( Arrays.equals(array, sorted) );			
		}
	}
	
	@Test
	public void testDeleteFromRootLevelWithThreeLevels() {
		DHeap heap = new DHeap(3, 100);
		DHeap_Item[] items = new DHeap_Item[1+3+9];
		for (int i = 0; i < 1 + 3 + 9; i++) {
			DHeap_Item item = new DHeap_Item("" + i, i);
			items[i] = item;
			heap.Insert(item);	
		}
		heap.Delete_Min();
		assertEquals(12, heap.getSize());
		assertEquals(items[1], heap.Get_Min());
		// The last one was moved up, then heapify pushed it down to replace with "1",
		// Then it was switched again with the smallest son
		assertEquals(4, items[12].getPos());
	}
	
	@Test
	public void testDeleteFromSecondLevelWithThreeLevels() {
		DHeap heap = new DHeap(3, 100);
		DHeap_Item[] items = new DHeap_Item[1+3+9];
		for (int i = 0; i < 1 + 3 + 9; i++) {
			DHeap_Item item = new DHeap_Item("" + i, i);
			items[i] = item;
			heap.Insert(item);	
		}
		heap.Delete(items[2]);
		assertEquals(12, heap.getSize());
		System.out.println("assaf" + items[0].getKey() + " " + heap.Get_Min().getKey());
		//System.out.println(items[0].equals(heap.Get_Min()));
		assertEquals(items[0], heap.Get_Min());
		// The last one was moved up, then heapify pushed it down to replace with "2",
		// Then it was switched again with the smallest son
		assertEquals(2, items[7].getPos());
		assertEquals(7, items[12].getPos());
	}
	
	@Test
	public void testMeasurementsForDHeapSort() {
		
		for (int m : new int[]{1000, 10000, 100000}) {
			for (int d : new int[]{2,3,4}) {
				double totalComparisons = 0;
				for (int iteration = 0; iteration < 10; iteration++) {
					int[] array = generateArray(m, 1000);
					totalComparisons += DHeap.DHeapSort(array, d);
				}
				double average = totalComparisons / 10;
				System.out.println(String.format("HeapSort comparison\tm=%d\td=%d\nAverage: %f", m, d, average));
			}
		}
	}
	
	@Test
	public void testMeasurementsForDecreaseKey() {
		
			for (int d : new int[]{2,3,4}) {
				for (int x : new int[]{1, 100, 1000}) {
				double totalComparisons = 0;
				for (int iteration = 0; iteration < 10; iteration++) {
					int[] array = generateArray(100000, 1000);
					DHeap_Item[] items = convertToDHeap_Item(array);
					DHeap heap = new DHeap(d, array.length);
//					heap.arrayToHeap(items);
					for (DHeap_Item item : items) {
					    heap.Insert(item);     
                        
                    }
					assertTrue(heap.isHeap());
					for (DHeap_Item item : items) {
					    totalComparisons += heap.Decrease_Key(item, x);						
					}
					assertTrue(heap.isHeap());
				}
				double average = totalComparisons / 10;
				System.out.println(String.format("DecreaseKey comparison\tx=%d\td=%d\nAverage: %f", x, d, average));
				System.out.println("--");
			}
		}
		System.out.println("---------------");
	}
	
	
	private static int[] generateArray(int elements, int maxValue) {
		int[] elem = new int[elements];
		for (int i = 0; i < elem.length; i++) {
			elem[i] = r.nextInt(maxValue);
		}
		return elem;
	}
	
	private static DHeap_Item[] convertToDHeap_Item(int[] array) {
		DHeap_Item[] elem = new DHeap_Item[array.length];
		for (int i = 0; i < array.length; i++) {
			elem[i] = new DHeap_Item("", array[i]);
		}
		return elem;
	}


}