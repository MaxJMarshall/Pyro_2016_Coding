package com.qualcomm.ftcrobotcontroller.opmodes;

public class OmegaAutoRedClose12 extends Omega10{

    public void runOpMode() throws InterruptedException {

        Myinit();
        forward(3);         //three tiles
        turn_left(1346);    //90 degrees
        forward(.75);       //one tile
        turn_left(673);     //45 degrees
        forward(1);         //one tile
    }
}