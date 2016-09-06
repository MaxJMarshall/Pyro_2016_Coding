package com.qualcomm.ftcrobotcontroller.opmodes;

public class OmegaAutoBlueFar12 extends Omega10{

    public void runOpMode() throws InterruptedException {

        Myinit();
        forward(3);         //three tiles
        turn_right(673);    //90 degrees
        forward(.45);       //one tile
        turn_left(1346);    //90 degrees
        forward(4);         //one tile
    }
}