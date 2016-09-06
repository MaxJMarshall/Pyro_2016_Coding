package com.qualcomm.ftcrobotcontroller.opmodes;

public class OmegaAutoBlueClose12 extends Omega10{

    public void runOpMode() throws InterruptedException {

        Myinit();
        forward(3);         //three tiles
        turn_right(1346);   //90 degrees
        forward(.75);       //one tile
        turn_right(673);    //45 degrees
        forward(1);         //one tile
    }
}