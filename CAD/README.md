<!-- CAD -->

> [!IMPORTANT]
> **Os procedimentos listados a seguir devem ser realizados antes de exportar os arquivos `models.glb`**

<div align="justify">

<h2>model.glb</h2>

Este arquivo representa a estrutura base do robô, ou seja, o chassi principal do projeto juntamente com todos os mecanismos estruturais fixos.

Componentes que realizam qualquer tipo de movimento ou articulação — como intake, braços, elevadores, shooters, rodas de coleta e mecanismos similares — devem ser exportados separadamente, permitindo que sejam manipulados individualmente pela simulação.

O primeiro ponto, e talvez o mais importante para facilitar tanto a configuração quanto o entendimento do arquivo `config.json`, é garantir que o robô completo esteja centralizado corretamente em relação aos eixos `X`, `Y` e `Z` do ambiente CAD.

Além disso, também é altamente recomendado que todos os mecanismos móveis sejam exportados em suas posições iniciais de repouso, como por exemplo:
- intake recolhido
- braços articulados em posição inicial
- shooter parado
- elevadores retraídos
- mecanismos auxiliares zerados

Essa padronização simplifica significativamente o processo de configuração dos offsets, ângulos e posições dentro da simulação 3D.

<table align="center">

<tr>

<td align="center" width="400">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/RobotPerspectiva.gif" width="400">
</td>

<td align="center" width="360">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/ROBOT1.png" width="360">
</td>

</tr>

</table>

Após centralizar e restringir corretamente o robô no ambiente CAD, o próximo passo é exportar apenas a parte estrutural do projeto.

Para isso, remova temporariamente todos os componentes que serão manipulados individualmente pela simulação, como mecanismos de subsistemas, articulações e partes móveis, deixando apenas a estrutura fixa do robô.

Entre os componentes que normalmente devem ser removidos antes da exportação estão:
- intake
- braços articulados
- elevadores
- shooters
- rodas de coleta
- mecanismos rotacionais
- sistemas telescópicos

O objetivo é garantir que o arquivo `model.glb` contenha exclusivamente o chassi e os elementos estruturais fixos do robô, enquanto os mecanismos móveis serão exportados separadamente para posterior manipulação dentro do AdvantageScope.

<table align="center">

<tr>

<td align="center" width="350">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/ModelChassi.gif" width="350">
</td>

<td align="center" width="370">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/CHASSI.png" width="370">
</td>

</tr>

</table>

Feito isso, agora já podemos exportar o primeiro modelo em formato `.glb`.

O processo de exportação pode variar dependendo do software CAD utilizado. No caso do Autodesk Inventor, por exemplo, o procedimento é o seguinte:

`Arquivo → Salvar Como → Salvar Cópia Como`

Em seguida:
- selecione a pasta onde deseja salvar o modelo
- recomenda-se criar uma pasta específica para os arquivos da simulação, já que este será apenas o primeiro modelo exportado
- dependendo da complexidade do robô, poderá ser necessário exportar diversos componentes individualmente

Antes de concluir a exportação, verifique se o tipo de arquivo selecionado está definido como `.glb`. Caso não esteja, altere manualmente o formato para `.glb` antes de salvar o arquivo.

A partir deste ponto, a ordem de exportação dos arquivos `model_x` não interfere diretamente no funcionamento da simulação. Entretanto, é extremamente importante manter consistência entre os nomes dos arquivos, o `config.json` e a ordem de associação dos componentes dentro do AdvantageScope.

Ou seja:
- `model_0` deverá ser associado como o primeiro componente
- `model_1` como o segundo
- `model_2` como o terceiro
- e assim sucessivamente

A lógica utilizada neste projeto foi a seguinte:

- `model_0` → Intake (sem o rolete)
- `model_1` → Rolete do intake
- `model_2` → Elevator
- `model_3` → Hand / braço articulável (sem os roletes)
- `model_4` → Rolete de coleta 1
- `model_5` → Rolete de coleta 2

Manter essa organização facilita significativamente o entendimento do projeto, a configuração do `config.json` e a associação correta dos componentes durante a manipulação 3D no AdvantageScope.

<h2>model_0.glb</h2>
Este arquivo representa exclusivamente a parte móvel do sistema de intake.

Com o CAD completo do robô já separado anteriormente, agora remova todos os componentes que não fazem parte diretamente da estrutura móvel do intake, como por exemplo:
- chassi
- elevador
- hand / braço articulável
- mecanismos auxiliares externos ao intake

Além disso, como a proposta deste projeto também inclui a simulação individual do rolete de coleta, o mesmo deverá ser exportado separadamente como outro `model_x`.

Por esse motivo, o rolete também deve ser removido deste arquivo, deixando apenas a estrutura articulável principal do intake.

<table align="center">

<tr>

<td align="center" width="350">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/Modelintake.gif" width="350">
</td>

<td align="center" width="450">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/INTAKE.png" width="450">
</td>

</tr>

</table>

Assim como realizado no modelo anterior, exporte novamente o componente utilizando o formato `.glb`, garantindo que o arquivo seja salvo corretamente para posterior utilização no AdvantageScope.

<h2>model_1.glb</h2>
Este arquivo representa exclusivamente o rolete de coleta do sistema de intake.

Diferentemente dos modelos anteriores, mecanismos que precisam realizar rotação em torno do próprio eixo exigem um cuidado especial durante a exportação.

Para esses casos, recomenda-se abrir diretamente o CAD individual do componente que será rotacionado — neste exemplo, o rolete de coleta — e exportá-lo separadamente em formato `.glb`.

Esse procedimento garante que:
- o eixo de origem (`offset`) do modelo fique centralizado corretamente
- a rotação aconteça em torno do próprio eixo físico do componente
- e a manipulação 3D dentro do AdvantageScope ocorra de maneira natural e precisa durante a simulação

<table align="center">

<tr>

<td align="center" width="400">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/RoletePerspectiva.gif" width="400">
</td>

</tr>

</table>

<h2>model_2.glb</h2>
Este arquivo representa exclusivamente a estrutura móvel do sistema de elevador.

Assim como realizado anteriormente no sistema de intake, abra o projeto CAD completo do robô e remova todos os componentes que não estejam diretamente relacionados ao movimento do elevador.

No exemplo deste projeto, o modelo final permanece contendo apenas o tubo de elevação.

O objetivo é garantir que somente os elementos realmente móveis do elevador sejam exportados e manipulados individualmente pela simulação.

<table align="center">

<tr>

<td align="center" width="350">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/ModelElevator.gif" width="350">
</td>

<td align="center" width="130">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/ELEVATOR.png" width="130">
</td>

</tr>

</table>



</div>