package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class OmegaAutoRedFar13 extends Omega11{


    public void runOpMode() throws InterruptedException {

        init();
        forward(3);      //three tiles
        turn_left(1346);    //90 degrees
        forward(1);      //one tile
        turn_right(134);    //90 degrees
        forward(1);      //one tile
        turn_left(1346);    //90 degrees
        forward(1);      //to 2 inches from bucket
        drop();             //drops the climbers
        backward(2.5+22);//three and a half tiles
        turn_right(2019);   //135 degrees
        forward(10);     //to mid-zone


    }


}