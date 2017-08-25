#include <Servo.h> 

#define trigPin 12
#define echoPin 11

Servo serwo;  

int getDistance()
{
	float dis;

	digitalWrite(trigPin, LOW);
	delayMicroseconds(2);
	digitalWrite(trigPin, HIGH);
	delayMicroseconds(10);
	digitalWrite(trigPin, LOW);

	dis = pulseIn(echoPin, HIGH);

	return dis / 58;
}

void setup()
{
	Serial.begin(9600);
	serwo.attach(9); 

	pinMode(trigPin, OUTPUT); 
	pinMode(echoPin, INPUT); 
}

void msg(int dis, int theta)
{
	int mag = 10000 + theta * 10 + dis;
	Serial.println(mag);
}

void loop()
{
	for (int i = 0; i < 360; i++)
	{
		int dis = getDistance();

		if (dis > 50)
		{
			dis = 50;
		}

		int val = map(dis, 0, 50, 0, 5);

		if (i <= 180)
		{
			serwo.write(i);
			msg(val, i);
		}
		else
		{
			serwo.write(360 - i);
			msg(val, 360 - i);
		}

		delay(200);
	}

}