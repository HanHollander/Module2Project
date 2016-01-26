cd ..
cd bin
start java server.controller.Server 7778 2 999999
timeout /t 1 /nobreak
start java client.controller.Qwirkle one localhost 7778 b s
timeout /t 1 /nobreak
start java client.controller.Qwirkle two localhost 7778 b s
