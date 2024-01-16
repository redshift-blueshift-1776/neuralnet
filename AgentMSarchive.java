package neuralnet2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

//you'd write a different agent class for something other than this particular approach to minesweeping (or something other than minesweeping)
public class AgentMS {
  private NeuralNetwork brain; //each agent has a brain (neural net)
  private Point2D position;  //where the agent is on the map
  private Point2D facing;   //which way they're facing (used as inputs) as an (x, y) pair
  private double rotation;  //the angle from which facing is calculated
  private double rotation2; //temporary storage
  private double speed;   //the speed of the agent
  private double lTrack, rTrack;  //the influence rating toward turning left and turning right, used as outputs
  private double fitness;   //how well the agent is doing, quantified (for the genetic algorithm)
  private double scale;    //the size of the agent
  private int closestMine;  //the index in the mines list of the mine closest to the agent (used to determine inputs for the neural net)
  
  public AgentMS() { //initialization
    Random rnd = new Random();
    brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
    rotation = rnd.nextDouble() * Math.PI * 2;
    lTrack = 0.16;
    rTrack = 0.16;
    fitness = 0;
    scale = ControllerMS.SCALE;
    closestMine = 0;
    position = new Point2D.Double(rnd.nextDouble() * Params.WIN_WIDTH, rnd.nextDouble() * Params.WIN_HEIGHT);
    facing = new Point2D.Double(-Math.sin(rotation), Math.cos(rotation));
  }
  
  public boolean update(ArrayList<Point2D> mines) {  //updates all the parameters of the sweeper, sounds fairly important
    if (Wrapper.METAANGLE == 2)
    {
    rotation = rotation2;
    }
    ArrayList<Double> inputs = new ArrayList<Double>();
    //find the closest mine, figure out the direction the mine is from the sweeper's perspective by creating a unit vector
    
    //your code goes here
    Point2D here = getPos();
    Point2D target = getClosestMine(mines);
    //Point2D target = mines.get(0);
    /*
    double closestSoFar = 99999999;
    Point2D closestObject = new Point2D.Double(0,0);
    double length;
    for (int i = 0; i < mines.size(); i++) {
      length = Point2D.distanceSq(mines.get(i).getX(), mines.get(i).getY(), position.getX(), position.getY());
      if (length < closestSoFar) {
        closestSoFar = length;
        closestObject = new Point2D.Double(position.getX() - mines.get(i).getX(), position.getY() - mines.get(i).getY());
        closestMine = i;
      }
    }
    target = closestObject;
    */
    //double dx = target.getX() - here.getX();
    //double dy = target.getY() - here.getY();
    double dx = target.getX();
    double dy = target.getY();
    double d = Point2D.distance(target.getX(), target.getY(), 0, 0);
    //dx = dx / d;
    //dy = dy / d;
    double angle = 0;
    if (dx != 0)
    {
      angle = Math.atan(dy / dx);
    }
    else
    {
      if (dy > 0)
      {
      angle = Math.PI / 2;
      }
      else
      {
      angle = Math.PI / -2;
      }
    }
    
    double angle2 = 0;
    if (facing.getX() != 0)
    {
      angle2 = Math.atan(facing.getY() / facing.getX());
    }
    else
    {
      if (facing.getY() > 0)
      {
      angle2 = Math.PI / 2;
      }
      else
      {
      angle2 = Math.PI / -2;
      }
    }
    
    
    //create the inputs for the neural net
    
    //your code goes here
    inputs.add(angle);
    //inputs.add(dx);
    //inputs.add(here.getX());
    //inputs.add(dy);
    //inputs.add(facing.getX());
    //inputs.add(facing.getY());
    //inputs.add(target.getX());
    //inputs.add(target.getY());
    inputs.add(angle2);
    //inputs.add(d);
    
    //System.out.println(target);
    //System.out.println(angle + " " + angle2);
    
    //get outputs from the sweeper's brain
    ArrayList<Double> output = brain.Update(inputs);
    if (output.size() < Params.OUTPUTS) {
      System.out.println("Incorrect number of outputs.");
      return false; //something went really wrong if this happens
    }
    
    //turn left or turn right?
    lTrack = output.get(0);
    rTrack = output.get(1);
    //lTrack = angle;
    //rTrack = angle2;
    double rotationForce = lTrack - rTrack;
    //double rotationForce = lTrack;
    if (Wrapper.METAANGLE != 5)
    {
    rotationForce = Math.min(ControllerMS.MAX_TURN_RATE, Math.max(rotationForce,  -ControllerMS.MAX_TURN_RATE)); //clamp between lower and upper bounds
    }
    rotation += rotationForce;
    rotation = (rotation + 420 * Math.PI) % (2 * Math.PI);
    rotation2 = rotation;
    if (Wrapper.METAANGLE == 1)
    {
    rotation = angle + Math.PI / 2;
    }
    if (Wrapper.METAANGLE == 2)
    {
    //rotation = (Math.PI / 2) * Math.round(2 * rotation / Math.PI + 1);
      rotation = (rotation + 420 * Math.PI) % (2 * Math.PI);
      if ((rotation >= 0) && (rotation < Math.PI / 4))
      {
        rotation = 0;
      }
      else if ((rotation >= Math.PI / 4) && (rotation < 3 * Math.PI / 4))
        {
        rotation = Math.PI / 2;
      }
      else if ((rotation >= 3 * Math.PI / 4) && (rotation < 5 * Math.PI / 4))
        {
        rotation = Math.PI;
      }
      else if ((rotation >= 5 * Math.PI / 4) && (rotation < 7 * Math.PI / 4))
        {
        rotation = 3 * Math.PI / 2;
      }
      else if ((rotation >= 7 * Math.PI / 4) && (rotation < 2 * Math.PI))
      {
        rotation = 0;
      }
    }
    if (Wrapper.METAANGLE == 3)
    {
    //rotation = (Math.PI / 2) * Math.round(2 * rotation / Math.PI + 1);
      angle = (angle + 420.5 * Math.PI) % (2 * Math.PI);
      if ((angle >= 0) && (angle < Math.PI / 4))
      {
        rotation = 0;
      }
      else if ((angle >= Math.PI / 4) && (angle < 3 * Math.PI / 4))
        {
        rotation = Math.PI / 2;
      }
      else if ((angle >= 3 * Math.PI / 4) && (angle < 5 * Math.PI / 4))
        {
        rotation = Math.PI;
      }
      else if ((angle >= 5 * Math.PI / 4) && (angle < 7 * Math.PI / 4))
        {
        rotation = 3 * Math.PI / 2;
      }
      else if ((angle >= 7 * Math.PI / 4) && (angle < 2 * Math.PI))
      {
        rotation = 0;
      }
    }
//System.out.println(lTrack + " " + rTrack);
    
    //update the speed and direction of the sweeper
    speed = Math.min(ControllerMS.MAX_SPEED, lTrack + rTrack);
    //speed = Math.min(ControllerMS.MAX_SPEED, rTrack);
    if (Wrapper.METASPEED == 1)
    {
    speed = d;
    }
    if (Wrapper.METAANGLE == 2)
    {
    speed = ControllerMS.MAX_SPEED;
    }
    if (Wrapper.METAANGLE == 3)
    {
    speed = ControllerMS.MAX_SPEED;
    }
    facing.setLocation(-Math.sin(rotation), Math.cos(rotation));
    
    //then update the position, torus style (1) or square style (2)
    double xPos = 0; //1
    double yPos = 0;
    if (Wrapper.MAPSTUFF == 1)
    {
    xPos = (Params.WIN_WIDTH + position.getX() + facing.getX() * speed) % Params.WIN_WIDTH; //1
    yPos = (Params.WIN_HEIGHT + position.getY() + facing.getY() * speed) % Params.WIN_HEIGHT;
    }
    if (Wrapper.MAPSTUFF == 2)
    {
    xPos = (position.getX() + facing.getX() * speed); //2
    yPos = (position.getY() + facing.getY() * speed);
    }
    //*/
    position.setLocation(xPos, yPos);
    //rotation = rotation2;
    return true;
  }
  
  public Point2D getClosestMine(ArrayList<Point2D> mines) { //finds the mine closest to the sweeper
    double closestSoFar = 99999999;
    Point2D closestObject = new Point2D.Double(0,0);
    double length;
    for (int i = 0; i < mines.size(); i++) {
      length = Point2D.distanceSq(mines.get(i).getX(), mines.get(i).getY(), position.getX(), position.getY());
      if (length < closestSoFar) {
        closestSoFar = length;
        //closestObject = new Point2D.Double(mines.get(i).getX(), mines.get(i).getY());
        closestObject = new Point2D.Double(position.getX() - mines.get(i).getX(), position.getY() - mines.get(i).getY());
        closestMine = i;
      }
    }
    return closestObject;
  }
  
  public int checkForMine(ArrayList<Point2D> mines, double size) { //has the sweeper actually swept up the closest mine to it this tick?
    if (Point2D.distance(position.getX(), position.getY(), mines.get(closestMine).getX(), mines.get(closestMine).getY()) < (size + scale / 2)) {
      incrementFitness();
      return closestMine;
    }
    return -1;
  }
  
  public void reset() { //reinitialize this sweeper's position/direction values
    Random rnd = new Random();
    rotation = rnd.nextDouble() * Math.PI * 2;
    position = new Point2D.Double(rnd.nextDouble() * Params.WIN_WIDTH, rnd.nextDouble() * Params.WIN_HEIGHT);
    facing = new Point2D.Double(-Math.sin(rotation), Math.cos(rotation));
    fitness = 0;
  }
  
  public void draw(Graphics2D g) { //draw the sweeper in its correct place
    AffineTransform at = g.getTransform(); //affine transforms are a neat application of matrix algebra
    g.rotate(rotation, position.getX(), position.getY()); //they allow you to rotate a g.draw kind of function's output
    //draw the sweeper using a fancy color scheme
    g.setColor(new Color(255, 255, 0));
    g.drawRect((int)(position.getX() - scale / 2), (int)(position.getY()-scale / 2), (int)scale,  (int)scale);
    g.setColor(new Color(0, Math.min(255, 15+(int)fitness*12), Math.min(255, 15+(int)fitness*12)));
    g.fillRect((int)(position.getX() - scale / 2)+1, (int)(position.getY()-scale / 2)+1, (int)scale-2,  (int)scale-2);
    
    
    //draw the direction it's facing 
    g.setTransform(at); //set the transform back to the normal transform
    g.setColor(new Color(255, 0, 255));
    //g.drawLine((int)(position.getX()), (int)(position.getY()), (int)(position.getX() - scale / 2 + facing.getX()*scale), (int)(position.getY() - scale / 2 + facing.getY()*scale));
    g.drawLine((int)(position.getX()), (int)(position.getY()), (int)(position.getX() + facing.getX()*scale), (int)(position.getY() + facing.getY()*scale));
    
    //draw its fitness
    g.setColor(new Color(0, 255, 255));
    g.drawString("" + fitness, (int)position.getX() - (int)(scale / 2), (int)position.getY() +2*(int)scale);
    
    //you're welcome to alter the drawing, I just wanted something simple and quasi-functional
  }
  
  //simple functions 
  public Point2D getPos() { return position; }
  public void incrementFitness() { fitness++; } //this may need to get more elaborate pending what you would want sweepers to learn...
  public double getFitness() { return fitness; }
  public void putWeights(ArrayList<Double> w) { brain.replaceWeights(w); }
  public int getNumberOfWeights() { return brain.getNumberOfWeights(); }
}
