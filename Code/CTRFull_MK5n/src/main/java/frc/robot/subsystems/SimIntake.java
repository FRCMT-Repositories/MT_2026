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
    private final static double Intake_Foward = 0.214334;
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
            Intake_CA, 0.0, -Intake_CO,new Rotation3d(0.0, 0, 0))});

        Logger.recordOutput("SubSystemSim/Intake/Coletor", new Pose3d[] { new Pose3d(
            Intake_CA + 0.256619 , 0.0, -Intake_CO + 0.214588 , new Rotation3d(0, Rotation, 0))});
            //0.256619 distancia do centro do robô até o centro do coletor {Intake recolhido}
            //0.214588 distancia do chão até o centro do coletor {Intake recolhido}
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

    public void config(double kP) {
        intakePID.setP(kP);
    }

    public static double getPosition() {
        return map(CurrentPosition, Intake_Zero, Intake_Foward, Encoder_Zero, Encoder_Foward);
    }

    public void setPosition(double position, double kP) {
        config(kP);
        SetPosition = map(position, Encoder_Zero, Encoder_Foward, Intake_Zero, Intake_Foward);
    }

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
