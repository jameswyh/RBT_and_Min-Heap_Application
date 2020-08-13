public class MinHeap { 
	//This class describes the structure of Min-heap implemented in the program. 
	//The Min-heap is implemented by array. 
	
	private Triplet[] heap = new Triplet[2000];//the array used to implement the Min-heap.
    private int size = 0;//the size of the Min-heap.

    public Triplet removeMin() {
    	//Get the min element out of the Min-heap.
    	if(size > 0) {
	        Triplet min = heap[0];
	        heap[0] = heap[size - 1];
	        heap[size - 1] = null;
	        size--;
	        minHeapify(0);
	        return min;
    	}
    	else
    		return null;
    }

    private void swap(int x, int y) {
    	//Swap the position of two elements in the Min-heap.
        Triplet t;
        t = heap[x];
        heap[x] = heap[y];
        heap[y] = t;
    }

    private void minHeapify(int x) {
    	//Heapify the heap when the heap is not satisfied as a Min-heap according to the executed_time of each element. 
    	//When two elements have the same executed_time, the element with smaller buildingNum become the smaller element in the Min-heap.
        if(size > 0) {
        	if(heap[left(x)] != null && heap[right(x)] != null) {
        		if(heap[left(x)].executed_time < heap[right(x)].executed_time || (heap[right(x)].executed_time == heap[left(x)].executed_time && heap[left(x)].buildingNum < heap[right(x)].buildingNum)) {
        			if(heap[x].executed_time > heap[left(x)].executed_time || (heap[x].executed_time == heap[left(x)].executed_time && heap[x].buildingNum > heap[left(x)].buildingNum)) {
        				swap(x, left(x));
        				minHeapify(left(x));
        			}
        		}
        		else if(heap[left(x)].executed_time > heap[right(x)].executed_time || (heap[right(x)].executed_time == heap[left(x)].executed_time && heap[left(x)].buildingNum > heap[right(x)].buildingNum)) {
        			if(heap[x].executed_time > heap[right(x)].executed_time || (heap[x].executed_time == heap[right(x)].executed_time && heap[x].buildingNum > heap[right(x)].buildingNum)) {
        				swap(x, right(x));
        				minHeapify(right(x));
        			}
        		}
        	}
        	else if(heap[right(x)] != null) {
        		if(heap[x].executed_time > heap[right(x)].executed_time || (heap[x].executed_time == heap[right(x)].executed_time && heap[x].buildingNum > heap[right(x)].buildingNum)) {
  					swap(x, right(x));
    				minHeapify(right(x));
        		}
        	}
        	else if(heap[left(x)] != null) {
        		if(heap[x].executed_time > heap[left(x)].executed_time || (heap[x].executed_time == heap[left(x)].executed_time && heap[x].buildingNum > heap[left(x)].buildingNum)) {
  					swap(x, left(x));
    				minHeapify(left(x));
        		}
        	}
        }
    }

    public void add(Triplet x) {
    	//dd new element to the Min-heap.
        heap[size] = x;
        int cur = size;
        while ((heap[cur].executed_time < heap[parent(cur)].executed_time) || ((heap[cur].executed_time == heap[parent(cur)].executed_time) && (heap[cur].buildingNum < heap[parent(cur)].buildingNum))) {
            swap(cur, parent(cur));
            cur = parent(cur);
        }
        size++;
    }

    private int parent(int x) {
    	//Get the parent element of a certain element.
        return (x-1)/2;
    }

    private int left(int x) {
    	//Get the left child element of a certain element.
        return 2*x + 1;
    }

    private int right(int x) {
    	//Get the right child element of a certain element.
        return 2*x + 2;
    }
}