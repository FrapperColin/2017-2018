#include "client.h"

void init()
{
   #ifdef WIN32 // windows 
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
    #ifdef WIN32 // windows
       WSACleanup();
    #endif
}

void application(char *address, char *name)
{
   int sock = init_connection(address);
   char buffer[BUF_SIZE];

   fd_set rdfs;

   // Une fois connecté, on envoie notre nom au serveur.
   write_server(sock, name);

   while(1)
   {
      FD_ZERO(&rdfs);

      // ajout file descriptor STDIN_FILENO 
      FD_SET(STDIN_FILENO, &rdfs);

      // ajout de la connection au socket
      FD_SET(sock, &rdfs);

      if(select(sock + 1, &rdfs, NULL, NULL, NULL) == -1)
      {
         perror("select()");
         exit(errno);
      }

      /* Si le client éssaye de taper quelque chose sur le clavier */
      if(FD_ISSET(STDIN_FILENO, &rdfs))
      {
         // On récupère ce que le client a écrit
         fgets(buffer, BUF_SIZE - 1, stdin);
         char *p = NULL;
         p = strstr(buffer, "\n");
         if(p != NULL)
         {
            *p = 0;
         }
         else
         {
            // clear le buffer
            buffer[BUF_SIZE - 1] = 0;
         }
         // On envoie le message au serveur
         write_server(sock, buffer);
         
         if(strstr(buffer, "/play_with")) // Demande de jouer contre un client
         {
            system("clear");
            jouer_partie(sock);
         }
         else if(strstr(buffer, "/play_all")) // Demande de jouer avec tous les clients contre le serveur
         {
            system("clear");
            jouer_all(sock);
         }
         else if(strstr(buffer, "/play")) // Demande de jouer contre le serveur
         {
            system("clear");
            jouer_partie(sock);
         }
         else if(strstr(buffer, "/list")) 
         {
            system("clear");
         }
         else if(strstr(buffer, "/menu"))
         {
            system("clear");
         }
      }
      // Le serveur ou les autres clients nous envoient un message
      else if(FD_ISSET(sock, &rdfs))
      {
         int n = read_server(sock, buffer);
         // Le serveur ne répond plus
         if(n == 0)
         {
            printf("Server disconnected !\n");
            close(sock);
            break;
         }
         else if(strstr(buffer, "PLAY_TOUS")) // Si c'est la demande de jouer tous ensemble 
         {
            //system("clear");
            jouer_all(sock); // On joue avec tous le monde
         }
         else
         {
            puts(buffer); // On affiche le message, chat classique
         }
      }
   }
   end_connection(sock);
}

int init_connection(char *address)
{
   int sock_client;
   HOSTENT* hp;
   SOCKADDR_IN adresse_serv;

   if ((hp = gethostbyname(address)) == NULL) 
   {
      perror("Problème avec l'hostname");
      exit(1);
   }

    bcopy(hp->h_addr, &adresse_serv.sin_addr, hp->h_length);
    adresse_serv.sin_family = hp->h_addrtype;
    adresse_serv.sin_port = htons(PORT);

    if ((sock_client = socket(hp->h_addrtype, SOCK_STREAM, 0)) < 0) 
    {
        perror("socket");
        exit(1);
    }

    if (connect(sock_client, (SOCKADDR *) &adresse_serv, sizeof (adresse_serv)) < 0) 
    {
        perror("connect");
        close(sock_client);
        exit(EXIT_FAILURE);
    }
    fprintf(stderr, "Connexion avec le serveur établie\n");

    return sock_client;
}

void jouer_all(int sock)
{

   MESSAGE_SERVEUR messageServeur;
   MESSAGE_CLIENT messageClient;

   bzero((char*)&messageServeur, sizeof(MESSAGE_SERVEUR));
   bzero((char*)&messageClient, sizeof(MESSAGE_CLIENT));
   char status = PUT ;
   // Boucle de jeu
   while(status != GAGNE || status != PERDU)
   {
      status = recevoirMessage(sock, &messageServeur);

      status = traiterMessage(messageServeur);
      if(status == GAGNE || status == PERDU || status == QUIT)
      {
         break ;
      }
      if(status != GAGNE || status != PERDU)
      {
         if(messageServeur.jouer == 1)
         {
            status = envoyerMessage(sock, messageClient);
         }
         else
         {
            printf("Attendez votre tour !!! \n");
         }
      }
   }
   if(status == QUIT)
   {
      printf("Probleme le serveur s'est déconnecté\n");
   }
}

void jouer_partie(int sock)
{

   MESSAGE_SERVEUR messageServeur;
   MESSAGE_CLIENT messageClient;

   bzero((char*)&messageServeur, sizeof(MESSAGE_SERVEUR));
   bzero((char*)&messageClient, sizeof(MESSAGE_CLIENT));
   char status = PUT ;
   while(status != GAGNE || status != PERDU)
   {
      if(status == QUIT)
      {
         break ;
      }
      status = recevoirMessage(sock, &messageServeur);

      status = traiterMessage(messageServeur);
      if(status == GAGNE || status == PERDU || status == QUIT)
      {
         break ;
      }
      else if(status != GAGNE || status != PERDU)
      {
         status = envoyerMessage(sock, messageClient);
      }
   }
   if(status == QUIT)
   {
      printf("Probleme le serveur s'est déconnecté\n");
   }
}

// envoie message client
char envoyerMessage(int socket, MESSAGE_CLIENT message)
{
   char status = PUT;
   int retour;
   char une_lettre  ;

   char line[256];

   printf("Entrer une lettre : \n");
   if (fgets(line, sizeof line, stdin) == NULL) 
   {
      printf("Input error.\n");
      exit(1);
   }
   // On met la lettre en majuscule
   une_lettre = toupper(line[0]);

   message.lettre = une_lettre;
   message.commande = status ;

   retour = send(socket, &message, sizeof(MESSAGE_CLIENT), 0);
   if(retour <= 0)
   {
      status = QUIT;   
   }
   return status;
}

char recevoirMessage(int socket, MESSAGE_SERVEUR *message)
{
   char status = PUT;
   int retour;

   retour = recv(socket, message, sizeof(MESSAGE_SERVEUR), 0);
   if(retour <= 0)
   {
      printf("On quitte problème pour recevoir \n");
      status = QUIT;
   }

   return status;
}


char traiterMessage(MESSAGE_SERVEUR message)
{
   system("clear");


   char my_commande = message.commande;
   
   char my_nbCoups = message.nbCoups ;
   int nbCOUPS = NB_COUPS ; 
   char nbCOUP = nbCOUPS +'0' ;
   // calcul du nombre de coups restants
   int total = nbCOUP - my_nbCoups ;
   if(message.commande==PERDU)
   {
      total = 0 ;
      print_pendu(total);
      printf("DOMMAGE VOUS AVEZ PERDU ! ");
      printf("\nLe mot recherché était :   %s\n", message.data);
   }
   else if(message.commande==GAGNE)
   {
      printf("VOUS AVEZ GAGNE BRAVO ! ");
      printf("\nLe mot recherché était bien :   %s\n", message.data);
   }
   else if(message.commande == QUIT)
   {
      printf("Le serveur a quitté précitament\n");
   }
   else 
   {
      print_pendu(total);
      printf("Il vous reste %d coups !!\n",  total);
      printf("\nLe mot recherché :   %s\n", message.data);
   }
   return message.commande;
}

void print_pendu(char nombreCoups)
{
    switch(nombreCoups) 
    {
        case 9:
            printf("\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                                            \n"
                         "\t\t\t                                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓     \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓     \n");
            break;
        case 8:
            printf("\t\t\t                                                             \n"
                         "\t\t\t                                                             \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓      \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓      \n");
            
            break;
        case 7:
            printf("\t\t\t        ________________________________________             \n"
                         "\t\t\t        ├┼─────────────────────────────────────┘             \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                                                   \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓      \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓      \n");
            
            break;
        case 6:
            printf("\t\t\t        ________________________________________              \n"
                         "\t\t\t        ├┼─────────────────────────────────────┘              \n"
                         "\t\t\t        ││  / /                                               \n"
                         "\t\t\t        ││ / /                                                \n"
                         "\t\t\t        ││/ /                                                 \n"
                         "\t\t\t        ││ /                                                  \n"
                         "\t\t\t        ││/                                                   \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                                                    \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓       \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓       \n");
            
            break;
        case 5:
            printf("\t\t\t        ________________________________________               \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘               \n"
                         "\t\t\t        ││  / /                               │                \n"
                         "\t\t\t        ││ / /                                │                \n"
                         "\t\t\t        ││/ /                                 │                \n"
                         "\t\t\t        ││ /                                 / \\              \n"
                         "\t\t\t        ││/                                 /   \\             \n"
                         "\t\t\t        ││                                 │     │             \n"
                         "\t\t\t        ││                                 │     │             \n"
                         "\t\t\t        ││                                  \\__ /             \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                                                     \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓        \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓        \n");
            
            break;
        case 4:
            printf("\t\t\t        ________________________________________                \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘                \n"
                         "\t\t\t        ││  / /                               │                 \n"
                         "\t\t\t        ││ / /                          \\\\\\||||||////        \n"
                         "\t\t\t        ││/ /                            \\\\  ~ ~  //          \n"
                         "\t\t\t        ││ /                              (  @ @  )             \n"
                         "\t\t\t        ││/                                \\ (_) /             \n"
                         "\t\t\t        ││                                 │\\___/│             \n"
                         "\t\t\t        ││                                 │ │ │ │              \n"
                         "\t\t\t        ││                                  \\│_│/              \n"
                         "\t\t\t        ││                                ___│ │___             \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                                                      \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓         \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓         \n");
            
            break;
        case 3:
            printf("\t\t\t        ________________________________________                 \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘                 \n"
                         "\t\t\t        ││  / /                               │                  \n"
                         "\t\t\t        ││ / /                          \\\\\\||||||////         \n"
                         "\t\t\t        ││/ /                            \\\\  ~ ~  //           \n"
                         "\t\t\t        ││ /                              (  @ @  )              \n"
                         "\t\t\t        ││/                                \\ (_) /              \n"
                         "\t\t\t        ││                                 │\\___/│              \n"
                         "\t\t\t        ││                                 │ │ │ │               \n"
                         "\t\t\t        ││                                  \\│_│/               \n"
                         "\t\t\t        ││                                ___│ │___              \n"
                         "\t\t\t        ││                               / _     _ \\            \n"
                         "\t\t\t        ││                              / / │   │ \\ \\          \n"
                         "\t\t\t        ││                             / /  │   │  \\ \\         \n"
                         "\t\t\t        ││                            / /   │   │   \\ \\        \n"
                         "\t\t\t        ││                           (  )   │   │   (  )         \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                                                       \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓          \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓          \n");
            
            break;
        case 2:
            printf("\t\t\t        ________________________________________                  \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘                  \n"
                         "\t\t\t        ││  / /                               │                   \n"
                         "\t\t\t        ││ / /                          \\\\\\||||||////          \n"
                         "\t\t\t        ││/ /                            \\\\  ~ ~  //            \n"
                         "\t\t\t        ││ /                              (  @ @  )               \n"
                         "\t\t\t        ││/                                \\ (_) /               \n"
                         "\t\t\t        ││                                 │\\___/│               \n"
                         "\t\t\t        ││                                 │ │ │ │                \n"
                         "\t\t\t        ││                                  \\│_│/                \n"
                         "\t\t\t        ││                                ___│ │___               \n"
                         "\t\t\t        ││                               / _     _ \\             \n"
                         "\t\t\t        ││                              / / │   │ \\ \\           \n"
                         "\t\t\t        ││                             / /  │   │  \\ \\          \n"
                         "\t\t\t        ││                            / /   │   │   \\ \\         \n"
                         "\t\t\t        ││                           (  )   │   │   (  )          \n"
                         "\t\t\t        ││                                  │   │                 \n"
                         "\t\t\t        ││                                  ├─╥─┤                 \n"
                         "\t\t\t        ││                                  │ ║ │                 \n"
                         "\t\t\t        ││                                  │ ║ │                 \n"
                         "\t\t\t        ││                                  │ ║ │                 \n"
                         "\t\t\t        ││                                 ███║███                \n"
                         "\t\t\t        ││                           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓           \n"
                         "\t\t\t   *************                     ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓           \n");
            
            break;
        case 1:
            printf("\t\t\t        ________________________________________                    \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘                    \n"
                         "\t\t\t        ││  / /                               │                     \n"
                         "\t\t\t        ││ / /                          \\\\\\||||||////            \n"
                         "\t\t\t        ││/ /                            \\\\  ~ ~  //              \n"
                         "\t\t\t        ││ /                              (  @ @  )                 \n"
                         "\t\t\t        ││/                                \\ (_) /                 \n"
                         "\t\t\t        ││                                 │\\___/│                 \n"
                         "\t\t\t        ││                                 │ │ │ │                  \n"
                         "\t\t\t        ││                                  \\│_│/                  \n"
                         "\t\t\t        ││                                ___│ │___                 \n"
                         "\t\t\t        ││                               / _     _ \\               \n"
                         "\t\t\t        ││                              / / │   │ \\ \\             \n"
                         "\t\t\t        ││                             / /  │   │  \\ \\            \n"
                         "\t\t\t        ││                            / /   │   │   \\ \\           \n"
                         "\t\t\t        ││                           (  )   │   │   (  )            \n"
                         "\t\t\t        ││                                  │   │                   \n"
                         "\t\t\t        ││                                  ├─╥─┤                   \n"
                         "\t\t\t        ││                                  │ ║ │                   \n"
                         "\t\t\t        ││                                  │ ║ │                   \n"
                         "\t\t\t        ││                                  │ ║ │                   \n"
                         "\t\t\t        ││                                 ███║███                  \n"
                         "\t\t\t        ││                  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓                      \n"
                         "\t\t\t   *************            ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓                      \n");
            
            break;
        case 0:
            printf("\t\t\t        ________________________________________                     \n"
                         "\t\t\t        ├┼────────────────────────────────────┬┘                     \n"
                         "\t\t\t        ││  / /                               │                      \n"
                         "\t\t\t        ││ / /                                │                      \n"
                         "\t\t\t        ││/ /                           \\\\\\||||||////             \n"
                         "\t\t\t        ││ /                             \\\\  ~ ~  //               \n"
                         "\t\t\t        ││/                               (  X X  )                  \n"
                         "\t\t\t        ││                                 \\ (_) /                  \n"
                         "\t\t\t        ││                                 │\\___/│                  \n"
                         "\t\t\t        ││                                  \\│_│/                   \n"
                         "\t\t\t        ││                                   │ │                     \n"
                         "\t\t\t        ││                                ___│ │___                  \n"
                         "\t\t\t        ││                               / _     _ \\                \n"
                         "\t\t\t        ││                              / / │   │ \\ \\              \n"
                         "\t\t\t        ││                             / /  │   │  \\ \\             \n"
                         "\t\t\t        ││                            / /   │   │   \\ \\            \n"
                         "\t\t\t        ││                           (  )   │   │   (  )             \n"
                         "\t\t\t        ││                                  │   │                    \n"
                         "\t\t\t        ││                                  ├─╥─┤                    \n"
                         "\t\t\t        ││                                  │ ║ │                    \n"
                         "\t\t\t        ││                                  │ ║ │                    \n"
                         "\t\t\t        ││                                  │ ║ │                    \n"
                         "\t\t\t        ││             ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  ███║███                   \n"
                         "\t\t\t   *************       ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓                            \n");
            break;
        default :
            break;
    }


}


void end_connection(int sock)
{
   closesocket(sock);
}

int read_server(int sock, char *buffer)
{
   int n = 0;

   if((n = recv(sock, buffer, BUF_SIZE - 1, 0)) < 0)
   {
      perror("recv()");
      close(sock);
      exit(errno);
   }
   buffer[n] = 0;

   return n;
}

void write_server(int sock, char *buffer)
{
   if(send(sock, buffer, strlen(buffer), 0) < 0)
   {
      perror("send()");
      close(sock);
      exit(errno);
   }
}

int main(int argc, char **argv)
{
   if(argc < 2) // Indiquer l'adresse utiliser et son pseudo
   {
      printf("Usage : %s [address] [pseudo]\n", argv[0]);
      return EXIT_FAILURE;
   }

   init();

   application(argv[1], argv[2]);

   end();

   return EXIT_SUCCESS;
}