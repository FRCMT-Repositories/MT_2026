<h1 align="center">FRCMT — Robot Simulation Library</h1>

<div align="justify">

Este repositório tem como objetivo disponibilizar materiais, exemplos e ferramentas voltadas ao desenvolvimento de simulações 3D aplicadas à FIRST Robotics Competition (FRC), permitindo que equipes compreendam, validem e desenvolvam seus mecanismos de maneira mais visual, organizada e acessível.

A proposta principal deste projeto é demonstrar, de forma prática e didática, como integrar modelos CAD exportados em `.glb` ao `AdvantageScope`, utilizando Java, WPILib e AdvantageKit para simular mecanismos completos do robô em tempo real.

Além da implementação prática, este repositório também busca explicar toda a lógica matemática e estrutural por trás da manipulação dos modelos 3D, apresentando detalhadamente:
- cálculos trigonométricos
- posicionamento espacial
- offsets dos componentes
- articulações
- sistemas rotacionais
- controle de poses 3D
- integração com odometria
- boas práticas de organização e desenvolvimento

---

<h2>Estrutura do Repositório</h2>

O projeto está dividido em duas pastas principais:

| Pasta | Descrição |
|---|---|
| [`CAD`](./CAD) | Contém todos os arquivos `.glb`, modelos CAD, offsets, referências geométricas, estruturas exportadas e componentes utilizados na simulação dos mecanismos |
| [`Code`](./Code) | Contém toda a lógica de programação responsável pela manipulação dos modelos 3D, integração com o AdvantageScope e implementação dos sistemas simulados |

---

<h2>Pasta CAD</h2>

Na pasta `CAD` estão disponíveis:
- modelos completos dos robôs
- componentes exportados individualmente
- arquivos `.glb`
- referências de offsets
- estruturas articuláveis
- mecanismos móveis
- modelos utilizados na simulação

Além disso, a documentação também apresenta:
- como exportar corretamente os modelos
- como configurar os offsets
- como posicionar os componentes
- como estruturar os arquivos para utilização no AdvantageScope

---

<h2>Pasta Code</h2>

A pasta `Code` contém toda a implementação responsável pela simulação dos mecanismos.

Os exemplos apresentados incluem:
- integração com `AdvantageKit`
- utilização de `Pose3d`
- manipulação de `Rotation3d`
- controle de componentes articuláveis
- movimentação baseada em trigonometria
- integração com odometria
- sistemas completos utilizando `YAGSL`
- sistemas completos utilizando `CTRE`

Além disso, cada exemplo busca demonstrar detalhadamente:
- de onde surgem os valores utilizados
- como os cálculos foram obtidos
- como os offsets foram definidos
- como os componentes acompanham outros mecanismos
- como transformar dados reais em movimentações simuladas

---

<h2>Objetivo do Projeto</h2>

Mais do que apenas disponibilizar código, este repositório busca servir como uma base de estudo, nivelamento e compartilhamento de conhecimento entre equipes.

Toda a estrutura foi desenvolvida pensando em:
- facilitar a compreensão da simulação 3D
- acelerar o desenvolvimento de novos projetos
- simplificar validações de mecanismos
- auxiliar no aprendizado de programação aplicada à FRC
- incentivar boas práticas de organização e desenvolvimento

A intenção é que qualquer equipe consiga utilizar este material como ponto de partida para construir seus próprios sistemas de simulação, independentemente da biblioteca, arquitetura ou drivetrain utilizado.

</div>