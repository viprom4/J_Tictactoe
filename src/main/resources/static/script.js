const boardElement = document.getElementById('board');
const statusElement = document.getElementById('status');
const restartBtn = document.getElementById('restart');

// Панели
const authPanel = document.getElementById('auth-panel');
const menuPanel = document.getElementById('menu-panel');
const availableGamesPanel = document.getElementById('available-games-panel');
const gamePanel = document.getElementById('game-panel');
const historyPanel = document.getElementById('history-panel');
const leaderboardPanel = document.getElementById('leaderboard-panel');

// Блок с именами
const p1NameElement = document.getElementById('p1-name');
const p2NameElement = document.getElementById('p2-name');

// Элементы меню
const btnPlayBot = document.getElementById('btn-play-bot');
const btnPlayMultiplayer = document.getElementById('btn-play-multiplayer');
const btnJoinGame = document.getElementById('btn-join-game');
const btnHistory = document.getElementById('btn-history');
const btnLeaderboard = document.getElementById('btn-leaderboard');
const btnBackToMenu = document.getElementById('btn-back-to-menu');
const btnHistoryBack = document.getElementById('btn-history-back');
const btnLeaderboardBack = document.getElementById('btn-leaderboard-back');
const gamesListElement = document.getElementById('games-list');
const historyListElement = document.getElementById('history-list');
const leaderboardListElement = document.getElementById('leaderboard-list');
const logoutBtn = document.getElementById('logout-btn');

// Элементы авторизации
const loginBtn = document.getElementById('login-btn');
const loginInput = document.getElementById('login-input');
const passwordInput = document.getElementById('password-input');
const tabLogin = document.getElementById('tab-login');
const tabRegister = document.getElementById('tab-register');
const formLogin = document.getElementById('form-login');
const formRegister = document.getElementById('form-register');
const registerBtn = document.getElementById('register-btn');
const regLoginInput = document.getElementById('reg-login-input');
const regPasswordInput = document.getElementById('reg-password-input');
const regMessage = document.getElementById('reg-message');

let gameId = null;
let isWaiting = false;
let accessToken = null;
let myUserId = null;
let myLogin = null;
let pollInterval = null;
let isBotGame = false;
let isWaitingForPlayers = false; // Флаг: ждем ли мы второго игрока

// --- АВТОРИЗАЦИЯ ---

tabLogin.addEventListener('click', () => {
    tabLogin.style.borderBottom = '2px solid var(--x-color)';
    tabLogin.style.color = 'var(--text-color)';
    tabRegister.style.borderBottom = 'none';
    tabRegister.style.color = '#888';
    formLogin.style.display = 'flex';
    formRegister.style.display = 'none';
});

tabRegister.addEventListener('click', () => {
    tabRegister.style.borderBottom = '2px solid var(--x-color)';
    tabRegister.style.color = 'var(--text-color)';
    tabLogin.style.borderBottom = 'none';
    tabLogin.style.color = '#888';
    formLogin.style.display = 'none';
    formRegister.style.display = 'flex';
});

loginBtn.addEventListener('click', async () => {
    const login = loginInput.value;
    const password = passwordInput.value;
    if (!login || !password) return alert("Введите логин и пароль");

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login: login, password: password })
        });

        if (response.ok) {
            const data = await response.json();
            accessToken = data.accessToken;
            myLogin = login;
            
            const gameResponse = await fetch('/game/bot', { 
                method: 'POST', 
                headers: { 'Authorization': 'Bearer ' + accessToken } 
            });
            
            if (gameResponse.ok) {
                const game = await gameResponse.json();
                myUserId = String(game.player1);
                
                sessionStorage.setItem('accessToken', accessToken);
                sessionStorage.setItem('myUserId', myUserId);
                sessionStorage.setItem('myLogin', myLogin);
                showMenuPanel();
            }
        } else {
            alert("Неверный логин или пароль");
        }
    } catch (e) {
        alert("Ошибка соединения");
    }
});

registerBtn.addEventListener('click', async () => {
    const login = regLoginInput.value;
    const password = regPasswordInput.value;
    if (!login || !password) return;

    try {
        const response = await fetch('/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, password })
        });

        if (response.ok) {
            regMessage.textContent = "Успешно! Входим...";
            regMessage.style.color = "#4da6ff";
            
            const loginResponse = await fetch('/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, password })
            });
            
            if (loginResponse.ok) {
                const data = await loginResponse.json();
                accessToken = data.accessToken;
                myLogin = login;
                
                const gameResponse = await fetch('/game/bot', { 
                    method: 'POST', 
                    headers: { 'Authorization': 'Bearer ' + accessToken } 
                });
                
                if (gameResponse.ok) {
                    const game = await gameResponse.json();
                    myUserId = String(game.player1);
                    sessionStorage.setItem('accessToken', accessToken);
                    sessionStorage.setItem('myUserId', myUserId);
                    sessionStorage.setItem('myLogin', myLogin);
                    showMenuPanel();
                }
            }
        } else {
            const err = await response.json();
            regMessage.textContent = err.message || "Ошибка: логин занят";
            regMessage.style.color = "#ff6b6b";
        }
    } catch (e) {
        regMessage.textContent = "Ошибка сети";
    }
});

logoutBtn.addEventListener('click', () => {
    sessionStorage.clear();
    accessToken = null;
    myUserId = null;
    myLogin = null;
    gameId = null;
    isBotGame = false;
    isWaitingForPlayers = false;
    clearInterval(pollInterval);
    showAuthPanel();
});

// --- ГЛАВНОЕ МЕНЮ ---

btnPlayBot.addEventListener('click', async () => {
    if (!accessToken) return showAuthPanel();
    const response = await fetch('/game/bot', { method: 'POST', headers: { 'Authorization': 'Bearer ' + accessToken } });
    if (!response.ok) return alert("Ошибка: " + response.status);
    const game = await response.json();
    isBotGame = true; 
    startGameSession(game);
});

btnPlayMultiplayer.addEventListener('click', async () => {
    if (!accessToken) return showAuthPanel();
    const response = await fetch('/game/multiplayer', { method: 'POST', headers: { 'Authorization': 'Bearer ' + accessToken } });
    if (!response.ok) return alert("Ошибка: " + response.status);
    const game = await response.json();
    isBotGame = false; 
    startGameSession(game);
});

btnJoinGame.addEventListener('click', async () => {
    menuPanel.style.display = 'none';
    availableGamesPanel.style.display = 'flex';
    await loadAvailableGames();
});

btnHistory.addEventListener('click', async () => {
    menuPanel.style.display = 'none';
    historyPanel.style.display = 'flex';
    await loadHistory();
});

btnLeaderboard.addEventListener('click', async () => {
    menuPanel.style.display = 'none';
    leaderboardPanel.style.display = 'flex';
    await loadLeaderboard();
});

btnBackToMenu.addEventListener('click', () => {
    availableGamesPanel.style.display = 'none';
    showMenuPanel();
});

btnHistoryBack.addEventListener('click', () => {
    historyPanel.style.display = 'none';
    showMenuPanel();
});

btnLeaderboardBack.addEventListener('click', () => {
    leaderboardPanel.style.display = 'none';
    showMenuPanel();
});

// Обработчик кнопки "В главное меню" (с отменой игры)
restartBtn.addEventListener('click', async () => {
    // Если мы были в режиме ожидания соперника и это не игра с ботом — удаляем игру с сервера
    if (isWaitingForPlayers && !isBotGame) {
        try {
            await fetch(`/game/${gameId}`, { 
                method: 'DELETE', 
                headers: { 'Authorization': 'Bearer ' + accessToken } 
            });
        } catch (e) { 
            console.error("Не удалось удалить игру", e); 
        }
    }
    
    clearInterval(pollInterval);
    isBotGame = false;
    isWaitingForPlayers = false;
    gamePanel.style.display = 'none';
    showMenuPanel();
});

// --- СПИСКИ ---

async function loadAvailableGames() {
    gamesListElement.innerHTML = 'Загрузка...';
    try {
        const response = await fetch('/game/available', { headers: { 'Authorization': 'Bearer ' + accessToken } });
        if (!response.ok) throw new Error();
        
        const games = await response.json();
        gamesListElement.innerHTML = '';
        
        if (games.length === 0) {
            gamesListElement.innerHTML = '<div class="list-item" style="color: var(--text-muted); justify-content: center;">Нет доступных игр</div>';
            return;
        }

        games.forEach(game => {
            const div = document.createElement('div');
            div.className = 'list-item';
            
            const creatorName = game.player1Login ? game.player1Login : "Неизвестный";
            div.innerHTML = `<span>Игра от <strong>${creatorName}</strong></span>`;
            div.onclick = () => joinGame(game.id);
            gamesListElement.appendChild(div);
        });
    } catch (e) {
        gamesListElement.innerHTML = 'Ошибка загрузки';
    }
}

async function loadHistory() {
    historyListElement.innerHTML = 'Загрузка...';
    try {
        const response = await fetch('/game/history', { headers: { 'Authorization': 'Bearer ' + accessToken } });
        if (!response.ok) throw new Error();
        
        const games = await response.json();
        historyListElement.innerHTML = '';
        
        if (games.length === 0) {
            historyListElement.innerHTML = '<div class="list-item" style="color: var(--text-muted); justify-content: center;">История пуста</div>';
            return;
        }

        games.forEach(game => {
            const div = document.createElement('div');
            div.className = 'list-item';
            div.style.flexDirection = 'column';
            div.style.alignItems = 'flex-start';
            div.style.gap = '5px';
            
            let resultText = "";
            if (game.status === 'DRAW') {
                resultText = "Ничья 🤝";
            } else if (game.status === 'X_WINS') {
                const winner = game.player1Login || "Игрок 1";
                resultText = `Победил ${winner}`;
            } else if (game.status === 'O_WINS') {
                if (!game.player2) {
                    resultText = "Победил компьютер 🤖";
                } else {
                    const winner = game.player2Login || "Игрок 2";
                    resultText = `Победил ${winner}`;
                }
            }

            const opponent = (String(game.player1Login) === String(myLogin)) 
                             ? (game.player2Login || 'Компьютер') 
                             : game.player1Login;

            div.innerHTML = `
                <div>Против: <strong>${opponent}</strong></div>
                <div style="font-size: 0.8rem; color: var(--text-muted);">${resultText}</div>
            `;
            historyListElement.appendChild(div);
        });
    } catch (e) {
        historyListElement.innerHTML = 'Ошибка загрузки истории';
    }
}

async function loadLeaderboard() {
    leaderboardListElement.innerHTML = 'Загрузка...';
    try {
        const response = await fetch('/game/leaderboard?limit=10', { headers: { 'Authorization': 'Bearer ' + accessToken } });
        if (!response.ok) throw new Error();
        
        const players = await response.json();
        leaderboardListElement.innerHTML = '';
        
        if (players.length === 0) {
            leaderboardListElement.innerHTML = '<div class="list-item" style="color: var(--text-muted); justify-content: center;">Нет данных</div>';
            return;
        }

        players.forEach((player, index) => {
            const div = document.createElement('div');
            div.className = 'list-item';
            
            const winRatePercent = (player.winRate * 100).toFixed(1);

            div.innerHTML = `
                <div style="display: flex; gap: 15px; align-items: center;">
                    <span class="rank">${index + 1}</span>
                    <strong>${player.login}</strong>
                </div>
                <span class="score">${winRatePercent}%</span>
            `;
            leaderboardListElement.appendChild(div);
        });
    } catch (e) {
        leaderboardListElement.innerHTML = 'Ошибка загрузки таблицы';
    }
}

async function joinGame(id) {
    try {
        const response = await fetch(`/game/${id}/join`, { method: 'POST', headers: { 'Authorization': 'Bearer ' + accessToken } });
        if (!response.ok) throw new Error("Не удалось присоединиться");
        
        const game = await response.json();
        isBotGame = false;
        startGameSession(game);
    } catch (e) {
        alert("Ошибка: возможно, к игре уже присоединились");
        await loadAvailableGames();
    }
}

// --- ИГРОВОЙ ПРОЦЕСС ---

function startGameSession(game) {
    clearInterval(pollInterval);
    gameId = game.id;
    isWaiting = false;
    
    // Проверяем, ждем ли мы игрока
    isWaitingForPlayers = (game.status === 'WAITING_FOR_PLAYERS');
    
    authPanel.style.display = 'none';
    menuPanel.style.display = 'none';
    availableGamesPanel.style.display = 'none';
    historyPanel.style.display = 'none';
    leaderboardPanel.style.display = 'none';
    gamePanel.style.display = 'block';

    renderBoard(game);
    
    if (!isBotGame) {
        pollInterval = setInterval(() => pollGameState(), 2000);
    }
}

async function pollGameState() {
    if (isWaiting) return; 
    try {
        const response = await fetch(`/game/${gameId}`, { headers: { 'Authorization': 'Bearer ' + accessToken } });
        if (!response.ok) return;
        const game = await response.json();
        
        // Если игра началась (кто-то присоединился), снимаем флаг ожидания
        if (game.status !== 'WAITING_FOR_PLAYERS') {
            isWaitingForPlayers = false;
        }
        
        renderBoard(game);
    } catch (e) { console.error("Ошибка поллинга"); }
}

function renderBoard(game) {
    const field = game.field;
    boardElement.innerHTML = '';
    
    let mySymbol;
    if (isBotGame) {
        mySymbol = 1; 
    } else {
        mySymbol = (String(game.player1Login) === String(myLogin)) ? 1 : 2;
    }

    if (isBotGame) {
        p1NameElement.textContent = game.player1Login || myLogin;
        p2NameElement.textContent = "Компьютер";
    } else {
        if (mySymbol === 1) {
            p1NameElement.textContent = game.player1Login || myLogin;
            p2NameElement.textContent = game.player2Login || "Ожидание...";
        } else {
            p1NameElement.textContent = game.player2Login || myLogin;
            p2NameElement.textContent = game.player1Login || "Противник";
        }
    }

    for (let r = 0; r < 3; r++) {
        for (let c = 0; c < 3; c++) {
            const cell = document.createElement('div');
            cell.className = 'cell';
            
            const val = field[r][c];
            if (val === 1) {
                cell.textContent = 'X';
                cell.classList.add('x', 'disabled');
            } else if (val === 2) {
                cell.textContent = 'O';
                cell.classList.add('o', 'disabled');
            } else {
                const isMyTurn = (game.status === 'X_TURN' && mySymbol === 1) || (game.status === 'O_TURN' && mySymbol === 2);
                if (isMyTurn) {
                    cell.addEventListener('click', () => makeMove(r, c, field, mySymbol));
                } else {
                    cell.classList.add('disabled');
                }
            }
            boardElement.appendChild(cell);
        }
    }
    updateStatus(game.status, mySymbol, isBotGame, game);
}

async function makeMove(row, col, currentField, mySymbol) {
    if (isWaiting) return;
    isWaiting = true;
    statusElement.textContent = "Отправка хода...";

    const newField = currentField.map(arr => [...arr]);
    newField[row][col] = mySymbol;

    try {
        const response = await fetch(`/game/${gameId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + accessToken },
            body: JSON.stringify({ field: newField })
        });

        if (!response.ok) {
            const err = await response.json();
            statusElement.textContent = `Ошибка: ${err.message}`;
            isWaiting = false;
            return;
        }

        const updatedGame = await response.json();
        renderBoard(updatedGame);
        isWaiting = false;
        
    } catch (e) {
        statusElement.textContent = "Ошибка сети";
        isWaiting = false;
    }
}

function updateStatus(status, mySymbol, isBot, game) {
    statusElement.style.color = 'var(--text-color)';
    switch(status) {
        case 'WAITING_FOR_PLAYERS':
            statusElement.textContent = "Ожидание второго игрока...";
            break;
        case 'X_TURN':
            statusElement.textContent = (mySymbol === 1) ? "Ваш ход (X)" : "Ход противника (X)";
            break;
        case 'O_TURN':
            if (isBot) {
                statusElement.textContent = "Ход компьютера...";
            } else {
                statusElement.textContent = (mySymbol === 2) ? "Ваш ход (O)" : "Ход противника (O)";
            }
            break;
        case 'X_WINS':
            if (mySymbol === 1) {
                statusElement.textContent = "Вы победили! 🎉";
            } else {
                const winnerLogin = game.player1Login || "Игрок 1";
                statusElement.textContent = `Победил ${winnerLogin}`;
            }
            statusElement.style.color = 'var(--x-color)';
            break;
        case 'O_WINS':
            if (mySymbol === 2) {
                statusElement.textContent = "Вы победили! 🎉";
            } else {
                if (isBot) {
                    statusElement.textContent = "Победил компьютер 🤖";
                } else {
                    const winnerLogin = game.player2Login || "Игрок 2";
                    statusElement.textContent = `Победил ${winnerLogin}`;
                }
            }
            statusElement.style.color = 'var(--o-color)';
            break;
        case 'DRAW':
            statusElement.textContent = "Ничья! 🤝";
            statusElement.style.color = '#aaa';
            break;
    }
}

// --- ИНИЦИАЛИЗАЦИЯ ---
function init() {
    const savedToken = sessionStorage.getItem('accessToken');
    const savedUserId = sessionStorage.getItem('myUserId');
    const savedLogin = sessionStorage.getItem('myLogin');
    
    if (savedToken && savedUserId) {
        accessToken = savedToken;
        myUserId = savedUserId;
        myLogin = savedLogin;
        showMenuPanel();
    } else {
        showAuthPanel();
    }
}

function showAuthPanel() {
    authPanel.style.display = 'flex';
    menuPanel.style.display = 'none';
    gamePanel.style.display = 'none';
    availableGamesPanel.style.display = 'none';
    historyPanel.style.display = 'none';
    leaderboardPanel.style.display = 'none';
    tabLogin.click();
}

function showMenuPanel() {
    authPanel.style.display = 'none';
    menuPanel.style.display = 'flex';
    gamePanel.style.display = 'none';
    availableGamesPanel.style.display = 'none';
    historyPanel.style.display = 'none';
    leaderboardPanel.style.display = 'none';
}

init();