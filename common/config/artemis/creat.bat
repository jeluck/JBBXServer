%ARTEMIS_HOME%\bin\artemis create --allow-anonymous --no-amqp-acceptor --no-hornetq-acceptor --no-mqtt-acceptor --no-stomp-acceptor --no-web --role jbb --password jbb --verbose --user jbb .

$ARTEMIS_HOME/bin/artemis create --allow-anonymous --no-amqp-acceptor --no-hornetq-acceptor --no-mqtt-acceptor --no-stomp-acceptor --no-web --role jbb --password jbb --verbose --user jbb .

rem Used options
rem --allow-anonymous - Enables anonymous configuration on security
rem --disable-persistence - Disable message persistence to the journal
rem --user <user> - The username
rem --role <role> - The name for the role created
rem --password <password> - The user's password
rem --no-amqp-acceptor --no-hornetq-acceptor --no-mqtt-acceptor --no-stomp-acceptor - Disable specific acceptor
rem --no-web - This will remove the web server definition from bootstrap.xml

rem Other options
rem --data <data> - Directory where ActiveMQ Data is used. Path are relative to artemis.instance/bin
rem --default-port <defaultPort> - The port number to use for the main 'artemis' acceptor (Default:61616)
rem --encoding <encoding> - The encoding that text files should use
rem --force - Overwrite configuration at destination directory
rem --home <home> - Directory where ActiveMQ Artemis is installed
rem --host <host> - The host name of the broker (Default: 0.0.0.0 or input if clustered)
rem --http-port <httpPort> - The port number to use for embedded web server (Default: 8161)
rem --java-options <javaOptions> - Extra java options to be passed to the profile
rem --name <name> - The name of the broker (Default: same as host)
rem --ping <ping>
rem --port-offset <portOffset> - Off sets the default port
rem --queues <queues> - comma separated list of jms queues
rem --topics <topics> - comma separated list of jms topics
