# Vanila Client

Mod Forge para Minecraft 1.8.9 (Forge 11.15.1.2318+). Oito opções, todas ligadas por padrão:

| Opção | O que faz |
|---|---|
| `toggle-sprint` | Tecla **R** liga/desliga a corrida contínua. |
| `perspective` | Segure **Alt esquerdo** para girar a câmera em volta do personagem sem virar ele. |
| `all-text-background-transparent` | Tira o fundo escuro do chat, da caixa de digitar, nametags, scoreboard, tab e F3. |
| `old-animation-1.7` | Blockhit, braço balançando ao defender/comer/puxar o arco e armadura vermelha ao tomar dano. |
| `hit-delay-fix` | Remove o atraso de clique depois de errar um golpe. |
| `dynamic-fov` | FOV muda ao correr, voar e com poções (vanilla). Desligue para FOV fixo; o zoom do arco continua. |
| `text-shadow` | Sombra nos textos do scoreboard, F3 e nametags, para ler bem sem o fundo escuro. |
| `full-bright` | Brilho no máximo (cavernas e noite claras). Tecla **G** liga/desliga; o brilho do seu options.txt não é alterado. |

Correções que ficam sempre ligadas: a mira do jogador acompanha a câmera sem o atraso de um tick
da 1.8 (blocos que às vezes não eram colocados) e as teclas da hotbar ligadas a botões do mouse
(laterais) também trocam itens no inventário, como os números do teclado.

As teclas podem ser trocadas em Controles → "Vanila Client". As opções ficam em
Mods → Vanila Client → Config, ou em `config/vanilaclient.cfg`.

## OptiFine

Compatível com o OptiFine 1.8.9 HD U M5 (jar como mod na pasta `mods`). O OptiFine substitui
várias classes que o mod altera (EntityRenderer, ItemRenderer, Render, RendererLivingEntity,
LayerArmorBase, GuiIngame, GuiOverlayDebug, FontRenderer), mas todos os pontos de injeção
continuam iguais nas versões dele. O `dynamic-fov` usa o `FOVUpdateEvent` do Forge, que o
OptiFine dispara, e não interfere no zoom (tecla C).

## Compilar

Precisa de um JDK 17 para rodar o Gradle (o JDK 8 de compilação é baixado sozinho):

```sh
JAVA_HOME=~/.jdks/jdk-17.0.21.1+2 ./gradlew build
```

O mod sai em `build/libs/vanilaclient-1.0.0.jar`. Coloque na pasta `mods` de um perfil Forge 1.8.9.
