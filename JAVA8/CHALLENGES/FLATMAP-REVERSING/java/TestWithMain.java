import java.util.List;
import java.util.ArrayList;

public class TestWithMain {
  public static void main(String[] args) {
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);

    //partition([1,2,3,4,5], 2); // retourne: [ [1,2], [3,4], [5] ]
    System.out.println(MyList.partition(list, 2));

    //partition([1,2,3,4,5], 3); // retourne: [ [1,2,3], [4,5] ]
    System.out.println(MyList.partition(list, 3));

    //partition([1,2,3,4,5], 1); // retourne: [ [1], [2], [3], [4], [5]
    System.out.println(MyList.partition(list, 1));
  }
}
