call set_env.bat
call lint_kube_objects.bat
call lint_source_code.bat
call compile.bat
docker build . Dockerfile