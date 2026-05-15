package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SimElevator extends SubsystemBase {
    private PIDController elevatorPID = new PIDController(1.5, 0, 0.0);

    private final static double Encoder_Zero = 0;  /* Valores reais do motor */
    private static double Encoder_Up = 1000;

    private final static double Elevator_Zero = 0;  /* Valores reais da distancia com base no CAD */
    private final static double Elevator_Up = 0.4;
    private static double CurrentPosition = 0;
    private static double OutPosition = 0;
    private static double SetPosition = 0;
    
    public SimElevator(double realUp, double kPUp){
        Encoder_Up = realUp;
        elevatorPID.setP(kPUp);
    }

    @Override
    public void periodic() {
        OutPosition = elevatorPID.calculate(CurrentPosition, SetPosition);
        CurrentPosition += OutPosition * 0.02;
        CurrentPosition = MathUtil.clamp(CurrentPosition, Elevator_Zero, Elevator_Up);

        Logger.recordOutput("SubSystemSim/Elevator", new Pose3d[] { new Pose3d(
            0.0, 0.0, CurrentPosition, new Rotation3d(0.0, 0, 0))});
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
        elevatorPID.setP(kP);
    }

    public static double getPosition() {
        return CurrentPosition;
    }

    public void setPosition(double position, double kP) {
        config(kP);
        SetPosition = map(position, Encoder_Zero, Encoder_Up, Elevator_Zero, Elevator_Up);
    }

}
