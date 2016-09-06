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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaTeleOpFullW extends OpMode {

	DcMotor motorER1;
	DcMotor motorER2;
	DcMotor motorFrontRight;
	DcMotor motorBackRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackLeft;

	Servo servoVA1;
	Servo backDrive;
	Servo zipLeft;
	Servo zipRight;
	Servo servoClimber;
	ServoController toBeDisabled;
	boolean driveOrient = true;
	float incrementDrive = 5;
	float incrementER = 5;
	double pos = 0.54;
	double LZipPos = 65/255.0;
	double RZipPos = 190/255.0;
	/**
	 * Constructor
	 */
	public OmegaTeleOpFullW() {

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
		 * For the Omega System we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *   "motor_3" is on the right side of the bot.
		 *   "motor_4" is on the left side of the bot and reversed.
		 *
		 */
		motorER1 = hardwareMap.dcMotor.get("motorER1");
		motorER2 = hardwareMap.dcMotor.get("motorER2");
		servoVA1 = hardwareMap.servo.get("servo_1");
		servoClimber = hardwareMap.servo.get("servo_2");
		backDrive = hardwareMap.servo.get("servo_4");
		zipLeft = hardwareMap.servo.get("servo_5");
		zipRight = hardwareMap.servo.get("servo_6");
		toBeDisabled = hardwareMap.servoController.get("Servo Controller 1");
		motorFrontLeft = hardwareMap.dcMotor.get("motorL1");
		motorFrontRight = hardwareMap.dcMotor.get("motorR1");
		motorBackLeft = hardwareMap.dcMotor.get("motorL2");
		motorBackRight = hardwareMap.dcMotor.get("motorR2");
		motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
		motorBackRight.setDirection(DcMotor.Direction.REVERSE);
		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorER1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorER2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
	}
	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Gamepad 1 controls the drive motors via the left and right stick
		 *
		 * Gamepad 2 controls the arm via the
		 */

		//Code to set the relation of gamepad 1 to the code
		float throttleL = gamepad1.left_stick_y;
		float throttleR = gamepad1.right_stick_y;
		float accelerate = gamepad1.right_trigger;
		float decelerate = gamepad1.left_trigger;
		boolean full = gamepad1.right_bumper;
		boolean low = gamepad1.left_bumper;
		boolean climbers = gamepad1.a;
		boolean revDrive = gamepad1.dpad_down;
		boolean fwdDrive = gamepad1.dpad_up;
		float stopRetract = gamepad1.left_trigger;
		float letRetract = gamepad1.right_trigger;

		//Code to set the relation of gamepad 2 to the code
		float extendRetract = gamepad2.left_stick_y;
		float varAngle = gamepad2.right_stick_y;
		boolean speedUpER = gamepad2.right_bumper;
		boolean slowDownER = gamepad2.left_bumper;
		boolean left = gamepad2.x;
		boolean right = gamepad2.b;
		boolean in = gamepad2.dpad_right;
		boolean out = gamepad2.dpad_left;
		boolean stopServos = gamepad2.a;
		boolean enableServos = gamepad2.y;


		// clip the right/left values so that the values never exceed +/- 1
		throttleR = Range.clip(throttleR, -1, 1);
		throttleL = Range.clip(throttleL, -1, 1);
		incrementDrive = Range.clip(incrementDrive, 1, 5);
		incrementER = Range.clip(incrementER, 1, 5);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		throttleL = (float) scaleInput(throttleL);
		throttleR = (float) scaleInput(throttleR);


		// clip the right/left values so that the values never exceed +/- 1
		varAngle = Range.clip(varAngle, -1, 1);
		extendRetract = Range.clip(extendRetract, -1, 1);
		pos = (Range.clip(pos,.25,.99));

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		extendRetract = (float) scaleInput(extendRetract);

		// clip the right/left values so that the values never exceed +/- 1
		varAngle = Range.clip(varAngle, -1, 1);
		pos = Range.clip(pos, (float) 0.01, (float) 0.99);
		LZipPos = Range.clip(LZipPos, (float) 0.2, (float) 0.8);
		RZipPos = Range.clip(RZipPos, (float) 0.2, (float) 0.8);
		servoVA1.setPosition(pos);
		zipLeft.setPosition(LZipPos);
		zipRight.setPosition(RZipPos);

		if (varAngle > 0.1 || varAngle < -0.1) {
			pos = pos + varAngle*.007;
		}
		//Code to implement the top speed changing on drive
		if (incrementDrive == 5) {
			motorFrontRight.setPower(throttleR);
			motorFrontLeft.setPower(throttleL);
			motorBackRight.setPower(throttleR);
			motorBackLeft.setPower(throttleL);
		}
		if (incrementDrive == 4) {
			motorFrontRight.setPower(throttleR * 0.8);
			motorFrontLeft.setPower(throttleL * 0.8);
			motorBackRight.setPower(throttleR * 0.8);
			motorBackLeft.setPower(throttleL * 0.8);
		}
		if (incrementDrive == 3) {
			motorFrontRight.setPower(throttleR * 0.6);
			motorFrontLeft.setPower(throttleL * 0.6);
			motorBackRight.setPower(throttleR * 0.6);
			motorBackLeft.setPower(throttleL * 0.6);
		}
		if (incrementDrive == 2) {
			motorFrontRight.setPower(throttleR * 0.4);
			motorFrontLeft.setPower(throttleL * 0.4);
			motorBackRight.setPower(throttleR * 0.4);
			motorBackLeft.setPower(throttleL * 0.4);
		}
		if (incrementDrive == 1) {
			motorFrontRight.setPower(throttleR * 0.2);
			motorFrontLeft.setPower(throttleL * 0.2);
			motorBackRight.setPower(throttleR * 0.2);
			motorBackLeft.setPower(throttleL * 0.2);
		}
		//Code to implement the top speed changing on extend retract
		if (incrementER == 5) {
			motorER1.setPower(extendRetract);
			motorER2.setPower(extendRetract);
		}
		if (incrementER == 4) {
			motorER1.setPower(extendRetract * 0.8);
			motorER2.setPower(extendRetract * 0.8);
		}
		if (incrementER == 3) {
			motorER1.setPower(extendRetract * 0.6);
			motorER2.setPower(extendRetract * 0.6);
		}
		if (incrementER == 2) {
			motorER1.setPower(extendRetract * 0.4);
			motorER2.setPower(extendRetract * 0.4);
		}
		if (incrementER == 1) {
			motorER1.setPower(extendRetract * 0.2);
			motorER2.setPower(extendRetract * 0.2);
		}

		if(climbers){
			servoClimber.setPosition(1);
		}
		else{
			servoClimber.setPosition(0);
		}
		if(fwdDrive){
			driveOrient = true;

		}
		if(revDrive){
			driveOrient = false;
		}
		if(driveOrient){
			// write the values to the motors
			motorFrontRight.setPower(throttleR);
			motorFrontLeft.setPower(throttleL);
			motorBackRight.setPower(throttleR);
			motorBackLeft.setPower(throttleL);
		}
		if(!driveOrient){
			// write the values to the motors
			motorFrontRight.setPower(-throttleL);
			motorFrontLeft.setPower(-throttleR);
			motorBackRight.setPower(-throttleL);
			motorBackLeft.setPower(-throttleR);
		}
		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		//Code to limit the extension and retraction
		/*if(motorER1.getCurrentPosition()>1120*8){
			motorER1.setPower(-1);
		}
		if(motorER1.getCurrentPosition()<0){
			motorER1.setPower(1);
		}*/
		//Code to change the top speed of the drive
		if(accelerate == 1){
			incrementDrive++;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(decelerate == 1){
			incrementDrive--;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Code to change the top speed of the drive quickly
		if(full){
			incrementDrive = 5;
		}
		if(low){
			incrementDrive = 2;
		}
		//Code to change the top speed of the extend an retract
		if(speedUpER){
			incrementER++;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(slowDownER){
			incrementER--;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//Code to bring the left zip line servo out
		if(left&&out){
			LZipPos += .01;
		}
		//Code to bring the right zip line servo out
		if(right&&out){
			RZipPos += .01;
		}
		//Code to bring the right zip line servo in
		if(right&&in){
			RZipPos -= 0.01;
		}
		if(left&&in){
			LZipPos -= 0.01;
		}
		if(stopRetract<.6){
			backDrive.setPosition(1);
		}
		if(letRetract>.6){
			backDrive.setPosition(0);
		}
		if(letRetract==0 && stopRetract==0){
			backDrive.setPosition(.5);
		}
		if(stopServos){
			toBeDisabled.pwmDisable();
		}
		if(enableServos){
			toBeDisabled.pwmEnable();
		}
		telemetry.addData("Var Angle Pos", servoVA1.getPosition());
		telemetry.addData("Drive top speed",incrementDrive);
		telemetry.addData("Ext/Ret top speed",incrementER);
		telemetry.addData("Is Forwards",driveOrient);
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	/*@Override
	public void stop() {

	}*/

	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		if (index < 0) {
			index = -index;
		} else if (index > 16) {
			index = 16;
		}
		
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}

}
