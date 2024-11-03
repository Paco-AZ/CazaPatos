@echo off
setlocal enabledelayedexpansion

set "count=1"
for %%f in (redduckright*.png) do (
    set "filename=%%~nf"
    set "extension=%%~xf"
    set "number="
    
    for /l %%i in (0,1,9) do (
        if "!filename:~-1!"=="%%i" (
            set "number=1"
        )
    )
    
    if defined number (
        ren "%%f" "right!count!!extension!"
        set /a count+=1
    )
)
