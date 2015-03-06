 
/**
 * D-Heap
 */
//***************************************************************************
//submitted by: assaf oren    -   username: assaforen    -  ID: 301750956
//				tzur elyiahu  -   username: tzurelyiahu  -  ID: 300647799
//***************************************************************************
 
public class DHeap {
 
    private int size, max_size, d;
    public DHeap_Item[] array;
 
    // Constructor
    // m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
        max_size = m_size;
        d = m_d;
        array = new DHeap_Item[max_size];
        size = 0;
    }
 
    // Getter for size
    public int getSize() {
        return size;
    }
 
    /**
     * public void arrayToHeap()
     * 
     * The function builds a new heap from the given array. Previous data of the
     * heap should be erased. preconidtion: array1.length() <= max_size
     * postcondition: isHeap() size = array.length()
     */
    public int arrayToHeap(DHeap_Item[] array1) {
    	this.size = 0;
        // Fill the array with the new data
        for (int i = 0; i < array1.length; i ++) {
            this.array[i] = array1[i];
            this.array[i].setPos(i);
            this.size++;
        } // Erase remaining old data
        for (int i = array1.length; i < this.max_size; i ++) {
            this.array[i] = null;
        }
   
        int comparisons = 0;
        // Work on the tree in the order taught in class
        int lastParentChecked = -1;
        for (int i = parent(this.getSize(),this.d); i >= 0; i --) {
        	// If we already heapified this parent, no need to do it again
        	// Else - heapify down
        	if (i == lastParentChecked) continue;
            comparisons = comparisons + this.Heapify_Down(i);
            lastParentChecked = i;
        }

        return comparisons;
         
    }
 
    /**
     * public boolean isHeap()
     * 
     * The function returns true if and only if the D-ary tree rooted at
     * array[0] satisfies the heap property or size == 0.
     * 
     */
    public boolean isHeap() {
    	//pass on all the nodes, check if every key is larger than the father's key
    	for (int i = this.getSize()-1; i > 0; i--) {
    		if (this.array[i].getKey() < this.array[parent(i,this.d)].getKey())
    			return false;
    	}
    	return true;
    }
 
    /**
     * public static int parent(i, d), child(i,k,d) (2 methods)
     * 
     * precondition: i >= 0, d >= 2
     * 
     * The methods compute the index of the parent and the k-th child of vertex
     * i in a complete D-ary tree stored in an array. 1 <= k <= d. Note that
     * indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
	if (i == 0) { 
			return -1;
	}
        return (i - 1) / d;
    }
 
    public static int child(int i, int k, int d) {
        return d * i + k;
    }
 
    /**
     * public int Insert(DHeap_Item item)
     * 
     * precondition: item != null isHeap() size < max_size
     * 
     * postcondition: isHeap()
     */
    public int Insert(DHeap_Item item) {
        int comparisons = 0;
        // Insert as last item
        this.array[this.getSize()] = item;
        item.setPos(this.getSize());
        // Heapify up
        comparisons = Heapify_Up(item.getPos());
        this.size++;
        return comparisons;
    }
 
    public int Heapify_Down(int i) {
        int comparisons = 0, min = 0;
        // Find minimum child
        // First, check if has at least one child - if not, we're finished
        if (i * this.d + 1 < this.getSize()) {
            min = i * this.d + 1;
        } else {
            return 0;
        }
         
        // Check all other child (if we're still within the array's size)
        for (int j = 2; ((j <= this.d) && (i * this.d + j < this.getSize())); j++) {
            comparisons++;
            if (this.array[i * this.d + j].getKey() < this.array[min].getKey()) {
                min = i * this.d + j;
            }
        }
        
        comparisons++; // For the next if comparison
        if (this.array[min].getKey() < this.array[i].getKey()) {
            this.Swap_Places(min, i);
            return comparisons + Heapify_Down(min);
        }
         
        return comparisons;
    }
     //a method that swaps places between DHeap items in the array(which represents a Heap)
    //as seen in the lecture
    public void Swap_Places(int i, int j) {
        DHeap_Item tempChild = this.array[i];
        DHeap_Item tempParent = this.array[j];
 
        this.array[j] = tempChild;
        this.array[j].setPos(j);
 
        this.array[i] = tempParent;
        this.array[i].setPos(i);
 
    }
 
    public int Heapify_Up(int i) {
    	//while we're not at the start of the array(Heap), we compare each child to his father.
    	//as seen in the lecture
        int comparisons = 1;
        while ((i > 0)
                && (this.array[i].getKey() < this.array[parent(i, this.d)]
                        .getKey())) {
 
            this.Swap_Places(i,parent(i, this.d));
             
 
            i = parent(i, this.d);
            comparisons++;
        }
        return comparisons;
    }
 
    /**
     * public int Delete_Min()
     * 
     * precondition: size > 0 isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete_Min() {
    	//delete the minimum node of the Heap, which is located at the first place on the array.
        this.Swap_Places(0,this.getSize()-1);
        this.array[this.getSize()-1] = null;
        this.size--;
        //call the Heapify down, as seen in the lecture
        return Heapify_Down(0);
    }
 
    /**
     * public String Get_Min()
     * 
     * precondition: heapsize > 0 isHeap() size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min() {
    	//returns the minimum element of the heap, which is located at the beginning of the array
        return this.array[0];
    }
 
    /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     * 
     * precondition: item.pos < size; item != null isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta) {
    	//as seen in the lecture, we decrease the item key and preform a heapify up 
    	//in order for the Heap rule to be kept
        item.setKey(item.getKey()-delta);
        return Heapify_Up(item.getPos());
    }
 
    /**
     * public void Delete(DHeap_Item item)
     * 
     * precondition: item.pos < size; item != null isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item) {
        int comparisons = 0;
        // Decrease key to int.min_value, therefore it will be the minimum in the array
        comparisons = comparisons + this.Decrease_Key(item, item.getKey() - Integer.MIN_VALUE);
        return comparisons + this.Delete_Min();
    }

 
    /**
     * Sort the array in-place using heap-sort (build a heap, and perform n
     * times: get-min, del-min). Sorting should be done using the DHeap, value
     * of item is irrelevant. Return the number of comparison performed.
     */
    public static int DHeapSort(int[] array, int d) {
        DHeap heap = new DHeap(d,array.length);
        int comparisons = 0;
         
        // Insert items to the array in the heap
        for (int i = 0; i < array.length; i++) {
            heap.array[i] = new DHeap_Item("v",array[i]);
            heap.array[i].setPos(i);
        }

        // Covert array to proper heap
        comparisons = heap.arrayToHeap(heap.array);
         
        // Get sorted items using GetMin
        for (int i = 0; i < array.length; i++) {
            array[i] = heap.Get_Min().getKey();
            comparisons = comparisons + heap.Delete_Min();
        }
 
        return comparisons;
    }
}