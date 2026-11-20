# Servidor do vanila-badge

Diz ao mod quais jogadores também usam o Vanila Client, para o V aparecer no nametag e no tab.
Roda de graça num Cloudflare Worker, com um Durable Object que guarda a lista na memória.

## Publicar

Precisa de uma conta na Cloudflare (o plano gratuito basta).

```sh
cd server
npx wrangler login
npx wrangler deploy
```

O deploy mostra o endereço, algo como `https://vanila-badge.<sua-conta>.workers.dev`.
Coloque esse endereço na opção `badge-server` (Mods → Vanila Client → Config, ou
`config/vanilaclient.cfg`). Para todo mundo receber o endereço já configurado, troque o valor
padrão em `Config.java` e lance uma versão nova.

## Como funciona

A cada minuto cada client manda `POST /sync` com o próprio UUID e os UUIDs do tab:

```json
{ "uuid": "…", "players": ["…", "…"] }
```

e recebe de volta os que avisaram nos últimos 3 minutos: `{ "users": ["…"] }`.

O plano gratuito dá 100 mil requisições por dia, o que cobre uns 70 jogadores conectados o dia
inteiro. O servidor confia no UUID que o client manda, então alguém poderia forjar o V de outra
pessoa. Para um selo só visual, isso não é um problema.
