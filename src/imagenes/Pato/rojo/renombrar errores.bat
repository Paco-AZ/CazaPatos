@echo off
setlocal enabledelayedexpansion

set "count=1"
for %%i in (1 2 3 4) do (
    if exist "leftU%%i - copia.png" (
        ren "leftU%%i - copia.png" "leftD!count!.png"
        set /a count+=1
    )
)
