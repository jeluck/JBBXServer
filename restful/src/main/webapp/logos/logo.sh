for file in ./*
do
    if test -d $file
    then
        echo $file 是目录
	cp $file/logo.png ids/${file}logo.png
    fi
done
