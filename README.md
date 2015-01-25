<h1>~~~ Morgane Game ~~~</h1>
Projet réalisé durant la dernière année en école d'ingénierie logiciel pour la validation de la formation sur les systèmes répartis.

<h2>Introduction </h2>

Implémenter un mini jeu distribué. 
Le but du jeu est d'attrapper tous les cookies du plateau en évitant de se prendre les mines. Le jeu peut accueillir jusqu'à 4 personnes. Si d'autres personnes essayent de se connecter, il se retrouveront en mode spectateur. Lorsque le jeu est terminé, celui-ci affiche le joueur gagnant et re-initialise le plateau, déconnecte les joueurs et attend une nouvelle partie.

<h2>Description technique du projet</h2>

Nous avons utilisé la technologie de Google Web Toolkit RPC pour réaliser le système Client/Server. Nous lançons le serveur sur un TomCat d’Apache et c’est dans un navigateur Web que le client peut interagir avec le serveur.
Pour l’affichage des objets et avoir un rendu graphique dynamique, nous utilisons la technologie des Canvas (HTML) qui est déjà implémenter dans certain navigateur. De même, pour améliorer le rendu graphique de la page HTML, nous avons utilisés les fonctionnalités de la programmation HTML et CSS.
L’avantage d’utiliser GWT est qu’il est simple d’implémenter un service de Client/Server. A l’inverse son utilisation ne fonctionne que dans le sens Client -> Serveur. Le serveur ne retient pas l’état des connections et ne peut retenir les clients. Nous avons donc ajouté en module supplémentaire étant en source libre sur internet qui est « GWTEventServer » qui permet au serveur d’enregistrer les connections et créer des « channels » entre le serveur et les clients permettant ainsi au serveur de pouvoir émettre des notifications aux clients.
Les requêtes des clients vers le serveur sont de type Asynchrone.
Nous pensions utiliser Google App Engine, pour stocker notre application serveur sur les serveurs de Google permettant d’assurer la tolérance aux fautes. Sauf que pour le moment Google ne permet pas la notification du serveur vers les clients. 

Nous nous sommes redirigés vers l’utilisation de <g>Tomcat d’Apache</g> pour faire fonctionner notre application serveur.

<h2>Tolérance aux fautes </h2>

Nous avons utilisé la technique de la réplication passive à froid. Nous enregistrons en dure sur le disque local du serveur les données de la partie en cours en cas de défaillance du serveur. En ce qui concerne les clients, nous autorisons une déconnexion de leur part ; à leur retour il retrouve leur place et leur score.
