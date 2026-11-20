// Servidor do vanila-badge: guarda quem está com o Vanila Client aberto e responde
// quais jogadores de uma lista também estão. Tudo fica na memória de um único Durable Object;
// se ele reiniciar, a lista se refaz no minuto seguinte com os avisos dos clients.

const ONLINE_MS = 3 * 60 * 1000; // o client avisa a cada minuto
const MAX_PLAYERS = 200;
const UUID_RE = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/;

export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    if (url.pathname !== "/sync" || request.method !== "POST") {
      return new Response("Vanila Client badge server", { status: url.pathname === "/" ? 200 : 404 });
    }
    const stub = env.ONLINE.get(env.ONLINE.idFromName("global"));
    return stub.fetch(request);
  },
};

export class Online {
  constructor() {
    this.seen = new Map(); // uuid -> último aviso (ms)
  }

  async fetch(request) {
    let body;
    try {
      body = await request.json();
    } catch {
      return new Response("bad json", { status: 400 });
    }
    const now = Date.now();
    const self = typeof body.uuid === "string" ? body.uuid.toLowerCase() : "";
    if (!UUID_RE.test(self)) return new Response("bad uuid", { status: 400 });
    this.seen.set(self, now);

    const players = Array.isArray(body.players) ? body.players.slice(0, MAX_PLAYERS) : [];
    const users = [];
    for (const p of players) {
      if (typeof p !== "string") continue;
      const id = p.toLowerCase();
      const last = this.seen.get(id);
      if (last !== undefined && now - last < ONLINE_MS) users.push(id);
    }

    // Limpeza de quem saiu, de vez em quando.
    if (Math.random() < 0.01) {
      for (const [id, last] of this.seen) if (now - last >= ONLINE_MS) this.seen.delete(id);
    }

    return Response.json({ users });
  }
}
