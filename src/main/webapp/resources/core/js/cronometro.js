let startTime;
let elapsedTime = 0;
let timerInterval;

export function timeToString(time) {
    const diffInHrs = time / 3600000;
    const hh = Math.floor(diffInHrs);

    const diffInMin = (diffInHrs - hh) * 60;
    const mm = Math.floor(diffInMin);

    const diffInSec = (diffInMin - mm) * 60;
    const ss = Math.floor(diffInSec);

    const formattedHH = String(hh).padStart(2, '0');
    const formattedMM = String(mm).padStart(2, '0');
    const formattedSS = String(ss).padStart(2, '0');

    return `${formattedHH}:${formattedMM}:${formattedSS}`;
}

export function print(txt) {
    document.getElementsByClassName('reloj')[0].textContent = txt;
}

export function start() {
    startTime = Date.now() - elapsedTime;
    timerInterval = setInterval(function printTime() {
        elapsedTime = Date.now() - startTime;
        print(timeToString(elapsedTime));
    }, 1000);
    
}

export function stop() {
    clearInterval(timerInterval);
    showButton('START');
}

export function reset() {
    clearInterval(timerInterval);
    print("00:00:00");
    elapsedTime = 0;
    showButton('START');
}

