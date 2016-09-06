/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.hardware.DcMotorController;


/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaAutoBlueFar10 extends OmegaTeleOp10{










	final static int ENCODER_CPR = 1120;
	final static double GEAR_RATIO = 2;
	final static int WHEEL_DIAMETER = 6;
	int DISTANCE = 12; //inches

	final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
	private double ROTATIONS = DISTANCE / CIRCUMFERENCE;
	private double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;



	/**
	 * Constructor
	 */
	public OmegaAutoBlueFar10(){

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {



		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the Omega Autonomous we continue the Omega Tele op which has the flowing motors,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *   "motor_3" is on the right side of the bot.
		 *   "motor_4" is on the left side of the bot and reversed.
		 *   
		 *
		 */




	}

	public void start()
	{


	}

	public void reset_encoders()
    {
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public void forward()
    {
        motorFrontLeft.setPower(1);
        motorFrontRight.setPower(1);
        motorBackLeft.setPower(1);
        motorBackRight.setPower(1);
    }

    public void halt()
    {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public void turn_right()
    {
        motorFrontLeft.setPower(1);
        motorFrontRight.setPower(1);
        motorBackLeft.setPower(-1);
        motorBackRight.setPower(-1);
    }

    public void turn_left()
    {
        motorFrontLeft.setPower(-1);
        motorFrontRight.setPower(-1);
        motorBackLeft.setPower(1);
        motorBackRight.setPower(1);
    }

    public boolean have_encoders_reached(){
        if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() >= COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS){
            return true;
        }
        else{
            return false;
        }
    }
    public void revert_encoders(){
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public boolean turnL_encoder_value(){
        if (motorBackLeft.getCurrentPosition() >= -COUNTS || motorBackRight.getCurrentPosition() >= COUNTS || motorFrontLeft.getCurrentPosition() >= -COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean turnR_encoder_value(){
        if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() >= -COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() >= -COUNTS){
            return true;
        }
        else{
            return false;
        }
    }



	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        telemetry.addData("time",this.time);
        telemetry.addData("Run Mode", motorBackLeft.getMode());
        telemetry.addData("Back Left Motor", motorBackLeft.getCurrentPosition());
        telemetry.addData("Front Left Motor", motorFrontLeft.getCurrentPosition());
        telemetry.addData("Back Right Motor", motorBackRight.getCurrentPosition());
        telemetry.addData("Front Right Motor", motorFrontRight.getCurrentPosition());
        reset_encoders();
        revert_encoders();

		if(this.time > 0 && this.time < 2) {
            forward();
            if (have_encoders_reached())
                halt();

		}

        if(this.time > 2 && this.time < 2.4){
            reset_encoders();

        }
        if(this.time > 2.4 && this.time < 3.4){
            revert_encoders();
            turn_left();

            if(have_encoders_reached())
                halt();
        }















	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}




}
