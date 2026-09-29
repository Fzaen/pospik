@echo off
title POS PIK - Git Push to GitHub
echo Adding modified files...
git add .
set /p msg="Masukkan pesan commit (kosongkan untuk default 'Update POS PIK'): "
if "%msg%"=="" set msg=Update POS PIK
echo Committing with message: "%msg%"
git commit -m "%msg%"
echo Pushing to GitHub (main)...
git push origin main
echo ========================================
echo Push to GitHub completed successfully!
echo ========================================
pause
