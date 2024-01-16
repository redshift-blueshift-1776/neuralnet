package neuralnet2;

//much of the implementation design inspired by an online resource

import java.awt.EventQueue;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Wrapper extends JFrame {
  
  public static final int FRAMESIZE = 700;  //sizing of the window
  public static final int BTNSPACE = 126;  //room for the buttons
  public static final int HRZSPACE = 8;  //a bit of side padding
  
  public static final int MINESTUFF = 1;  //1 = standard, 2 = removes mines, 3 = one line, 4 = duplicates mines
  public static final int MAPSTUFF = 1;  //1 = torus, 2 = square
  public static final int RESETMINES = 1;  //1 = yes, 0 = no
  
  public static final int METAANGLE = 2;
  //0 = no
  //1 = yes (only goes to the left)
  //2 = taxicab (can't use metaspeed)
  //3 = taxicab meta (not completely right?)
  //5 = instant turning (not working because of NN structure)
  
//taxicab is better than normal in both categories because of the speed and instant turning
  //taxicab meta is better at average fitness because of the speed, but worse at best fitness because of the distance
  
  public static final int METASPEED = 0;  //1 = yes, 0 = no
  
  //in theory, all of the parameters could go here and controller's constructor could be expanded to take them all in 
  //debatable if that's what you'd want, though
  
  public Wrapper() {     //normal stuff for a timer based simulation
    setSize(FRAMESIZE+HRZSPACE, FRAMESIZE+BTNSPACE);
    add(new ControllerMS(FRAMESIZE, FRAMESIZE));
    setResizable(false);
    setTitle("Neural net agents");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        Wrapper go = new Wrapper();
        go.setVisible(true);
      }
    });
  }
}
