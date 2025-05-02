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

async function carregarCalendario() {
    const response = await fetch(`/api/get-calendario.php?funcionario_id=<?php echo $funcionario_id; ?>`);
    const registros = await response.json();
    
    const container = document.getElementById('calendario-dias');
    for (let i = 1; i <= 31; i++) {
        const dia = i;
        const registro = registros.find(r => r.dia === i);
        const div = document.createElement('div');
        div.className = 'day';
        
        if (registro) {
            div.classList.add('active');
            div.innerHTML = `${i}<br><small>${registro.horas}h</small>`;
        } else {
            div.innerText = i;
        }
        
        container.appendChild(div);
    }
}
