import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;

public class Test extends JFrame{
  JLabel label = new JLabel();
  private Clock clock;
  private JPanel contentPane = new JPanel();

  public  Test(String title){
    this.setTitle(title);
    this.setSize(300, 300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);

    this.clock = new Clock();
    this.clock.addObserver(new Observer(){
      public void update(String hour){
        label.setText(hour);
      }
    });

    Font police = new Font("DS-digital", Font.TYPE1_FONT, 40);
    this.label.setBackground(Color.WHITE);
    this.label.setFont(police);
    this.label.setBounds(150, 30, 300, 40);
    this.label.setHorizontalAlignment(JLabel.CENTER);

    this.contentPane.add(label);

    this.setContentPane(this.contentPane);
    this.setVisible(true);
    this.clock.run();
  }

  public static void main(String[] args){
    new Test("My Clock");
  }

}
