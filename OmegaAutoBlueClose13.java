package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class OmegaAutoBlueClose13 extends Omega11{

    public void runOpMode() throws InterruptedException {

        init();
        forward(3);         //three tiles
        turn_right(1346);   //90 degrees
        forward(1);         //one tile
        turn_left(1346);    //90 degrees
        forward(1);         //one tile
        turn_right(1346);   //90 degrees
        forward(1);         //to 2 inches from bucket
        drop();             //drops the climbers
        backward(1);        //one tile
        turn_right(1346);   //90 degrees
        forward(1.5);       //one and a half tiles
        turn_left(673);     //45 degrees
        forward(10);        //to mid-zone
    }
}