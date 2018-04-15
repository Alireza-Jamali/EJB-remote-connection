# EJB-remote-connection
A simple example of EJB remote connection to network machines

MessageInterface is an interface (hence the name!) that must be added as a library by both EJBConnectionTest and TestClient.
EJBConnectionTest will be deployed to remote machine.
server ip address must be added to TestClient field variable of the same name.
EJBConnectionTest must have the implementations of any method declared in MessageInterface.

