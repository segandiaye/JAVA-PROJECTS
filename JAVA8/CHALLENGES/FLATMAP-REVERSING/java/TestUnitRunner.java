import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestUnitRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(JUnitTests.class);

      System.out.printf("Test exécuté: %s, Échoué: %s%n", result.getRunCount(), result.getFailureCount());

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }

      System.out.println("Est-ce que tout est OK? : " + result.wasSuccessful());
   }
}
