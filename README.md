**Contexte et problématiques :**

Dans le contexte de l’exercice, il faut se placer dans un contexte professionnel et surtout celui de Instant system. L’application est un SAAS qui peut être utilisé par de très nombreux utilisateurs (**problématique numéro 1**) pouvant accéder à la map de l’application mobile et avoir accès, entre autres, aux informations sur les parking les plus proches de chez soi.

Dans ce contexte là, il faudra gérer la scalabilité verticale (augmenter les ressources pour un serveur) et horizontale (augmenter le nombre de serveurs) pour que cette application soit durable en fonction du nombre d'utilisateurs croissants. 

Sachant que notre exercice comporte deux sources de données. Une pour la liste des parkings de la ville et une autre le nombre de places disponible pour chaque parking de cette ville en temps réel.(**problématique numéro 2**).

La première source de données de la liste des parkings (**source1**) ne devrait pas évoluer souvent mais périodiquement alors que la seconde source de données (**source2**) évolue en permanence d’où la notion de temps réel. 

On peut déjà imaginer pour la source1 (liste des parkings) qu’on va extraire les données pour les enregistrer dans notre propre base de données. Afin d’éviter la dépendance vers une source de données extérieure et d'être potentiellement exposé à un risque d’arrêt de service de notre fournisseur de données sans ne pouvoir rien contrôler. (**problématique numéro 3**). Ces données peuvent être mis à jour périodiquement à des moment où l’application est la moins utilisée ( par exemple la nuit) en utilisant un batch avec spring batch.

L’application doit pouvoir fonctionner dans d’autres villes : l’URL et format des données parking pourront donc être complètement différents. Ceci signifie qu’il y a une notion de normalisation des données à faire pour pouvoir les interpréter quelque soit la sémantique du fichier source en entrée. (**problématique numéro 4**). 

On pourrait mettre en place un batch (JOB spring batch) par ville et par type de données (dans notre cas Poitiers et parking)  
On aurait un batch avec n étapes (STEP) avec : 
-un itemReader pour aller lire en base de données l’URL retournant un json (dans notre cas mais cela peut être différent avec une autre URL) avec la liste des parkings de la ville X
-Un itemProcessor pour réaliser la normalisation des données provenant d’URL différentes avec des structures différentes.
-Un itemWriter pour enregistrer chaque parking en base de données. 

Pour que le serveur puisse accéder à l’URL en fonction de la ville où l’utilisateur veut voir les parkings les plus proches, il faut au préalable enregistrer ces URL dans un registre en base de données pour pouvoir les charger au lancement de l’application. (**problématique numéro 5**) 



**Architecture choisie :**


Afin de réaliser cette application, on pourrait imaginer une architecture micro-service avec Spring Cloud. L’application mobile va accéder au serveur par l’API Gateway qui va servir d’aiguilleur pour accéder aux micro services de l’application qui eux seront aussi sous forme d’API REST. (Dans notre cas il y en aura qu’un seul d’implémenter (Parking), les autres étant par exemple pour la gestion des vélos, des transports en commun, du covoiturage etc..)
Il y aura donc un micro-service qui va retourner la liste des parkings les plus proche de chez soi (API RESTful)  agrémenté par le nombre de places disponible sous forme d'événement. (EVENT sourcing). 
L’avantage d'utiliser une api gateway tel que le composant zuul de netflix est d’avoir un load balancer applicatif qui va interroger le serveur eureka pour rediriger les requêtes vers l’instance du micro-service ayant le moins de charge. De plus cet api gateway va permettre de gérer les problématiques C.O.R.S (Cross origin platform) et de gérer la sécurité de notre application dans un point central.

Pour récupérer les données en temps réel de différentes sources de données, on va utiliser Apache Kafka. Dans notre contexte, c'est idéal pour permettre la montée en charge (cluster KAFKA et broker)  car nous sommes en mode SAAS. De plus, on va pouvoir répartir en topics les besoins. (Parking, bike, transport en commun, etc..) et les partitions de chaque topic par ville. De plus, kafka permet de garder les données après consommation. Ceci nous permet en cas de non disponibilité du service pendant un certain temps d’avoir accès à la dernière valeur connue.

Notre serveur communique l'information en temps réel à notre client WEB ( app angular par exemple) avec Server sent events en utilisant apache kafka et spring web flux.

**Comment est construite mon API ?**

Lorsque l’utilisateur de l’application demande d’afficher la liste des parkings autour de sa position il fait appel à l’api par  : 

/parkings/{city} avec en paramètre le nom de la ville, les cordonnées de sa position et le rayon de la recherche (en mètre). Vous pouvez consulter plus en détail cette API sur l’URL : 

**http://localhost:8086/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config**

Les parkings les plus proches sont recherchés en base de données. J’ai implémenté le calcul de traitement de la distance des parkings par rapport à notre position en utilisant la formule mathématique : 

R*Cos-1(sin(a)sin(b)+cos(a)cos(b)cos(c-d))

avec R le rayon de la terre
a = latitudeA
b = latitudeB
c = longitudeA
d = longitudeB

Ceci me permet d’avoir la distance entre le point A et le point B à vol d’oiseau.

Cependant j’aurai pu utiliser, par exemple, l’API direction de google pour connaître la distance réel entre le point A et le point B en passant par les rues.
Il aurait également été plus efficace de faire une requête en base de données qui fasse directement le calcul pour me retourner uniquement les parkings compris dans le rayon (fourni en paramètre). Cependant j’ai décidé de l’implémenter en JAVA dans le mini projet.

De quelle façon je récupère les données de la source 2 (data parking temps réel) ?

J’ai fait attention dans mon code à, lorsque je récupère le nombre de places disponibles, à ne pas mettre en échec tout l’appel du service à l’utilisateur. Si l’API ne récupère pas le nombre de places disponibles pour les parkings parce que le service de la source 2 (nombre de places de parking)  est indisponible, l’API  renvoie quand même à l’utilisateur la liste des parkings autour de sa position sans l’information “places disponibles”.

Il est primordial de mettre en place une implémentation d’un pattern “circuit breaker” à ce niveau la (non implémenté) pour ne pas appeler le service externe en continu en cas d’arrêt de services ou d’indisponibilité.

**Comment ai-je rendu mon service générique peu importe la ville, l’URL, le type et la structure de la source de données ?**

Que nous recherchions la liste des parkings et ses places disponibles à Poitiers, Nice, Mulhouse, etc.. notre service fonctionnera.
Une nouvelle interface “DataParkingProcess” a été créée avec une implémentation par ville.J’ai fait attention à ne pas rendre dépendant ces implémentations avec le format de données source. Ceci signifie que même si une source de données pour une ville passe du format JSON au format XML, l'application le prendra en compte. Il suffira d’implémenter l’interface DataFormatter<DataType>. Dans mon cas j’ai implémenté DataJsonFormatter et j’ai créé une classe DataXmlFormatter (non implémenté). On peut faire autant d’implémentation que nécessaire. De plus, la généricité avec le DataType permet de savoir quel type de données on manipule dans nos implémentations de DataParkingProcess.

Dans le cadre du mini projet mes URL sont en dur dans chaque classe implémenté (ParkingPoitierDataProcessImpl, ParkingNiceDataProcessImpl). Cependant elles devront être en base de données dans une table parkingRegistry par exemple dans le cadre d’un vrai projet.

—---------------------------------------------------------------------------------------------------------------------

**Lancement de l’application** : 

Pré requis : JAVA 8 (JDK 1.8)
                     Spring boot 2.6.0
	         Postgresql 9.5

Note : La version de JAVA est 8 car déjà préconfigurée sur mon poste avec compatibilité spring boot et spring cloud. Il faudra bien entendu monter de versions (JAVA 17, Spring boot 3.0.0) dans le cadre d’un vrai projet.

Compilation Maven : Clique droit sur le pom parent => Maven clean, maven install ( build success voir capture d'écran dossier https://github.com/quentin68/appInstantSystem/tree/master/z_captureEcranApp)
	
Chaque microservice est une application Spring Boot et peut être démarré localement à l'aide de votre IDE (clique droit projet => run as JAVA application). Il faut bien s’assurer d’avoir configuré le path vers la classe main de l’application spring boot.
ou ../mvnw spring-boot:run.

Les services de support (configserver et eureka discovery) doivent être démarrés avant toutes autres applications (API gateway, API parkings-service, etc..). 

Serveur eureka discovery - http://localhost:8761

Serveur de configuration - http://localhost:8888

APIGateway - http://localhost:8086

Services parking - port aléatoire, consultez le tableau de bord Eureka (http://localhost:8761)


**Documentations**

-Swagger : 

Dans le cadre du projet j’ai documenté mon API rest parkings service avec OPEN API 3

http://localhost:8086/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

-Javadoc : 

Il est possible de générer la Javadoc

**Tests unitaires**

Utilisation de Junit 5 Jupiter pour réaliser des tests sur mon service principal de récupération de parkings les plus proche en fonction de sa localisation (position).
Il est important de tester l’ensemble des services que l’on développe et de parcourir les chemins (conditions) pour éviter tout problème en production.

Dans ce projet je n’ai pas mis en place les tests d’intégration simulant un appel API REST complet. 

Ces tests devront également être automatisés et exécuter au moment de la compilation (build) pour vérifier qu’il n’y a pas de régression dans le code 


**Gestion des exceptions : **

J’ai créé des exceptions héritant de la classe Exception : 
-ParkingBadRequestException
-ParkingNotFoundException
pour envoyer un code erreur plus précis en fonction du problème rencontré. Ces exceptions sont gérées par Spring par un @ControllerAdvice. Ceci permet de gérer les exceptions d’un micro service de manière centralisée.


**Base de données :** 

En ce qui concerne les tests j’utilise une base de données volatile hsql qui est créée à la volée puis supprimée à la fin du test. Des données y sont insérées avec un script data.sql et le schéma est créé avec un script schéma.sql dans le dossier src/java/ressources/db/hsql

En dehors des tests, j'utilise le SGBD postgresql : 
Pour exécuter l’application avec postgresql il faut rajouter le profil postgresql en argument de la VM comme ceci :
   -Dspring.profiles.active=postgresql

le schéma est créé avec un script schéma.sql dans le dossier src/java/ressources/db/postgresql
J’ai désactivé l’auto génération par hibernate pour écrire un script sql à la main pour postgresql.

<u>Remarque</u> : 

En ce qui concerne la modélisation,pour la simplicité du mini projet, je n’ai créé qu’une seule table parking avec le nom de la ville dans cette table.
Dans un vrai projet, j’aurai créé également une table ville. Le parking aurait donc l’id de la ville et non le nom de la ville. l’API ferait appelle à la liste des parkings avec l’id de la ville.


**Architecture DAO**

En ce qui concerne l’accès aux données, nous sommes sur une architecture classique. Le controller REST appelle un service qui accède au repository qui récupère les données en base de données (ici postgresql) et les map dans un objet (entité) grâce à l'utilisation d’un ORM tel que hibernate avec Spring data JPA). 
Ensuite l’objet entité est mappé avec un DTO (utilisation de mapStruct). Et c’est ce DTO et uniquement celui ci qui est envoyé au client.

Il y a un respect du principe d’encapsulation dans chacune des classes avec les propriétés en private. Celle ci sont accessibles avec des getter et des setter (lombok peut être utilisé pour éviter de générer à la main les getter et setter grâce à l’annotation @Getter et @Setter, de même pour le constructeur sans argument)



**Gestion des configurations de l’application**

Les configurations de l’application et des micro services sont récupérées par notre serveur de configuration depuis un repository GIT. Les données de configuration en production devront être chiffrées pour ne pas être visibles en clair.


**Déploiement continu / Intégration continue**

Dans le cadre d’un vrai projet, il est nécessaire d’accentuer ce point notamment pour que l’équipe de développement soit en collaboration étroite avec l’équipe devops. 
Ceci se fait par la mise en place de pipeline pouvant 
Dans le but de réduire considérablement les délais de livraison.

**Gestion des logs :**
La gestion des logs devra être faite par logback et afin de centraliser la gestion de ces logs il est intéressant la programmation orienté aspect. Dans le cadre de micro service il est intéressant d’avoir une gestion de logs distribués.

**Internationalisation : **
Pour des raisons de simplicité pour ce projet les messages d’exception sont en dur dans l’application en français mais ceci devra être géré dans Spring avec i18n. 


**Le RAF : **

1) Le batch 

Mise en place du batch, qui pour chaque ville, va récupérer la bonne source de données et l’intégrer dans notre propre base de données afin d’avoir une normalisation des données et ne pas être dépendant d’un service extérieur.

En terme de modélisation j’imaginais : 

-Une table Parking( <u>idParking</u>, nom, longitude, latitude,**#idVille**,etc..)
	
-Une table Ville(<u>idVille</u>, nomVille)
	
-Une table ContextJobRepo(id, url, **#idVille,#idTypeJob**) 
avec l’url étant la source de données où l’on récupère les données. Par exemple pour notre liste de parking : https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilite-parkings-grand-poitiers-donnees-metiers&rows=1000&facet=nom_du_parking&facet=zone_tarifaire&facet=statut2&facet=statut3

-Une table TypeJob(<u>idType</u>, nomType) avec  nomType un enum : listParking, listBike, etc..

Comment serait construit notre batch ?

Notre Job aurait deux paramètres. Un pour la ville et un pour le typeJob.
Ceci signifie qu’on peut exécuter le batch pour une ville et un type de Job données (Exemple liste des parkings pour Poitiers seulement) mais on peut également exécuter le batch pour toutes les villes.
	
Algo : 

Batch 
	
=> Job (idville, idtypejob)

=> On récupère en base de données, grâce à ces paramètres, la liste des URL avec sa ville et son typeJob correspondant (jointure nécessaire pour récupérer le nom de la ville et le nom du typejob)

=> Pour chaque URL récupérée on effectue une étape (step) du JOB. (il y aura donc n étapes).

=> Pour chaque étape
	=> ItemReader
	=> ItemProcessor
	=> ItemWriter


Que font chacun de ces traitements ?

-ItemReader : GET de l’URL ( Récupérer les données retournées par l’URL, dans notre cas c’est du JSON )

-ItemProcessor : En utilisant l’API JAVA reflection on pourrait retrouver le nom de la classe (Exemple ListParkingPoitiersProcess.class) avec la sémantique : 
 [typeJob][nomVIlle]Process.class puis exécuter la méthode process() de cette classe. Qui sera implémenté en fonction du besoin

-itemWriter : Sauvegarder dans notre base de données. (dans notre cas postgresql)


**Suites RAF :**

-Mise en place des serveurs sent event en utilisant apache kafka et spring web flux pour gérer des données en temps réel pour le nombre de place disponible des parkings 
-Mise en place de spring security pour gérer l’accès au ressources de l’application en fonction du rôle. Dans la plupart du temps, le client obtient un access token qui est valide pour la durée de sa session. Il devra ajouter dans le header Authorization se sa requête HTTP le bearer token. Ce jeton s’obtient par l'intermédiaire d’un serveur d’authentification tel que Keycloack.<br>
-Mettre en place un système de cache distribué.<br>
-Mettre en place un serveur de log distribué.<br>
-Mettre en place Spring boot admin pour gérer au mieux les ressources CPU, mémoire, etc.<br>
-Mettre en place un circuit breaker pour les appels de services extérieurs et notamment pour celui appelé en temps réel chaque seconde.<br>
-Créer des images docker à partir d’un dockerFile.<br>
-Déployer ses micro-service dans une plateforme (PAAS) ou (CAAS conteneur as a service s' ils sont conteneurisés).

Capture d'écran Swagger et tests à cette URL https://github.com/quentin68/appInstantSystem/tree/master/z_captureEcranApp
	
Temps de travail : 10 heures

-Analyse approfondi pour définir l'architecture de l'application.<br>
-Développement micro services Spring boot avec organisation des pom Maven (Pom Parent puis pom enfant).<br>
-Configuration centrale de tous les micro services sur un serveur de configuration accèssible depuis repo GIT.<br>
-Gestion des exceptions.<br>
-Mise en place des tests unitaires.<br>
-Documentation JAVA Doc + OPEN API 3 (swagger).<br>
-Relecture de code.<br>
-Documentation Readme approfondi avec l'explication de mes choix + le reste à faire et comment je l'envisage.



