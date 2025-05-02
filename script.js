// Relógio em tempo real
function atualizarRelogio() {
    const agora = new Date();
    document.getElementById('relogio').innerText = `Relógio: ${agora.toLocaleTimeString()}`;
}
setInterval(atualizarRelogio, 1000);

// Simulação de registro de ponto
function registrar(tipo) {
    const funcionarioId = 1; // Exemplo: ID do usuário logado
    fetch('/api/registrar-ponto.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tipo, funcionario_id: funcionarioId })
    }).then(response => response.json())
      .then(data => alert(`Registro de ${tipo} salvo!`));
}
