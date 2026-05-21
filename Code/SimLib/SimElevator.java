package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
        OutPosition = elevatorPID.calculate(getRawPosition(), SetPosition);
        CurrentPosition += OutPosition * 0.02;
        CurrentPosition = MathUtil.clamp(getRawPosition(), Elevator_Zero, Elevator_Up);
        
        Logger.recordOutput("SubSystemSim/Elevator", new Pose3d[] { new Pose3d(
            0.0, 0.0, getRawPosition(), new Rotation3d(0.0, 0, 0))});
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
    * @param kP Define o ganho proporcional do sistema elevador.
    */
    public void config(double kP) {
        elevatorPID.setP(kP);
    }

    /**
    * @return Posição do elevador com base no CAD
    *
    * @param null
    */
    public static double getRawPosition() {
        return CurrentPosition;
    }

    /**
    * @return Posição do elevador com base no encoder Real
    *
    * @param null
    */
    public static double getRealPosition() {
        return map(CurrentPosition, Elevator_Zero, Elevator_Up, Encoder_Zero, Encoder_Up);
    }

    /**
    * @return null
    *
    * @param position Define o setpoint de posição do elevador.
    * @param kP Define o ganho proporcional do sistema elevador.
    */
    public void setPosition(double position, double kP) {
        config(kP);
        SetPosition = map(position, Encoder_Zero, Encoder_Up, Elevator_Zero, Elevator_Up);
    }


    public Command CMDsetPosition(double position, double kP){
        return Commands.runOnce(() -> setPosition(position, kP));
    }

}
