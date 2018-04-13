#ifndef SERVER_H
#define SERVER_H

#ifdef WIN32 // pour windows

#include <winsock2.h>

#elif defined (linux)

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h> 
#include <netdb.h> 
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <time.h>    
#include <ctype.h>
#define INVALID_SOCKET -1
#define SOCKET_ERROR -1
#define closesocket(s) close(s) // utiliser de closesocket

#define LONGUEUR_DATA 26 // longueur du champ DATA (longueur maximale d'un mot du dictionnaire) 
#define NB_COUPS 10 // Nombre d'éssai maximum 

// type de message pour notre structure
#define PUT    'P'
#define QUIT   'Q'
#define GAGNE  'W'
#define PERDU  'L'
#define ERREUR 'E'

#define PORT 25
#define MAX_CLIENTS 100 // maximum de clients
#define BUF_SIZE	1024

typedef struct sockaddr_in SOCKADDR_IN;
typedef struct sockaddr SOCKADDR;

// Structure client avec socket associée et son nom
typedef struct
{
   int sock;
   char name[BUF_SIZE];
}Client;

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
void application();
int init_connection(char* ip);
void end_connection(int sock);
int read_client(int sock, char *buffer);
void write_client(int sock, char *buffer);
void envoie_tous(Client *clients, Client client, int actual, char *buffer, char from_server);
void remove_client(Client *clients, int to_remove, int *actual);
void clear_clients(Client *clients, int actual);
char *menu();
char *get_list(Client *clients, Client sender, int actual);
char *concat_message(char* m1, char* m2);
void jouerPendu(int socket, char* niveau);
char envoyerMessage(int socket, MESSAGE_SERVEUR message, char type, char nbCoups, char *mot);
char recevoirMessage(int socket, MESSAGE_CLIENT *message);
char traiterMessage(MESSAGE_CLIENT message, char *motOriginal, char *motMasque, char *nbCoups);
Client getClient(char* pseudo, Client *clients, int actual);
void play_against(int client_adverse, int client_actuel, char* mot_propose);
char envoyerMessageUnique(int socket, MESSAGE_SERVEUR message, char type, char nbCoups, char *mot);
void play_all(Client *clients, Client actual_client, int actual, char* niveau);
int piocherMot(char *motPioche, char* niveau);
int nombreAleatoire(int nombreMax);

#endif 