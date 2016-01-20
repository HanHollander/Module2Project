cd ..
cd bin
start java server.Server 7778 4 30000
timeout /t 1 /nobreak
start java client.Qwirkle one localhost 7778 b
timeout /t 1 /nobreak
start java client.Qwirkle two localhost 7778 b
timeout /t 1 /nobreak
start java client.Qwirkle one localhost 7778 b
timeout /t 1 /nobreak
start java client.Qwirkle two localhost 7778 b
