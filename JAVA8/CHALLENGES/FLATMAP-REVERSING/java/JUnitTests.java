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
    Integer[][] array2 = new Integer[][]{{1, 2, 3}, {4, 5}};
    Integer[][] array3 = new Integer[][]{{1}, {2}, {3}, {4}, {5}};

    List<Integer[][]> lists = new ArrayList<Integer[][]>();
    lists.add(array1);
    lists.add(array2);
    lists.add(array3);

    for(Integer[][] ls: lists){
      for(int i = 0; i < ls.length; i++){
          List<Integer> val = Arrays.asList(ls[i]);
          assertEquals(val, MyList.partition(list, ls[0].length).get(i));
      }
    }

  }
}
