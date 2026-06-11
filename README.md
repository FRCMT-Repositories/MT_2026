<h1 align="center">FRCMT — Robot Simulation Library</h1>

<div align="justify">

<h2>Quick Start</h2>

Esta seção foi criada para permitir que qualquer equipe coloque a simulação em funcionamento no menor tempo possível. Caso deseje apenas validar o ambiente e compreender o fluxo de trabalho, siga os passos abaixo na ordem apresentada.

---

<h3>STEP 1 — Obtenção do Modelo 3D</h3>

Escolha uma das opções abaixo:

### Utilizar um modelo já configurado

- Modelo **MK4i**: [Robot_MTModelA](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/Advantage%20Scope/Robot_MTModelA)
- Modelo **MK5n**: [Robot_MTModelB](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/Advantage%20Scope/Robot_MTModelB)

### Criar um modelo próprio

Caso deseje simular um robô personalizado, siga o processo completo de exportação e configuração dos arquivos `.glb` disponível em:

- [Exportação dos Modelos CAD](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/CAD#readme)

---

<h3>STEP 2 — Download do Código Base</h3>

Escolha o projeto correspondente ao modelo desejado:

| Modelo | Biblioteca | Projeto |
|----------|----------|----------|
| MK4i | YAGSL | [YAGSLFull_MK4i](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/YAGSLFull_MK4i) |
| MK5n | CTRE Phoenix | [CTRFull_MK5n](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/CTRFull_MK5n) |

---

<h3>STEP 3 — Primeira Simulação</h3>

Com o projeto aberto e compilando corretamente, siga o guia de configuração inicial para executar sua primeira simulação:

- [Simulação utilizando YAGSL (MK4i)](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/YAGSLFull_MK4i#entendendo-o-c%C3%B3digo)
- [Simulação utilizando CTRE (MK5n)](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/CTRFull_MK5n#entendendo-o-c%C3%B3digo)

---

<h3>STEP 4 — Validação no AdvantageScope</h3>

Após iniciar a simulação, valide se a odometria e os modelos 3D estão sendo exibidos corretamente:

- [Validação do Modelo MK4i](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/YAGSLFull_MK4i#passo-4-valida%C3%A7%C3%A3o)
- [Validação do Modelo MK5n](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/CTRFull_MK5n#passo-4-valida%C3%A7%C3%A3o)

---

<h3>STEP 5 — Entendendo a Biblioteca de Simulação</h3>

Após validar o funcionamento básico da simulação, recomenda-se compreender o funcionamento interno da biblioteca responsável pela manipulação dos componentes 3D:

- [SimLib — Biblioteca de Simulação](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/Code/SimLib#readme)

Nesta etapa são apresentados:
- conceitos de `Pose3d`
- manipulação de componentes
- cálculos trigonométricos
- offsets
- sistemas articuláveis
- boas práticas para desenvolvimento de novos mecanismos

---

<h3>STEP 6 — Entendendo a Exportação dos Modelos</h3>

Por fim, caso deseje criar seus próprios mecanismos e modelos personalizados, consulte a documentação completa de exportação:

- [Exportação de Modelos .glb](https://github.com/FRCMT-Repositories/Simulation_3D/tree/main/CAD#readme)

Nesta seção são abordados:
- preparação do CAD
- definição de offsets
- posicionamento dos componentes
- exportação dos arquivos `.glb`
- configuração do `config.json`
- associação dos componentes no AdvantageScope

---

</div>

> [!TIP]
> <div align="justify">
>
> Para uma primeira experiência, recomenda-se utilizar os modelos já disponibilizados (`MK4i` ou `MK5n`) e validar toda a cadeia de simulação antes de iniciar a criação de modelos personalizados.

---

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