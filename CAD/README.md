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

<td align="center" width="350">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/ROBOT1.png" width="350">
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

<td align="center" width="380">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/CAD/CHASSI.png" width="380">
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

</div>