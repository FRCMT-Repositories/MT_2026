package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

public class SubSystemSIM extends SubsystemBase {

    private double subGavetaPositon = 0.0;
    private double subIntakeAngle = 120;
    private double subIntakeVelocity = 0;
    private static double subShooterVelocity = 0;
    private double subClimberPositon = -0.1;

    private static double intakeAngleSim = 0.0;
    private static double climberPositionSim = -0.1;
    private double intakeVelocitySim = 0.0;
    private double shooterVelocitySim = 0.0;

    private PIDController intakePID = new PIDController(1.5, 0, 0.0);
    private PIDController intakeVelocity = new PIDController(1.5, 0, 0.0);
    private PIDController elevatorPID = new PIDController(1.5, 0, 0.0);
    private PIDController handPID = new PIDController(1.5, 0, 0.0);
    private PIDController handVelocity = new PIDController(1.5, 0, 0.0);
    
    public final class Intake {
        private final static double GavetaReal_Zero = 0;
        private final static double GavetaReal_Foward = 50;

        private final static double GavetaSIM_Zero = 0;
        private final static double GavetaSIM_Foward = 0.214334;
        private static double CurrentPosition = 0;
        private static double OutPosition = 0;
        private static double SetPosition = 0;

        private static double SetpointVelocity = 0;
        private static double CurrentVelocity = 0;
        private static double Rotation = 0;
    }

    public final class Elevator {
        private final static double Real_Zero = 0;
        private final static double Real_Up = 1000;

        private final static double SIM_Zero = 0;
        private final static double SIM_Up = 0.4;
        private static double CurrentPosition = 0;
        private static double OutPosition = 0;
        private static double SetPosition = 0;
    }

    public final class Hand {
        private final static double Real_Zero = -180;
        private final static double Real_Articulation = 180;

        private final static double SIM_Zero = -Math.PI;
        private final static double SIM_Articulation = Math.PI;
        private static double CurrentAngle = 0;
        private static double OutPosition = 0;
        private static double SetAngle = 0;

        private static double SetpointVelocity = 0;
        private static double CurrentVelocity = 0;
        private static double Rotation = 0;
    }
    public SubSystemSIM() {
        handPID.enableContinuousInput(Hand.SIM_Zero, Hand.SIM_Articulation);
    }

    public void configIntake(double kP) {
        intakePID.setP(kP);
    }

    public static double getIntakePosition() {
        return map(Intake.CurrentPosition, Intake.GavetaSIM_Zero, Intake.GavetaSIM_Foward, Intake.GavetaReal_Zero, Intake.GavetaReal_Foward);
    }

    public void setIntakePosition(double position, double kP) {
        configIntake(kP);
        Intake.SetPosition = map(position, Intake.GavetaReal_Zero, Intake.GavetaReal_Foward, Intake.GavetaSIM_Zero, Intake.GavetaSIM_Foward);
    }

    public void setIntakeVelocity(double speed) {
        Intake.SetpointVelocity = speed;
    }

    public void configElevator(double kP) {
        elevatorPID.setP(kP);
    }

    public static double getElevatorPosition() {
        return map(Elevator.CurrentPosition, Elevator.SIM_Zero, Elevator.SIM_Up, Elevator.Real_Zero, Elevator.Real_Up);
    }

    public void setElevatorPosition(double position, double kP) {
        configElevator(kP);
        Elevator.SetPosition = map(position, Elevator.Real_Zero, Elevator.Real_Up, Elevator.SIM_Zero, Elevator.SIM_Up);
    }

    public void configHand(double kP) {
        handPID.setP(kP);
    }

    public static double getHandAngle() {
        return map(Hand.CurrentAngle, Hand.SIM_Zero, Hand.SIM_Articulation, Hand.Real_Zero, Hand.Real_Articulation);
    }

    public void setHandAngle(double angle, double kP) {
        configHand(kP);
        Hand.SetAngle = map(angle, Hand.Real_Zero, Hand.Real_Articulation, Hand.SIM_Zero, Hand.SIM_Articulation);
    }

    public void setHandVelocity(double speed) {
        Hand.SetpointVelocity = speed;
    }

    // simulationPeriodic
    @Override
    public void periodic() {

        intakeMove();
        intakeVelocity();
        elevatorMove();
        handArticulation();
        handVelocity();

        double Intake_CO = Intake.CurrentPosition * Math.sin(Math.toRadians(5.71));
        double Intake_CA = Intake.CurrentPosition * Math.cos(Math.toRadians(5.71));

        double Whell1_CO = 0.715675 * Math.sin(Math.toRadians(13.97 + Math.toDegrees(Hand.CurrentAngle)));
        double Whell1_CA = 0.715675 * Math.cos(Math.toRadians(13.97 + Math.toDegrees(Hand.CurrentAngle)));

        double Whell2_CO = 0.715675 * Math.sin(Math.toRadians(-13.97 + Math.toDegrees(Hand.CurrentAngle)));
        double Whell2_CA = 0.715675 * Math.cos(Math.toRadians(-13.97 + Math.toDegrees(Hand.CurrentAngle)));

        Logger.recordOutput("SubSystemSim/IntakeVelocity", Intake.CurrentVelocity);
        Logger.recordOutput("SubSystemSim/AngleHand", Math.toDegrees(Hand.SetAngle));

        Logger.recordOutput("SubSystemSim/Intake3D/Gaveta", new Pose3d[] { new Pose3d(
            Intake_CA, 0.0, -Intake_CO, new Rotation3d(0.0, 0, 0))});
        
        Logger.recordOutput("SubSystemSim/Intake3D/Coletor", new Pose3d[] { new Pose3d(
            Intake_CA + 0.256619 , 0.0, -Intake_CO + 0.214588 , new Rotation3d(0, Intake.Rotation, 0))});
            //0.256619 distancia do centro do robô até o centro do coletor {Intake recolhido}
            //0.214588 distancia do chão até o centro do coletor {Intake recolhido}

        Logger.recordOutput("SubSystemSim/Elevador", new Pose3d[] { new Pose3d(
            0.0, 0.0, Elevator.CurrentPosition, new Rotation3d(0.0, 0, 0))});

        Logger.recordOutput("SubSystemSim/Hand", new Pose3d[] { new Pose3d(
            -0.1524, 0.0, Elevator.CurrentPosition + 0.983900, new Rotation3d(Hand.CurrentAngle, 0, 0))});

        Logger.recordOutput("SubSystemSim/wheel1", new Pose3d[] { new Pose3d(
            0.028800, Whell1_CO, Elevator.CurrentPosition + (0.9839 - Whell1_CA), new Rotation3d(Hand.Rotation, 0, 0))});
        
        Logger.recordOutput("SubSystemSim/wheel2", new Pose3d[] { new Pose3d(
            0.028800, Whell2_CO, Elevator.CurrentPosition + (0.9839 - Whell2_CA), new Rotation3d(-Hand.Rotation, 0, 0))});

    }

    private void intakeMove(){
        Intake.OutPosition = intakePID.calculate(Intake.CurrentPosition, Intake.SetPosition);
        Intake.CurrentPosition += Intake.OutPosition * 0.02;
        Intake.CurrentPosition = MathUtil.clamp(Intake.CurrentPosition, Intake.GavetaSIM_Zero, Intake.GavetaSIM_Foward);
    }

    private void intakeVelocity(){
        double intakeVelocityOutput = intakeVelocity.calculate(Intake.CurrentVelocity, Intake.SetpointVelocity);
        Intake.CurrentVelocity += intakeVelocityOutput * 0.02;

        Intake.Rotation = Intake.Rotation + (0.1 * Intake.CurrentVelocity);
        if(Intake.Rotation > Math.PI || Intake.Rotation < -Math.PI){
            Intake.Rotation = 0;
        }
    }

    private void elevatorMove(){
        Elevator.OutPosition = elevatorPID.calculate(Elevator.CurrentPosition, Elevator.SetPosition);
        Elevator.CurrentPosition += Elevator.OutPosition * 0.02;
        Elevator.CurrentPosition = MathUtil.clamp(Elevator.CurrentPosition, Elevator.SIM_Zero, Elevator.SIM_Up);
    }

    private void handArticulation(){
        Hand.OutPosition = handPID.calculate(Hand.CurrentAngle, Hand.SetAngle);
        Hand.CurrentAngle += Hand.OutPosition * 0.02;
        Hand.CurrentAngle = MathUtil.clamp(Hand.CurrentAngle, Hand.SIM_Zero, Hand.SIM_Articulation);
    }

    private void handVelocity(){
        double handVelocityOutput = handVelocity.calculate(Hand.CurrentVelocity, Hand.SetpointVelocity);
        Hand.CurrentVelocity += handVelocityOutput * 0.02;

        Hand.Rotation = Hand.Rotation + (0.1 * Hand.CurrentVelocity);
        if(Hand.Rotation > Math.PI || Hand.Rotation < -Math.PI){
            Hand.Rotation = 0;
        }
    }

    /**
    * @return um valor linear dentro do range estabelecido.
    *
    * @param x Variavel de leitura.
    * @param in_min Valor minimo de entrada da variavel x.
    * @param in_max Valor maximo de entrada da variavel x.
    * @param out_min Valor de saida minimo permitido (PODE TER B.Ozinhos).
    * @param out_max Valor de saida maximo permitido (PODE TER B.Ozinhos).
    */
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        double result = (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        return result;
    }
}
