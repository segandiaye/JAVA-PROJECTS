import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class JUnitTests {
  @Test
  public void testMyList() {
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);

    Integer[][] array1 = new Integer[][]{{1, 2}, {3, 4}, {5}};
    //Integer[][] array2 = new Integer[][]{{1, 2, 3}, {4, 5}};
    //Integer[][] array3 = new Integer[][]{{1}, {2}, {3}, {4}, {5}};
    
    List<List<Integer>> myList1 = MyList.partition(list, 2);

    Integer[][] ints = myList1.stream()
        .map(arr -> arr.toArray(Integer[]::new))
        .toArray(Integer[][]::new);

    assertTrue(Arrays.deepEquals(array1, ints));
  }
}
