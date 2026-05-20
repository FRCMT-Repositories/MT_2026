<h1>ENTENDENDO O CÓDIGO</h1>
<div align="justify">

⚠️ **ATENÇÃO**

> Estes passos servem apenas para nortear oque é desenvolvido para o funcionamento correto da simulação, considerando um novo template, ou seja, um código novo, caso queira utilizar o exemplo completo, os passos citados a seguir devem ser desconsiderados, visto que essas modificações já estão inclusas.

<h2>PASSO 1: AdvantageKit</h2>

Ao baixar o template desejado ou criar um novo projeto — seja ele baseado em Command Robot, Command Timed Skeleton ou qualquer outra estrutura — é altamente recomendável alterar a classe principal do robô para utilizar o `LoggedRobot`.
Essa alteração é fundamental para permitir a utilização do Logger do AdvantageKit, possibilitando o plot de sinais, variáveis e estados internos do robô durante sua execução.
Além de auxiliar diretamente no desenvolvimento e validação dos subsistemas, essa ferramenta também se torna extremamente importante para processos de debug e análise de desempenho. O AdvantageKit permite registrar informações em uma linha do tempo sincronizada, possibilitando revisões pós-teste e pós-partida, facilitando a identificação e correção de problemas encontrados durante o funcionamento do robô.

Para utilizar esta ferramenta, precisamos ter a biblioteca `AdvantageKit` instalada.

> A instalação do AdvatangeKit pode ser realizada na aba `WPILib Vendor Dependencies` presente no canto superior esquerdo do `WPILib VS Code`.

Esta etapa deve ser realizada como parte das boas práticas de desenvolvimento, sendo utilizada frequentemente para o envio de sinais, posições, estados e demais informações relevantes para dashboards e ferramentas de análise, como o AdvantageScope.
A utilização do Logger facilita significativamente o monitoramento e visualização do comportamento do robô durante testes e partidas, tornando o processo de desenvolvimento mais organizado, eficiente e intuitivo.

**Observação**

> O uso do Logger não é obrigatório para a validação da simulação. Para o funcionamento básico dos modelos e plots, é possível utilizar ferramentas como `SmartDashboard`, `Shuffleboard` ou qualquer outro sistema de publicação de dados.
> Entretanto, conforme mencionado anteriormente, o uso do AdvantageKit é altamente recomendado devido à praticidade, organização e aos recursos avançados que a ferramenta oferece, especialmente para debug, análise temporal de sinais e revisão pós-teste ou pós-partida.

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

</div>







| Arquivo | Descrição |
|---|---|
| `config.json` | Conjunto de configurações responsáveis pela definição estrutural do modelo, incluindo nome do robô, posicionamento 3D do modelo principal, composição dos subsistemas (como intake, elevador e braço), além da configuração de elementos auxiliares, como câmeras e diferentes pontos de visualização utilizados na simulação. |
| `model.glb` | Modelo CAD base do projeto, contendo todos os elementos estruturais fixos do robô. |
| `model_0.glb` | Modelo CAD correspondente ao 1º componente do mecanismo. Neste exemplo, o componente representado é a gaveta do intake. |
| `model_1.glb` | Modelo CAD correspondente ao 2º componente do mecanismo. Neste exemplo, o componente representado é o coletor do intake. |
| `model_2.glb` | Modelo CAD correspondente ao 3º componente do mecanismo. Neste exemplo, o componente representado é o elevador. |
| `model_3.glb` | Modelo CAD correspondente ao 4º componente do mecanismo. Neste exemplo, o componente representado é o braço. |
| `model_4.glb` | Modelo CAD correspondente ao 5º componente do mecanismo. Neste exemplo, o componente representado é a 1ª roda de coleta do braço. |
| `model_5.glb` | Modelo CAD correspondente ao 6º componente do mecanismo. Neste exemplo, o componente representado é a 2ª roda de coleta do braço. |

**[Aprenda a configurar o .json e os models](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/Advantage%20Scope/Robot_MTModelA)**

<h3>Adicionando ao Advantage Scope</h3>
<div align="justify">

Após a seleção do modelo, tornasse necessario copiar a pasta do modelo escolhido para o diretório autoAssets do Advantage Scope, geralmente presente no seguinte diretório `C:\Users\user\AppData\Roaming\AdvantageScope\autoAssets`.

**ATENÇÃO**

- Recomenda-se criar um atalho para esta pasta, pois ela será utilizada com frequência durante o desenvolvimento e validação da simulação dos subsistemas.

- Durante o uso do Advantage Scope, em algumas situações o software pode remover automaticamente a pasta do modelo previamente carregada, tornando necessário adicioná-la novamente. Por esse motivo, recomenda-se não mover a pasta original do modelo diretamente para o diretório do Advantage Scope. Em vez disso, crie uma cópia da pasta do modelo dentro do diretório utilizado pelo software, preservando assim os arquivos originais do projeto.

</div>

<h2>PASSO 2: Definição do módulo swerve </h2>
<div align="justify">

Nesta etapa, tornasse necessario escolher a biblioteca responsável por controlar o seu módulo swerve, atualmente temos 2 exemplos funcionais:

</div>

<div align="center">

| MÓDULO | BIBLIOTECA | TIPO DE MOTOR |
|:---:|:---:|:---:|
| [MK4i](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/YAGSLFull_MK4i)| YAGSL | NEO e KRAKEN |
| [MK5n](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/CTRFull_MK5n) | CTRe | KRAKEN |

</div>

<div align="justify">

De forma geral, a biblioteca YAGSL atende muito bem diferentes configurações de módulos swerve, oferecendo grande flexibilidade e facilidade de adaptação para diversos tipos de hardware.

Entretanto, quando o robô é composto majoritariamente por dispositivos CTRE, a biblioteca oficial da CTRE tende a apresentar uma estrutura mais limpa, organizada e otimizada, além de proporcionar melhor desempenho e integração nativa entre os componentes.

**Observação:**

A escolha da biblioteca de desenvolvimento fica inteiramente a critério de cada equipe. Caso optem por utilizar soluções diferentes das apresentadas neste repositório, sintam-se totalmente à vontade para adaptá-las conforme as necessidades e preferências do projeto.

A única exigência para a utilização correta da simulação do robô é a disponibilização do plot da odometria do robô. Em ambos os exemplos apresentados anteriormente, cada biblioteca possui, em seu escopo principal, a publicação das informações de odometria necessárias para integração com o Advantage Scope. Esses dados são fundamentais para permitir o posicionamento e movimentação correta do robô dentro do ambiente de simulação.

⚠️ **ATENÇÃO**

> É extremamente importante destacar que todas as bibliotecas disponibilizadas neste repositório foram testadas utilizando as versões referentes à temporada de 2026.
> Atualizações futuras de bibliotecas, como por exemplo a YAGSL, podem ocasionar incompatibilidades ou pequenas quebras no código original.
> Entretanto, na maioria dos casos, essas alterações podem ser corrigidas com pequenos ajustes e adaptações relacionadas às mudanças introduzidas pelas novas versões das bibliotecas.

</div>

<h2>PASSO 3: Definição do módulo swerve </h2>
<div align="justify">


</div>
