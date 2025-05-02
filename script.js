// Busca registros do usuário logado
async function carregarRegistros() {
    const response = await fetch(`/api/get-registros.php?funcionario_id=<?php echo $funcionario_id; ?>`);
    const registros = await response.json();
    
    // Exibe horários no dashboard
    document.getElementById('entrada').innerText = registros.entrada || '00:00';
    document.getElementById('saida').innerText = registros.saida || '00:00';
}

// Registra ponto no backend
async function registrar(tipo) {
    const funcionario_id = <?php echo $funcionario_id; ?>;
    await fetch('/api/registrar-ponto.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tipo, funcionario_id })
    });
    carregarRegistros();
}
