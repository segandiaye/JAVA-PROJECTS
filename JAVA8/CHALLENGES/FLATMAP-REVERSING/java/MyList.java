import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyList {
  public static List<?> partition(List<?> list, int height){
    final int listSize = list.size();
    final int endRangeExclusive = listSize%height == 0 ? listSize/height : listSize/height + 1;

    List<List<?>> result = IntStream
      .range(0, endRangeExclusive)
      .mapToObj(x -> {
        int fromIndex = x*height;
        int toIndex = (x+1)*height;

        if((x == endRangeExclusive - 1) && listSize%height != 0){
          fromIndex = listSize - listSize%height;
          toIndex = listSize;
        }

        return list.subList(fromIndex, toIndex);
      })
      .collect(Collectors.toList());

    return result;
  }
}
