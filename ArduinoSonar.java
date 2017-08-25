package arduinosonar;

import processing.core.PApplet;
import processing.serial.Serial;
import processing.data.*;

public class ArduinoSonar extends PApplet {

	int lf = 10, num = 0, r =  640, s = 640, theta = 0, dis = 0;
	String myString = null, angle = "", distance = "";
	Serial myPort; 
	boolean side = true; 
	IntList vector_theta, vector_dis;
		
	/*
	 * TRUE right to left
	 * False left to right
	 */
	
	private void draw_box(int from, int to, int dis)
	{
		//red
		fill(139, 0, 0);
		arc(640,640, 1280,1280,radians(180 + from), radians(180 + to));
		
		//green
		noStroke();
		fill(0,20,5);
		int r = dis * 256;
		arc(640,640, r,r,radians(180 + from - 1), radians(180 + to + 1));
		
	}
	
	private void check(int theta, int dis) 
	{

        if(theta == 180)
        {
            side = true; 
            vector_theta.clear();
            vector_theta.append(360);
            vector_dis.clear();
            vector_dis.append(5);
        }
        if(theta == 0)
        {
            side = false; 
            vector_theta.clear();
            vector_theta.append(0);
            vector_dis.clear();
            vector_dis.append(5);
        }
       

        if(dis == vector_dis.get(vector_dis.size() - 1))
        {

        }
        else
        {
            vector_dis.append(dis);
            vector_theta.append(theta);
        }

        fill(255,0,0);
       
        //A2D 
        if(!(vector_dis.get(vector_dis.size() - 1 ) == 5))
        {
        	if(side)//RL
        	{
        		draw_box(theta, vector_theta.get(vector_theta.size() - 1), vector_dis.get(vector_dis.size() - 1));
        	}
        	else
        	{
        		draw_box(vector_theta.get(vector_theta.size() - 1), theta, vector_dis.get(vector_dis.size() - 1));
        	}
        }
        
        //OAD 
        for(int i = 0; i < vector_dis.size() - 1; i++)
        {
        	if(!(vector_dis.get(i) == 5))
            {
            	if(side)//RL
            	{
            		draw_box(vector_theta.get(i+1), vector_theta.get(i), vector_dis.get(i));
            	}
            	else
            	{
            		draw_box(vector_theta.get(i), vector_theta.get(i+1), vector_dis.get(i));
            	}
            }
        }
	}
	
	private int theta (int code)
	{
	  int theta = code - code % 10;
	  
	  theta = theta / 10;
	  
	  theta = theta % 1000;
	 
	   return 180 - theta;
	}
	
	private int dis (int code)
	{
	  int dis = code % 10;
	  
	   return dis;
	}

	private float get_X(int r, int sx, int theta)
	{
	  float x = r * cos(radians(theta)) + sx;
	  return x;
	}

	private float get_Y(int r, int sy, int theta)
	{
	  float y = r * sin(radians(theta)) + sy;
	  return y;
	}
	
	private void draw_line()
	{
		 stroke(167);
	     strokeWeight(1);
	     
	     line(640, 640, get_X(640, 640, 210), get_Y(640,640,210)); // 30
	     line(640, 640, get_X(640, 640, 240), get_Y(640,640,240)); // 60
	     line(640, 640, get_X(640, 640, 270), get_Y(640,640,270)); // 90
	     line(640, 640, get_X(640, 640, 300), get_Y(640,640,300)); // 120
	     line(640, 640, get_X(640, 640, 330), get_Y(640,640,330)); // 150
	     
	     noFill();
	     arc(640, 640, 256, 256, radians(180), radians(360),  PIE); // 256
	     arc(640, 640, 512, 512, radians(180), radians(360),  PIE); // 
	     arc(640, 640, 768, 768, radians(180), radians(360),  PIE);
	     arc(640, 640, 1024, 1024, radians(180), radians(360),  PIE);
	     arc(640, 640, 1280, 1280, radians(180), radians(360),  PIE);
	}
		
	public void settings() 
	{
		size(1280,640 + 50);
	}
	
	public void setup() 
    {
	      background(0);
	      
	      vector_theta = new IntList();
	      vector_dis = new IntList();
	      
	      myPort = new Serial(this, Serial.list()[0], 9600);
	      myPort.clear();
    }

	public void draw() 
    {
    	while (myPort.available() > 0) 
    	  {
    	    myString = myPort.readStringUntil(lf);
    	    
    	    if (myString != null) 
    	    {
    	    	
    	     num = new Double(myString).intValue();
    	     
    	     dis = dis(num);
    	     theta = theta(num);
    	     
    	     angle = "ANGLE: " +  Integer.toString(180 - theta);
    	     distance = "Distance: " + Integer.toString(dis);
    	    } 
    	  }

		 background(0);
		 
		 fill(255);
		 textSize(50);
		 textAlign(RIGHT);
		 text(angle,600,680);
		 textAlign(LEFT);
		 text(distance,680,680);
		 
		 
	     stroke(167);
	     fill(0,20,5);
	     strokeWeight(1);
	     arc(640, 640, 1280, 1280, radians(180), radians(360),  PIE);
	     
	     check(theta, dis);
	     
	     draw_line();
	     
	     strokeWeight(4);
	     stroke(255,0,0);
	     line(640, 640, get_X(r, s, 180 + theta), get_Y(r, s, 180 + theta));
    	
	     myPort.clear();
	     delay(100);
  
    } 
	
	public static void main(String _args[]) 
	{
		PApplet.main(new String[] { arduinosonar.ArduinoSonar.class.getName() });
	}
}
