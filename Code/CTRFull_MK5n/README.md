<!-- MK5n -->

<h1>ENTENDENDO O CÓDIGO</h1>

> [!NOTE]
> **INICIALIZAÇÃO**
> <div align="justify">
> Estes passos servem apenas para nortear oque é desenvolvido para o funcionamento correto da simulação, considerando um novo template, ou seja, um código novo, caso queira utilizar o exemplo completo, os passos citados a seguir devem ser desconsiderados, visto que essas modificações já estão inclusas.
> </div>

<div align="justify">

O template base utilizado neste projeto foi desenvolvido a partir dos exemplos oficiais fornecidos pela própria CTRE através do Phoenix Tuner X, juntamente com sua documentação oficial, disponível [aqui](https://pro.docs.ctr-electronics.com/en/latest/docs/tuner/tuner-swerve/index.html).

A partir dessa estrutura inicial, foram realizadas diversas adaptações e expansões para incorporar recursos de simulação, integração com o AdvantageScope, utilização do AdvantageKit e implementação dos subsistemas específicos presentes nos robôs Modelo A e Modelo B.

<h2>PASSO 1: AdvantageKit</h2>

Ao baixar o template desejado ou criar um novo projeto — seja ele baseado em Command Robot, Command Timed Skeleton ou qualquer outra estrutura — é altamente recomendável alterar a classe principal do robô para utilizar o `LoggedRobot`.
Essa alteração é fundamental para permitir a utilização do Logger do AdvantageKit, possibilitando o plot de sinais, variáveis e estados internos do robô durante sua execução.
Além de auxiliar diretamente no desenvolvimento e validação dos subsistemas, essa ferramenta também se torna extremamente importante para processos de debug e análise de desempenho. O AdvantageKit permite registrar informações em uma linha do tempo sincronizada, possibilitando revisões pós-teste e pós-partida, facilitando a identificação e correção de problemas encontrados durante o funcionamento do robô.

Para utilizar esta ferramenta, precisamos ter a biblioteca `AdvantageKit` instalada.

</div>

> [!NOTE]
> **AdvantageKit**
> <div align="justify">
> A instalação do AdvatangeKit pode ser realizada na aba WPILib Vendor Dependencies presente no canto superior esquerdo do WPILib VS Code.
> </div>


<div align="justify">

Esta etapa deve ser realizada como parte das boas práticas de desenvolvimento, sendo utilizada frequentemente para o envio de sinais, posições, estados e demais informações relevantes para dashboards e ferramentas de análise, como o AdvantageScope.
A utilização do Logger facilita significativamente o monitoramento e visualização do comportamento do robô durante testes e partidas, tornando o processo de desenvolvimento mais organizado, eficiente e intuitivo.

</div>

> [!NOTE]
> **Logger**
> <div align="justify">
> O uso do Logger não é obrigatório para a validação da simulação. Para o funcionamento básico dos modelos e plots, é possível utilizar ferramentas como `SmartDashboard`, `Shuffleboard` ou qualquer outro sistema de publicação de dados.
> Entretanto, conforme mencionado anteriormente, o uso do AdvantageKit é altamente recomendado devido à praticidade, organização e aos recursos avançados que a ferramenta oferece, especialmente para debug, análise temporal de sinais e revisão pós-teste ou pós-partida.
</div>
<div align="justify">

<h2>PASSO 2: Looger e Odometria</h2>

Apartir de agora, entramos de fato em modificações no código, para comprovar o funcionamento.

<h3>Inicialização do Logger</h3>

No arquivo principal do projeto, `Robot.java`, altere a extensão da classe para `LoggedRobot`, permitindo que os logs e sinais do robô sejam corretamente enviados e registrados pelo AdvantageKit.
Além disso, também será necessário inicializar o sistema de logging dentro da classe `Robot`. Após realizar essa alteração, o próprio ambiente de desenvolvimento solicitará automaticamente os imports necessários para o funcionamento da biblioteca.

Ao final, a estrutura do código deverá ficar semelhante ao exemplo apresentado abaixo:

### Imports

```java
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
```

### Extensão da classe

```java
public class Robot extends LoggedRobot {
```

### Inicialização do Logger

```java
public Robot() {
    m_robotContainer = new RobotContainer();

    Logger.recordMetadata("ProjectName", "MeuRobo");

    if (isReal()) {
        Logger.addDataReceiver(new WPILOGWriter("/home/lvuser/logs"));
        Logger.addDataReceiver(new NT4Publisher());
    } else {
        Logger.addDataReceiver(new WPILOGWriter("logs"));
        Logger.addDataReceiver(new NT4Publisher());
    }

    Logger.start();
}
```

**Explicação**

A classe principal do robô precisa herdar (`extends`) de `LoggedRobot` para que o AdvantageKit consiga interceptar e gerenciar automaticamente o sistema de logs do projeto. É essa extensão que adiciona ao robô toda a estrutura necessária para gravação, publicação e sincronização dos sinais utilizados pelo Logger.

Além disso, a inicialização utilizando `Logger.start()` é responsável por iniciar efetivamente o sistema de logging durante a execução do robô. Sem essa inicialização, mesmo que existam chamadas como `Logger.recordOutput()`, nenhuma informação será realmente enviada, gravada ou disponibilizada para ferramentas como o AdvantageScope.

### Envio da odometria

Por padrão, a biblioteca CTRE já disponibiliza um método responsável por retornar a pose do robô dentro do arquivo `CommandSwerveDrivetrain.java`. Além disso, esse subsistema também possui um método `periodic()`, que é executado continuamente durante o funcionamento do robô.

Dessa forma, podemos aproveitar essa estrutura para realizar o envio (plot) das informações de odometria para o AdvantageScope.

```java
  @Override
  public void periodic() {

    Pose2d currentPose = this.getState().Pose;
    Logger.recordOutput("ODOMETRY", currentPose);
    
  }
```

Note que, diferentemente do `SmartDashboard.put...`, o `Logger.recordOutput()` não precisa que informemos explicitamente o tipo de dado que será enviado. Isso acontece porque o Logger possui suporte automático para diferentes tipos de informações, sendo capaz de interpretar e registrar valores como números, booleanos, poses, arrays, estruturas geométricas e diversos outros objetos utilizados no WPILib.

Aproveitando que já estamos trabalhando nesta classe, também podemos definir uma posição inicial fixa para o robô durante a simulação, facilitando 
testes, validações e a visualização do comportamento dos subsistemas dentro do ambiente virtual.
```java
  @Override
  public void simulationPeriodic()
  {
     if(ctrInit){
       if(!isRedAlliance()) this.resetPose(new Pose2d(2, 4, new Rotation2d(Math.PI)));
       else this.resetPose(new Pose2d(13.5, 4, new Rotation2d(0)));
       ctrInit = false;
     }
  }
```

> A variável `ctrInit` consiste em um simples `private boolean ctrInit = true;` declarado globalmente, utilizado para garantir que determinadas instruções sejam executadas apenas no primeiro ciclo da simulação.
> Dessa forma, o preset inicial da odometria consegue seguir corretamente a lógica definida dentro do `simulationPeriodic()`, evitando conflitos entre a posição inicial configurada e as atualizações de movimento do robô durante a simulação.


<h2>PASSO 3: Movimentação</h2>

No `RobotContainer`, precisamos definir o estilo de movimentação desejado para o drivetrain, além de configurar os principais parâmetros operacionais, como velocidade máxima de translação (`MaxSpeed`) e velocidade máxima angular (`MaxAngularRate`).

```java
public double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);
```

Em seguida, definimos o modo de direção utilizado pelo robô. Neste exemplo, estamos utilizando controle `FieldCentric`, com deadband para translação e rotação, além do modo `OpenLoopVoltage` para o acionamento dos módulos:

```java
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
```

Também precisamos instanciar o drivetrain utilizando os parâmetros gerados automaticamente pelo Phoenix Tuner X no arquivo `TunerConstants` durante a configuração do sistema swerve:

```java
  private CommandSwerveDrivetrain driveBase = TunerConstants.createDrivetrain();
```

Por fim, dentro do método `configureBindings()`, definimos o comando padrão do drivetrain através do `setDefaultCommand()`. É neste ponto que associamos os eixos do controle aos movimentos do robô:
```java
  private void configureBindings() {
    driveBase.setDefaultCommand(driveBase.applyRequest(() -> { return drive
      .withVelocityX(-driver.getLeftY() * MaxSpeed * driver.getRightTriggerAxis())
      .withVelocityY(-driver.getLeftX() * MaxSpeed * driver.getRightTriggerAxis())
      .withRotationalRate(-driver.getRightX() * MaxSpeed);
    }));
...
```

</div>

> [!IMPORTANT]
> <div align="justify">
> 
> Para a rotação do robô, utilize MaxAngularRate, e não MaxSpeed, pois MaxSpeed representa velocidade linear em metros por segundo, enquanto MaxAngularRate representa velocidade angular em radianos por segundo.

</div>

> [!WARNING]
> **ESTA CONFIGURAÇÃO NÃO AFETA A SIMULAÇÃO**
> <div align="justify">
> 
> O código disponibilizado foi originalmente testado e validado utilizando o robô **APEX 2.0**, da equipe **FRC 10291**. Por esse motivo, os valores presentes no arquivo `TunerConstants.java` estão diretamente relacionados à configuração física do chassi da equipe MUTUM X.
>
> Dessa forma, embora a simulação funcione corretamente, a utilização prática desse código em um robô real provavelmente não apresentará o comportamento esperado, a menos que os IDs CAN, relações mecânicas, offsets e valores de PID sejam idênticos aos utilizados no projeto original.
> </div>

<div align="justify">

<h2>PASSO 4: Validação</h2>
Agora que já configuramos a base do código, vamos iniciar a simulação.

<h3>4.1 - Inicialização</h3>

Clique no ícone da WPILib destacado em vermelho na imagem abaixo. Em seguida, pesquise por `simulate`, selecione a opção indicada pela seta verde — correspondente à mesma descrição apresentada — e, por fim, clique em `OK` para iniciar a simulação do projeto.

<table align="center">

<tr>

<td align="center" width="1000">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_1.png" width="1000">
</td>

</tr>

</table>

Após essa etapa, o projeto será compilado automaticamente (`build`). Caso não haja erros durante a compilação, uma nova janela será exibida solicitando a utilização da Driver Station em conjunto com a simulação.

Habilite essa opção e, em seguida, clique em `OK` para continuar a execução da simulação do robô.

<table align="center">

<tr>

<td align="center" width="500">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_1_1.png" width="500">
</td>

</tr>

</table>

Após alguns segundos de inicialização, uma janela semelhante à apresentada na imagem abaixo deverá ser aberta automaticamente:

<table align="center">

<tr>

<td align="center" width="1000">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_2.png" width="1000">
</td>

</tr>

</table>

Durante a execução da simulação, o VS Code permanecerá exibindo uma janela de controle contendo as opções para pausar, continuar ou encerrar a simulação do robô.

<table align="center">

<tr>

<td align="center" width="300">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_3.png" width="300">
</td>

</tr>

</table>

</div>

> [!WARNING]
> **SITUAÇÕES EM QUE A SIMULAÇÃO NÃO INICIA**
> <div align="justify">
> Durante o desenvolvimento do código, podem ocorrer situações em que a simulação deixe de iniciar corretamente. Na maioria dos casos, isso acontece porque o sistema detecta comportamentos considerados inseguros ou inconsistentes dentro da aplicação.
>
> Alguns exemplos comuns incluem:
>
> - Variáveis declaradas sem inicialização adequada
> - Objetos ou subsistemas não instanciados corretamente
> - Conflitos de controle simultâneo sobre um mesmo componente
> - Erros de lógica envolvendo comandos, threads ou schedulers
> - Referências nulas (`NullPointerException`)
> - Loops ou chamadas recursivas indevidas
>
> Em geral, se determinado problema impede o funcionamento da simulação, existe uma grande chance de que o mesmo comportamento também cause falhas no robô real. Por esse motivo, recomenda-se sempre corrigir completamente os erros apresentados antes de prosseguir com novos testes ou implementações.
> </div>
<div align="justify">

<h3>4.2 - AdvantageScope</h3>

Com a simulação já em execução, agora podemos validar se a leitura da odometria está funcionando corretamente.

Para isso, abra o `AdvantageScope`, seja diretamente pelo computador ou através da opção `Start Tool`, disponível no ícone da WPILib dentro do VS Code.

Após iniciar o software, conecte-o à simulação utilizando a opção `File → Connect to Simulator → Default` ou, se preferir, através do atalho `Ctrl + Shift + K`.

<table align="center">

<tr>

<td align="center" width="750">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_4.png" width="750">
</td>

</tr>

</table>

A partir desse momento, sua simulação estará funcionando integrada à Driver Station. Isso significa que o robô passará a respeitar os estados de habilitação (`Enable/Disable`) exatamente como ocorre em um robô real.

Dessa forma, será possível executar normalmente o modo teleoperado através da Driver Station, enquanto os controles responsáveis pela movimentação e acionamento dos subsistemas também serão gerenciados por ela, reproduzindo com maior fidelidade o comportamento real do robô durante partidas e testes.

<table align="center">

<tr>

<td align="center" width="750">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_5.png" width="750">
</td>

</tr>

</table>

Note que as variáveis de saída definidas no código já devem estar sendo exibidas no AdvantageScope, conforme destacado pela seleção em azul na imagem.

Além disso, a linha do tempo da simulação também deverá começar a avançar continuamente, como indicado pela seleção em verde, demonstrando que os sinais estão sendo atualizados corretamente em tempo real.

Por fim, a Driver Station deverá indicar que o robô está conectado e comunicando normalmente com a simulação, conforme destacado pela seleção em vermelho.

<h3>4.3 - Comprovação do funcionamento</h3>

Localize o plot criado anteriormente na classe `CommandSwerveDrivetrain`, identificado como `ODOMETRY`. Em seguida, arraste esse sinal para o campo `Poses`, presente na visualização `3D Field` ou `2D Field` do AdvantageScope.

Ao realizar essa associação, o robô passará a ser exibido e atualizado dinamicamente no ambiente de simulação conforme as informações de odometria enviadas pelo código.

<table align="center">

<tr>

<td align="center" width="800">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_6.png" width="800">
</td>

</tr>

</table>

A partir desse momento, uma projeção estimada do robô deverá ser exibida na arena virtual do AdvantageScope.

Com o modo teleoperado habilitado na Driver Station, o robô passará a responder aos comandos do controle, movimentando-se dinamicamente dentro do campo conforme as informações de odometria e os comandos enviados durante a simulação.

<table align="center">

<tr>

<td align="center" width="470">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_7_.png" width="470">
</td>

<td align="center" width="470">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_8_.png" width="470">
</td>

</tr>

<tr>

<td align="center" width="470">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/POSE3D_.gif" width="470">
</td>

<td align="center" width="470">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/POSE2D.gif" width="470">
</td>

</tr>

</table>

</div>

> [!NOTE]
> **VALIDAÇÃO E CORREÇÃO DOS CONTROLES**
> <div align="justify">
> 
> Com a simulação funcionando corretamente, o robô deverá respeitar adequadamente as orientações da arena e o comportamento esperado para cada aliança (`Blue` e `Red`).
>
> Dessa forma, ao operar o robô, é importante validar se os movimentos executados correspondem corretamente aos comandos enviados. Por exemplo, caso o robô gire no sentido oposto ao esperado ou apresente movimentações invertidas em relação ao campo, isso indica que determinados eixos, orientações ou referências de rotação ainda precisam ser ajustados na programação.
>
> A simulação é justamente uma excelente ferramenta para identificar e corrigir esse tipo de inconsistência antes da utilização prática no robô real.
> </div>

<div align="justify">

Chegou até aqui e está tudo funcionando corretamente? Então agora vamos avançar para a próxima etapa: a simulação completa dos subsistemas e componentes do robô.

Para isso, acesse a pasta [`SimLib`](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/SimLib), onde estão disponíveis as estruturas, exemplos e utilitários responsáveis pela simulação dos mecanismos utilizados no projeto.

</div>


