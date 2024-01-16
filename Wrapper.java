package neuralnet2;

//much of the implementation design inspired by an online resource

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.io.*;
import java.util.*;

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
  
  public static final int UPDATEMETHOD = 1;  //1 = Backpropogation, 0 = Genetic Algorithm, 2 = both? Backpropogation appears to be impossible because of local maximums around 450?
  public static final int TRYTHIS = 2; //1 = new, 0 = old, 2 = working?
  public static final int MAXGEN = 333;
  public static ArrayList<String> numberToConstellation = new ArrayList<String>();
  public static Scanner sc;
  public static Scanner sc2;
  
  public static ArrayList<Double> rightAscensions = new ArrayList<Double>();
  public static ArrayList<Double> declinations = new ArrayList<Double>();
  public static ArrayList<Double> givenWeights = new ArrayList<Double>();
  public static ArrayList<String> answers = new ArrayList<String>();
  public static int thing;
  
  //in theory, all of the parameters could go here and controller's constructor could be expanded to take them all in 
  //debatable if that's what you'd want, though
  
  public Wrapper() {     //normal stuff for a timer based simulation
    setSize(FRAMESIZE+HRZSPACE, FRAMESIZE+BTNSPACE);
    //add(new ControllerMS(FRAMESIZE, FRAMESIZE));
    add(new ControllerStars(FRAMESIZE, FRAMESIZE));
    //setResizable(false);
    //setTitle("Neural net agents");
    //setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public static void main(String[] args) throws IOException , FileNotFoundException {
    sc = new Scanner(new File("/Users/forest/neuralnet2/Constellations_and_Stars.txt")); //training set
    //sc = new Scanner(new File("/Users/forest/neuralnet2/Constellations_and_Stars_2.txt"));
    //sc = new Scanner(new File("/Users/forest/neuralnet2/Constellations_and_Stars_3.txt")); //testing set
    //sc = new Scanner(new File("/Users/forest/neuralnet2/Constellations_and_Stars_4.txt")); //small set
    sc2 = new Scanner(System.in);
    int q = 0;
    while (q < 2)
    {
      numberToConstellation.add(sc.next());
      q++;
    }
    
    while (Wrapper.sc.hasNext())
    {
      //System.out.println("Line");
      rightAscensions.add(Wrapper.sc.nextDouble());
      //System.out.println("Declination");
      declinations.add(Wrapper.sc.nextDouble());
      
      //System.out.println("Answer");
      answers.add(Wrapper.sc.next());
    }
    //System.out.println(answers.size());
    
    thing = 0;
    if (thing == 1)
    {
      givenWeights = new ArrayList<Double>();
      q = 0;
      while (q < 12)
      {
        givenWeights.add(sc2.nextDouble());
        q++;
      }
      NeuralNetwork brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
      brain.replaceWeights(givenWeights);
      while (2 > 1)
      {
        ArrayList<Double> inputs = new ArrayList<Double>();
        System.out.println("Right Ascension");
        inputs.add(sc2.nextDouble());
        System.out.println("Declination");
        inputs.add(sc2.nextDouble());
        ArrayList<Double> output = brain.Update(inputs);
        if (output.size() < Params.OUTPUTS) {
          System.out.println("Incorrect number of outputs."); //something went really wrong if this happens
        }
        /*
         q = 0;
         double sum = 0;
         while (q < Params.OUTPUTS)
         {
         sum += output.get(q);
         q++;
         }
         q = 0;
         while (q < Params.OUTPUTS)
         {
         output.set(q, output.get(q) / sum);
         q++;
         }
         */
        q = 0;
        double max = 0;
        while (q < Params.OUTPUTS)
        {
          System.out.print(numberToConstellation.get(q) + ": " + output.get(q) + ". ");
          max = Math.max(max, output.get(q));
          q++;
        }
        q = 0;
        while (q < Params.OUTPUTS)
        {
          if (output.get(q) == max)
          {
            System.out.println("This is in the constellation of " + numberToConstellation.get(q));
          }
          q++;
        }
      }
    }
    else if (thing == 2)
    {
      //ArrayList<Double> givenWeights = new ArrayList<Double>();
      q = 0;
      while (q < 12)
      {
        givenWeights.add(sc2.nextDouble());
        q++;
      }
      //1System.out.println(givenWeights);
      EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          Wrapper go = new Wrapper();
          //go.setVisible(true);
          go.setVisible(false);
        }
      });
    }
    else if (thing == 3)
    {
      int correct = 0;
      q = 0;
      while (q < 12)
      {
        givenWeights.add(sc2.nextDouble());
        q++;
      }
      NeuralNetwork brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
      brain.replaceWeights(givenWeights);
      q = 0;
      while (q < 300)
      {
        ArrayList<Double> inputs = new ArrayList<Double>();
        //System.out.println("Right Ascension");
        inputs.add(rightAscensions.get(q));
        //System.out.println("Declination");
        inputs.add(declinations.get(q));
        ArrayList<Double> output = brain.Update(inputs);
        if (output.size() < Params.OUTPUTS) {
          System.out.println("Incorrect number of outputs."); //something went really wrong if this happens
        }
        String answer = "";
        int k = 0;
        double max = 0;
        while (k < Params.OUTPUTS)
        {
          System.out.print(numberToConstellation.get(k) + ": " + output.get(k) + ". ");
          max = Math.max(max, output.get(k));
          k++;
        }
        /*
         k = 0;
         while (k < Params.OUTPUTS)
         {
         if (output.get(k) == max)
         {
         answer = numberToConstellation.get(k);
         }
         k++;
         }
         */
        k = Params.OUTPUTS - 1;
        while (k >= 0)
        {
          if (output.get(k) == max)
          {
            answer = numberToConstellation.get(k);
          }
          k--;
        }
        System.out.println("(RA, Dec) = (" + rightAscensions.get(q) + ", " + declinations.get(q) + "). This is in the constellation of " + answer + ". And it is actually in the constellation of " + answers.get(q));
        if (answers.get(q).equals(answer))
        {
          //System.out.println("The result was correct");
          correct++;
        }
        q++;
      }
      System.out.println(correct);
    }
    else if (thing == 4)
    {
      givenWeights = new ArrayList<Double>();
      while (2 > 1)
      {
        givenWeights = new ArrayList<Double>();
        double fitness = 2 * answers.size();
        q = 0;
        while (q < 12)
        {
          givenWeights.add(sc2.nextDouble());
          q++;
        }
        NeuralNetwork brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
        brain.replaceWeights(givenWeights);
        q = 0;
        while (q < 300)
        {
          ArrayList<Double> inputs = new ArrayList<Double>();
          //System.out.println("Right Ascension");
          inputs.add(rightAscensions.get(q));
          //System.out.println("Declination");
          inputs.add(declinations.get(q));
          ArrayList<Double> output = brain.Update(inputs);
          if (output.size() < Params.OUTPUTS) {
            System.out.println("Incorrect number of outputs."); //something went really wrong if this happens
          }
          int answer = 0;
          int k = 0;
          
          ArrayList<Double> results = new ArrayList<Double>(); //get the results
          k = 0;
          while (k < Params.OUTPUTS)
          {
            results.add(output.get(k));
            k++;
          }
          String answerString = Wrapper.answers.get(q);
          answer = Wrapper.numberToConstellation.indexOf(answerString);
          
          ArrayList<Double> rightAnswer = new ArrayList<Double>();
          k = 0;
          while (k < Params.OUTPUTS)
          {
            if (k == answer)
              //if (1 == q)
            {
              rightAnswer.add(1.0);
            }
            else
            {
              rightAnswer.add(0.0);
            }
            k++;
          }
          
          k = 0;
          while (k < Params.OUTPUTS)
          {
            fitness -= (rightAnswer.get(k) - results.get(k)) * (rightAnswer.get(k) - results.get(k));
            //System.out.println(fitness);
            k++;
          }
          q++;
        }
        System.out.println(fitness);
      }
    }
    else
    {
      EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          Wrapper go = new Wrapper();
          //go.setVisible(true);
          go.setVisible(false);
        }
      });
    }
    
    
    /*
     HashSet<String> constellations = new HashSet<String>();
     int q = 0;
     while (q < 300)
     {
     constellations.add(sc.next());
     q++;
     }
     q = 0;
     for (String i : constellations)
     {
     System.out.println(i + " " + q);
     q++;
     }
     */
  }
}
