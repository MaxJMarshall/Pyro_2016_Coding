package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class OmegaTestingOp14 extends OmegaTeleOp11{

    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;




    final static int ENCODER_CPR = 1120;
    final static double GEAR_RATIO = 1;
    final static int WHEEL_DIAMETER = 6;
    double DISTANCE = 12; //inches

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    private double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    private double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

    public void runOpMode() throws InterruptedException {

        myInit();
        revert_encoders();
        turn_right(12);  //90 degrees



    }

    public void myInit(){
        motorFrontLeft = hardwareMap.dcMotor.get("motor_1");
        motorFrontRight = hardwareMap.dcMotor.get("motor_2");
        motorBackLeft = hardwareMap.dcMotor.get("motor_3");
        motorBackRight = hardwareMap.dcMotor.get("motor_4");
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        motorVarAngle = hardwareMap.dcMotor.get("motor_5");
        motorE = hardwareMap.dcMotor.get("motor_6");
        motorR1 = hardwareMap.dcMotor.get("motor_7");
        motorR2 = hardwareMap.dcMotor.get("motor_8");

    }


    public void forward(double f)
    {
        do {
            motorFrontLeft.setPower(1);
            motorFrontRight.setPower(1);
            motorBackLeft.setPower(1);
            motorBackRight.setPower(1);
        }
        while (!have_encoders_reached(f));
        halt();
    }

    public void halt()
    {

        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public void turn_right(double f)
    {
        do {
            motorFrontLeft.setPower(1);
            motorFrontRight.setPower(-1);
            motorBackLeft.setPower(1);
            motorBackRight.setPower(-1);
        }while(!turnR_encoder_value(f));
        halt();
    }

    public void turn_left(double f)
    {

        do {
            motorFrontLeft.setPower(-1);
            motorFrontRight.setPower(1);
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(1);
        }while(!turnL_encoder_value(f));
        halt();
    }

    public boolean have_encoders_reached(double f){
        DISTANCE = f;
        if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() <= -COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() <= -COUNTS){
            revert_encoders();
            reset_encoders();
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

    public void reset_encoders()
    {
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

    }

    public boolean turnL_encoder_value(double f){
        DISTANCE = f;
        if (motorBackLeft.getCurrentPosition() <= -COUNTS || motorBackRight.getCurrentPosition() <= -COUNTS || motorFrontLeft.getCurrentPosition() <= -COUNTS || motorFrontRight.getCurrentPosition() <= -COUNTS){
            revert_encoders();
            reset_encoders();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean turnR_encoder_value(double f){
        DISTANCE = f;
        if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() >= COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS){
            revert_encoders();
            reset_encoders();
            return true;
        }
        else{
            return false;
        }
    }

}