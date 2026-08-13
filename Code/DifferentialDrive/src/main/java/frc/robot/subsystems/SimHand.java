package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

public class SimHand extends SubsystemBase {
    private PIDController handPID = new PIDController(1.5, 0, 0.0);
    private PIDController handVelocity = new PIDController(1.5, 0, 0.0);

    private static double Encoder_Zero = -180;
    private static double Encoder_Articulation = 180;

    private final static double Hand_Zero = -Math.PI;
    private final static double Hand_Articulation = Math.PI;
    private static double CurrentAngle = 0;
    private static double OutPosition = 0;
    private static double SetAngle = 0;

    private static double SetpointVelocity = 0;
    private static double CurrentVelocity = 0;
    private static double Rotation = 0;

    
    public SimHand(double articulationMin, double articulationMax, double kPAngle, double kPWheel){
        Encoder_Zero = articulationMin;
        Encoder_Articulation = articulationMax;
        handPID.setP(kPAngle);
        handVelocity.setP(kPWheel);
    }

    @Override
    public void periodic() {

        handArticulation();
        handVelocity();

        double Whell1_CO = 0.715675 * Math.sin(Math.toRadians(13.97 + Math.toDegrees(getRawAngle())));
        double Whell1_CA = 0.715675 * Math.cos(Math.toRadians(13.97 + Math.toDegrees(getRawAngle())));

        double Whell2_CO = 0.715675 * Math.sin(Math.toRadians(-13.97 + Math.toDegrees(getRawAngle())));
        double Whell2_CA = 0.715675 * Math.cos(Math.toRadians(-13.97 + Math.toDegrees(getRawAngle())));

        Logger.recordOutput("SubSystemSim/HandVelocity", CurrentVelocity);
        Logger.recordOutput("SubSystemSim/AngleHand", Math.toDegrees(SetAngle));

        Logger.recordOutput("SubSystemSim/Hand", new Pose3d[] { new Pose3d(
            -0.162433, 0.0, SimElevator.getRawPosition() + 1.032257, new Rotation3d(getRawAngle(), 0, 0))});

        Logger.recordOutput("SubSystemSim/Hand/wheel1", new Pose3d[] { new Pose3d(
            0.028800, Whell1_CO, SimElevator.getRawPosition() + (1.032257 - Whell1_CA), new Rotation3d(Rotation, 0, 0))});
        
        Logger.recordOutput("SubSystemSim/Hand/wheel2", new Pose3d[] { new Pose3d(
            0.028800, Whell2_CO, SimElevator.getRawPosition() + (1.032257 - Whell2_CA), new Rotation3d(-Rotation, 0, 0))});
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

    /**
    * @return null
    *
    * @param kP Define o ganho proporcional do sistema Hand.
    */
    public void config(double kP) {
        handPID.setP(kP);
    }

    /**
    * @return Retorna o angulo do Hand com base no CAD.
    *
    * @param null.
    */
    public static double getRawAngle() {
        return CurrentAngle;
    }

    /**
    * @return Retorna o angulo do Hand com base no encoder.
    *
    * @param null.
    */
    public static double getRealAngle() {
        return map(CurrentAngle, Hand_Zero, Hand_Articulation, Encoder_Zero, Encoder_Articulation);
    }

    /**
    * @return null
    *
    * @param angle Define o setpoint do angulo do sistema Hand
    * @param kP Define o ganho proporcional do sistema que angulo o Hand.
    */
    public void setAngle(double angle, double kP) {
        config(kP);
        SetAngle = map(angle, Encoder_Zero, Encoder_Articulation, Hand_Zero, Hand_Articulation);
    }

    /**
    * @return null
    *
    * @param speed Define a velocidade do outtake.
    */
    public void setVelocity(double speed) {
        SetpointVelocity = speed;
    }

    /**
    * @return null
    *
    * @param null.
    */
    private void handArticulation(){
        OutPosition = handPID.calculate(CurrentAngle, SetAngle);
        CurrentAngle += OutPosition * 0.02;
        CurrentAngle = MathUtil.clamp(CurrentAngle, Hand_Zero, Hand_Articulation);
    }

    /**
    * @return null
    *
    * @param null.
    */
    private void handVelocity(){
        double handVelocityOutput = handVelocity.calculate(CurrentVelocity, SetpointVelocity);
        CurrentVelocity += handVelocityOutput * 0.02;

        Rotation = Rotation + (0.1 * CurrentVelocity);
        if(Rotation > Math.PI || Rotation < -Math.PI){
            Rotation = 0;
        }
    }
}
