package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SimIntake extends SubsystemBase {
    private PIDController intakePID = new PIDController(1.5, 0, 0.0);
    private PIDController intakeVelocity = new PIDController(1.5, 0, 0.0);
    
    private static double Encoder_Zero = 0;
    private static double Encoder_Foward = 50;
    
    private final static double Intake_Zero = 0;
    private final static double Intake_Foward = 0.210;
    private static double SetPosition = 0;
    private static double CurrentPosition = 0;
    private static double OutPosition = 0;

    private static double SetpointVelocity = 0;
    private static double CurrentVelocity = 0;
    private static double Rotation = 0;

    public SimIntake(double realFoward, double kPFoward, double kPWheel){
        Encoder_Foward = realFoward;
        intakePID.setP(kPFoward);
        intakeVelocity.setP(kPWheel);
    }

    @Override
    public void periodic() {

        intakeMove();
        intakeVelocity();

        double Intake_CO = CurrentPosition * Math.sin(Math.toRadians(5.71));
        double Intake_CA = CurrentPosition * Math.cos(Math.toRadians(5.71));

        Logger.recordOutput("SubSystemSim/Intake/Velocity", CurrentVelocity);

        Logger.recordOutput("SubSystemSim/Intake/Gaveta", new Pose3d[] { new Pose3d(
            Intake_CA, 0.0, -Intake_CO, new Rotation3d(0.0, 0, 0))});
        
        Logger.recordOutput("SubSystemSim/Intake/Coletor", new Pose3d[] { new Pose3d(
            Intake_CA + 0.27168 , 0.0, -Intake_CO + 0.21308 , new Rotation3d(0, Rotation, 0))});
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
    * @param kP Define o ganho proporcional do sistema Intake.
    */
    public void config(double kP) {
        intakePID.setP(kP);
    }

    /**
    * @return Retorna a posição atual de articulação do intake com base no CAD.
    *
    * @param null.
    */
    public static double getRawPosition() {
        return CurrentPosition;
    }

    /**
    * @return Retorna a posição atual de articulação do intake com base no encoder
    *
    * @param null.
    */
    public static double getRealPosition() {
        return map(CurrentPosition, Intake_Zero, Intake_Foward, Encoder_Zero, Encoder_Foward);
    }

    /**
    * @return null.
    *
    * @param position Define a velocidade do sistema de coleta do intake.
    * @param kP Define o ganho proporcional do sistema de articulação do intake.
    */
    public void setPosition(double position, double kP) {
        config(kP);
        SetPosition = map(position, Encoder_Zero, Encoder_Foward, Intake_Zero, Intake_Foward);
    }

    /**
    * @return null.
    *
    * @param speed Define a velocidade do sistema de coleta do intake.
    */
    public void setVelocity(double speed) {
        SetpointVelocity = speed;
    }

    private void intakeMove(){
        OutPosition = intakePID.calculate(CurrentPosition, SetPosition);
        CurrentPosition += OutPosition * 0.02;
        CurrentPosition = MathUtil.clamp(CurrentPosition, Intake_Zero, Intake_Foward);
    }

    private void intakeVelocity(){
        double intakeVelocityOutput = intakeVelocity.calculate(CurrentVelocity, SetpointVelocity);
        CurrentVelocity += intakeVelocityOutput * 0.02;

        Rotation = Rotation + (0.1 * CurrentVelocity);
        if(Rotation > Math.PI || Rotation < -Math.PI){
            Rotation = 0;
        }
    }
}
