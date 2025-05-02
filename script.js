// Simulação de cronômetro
let timer = document.querySelector('.timer');
let interval;

function startTimer() {
    let count = 0;
    interval = setInterval(() => {
        const hours = Math.floor(count / 3600);
        const minutes = Math.floor((count % 3600) / 60);
        const seconds = count % 60;
        timer.textContent = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        count++;
    }, 1000);
}

document.querySelector('button').addEventListener('click', () => {
    if (!interval) {
        startTimer();
        document.querySelector('button').textContent = 'Detener';
    } else {
        clearInterval(interval);
        interval = null;
        document.querySelector('button').textContent = 'Iniciar Jornada';
    }
});
