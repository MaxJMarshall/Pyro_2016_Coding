package com.qualcomm.ftcrobotcontroller.opmodes;

public class OmegaAutoRedFar12 extends Omega10{

    public void runOpMode() throws InterruptedException {

        Myinit();
        forward(3);         //three tiles
        turn_left(673);     //45 degrees
        forward(.45);       //one tile
        turn_right(1346);   //90 degrees
        forward(4);         //one tile
    }
}