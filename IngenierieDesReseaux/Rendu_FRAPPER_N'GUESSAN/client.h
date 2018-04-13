#ifndef CLIENT_H
#define CLIENT_H

#ifdef WIN32

#include <winsock2.h>

#elif defined (linux)

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h> /* close */
#include <netdb.h> 
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <ctype.h>
#define INVALID_SOCKET -1
#define SOCKET_ERROR -1
#define closesocket(s) close(s)

// type de message pour notre structure
#define PUT    'P'
#define QUIT   'Q'
#define GAGNE  'W'
#define PERDU  'L'
#define ERREUR 'E'


#define BUF_SIZE 1024
#define LONGUEUR_DATA 26 // longueur du champ DATA (longueur maximale d'un mot du dictionnaire) 
#define NB_COUPS 10 // Nombre d'éssai maximum
#define PORT	 25


typedef struct sockaddr_in SOCKADDR_IN;
typedef struct sockaddr SOCKADDR;
typedef struct hostent HOSTENT ;

// Structure message client utilisé dans le jeu du pendu
typedef struct
{
   char commande; // La commande du client
   char lettre; // La lettre proposée du client
} MESSAGE_CLIENT;

// Structure message serveur utilisée dans le jeu du pendu
typedef struct
{
   char commande; // La commande du serveur
   char nbCoups; // Le nombre de coups proposés
   char data[LONGUEUR_DATA]; // Le mot 
	int jouer ; // boolean(entier) jouer utiliser pour indiquer à quel tour le client doit jouer
} MESSAGE_SERVEUR;


#else

#error not defined for this platform

#endif

void init();
void end();
void application(char *address, char *name);
int init_connection(char *address);
void end_connection(int sock);
int read_server(int sock, char *buffer);
void write_server(int sock, char *buffer);
char envoyerMessage(int socket, MESSAGE_CLIENT message);
char recevoirMessage(int socket, MESSAGE_SERVEUR *message);
char traiterMessage(MESSAGE_SERVEUR message);
void jouer_partie(int sock);
void jouer_all(int sock);
void print_pendu(char nombreCoups);



#endif 