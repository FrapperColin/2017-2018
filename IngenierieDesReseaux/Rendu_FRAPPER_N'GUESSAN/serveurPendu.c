#include "serveur.h"


void init()
{
   #ifdef WIN32
      WSADATA wsa;
      int err = WSAStartup(MAKEWORD(2, 2), &wsa);
      if(err < 0)
      {
         puts("WSAStartup failed !");
         exit(EXIT_FAILURE);
      }
   #endif
}

void end()
{
   #ifdef WIN32
      WSACleanup();
   #endif
}

int init_connection(char* adresse)
{
   int sock = socket(AF_INET, SOCK_STREAM, 0);
   
   SOCKADDR_IN sin ;

   if(sock == INVALID_SOCKET)
   {
      perror("socket()");
      exit(errno);
   }

   sin.sin_addr.s_addr = inet_addr(adresse);
   sin.sin_port = htons(PORT);
   sin.sin_family = AF_INET;

   // Condition pour éviter le message classique "Address already in use"
   if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &(int){ 1 }, sizeof(int)) < 0)
   {
      perror("setsockopt(SO_REUSEADDR) failed");
   }


   if(bind(sock,(SOCKADDR *) &sin, sizeof sin) == SOCKET_ERROR)
   {
      perror("bind()");
      exit(errno);
   }

   if(listen(sock, MAX_CLIENTS) == SOCKET_ERROR)
   {
      perror("listen()");
      exit(errno);
   }
   return sock;
}

void end_connection(int sock)
{
   closesocket(sock);
}

void application()
{
   char* ip = "127.0.0.1";

   int sock = init_connection(ip);
   char buffer[BUF_SIZE];
   // L'index du client actuel
   int actuel = 0;

   int max = sock;
   // Un tableau de clients
   Client clients[MAX_CLIENTS];

   fd_set rdfs;

   while(1)
   {
      int i = 0;
      FD_ZERO(&rdfs);

      // ajout file descriptor STDIN_FILENO 
      FD_SET(STDIN_FILENO, &rdfs);

      // ajout de la connection au socket
      FD_SET(sock, &rdfs);

      // ajout de socket pour chaque client
      for(i = 0; i < actuel; i++)
      {
         FD_SET(clients[i].sock, &rdfs);
      }

      if(select(max + 1, &rdfs, NULL, NULL, NULL) == -1)
      {
         perror("select()");
         exit(errno);
      }

      /* Si quelqu'un éssaye de taper quelque chose sur le clavier */
      if(FD_ISSET(STDIN_FILENO, &rdfs))
      {
         /* On arrete le serveur */
         break;
      }
      else if(FD_ISSET(sock, &rdfs)) // premiere connection du client 
      {

         /* Nouveau client */
         SOCKADDR_IN csin ;
         size_t sinsize = sizeof csin;
         int csock = accept(sock, (SOCKADDR *)&csin, &sinsize);
         if(csock == SOCKET_ERROR)
         {
            perror("accept()");
            continue;
         }

         // A l'isse de la connection du client il nous faut récupérer son nom
         if(read_client(csock, buffer) == -1)
         {
            printf("Error\n");
            /* disconnected */
            continue;
         }

         // Calcul du nouveau maximum du fd
         max = csock > max ? csock : max;

         FD_SET(csock, &rdfs);

         Client c = { csock };
         strncpy(c.name, buffer, BUF_SIZE - 1);
         clients[actuel] = c;
         actuel++;
         // envoie du menu à l'issue de la connection du client
         char* data = menu();
         write_client(c.sock, data);
      }
      else // Le serveur écoute le client
      {
         int i = 0;
         for(i = 0; i < actuel; i++)
         {
            if(FD_ISSET(clients[i].sock, &rdfs)) // si le client est en train de parler
            {
               Client client = clients[i];
               int c = read_client(clients[i].sock, buffer);
               if(c == 0) // client déconnecté
               {
                  closesocket(clients[i].sock);
                  remove_client(clients, i, &actuel);
                  // Envoie du message aux autres clients pour prévenir que le client s'est déconnecté
                  strncpy(buffer, client.name, BUF_SIZE - 1);
                  strncat(buffer, " s'est déconnecté !", BUF_SIZE - strlen(buffer) - 1);
                  envoie_tous(clients, client, actuel, buffer, 1);
               }
               else
               {
                  if (strstr(buffer, "/list")) // le client demande la liste des personnes connectées 
                  {
                     system("clear");
                     write_client(clients[i].sock, get_list(clients,client, actuel));
                  }
                  else if(strstr(buffer, "/menu")) // le client demande le menu
                  {
                     system("clear");
                     char* data = menu();
                     write_client(clients[i].sock, data);
                  }
                  else if(strstr(buffer, "/play_with")) // le client cherche à jouer contre un client connecté
                  {
                     system("clear");
                     // On récupère le client demandé 
                     Client client_adverse = getClient(&buffer[11], clients, actuel);

                     char status ;
                     char demande[BUF_SIZE] = "Veuillez choisir un mot : " ; 
                     // Envoie d'un message afin que le client adverse choisisse un mot
                     write_client(client_adverse.sock, demande);
                     read_client(client_adverse.sock, buffer);
                     printf("Le mot du client adverse : %s\n", buffer);
                     // procédure de jeu avec un client adverse
                     play_against(client_adverse.sock, clients[i].sock, buffer);
                  }
                  else if(strstr(buffer, "/play_all")) // Tous les clients jouent tour à tour 
                  {
                     system("clear");
                     // procédure de jeu avec tous les clients (le &buffer[10] représente le niveau de difficulté du jeu)
                     play_all(clients, client, actuel, &buffer[10]);
                  }
                  else if (strstr(buffer, "/play")) // Joue contre le serveur
                  {
                     system("clear");
                     // procédure de jeu avec tous les clients (le &buffer[6] représente le niveau de difficulté du jeu)
                     jouerPendu(clients[i].sock, &buffer[6]);
                  }
                  else
                  {
                     // Chat classique, on envoie le message à tous les clients
                     envoie_tous(clients, client, actuel, buffer, 0);
                  }
               }
            }
         }
      }
   }
   // serveur déconnecté, on delete tout les clients
   clear_clients(clients, actuel);
   // close la connection
   end_connection(sock);
}


void play_all(Client *clients, Client actuel_client, int actuel, char* niveau)
{
   MESSAGE_SERVEUR messageServeur;
   MESSAGE_CLIENT messageClient;
   int actuel_player = 0 ;
   char coupsJoues = 0;
   int i = 0;
   char status = PUT;
   char motOriginal[LONGUEUR_DATA+1];

   if(strcmp ("facile", niveau) == 0) // Le client chosit le niveau facile
   {
      if (!piocherMot(motOriginal, niveau)) // choix d'un mot dans le dictionnaire de donnée
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else if (strcmp ("moyen", niveau) == 0) // Le client chosit le niveau moyen
   {
      if (!piocherMot(motOriginal, niveau)) // choix d'un mot dans le dictionnaire de donnée
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else if(strcmp ("difficile", niveau) == 0) // Le client chosit le niveau difficile
   {
      if (!piocherMot(motOriginal, niveau)) // choix d'un mot dans le dictionnaire de donnée
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else // si le client ne choisit pas de niveau spécifique, il est par défault initialiser à moyen
   {
      if (!piocherMot(motOriginal, "moyen"))
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   char motMasque[LONGUEUR_DATA+1];


   /* initialise le message */
   bzero((char*)&messageServeur, sizeof(MESSAGE_SERVEUR));
   bzero((char*)&messageClient, sizeof(MESSAGE_CLIENT));

   strcpy(motMasque, motOriginal);
   // mot masqué 
   for(i=1;i<(strlen(motOriginal));i++) motMasque[i] = '_';

   printf("Le mot à trouver est : %s\n", motOriginal);

   // Indique que tous les clients vont jouer
   char buffer[BUF_SIZE] = "PLAY_TOUS" ;
   status = PUT;
   // Envoie du mot à trouver à tous les clients
   envoie_tous(clients, actuel_client, actuel, buffer, 1);
   
   /* BOUCLE DE JEU */
   for(coupsJoues=0;(coupsJoues<=NB_COUPS) && (status == PUT || status == ERREUR);)
   {
      if(actuel_player == actuel) // afin d'éviter de chercher un client qui n'existe pas
      {
         actuel_player = 0 ;
      }
      for(i = 0; i < actuel; i++)
      {
         if(status != ERREUR)
         {
            // Si c'est le client qui a lancer la partie, il joue en premier
            if(clients[actuel_player].sock == clients[i].sock)
            {
               status = envoyerMessageUnique(clients[i].sock, messageServeur, PUT, coupsJoues, motMasque);
            }
            else
            {
               status = envoyerMessage(clients[i].sock, messageServeur, PUT, coupsJoues, motMasque);
            }
         }
      }
      if(status == QUIT) 
      {
         printf("%s\n"," Le client a quitté" );
         break ;
      }
      status = recevoirMessage(clients[actuel_player].sock, &messageClient);
      if(status == QUIT) 
      {
         printf("%s\n"," Problème quand on recoit le message" );
         break ;
      }
      status = traiterMessage(messageClient, motOriginal, motMasque, &coupsJoues);
      if(status == GAGNE || status == PERDU)
      {
         for(i = 0; i < actuel; i++)
         {
            // On envoie le message à tous les clients une fois la partie terminé
            envoyerMessage(clients[i].sock, messageServeur, status, coupsJoues, motOriginal);
         }
      }
      actuel_player ++;
   }
   if(status == GAGNE) // client a gagner
   {
      printf("Le client a gagné, bravo à lui !!\n");
   }
   else if(status == PERDU) // client a perdu
   {
      printf("Le client a perdu !!\n");
   } 
   else if(status == QUIT) // client a quitter / probleme
   {
      printf("Probleme dans la reception du client !!\n");
   }     
}

void play_against(int client_adverse, int client_actuel, char* mot_propose)
{
   MESSAGE_SERVEUR messageServeur;
   MESSAGE_CLIENT messageClient;

    int j = 0;
    for (j= 0 ; mot_propose[j] != '\0' ; j++) // Afin de mettre le mot proposé en majuscule
    {
        mot_propose[j] = toupper(mot_propose[j]);
    }

   char coupsJoues = 0;
   int i = 0;
   char status = PUT;
   char motOriginal[LONGUEUR_DATA+1];
   char motMasque[LONGUEUR_DATA+1];

   /* initialise le message */
   bzero((char*)&messageServeur, sizeof(MESSAGE_SERVEUR));
   bzero((char*)&messageClient, sizeof(MESSAGE_CLIENT));
      
   printf("Le mot à trouver est : %s\n", mot_propose);

   strcpy(motOriginal, mot_propose);
   strcpy(motMasque, motOriginal);
   // mot masqué 
   for(i=1;i<(strlen(motOriginal));i++) motMasque[i] = '_';

   /* BOUCLE DE JEU */
   status = PUT;
   for(coupsJoues=0;(coupsJoues<=NB_COUPS) && (status == PUT || status == ERREUR);)
   {
      if(status != ERREUR)
      {
         status = envoyerMessage(client_actuel, messageServeur, PUT, coupsJoues, motMasque);
      }
      if(status == QUIT) 
      {
         break ;
      }
      status = recevoirMessage(client_actuel, &messageClient);
      if(status == QUIT) 
      {
         break ;
      }
      status = traiterMessage(messageClient, motOriginal, motMasque, &coupsJoues);
      if(status == GAGNE || status == PERDU)
      {
         envoyerMessage(client_actuel, messageServeur, status, coupsJoues, motOriginal);
      }
   }
   if(status == GAGNE)
   {
      char gagne[BUF_SIZE] = "Votre adversaire a trouvé votre mot" ;
      write_client(client_adverse, gagne);
      printf("Le client a gagné, bravo à lui !! \n");
   }
   else if(status == PERDU)
   {
      char perdu[BUF_SIZE] = "Votre adversaire n'a pas trouvé votre mot" ;
      write_client(client_adverse, perdu);
      printf("Le client a perdu !!\n");
   } 
   else if(status == QUIT)
   {
      printf("Probleme dans la reception du client !!\n");
   }
}

void jouerPendu(int socket, char* niveau)
{
   MESSAGE_SERVEUR messageServeur;
   MESSAGE_CLIENT messageClient;
   char coupsJoues = 0;
   int i = 0;
   char status = PUT;
   char motOriginal[LONGUEUR_DATA+1];
   
   if(strcmp ("facile", niveau) == 0) 
   {
      if (!piocherMot(motOriginal, niveau))
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else if (strcmp ("moyen", niveau) == 0) 
   {
      if (!piocherMot(motOriginal, niveau))
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else if(strcmp ("difficile", niveau) == 0)
   {
      if (!piocherMot(motOriginal, niveau))
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }
   else
   {
      if (!piocherMot(motOriginal, "moyen"))
      {
         printf("Impossible de trouver le mot\n");
         exit(0);
      }
   }

   char motMasque[LONGUEUR_DATA+1];

   /* initialise le message */
   bzero((char*)&messageServeur, sizeof(MESSAGE_SERVEUR));
   bzero((char*)&messageClient, sizeof(MESSAGE_CLIENT));

   strcpy(motMasque, motOriginal);
   for(i=1;i<(strlen(motOriginal));i++) motMasque[i] = '_';

   printf("Le mot à trouver est : %s\n", motOriginal);

   /* BOUCLE DE JEU */
   status = PUT;
   for(coupsJoues=0;(coupsJoues<=NB_COUPS) && (status == PUT || status == ERREUR);)
   {
      if(status != ERREUR)
      {
         status = envoyerMessage(socket, messageServeur, PUT, coupsJoues, motMasque);
      }

      if(status == QUIT) 
      {
         break;
      }
      status = recevoirMessage(socket, &messageClient);
      if(status == QUIT) 
      {
         break ;
      }
      status = traiterMessage(messageClient, motOriginal, motMasque, &coupsJoues);
      if(status == GAGNE || status == PERDU)
      {
         envoyerMessage(socket, messageServeur, status, coupsJoues, motOriginal);
      }
   }
   if(status == GAGNE)
   {
      printf("Le client a gagné, bravo à lui\n");
   }
   else if(status == PERDU)
   {
      printf("Le client a perdu !!\n");
   } 
   else if(status == QUIT)
   {
      printf("Probleme dans la reception du client !!\n");
   }
}


Client getClient(char* pseudo, Client *clients, int actuel)
{
   // TODO tester si le client n'est pas dans la liste 
   for(int i = 0; i < actuel; i++)
   {
      if(strcmp(pseudo,clients[i].name) == 0)
      {
         return clients[i] ;
      }
   }
}

// Méthode utiliser pour prévenir le client que c'est à lui de jouer (initialisation du boolean trouve à 1 )
char envoyerMessageUnique(int socket, MESSAGE_SERVEUR message, char type, char nbCoups, char *mot)
{
   printf("Hello\n");
   char status = PUT;
   int retour;
   char type2 = type ;
   char nbCoups2 = (int)nbCoups;
   message.commande = type2;
   sprintf(&message.nbCoups, "%01d", nbCoups2);
   strcpy(message.data, mot);
   message.jouer = 1 ;

   retour = send(socket, &message, sizeof(MESSAGE_SERVEUR), 0);

   if(retour <= 0)
      status = QUIT;   
   return status;
}

// Envoie d'un message classique
char envoyerMessage(int socket, MESSAGE_SERVEUR message, char type, char nbCoups, char *mot)
{
   int retour;

   char status = PUT;
   char type2 = type ;
   char nbCoups2 = (int)nbCoups;
   message.commande = type;
   sprintf(&message.nbCoups, "%01d", nbCoups);
   strcpy(message.data, mot);

   retour = send(socket, &message, sizeof(MESSAGE_SERVEUR), 0);
   if(retour <= 0)
      status = QUIT;   
   return status;
}

char recevoirMessage(int socket, MESSAGE_CLIENT *message)
{

   char status = PUT;
   int retour;
   
   retour = recv(socket, message, sizeof(MESSAGE_CLIENT), 0);

   if(retour <= 0)
      status = QUIT;
   
   return status;
}

// procédure traiter message permettant de vérifier si la lettre proposé est dans le mot
char traiterMessage(MESSAGE_CLIENT message, char *motOriginal, char *motMasque, char *nbCoups)
{
   int i;
   int trouve;
   char status = PUT;

   // Le client a quitté la partie
   if(message.commande == QUIT) return message.commande;

   // Le client propose une lettre
   if(message.commande == PUT)
   {
      /* une lettre alphabétique valide ? */
      if(isalpha(message.lettre))
      {
         /* test la lettre proposée */
         trouve = 0;
         for(i=1;i<(strlen(motOriginal));i++)
         {
            if(message.lettre == *(motOriginal+i*sizeof(char)))
            {
               printf("Lettre proposée %c trouvée en %d\n", message.lettre, i);
               *(motMasque+i*sizeof(char)) = *(motOriginal+i*sizeof(char));
               trouve++;
            }
         }
         if(trouve == 0) // si il ne trouve pas de lettre
         {
            (*nbCoups)++;
            printf("Lettre '%c' introuvable - %d coup(s) joué(s) sur %d coups max = %d coups restant(s)\n", message.lettre, *nbCoups, NB_COUPS, (NB_COUPS-(*nbCoups)));
         }
         else  
         {
            printf("Lettre '%c' trouvée %d fois - %d coup(s) joué(s) sur %d coups max = %d coups restant(s)\n", message.lettre, trouve, *nbCoups, NB_COUPS, (NB_COUPS-(*nbCoups)));
         }
         trouve = 0; // réinitialisation du boolean
      }
         
      // Si le mot a été trouvé
      if(!strcmp(motMasque, motOriginal))
      {
         printf("Le mot %s a été découvert en %d coups => GAGNE\n", motOriginal, *nbCoups);
         status = GAGNE;
      }
      if(*nbCoups >= NB_COUPS) // nombre de coups dépassé
      {
         printf("Le mot %s non trouvé en %d coups => PERDU\n", motOriginal, *nbCoups);
         status = PERDU;
      }
   }
   else  status = ERREUR;  // mot incorrect
   
   return status;
}


void clear_clients(Client *clients, int actuel)
{
   int i = 0;
   for(i = 0; i < actuel; i++)
   {
      closesocket(clients[i].sock);
   }
}

void remove_client(Client *clients, int to_remove, int *actuel)
{
   /* we remove the client in the array */
   memmove(clients + to_remove, clients + to_remove + 1, (*actuel - to_remove - 1) * sizeof(Client));
   /* number client - 1 */
   (*actuel)--;
}

void envoie_tous(Client *clients, Client sender, int actuel, char *buffer, char from_server)
{
   int i = 0;
   char message[BUF_SIZE];
   message[0] = 0;
   for(i = 0; i < actuel; i++)
   {
      /* we don't send message to the sender */
      if(sender.sock != clients[i].sock)
      {
         if(from_server == 0)
         {
            strncpy(message, sender.name, BUF_SIZE - 1);
            strncat(message, " : ", sizeof message - strlen(message) - 1);
         }
         strncat(message, buffer, sizeof message - strlen(message) - 1);
         write_client(clients[i].sock, message);
      }
   }
}

char *get_list(Client *clients, Client sender, int actuel) {
   char* data = "liste des personnes connectées :\n";
   char* data1 ;
   if(actuel == 1)
   {
      data = "Personne n'est encore connecté" ;
   }
   else
   {
      for(int i = 0; i < actuel; i++)
      {
         if(sender.sock != clients[i].sock)
         {
            data1 = clients[i].name;
            data = concat_message(data, data1);
            data = concat_message(data, ",");
         }
      }
   }
   return data ;
}

char *concat_message(char* m1, char* m2) {
      
      char * message = (char *) malloc(1 + strlen(m1)+ strlen(m2) );
      strcpy(message, m1);
      strcat(message, m2);
      return message;
}


int read_client(int sock, char *buffer)
{
   int retour = 0;
   if((retour = recv(sock, buffer, BUF_SIZE - 1, 0)) < 0)
   {
      perror("receive()");
      retour = 0;
   }
   // vide buffer
   buffer[retour] = 0;

   return retour;
}

void write_client(int sock, char *buffer)
{
   if(send(sock, buffer, strlen(buffer), 0) < 0)
   {
      perror("send()");
      exit(errno);
   }
}

// Constituer le menu
char *menu()
{
   char *data = "******************************************************************************************************************\n****************************************** BIENVENUE DANS LE JEU DU PENDU !! *************************************\n******************************************************************************************************************\n\n";
   data = concat_message(data,"\n");
   char *data1 = "* /menu pour afficher le menu utilisateur                                                                        *";
   data = concat_message(data, data1);
   data = concat_message(data,"\n");
   char *data2 = "* /list affiche la liste des utilisateurs connectés                                                            *";
   data = concat_message(data, data2);
   data = concat_message(data,"\n");
   char *data3 = "* Il existe trois types de niveau : facile, moyen et difficile                                                  *" ;
   data = concat_message(data, data3);
   data = concat_message(data,"\n");
   char *data4 = "* /play pour jouer contre le serveur (vous pouvez choisir le niveau de la partie, exemple : /play facile)      *";
   data = concat_message(data, data4);
   data = concat_message(data,"\n");
   char *data5 = "* /play_with pour jouer contre un autre utilisateur  : /play_with [pseudo]                                       *" ;
   data = concat_message(data, data5);
   data = concat_message(data,"\n");
   char *data6 = "* /play_all pour jouer avec toutes les personnes tour à tour, vous pouvez également choisir la difficulté du mot *" ;
   data = concat_message(data, data6);
   data = concat_message(data,"\n");
   char *data7 = "* Vous pouvez également discuter comme un chat classique en tapant votre message.                                *" ;
   data = concat_message(data, data7);
   data = concat_message(data,"\n");

   return data;
}


int piocherMot(char *motPioche, char* niveau)
{
   FILE* dico = NULL; 
   int nombreMots = 0, numMotChoisi = 0, i = 0;
   int caractereLu = 0;
   if(strcmp ("facile", niveau) == 0)
   {
      dico = fopen("dico_facile.txt", "r"); // On ouvre le dictionnaire en lecture seule
   }
   else if (strcmp ("moyen", niveau) == 0)
   {
      dico = fopen("dico_moyen.txt", "r"); // On ouvre le dictionnaire en lecture seule
   }
   else if(strcmp ("difficile", niveau) == 0)
   {
      dico = fopen("dico_difficile.txt", "r"); // On ouvre le dictionnaire en lecture seule
   }
   else
   {
      dico = fopen("dico_moyen.txt", "r"); // On ouvre le dictionnaire en lecture seule
   }

    // On vérifie si on a réussi à ouvrir le dictionnaire
    if (dico == NULL) // Si on n'a PAS réussi à ouvrir le fichier
    {
        printf("\nImpossible de charger le dictionnaire de mots");
        return 0; // On retourne 0 pour indiquer que la fonction a échoué
        // A la lecture du return, la fonction s'arrête immédiatement.
    }

    // On compte le nombre de mots dans le fichier (il suffit de compter les
    // entrées \n
    while (caractereLu != EOF)
    {
        caractereLu = fgetc(dico);
        if (caractereLu == '\n')
            nombreMots++;
    }

    numMotChoisi = nombreAleatoire(nombreMots); // On pioche un mot au hasard

    // On recommence à lire le fichier depuis le début. On s'arrête lorsqu'on est arrivés au bon mot
    rewind(dico);
    while (numMotChoisi > 0)
    {
        caractereLu = fgetc(dico);
        if (caractereLu == '\n')
            numMotChoisi--;
    }

    /* Le curseur du fichier est positionné au bon endroit.
    On n'a plus qu'à faire un fgets qui lira la ligne */
    fgets(motPioche, 100, dico);

    // On vire l'\n à la fin
    motPioche[strlen(motPioche)-2] = '\0';
    fclose(dico);

    return 1; // Tout s'est bien passé, on retourne 1
}

int nombreAleatoire(int nombreMax)
{
    srand(time(NULL));
    return (rand() % nombreMax);
}

int main(int argc, char **argv)
{

   init();

   application();

   end();

   return EXIT_SUCCESS;
}